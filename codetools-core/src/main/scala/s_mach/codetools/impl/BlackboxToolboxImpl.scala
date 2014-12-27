/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____        __  ___              __
          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
       Lft1,:;:       , 1tfL:
       ;it1i ,,,:::;;;::1tti      s_mach.codetools
         .t1i .,::;;; ;1tt        Copyright (c) 2014 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools.impl

import s_mach.codetools.Result

import scala.reflect.macros.blackbox

trait BlackboxToolboxImpl {
  val c:blackbox.Context

  object Impl {
    def inferImplicit(aType:c.Type) : Result[c.Tree] = {
      val result = c.inferImplicitValue(aType)
      if (result.isEmpty) {
        Result.error(s"Implicit $aType does not exist")
      } else {
        Result(result)
      }
    }

    def getCompanionMethod(
      aType: c.Type,
      methodName: String
    ) : Result[c.universe.MethodSymbol] = {
      import c.universe._
      aType.typeSymbol.companion.typeSignature.decl(TermName(methodName)) match {
        case NoSymbol =>
          Result.error(s"$aType.$methodName method does not exist")
        case s => Result(s.asMethod)
      }
    }

    val tupleNamePattern = "scala\\.Tuple\\d{1,2}".r.pattern

    def isTupleType(aType:c.Type) : Boolean = {
      tupleNamePattern.matcher(aType.typeSymbol.fullName.toString).matches
    }

    type TypeSig = List[String]

    def calcProductTypeFields(aType: c.Type): Result[List[(String, c.Type)]] = {

      val aTypeParams = aType.typeConstructor.typeParams.map(_.toString)

      def filterMethod(method:c.universe.MethodSymbol) : Result[Option[c.universe.MethodSymbol]] = {
        import c.universe._

        if(
          // Silently ignore methods who have a different count of type parameters
          // than the type
          method.typeParams.size == aType.typeConstructor.typeParams.size &&
          // Silently ignore curried methods
          method.paramLists.size == 1
        ) {
          // Ignore but warn about methods whose type parameters are exact match
          // size but whose symbols don't match the type's type parameter symbols
          // exactly. Using just apply/unapply its not possible to figure out
          // how the type parameters align to the type's type parameter without
          // just assuming that they align exactly in the same order.
          /*
            Apply method can have different symbols in different order than type's
            type parameters:
            class A[X,Y](value1: X, value2: Y)
            object A {
              // compiles but possible weirdness in macro here! so just ignore it and warn
              def apply[BB,AA](value1: AA, value2: BB) =
                new A(value1,value2)
            }
           */
          if(method.typeParams.map(_.toString) == aTypeParams) {
            Result(Some(method))
          } else {
            Result(
              None,
              s"Ignoring possible matching $method method whose type parameter symbols ${method.typeParams} do not match ${aType.typeSymbol} type parameter symbols ${aType.typeConstructor.typeParams}"
            )
          }
        } else {
          Result(None)
        }
      }

      def mkTypeSig(oomType: List[c.Type]) : List[String] = {
        val (zomTypeParams,zomConcreteTypes) =
          oomType
            .zipWithIndex
            .partition(_._1.typeSymbol.isParameter)

        val unsortedTypeNames =
          zomTypeParams.zip(aTypeParams).map { case ((_, index), aTypeParam) =>
            (aTypeParam, index)
          } :::
          zomConcreteTypes.map { case (_type, index) => (_type.typeSymbol.fullName,index) }
        unsortedTypeNames
          .sortBy(_._2)
          .map(_._1)
      }

      def calcOomUnapplyTypeSig(
        unapplyMethod:c.universe.MethodSymbol
      ) : List[TypeSig] = {
        import c.universe._

        // Outer type for unapply is always Option, so strip it
        val TypeRef(_,_,oneOuterArg) = unapplyMethod.returnType
        oneOuterArg.head match {
           /*
            if the unapply methods returns Option[TupleX[A,B..Z]] then there are
            two possible product types (matching apply method determines which
            is correct):
            1) _:TupleXX[A,B,..Z]
            2) _:A,_:B,.._:Z
            */
          case tupleType@TypeRef(_, symbol, innerArgs)
            if innerArgs.nonEmpty & isTupleType(tupleType) =>
            List(mkTypeSig(List(tupleType)), mkTypeSig(innerArgs))
          // For anything else there is only one possible struct type
          case typeRef@TypeRef(_, _, _) =>
            List(mkTypeSig(List(typeRef)))
        }
      }

      def calcApplyTypeSig(
        applyMethod:c.universe.MethodSymbol
      ) : TypeSig = {
        mkTypeSig(
          applyMethod.paramLists.head.map { param =>
            param.typeSignature
          }
        )
      }

      Result.applicative(
        getCompanionMethod(aType,"unapply"),
        getCompanionMethod(aType,"apply")
      ) { (rawUnapplyMethod, applyMethod) =>
          Result.applicative(
            filterMethod(rawUnapplyMethod),
            Result.sequence {
              applyMethod
                .asTerm
                .alternatives
                .map { m =>
                  filterMethod(m.asMethod)
                }
            }.map(_.flatten)
          ) { (optUnapplyMethod,_oomApplyMethod) =>
            (_oomApplyMethod, optUnapplyMethod) match {
              case (Nil, _) =>
                Result.error(s"No eligible apply method found")
              case (_, None) =>
                Result.error(s"No eligible unapply method found")
              case (oomApplyMethod,Some(unapplyMethod)) =>
                // Search for first unapply type sig that matches an apply type sig
                val oomUnapplyTypeSig = calcOomUnapplyTypeSig(unapplyMethod)
                oomUnapplyTypeSig.toStream.map { unapplyTypeSig =>
                  oomApplyMethod.find { applyMethod =>
                    calcApplyTypeSig(applyMethod) == unapplyTypeSig
                  } match {
                    case Some(matchingApplyMethod) =>
                      // TODO: figure out proper way to get method type params to match type type params
                      val methodTypeParamToTypeParam =
                        matchingApplyMethod.typeParams
                          .map(_.fullName)
                          .zip(aType.typeArgs)
                          .toMap

                      Some {
                        matchingApplyMethod.paramLists.head.map { symbol =>
                          val symType = symbol.typeSignature
                          val _type =
                            methodTypeParamToTypeParam.getOrElse(
                              symType.typeSymbol.fullName,
                              symType
                            )
                          (symbol.name.toString,_type)
                        }
                      }
                    case None => None
                  }
                }
                  .collectFirst { case Some(productType) => productType } match {
                  case s@Some(oomSymbol) => Result(oomSymbol)
                  case None =>
                    Result.error {
                      s"No matching apply/unapply method pair found for ${aType.typeSymbol.fullName}\n" +
                      s"Found ${oomApplyMethod.size} apply methods:\n" +
                      oomApplyMethod.map(applyMethod => calcApplyTypeSig(applyMethod).mkString(",")).mkString("\n  ") +
                      s"Found unapply type signatures:\n" +
                      oomUnapplyTypeSig.map(_.mkString(",")).mkString("\n  ")
                    }
                }
            }
          }
      }
    }

  }
}
