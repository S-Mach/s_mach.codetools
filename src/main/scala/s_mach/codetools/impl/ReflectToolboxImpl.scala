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
package net.s_mach.codetools.impl

import net.s_mach.codetools.StructContext

trait ReflectToolboxImpl extends StructContext {
  object impl {
    def inferImplicitOrDie(
      aType:c.Type
    ) : c.Tree = {
      val result = c.inferImplicitValue(aType)
      if (result.isEmpty) {
        c.abort(c.enclosingPosition, s"Implicit $aType does not exist")
      }
      result
    }

    def getCompanionMethodOrDie(
      aType: c.Type,
      methodName: String
    ) : c.universe.MethodSymbol = {
      import c.universe._
      aType.typeSymbol.companion.typeSignature.decl(TermName(methodName)) match {
        case NoSymbol =>
          c.abort(
            c.enclosingPosition,
            s"$aType.$methodName method does not exist"
          )
        case s => s.asMethod
      }
    }

    val tupleNamePattern = "scala\\.Tuple\\d{1,2}".r.pattern

    def isTupleType(aType:c.Type) : Boolean = {
      tupleNamePattern.matcher(aType.typeSymbol.fullName.toString).matches
    }

    def calcStructType(aType: c.Type): StructType = {
      def filterMethodStructType(
        aType:c.Type,
        method:c.universe.MethodSymbol
      )(
        calcOomStructType: => List[StructType]
      ) : List[StructType] = {
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
          if(method.typeParams.map(_.toString) == aType.typeConstructor.typeParams.map(_.toString)) {
            // TODO: is the comparing the fullName of two symbols the same as == ?
            // TODO: does comparing fullName work with generics?
            val methodTypeParamToTypeParam =
              method.typeParams
                .map(_.fullName)
                .zip(aType.typeArgs)
                .toMap
            calcOomStructType.map { structType =>
              // Translate member generic type parameters to something bound
              // TODO: figure out normal way this subst is supposed to be done - prob doesn't work for bound generic types
              StructType(
                structType.oomMember.map { case orig@(optSymbol, _type) =>
                  methodTypeParamToTypeParam.get(_type.typeSymbol.fullName) match {
                    case Some(translatedType) =>
                      (optSymbol, translatedType)
                    case None => orig
                  }
                }
              )
            }
          } else {
            c.warning(
              c.enclosingPosition,
              s"Ignoring possible matching $method method whose type parameter symbols ${method.typeParams} does not match ${aType.typeSymbol} type parameter symbols ${aType.typeConstructor.typeParams}"
            )
            Nil
          }
        } else {
            c.warning(
              c.enclosingPosition,
              s"Ignoring possible matching $method method ${method.paramLists.size} ${method.typeParams.size} ${aType.typeParams.size}"
            )
          Nil
        }

      }

      def calcApplyStructType(
        aType:c.Type,
        applyMethod:c.universe.MethodSymbol
      ) : StructType = {
        filterMethodStructType(aType,applyMethod) {
          List(
            StructType(
              applyMethod.paramLists.head.map { param =>
                (Some(param),param.typeSignature)
              }
            )
          )
        }.head
      }

      def calcOomUnapplyStructType(
        aType:c.Type,
        unapplyMethod:c.universe.MethodSymbol
      ) : List[StructType] = {
        import c.universe._

        filterMethodStructType(aType,unapplyMethod) {
          // Outer type for unapply is always Option, so strip it
          val TypeRef(_,_,oneOuterArg) = unapplyMethod.returnType
          oneOuterArg.head match {
             /*
              if the unapply methods returns Option[TupleX[A,B..Z]] then there are
              two possible struct types (matching apply method determines which
              is correct):
              1) _:TupleXX[A,B,..Z]
              2) _:A,_:B,.._:Z
              */
            case tupleType@TypeRef(_, symbol, innerArgs)
              if innerArgs.nonEmpty &
                 isTupleType(tupleType) =>
              List(StructType(List((None,tupleType))), StructType(innerArgs.map(a => (None, a))))
            // For anything else there is only one possible struct type
            case typeRef@TypeRef(_, _, _) =>
              List(StructType(List((None,typeRef))))
          }
        }
      }

      val unapplyMethod = getCompanionMethodOrDie(aType,"unapply")
      val oomUnapplyMethodStructType =
        calcOomUnapplyStructType(aType,unapplyMethod)

      val oomApplyMethodAlt =
        getCompanionMethodOrDie(aType,"apply")
          .asTerm
          .alternatives
          .map(_.asMethod)

      val oomApplyMethodStructType =
        oomApplyMethodAlt.map { alt =>
          calcApplyStructType(aType, alt)
        }

      // Search for first unapply struct type that matches an apply struct type
      oomUnapplyMethodStructType.toStream.map { unapplyStructType =>
        oomApplyMethodStructType.find { applyStructType =>
          unapplyStructType matches applyStructType
        } match {
          case Some(applyStructType) =>
            Some(applyStructType)
          case None => None
        }
      }.collectFirst { case optStructType if optStructType.nonEmpty =>
        optStructType.get
      }.getOrElse {
        c.abort(
          c.enclosingPosition,
          s"No matching apply/unapply method pair found for ${aType.typeSymbol.fullName}\n" +
          s"Found ${oomApplyMethodAlt.size} apply methods:\n" +
          oomApplyMethodStructType.map(_.print).mkString("\n  ") +
          s"Found unapply struct type:\n" +
          oomUnapplyMethodStructType.map(_.print).mkString("\n  ")
        )
      }
    }

  }

}

