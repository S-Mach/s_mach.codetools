/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____       __  ___              __
          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
       Lft1,:;:       , 1tfL:
       ;it1i ,,,:::;;;::1tti      s_mach.codetools.play_json
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools.play_json

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.{Json, JsString}
import s_mach.codetools.IsValueClass

object ValueClassPlayJsonTest {
  implicit class StringVC(
  val underlying: String
  ) extends AnyVal with IsValueClass[String]

  val format_StringVC = Json.forValueClass.format[StringVC,String]
  val reads_StringVC = Json.forValueClass.reads[StringVC,String]
  val writes_StringVC = Json.forValueClass.writes[StringVC,String]
}

class ValueClassPlayJsonTest extends FlatSpec with Matchers {
  import ValueClassPlayJsonTest._

  "Json.forValueClass.reads" should "generate a reads for a value class" in {
    val test = "test"
    reads_StringVC.reads(JsString(test)).get should be(StringVC(test))
  }

  "Json.forValueClass.writes" should "generate a writes for a value class" in {
    val test = "test"
    writes_StringVC.writes(test) should be(JsString(test))
  }

  "Json.forValueClass.format" should "generate a format for a value class" in {
    val test = "test"
    format_StringVC.reads(JsString(test)).get should be(StringVC(test))
    format_StringVC.writes(test) should be(JsString(test))
  }
}
