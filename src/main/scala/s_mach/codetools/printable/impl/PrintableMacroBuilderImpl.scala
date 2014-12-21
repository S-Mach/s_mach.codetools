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
package net.s_mach.codetools.printable.impl

import net.s_mach.codetools.ReflectToolbox
import net.s_mach.codetools.printable.{PrintableMacroBuilder, Printable}

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

class PrintableMacroBuilderImpl(val c: blackbox.Context) extends PrintableMacroBuilder with ReflectToolbox {
  import c.universe._

  def build[A: c.WeakTypeTag]() : c.Expr[Printable[A]] = {
    val aType = c.weakTypeOf[A]
    val isTuple = isTupleType(aType)

    val typeName = {
      if(isTuple) {
        ""
      } else {
        aType.typeSymbol.name.toString
      }
    }

    def fmtSymbol(optSymbol:Option[c.Symbol]) : String = {
      if(isTuple) {
        ""
      } else {
        optSymbol.fold("")(_.name.toString + "=")
      }
    }

    val printableTypeConstructor = typeOf[Printable[_]].typeConstructor

    val structType = calcStructType(aType)

    val body =
      if(structType.oomMember.size == 1) {
        val (optSymbol, _type) = structType.oomMember.head
        val innerPrintableType = appliedType(printableTypeConstructor, List(_type))
        val innerPrintable = inferImplicitOrDie(innerPrintableType)
        q"""
val v = ${aType.typeSymbol.companion}.unapply(a).get
$typeName + "(" +
${fmtSymbol(optSymbol)} + $innerPrintable.print(v) +
")"
        """
      } else {
        val values =
          structType.oomMember
            .zipWithIndex
            .map { case ((optSymbol, _type), i) =>
              val innerPrintableType = appliedType(printableTypeConstructor, List(_type))
              val innerPrintable = inferImplicitOrDie(innerPrintableType)
              q"""${fmtSymbol(optSymbol)} + $innerPrintable.print(tuple.${TermName("_" + (i+1))})"""
            }
        q"""
val tuple = ${aType.typeSymbol.companion}.unapply(a).get
$typeName + "(" +
Seq(..$values).mkString(",") +
")"
        """
      }

    c.Expr[Printable[A]] {
      q"""
new Printable[$aType] {
  def print(a: $aType) = $body
}
      """
    }
  }
}
