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
import s_mach.codetools.IsDistinctTypeAlias

object DistinctTypeAliasPlayJsonTest {
  trait StringDTA_Tag
  type StringDTA = String with StringDTA_Tag with IsDistinctTypeAlias[String]
  import scala.language.implicitConversions
  implicit def StringDTA(s: String) = s.asInstanceOf[StringDTA]

  val format_StringDTA = Json.forDistinctTypeAlias.format[StringDTA,String]
  val reads_StringDTA = Json.forDistinctTypeAlias.reads[StringDTA,String]
  val writes_StringDTA = Json.forDistinctTypeAlias.writes[StringDTA,String]
}

class DistinctTypeAliasPlayJsonTest extends FlatSpec with Matchers {
  import DistinctTypeAliasPlayJsonTest._

  "Json.forDistinctTypeAlias.reads" should "generate a reads for a value class" in {
    val test = "test"
    reads_StringDTA.reads(JsString(test)).get should be(StringDTA(test))
  }

  "Json.forDistinctTypeAlias.writes" should "generate a writes for a value class" in {
    val test = "test"
    writes_StringDTA.writes(test) should be(JsString(test))
  }

  "Json.forDistinctTypeAlias.format" should "generate a format for a value class" in {
    val test = "test"
    format_StringDTA.reads(JsString(test)).get should be(StringDTA(test))
    format_StringDTA.writes(test) should be(JsString(test))
  }
}
