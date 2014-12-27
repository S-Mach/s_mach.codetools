///*
//                    ,i::,
//               :;;;;;;;
//              ;:,,::;.
//            1ft1;::;1tL
//              t1;::;1,
//               :;::;               _____        __  ___              __
//          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
//         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
//         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
//       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
//       Lft1,:;:       , 1tfL:
//       ;it1i ,,,:::;;;::1tti      s_mach.codetools
//         .t1i .,::;;; ;1tt        Copyright (c) 2014 S-Mach, Inc.
//         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
//          .L1 1tt1ttt,,Li
//            ...1LLLL...
//*/
//package s_mach.codetools
//
//import s_mach.codetools.testdata.TestData
//
//import scala.language.experimental.macros
//import scala.reflect.macros.blackbox
//import org.scalatest.{FlatSpec, Matchers}
//
//object BlackboxToolboxTest {
//  class TestToolbox(val c:blackbox.Context) extends BlackboxToolbox {
//    def test() : c.Expr[Result[String]] = {
//      c.universe.reify {
//        getCompanionMethod(
//          c.universe.weakTypeOf[TestData],
//          "apply"
//        ).map(_.toString)
//      }
//    }
//  }
//  def test() : Result[String] = macro macroTest
//  def macroTest(c:blackbox.Context)() : c.Expr[Result[String]] = {
//    val toolbox = new TestToolbox(c)
//    toolbox.test().asInstanceOf[c.Expr[Result[String]]]
//  }
//}
//
//class BlackboxToolboxTest extends FlatSpec with Matchers {
//  import BlackboxToolboxTest._
//
//  "getCompanionMethod" must "find the specified companion method when it is present" in {
//    test() should equal("apply")
//  }
//}
