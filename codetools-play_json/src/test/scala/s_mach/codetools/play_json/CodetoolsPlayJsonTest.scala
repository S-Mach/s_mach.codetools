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
import play.api.libs.json.Json

class CodetoolsPlayJsonTest extends FlatSpec with Matchers {
  "JsValue.pretty" should "pretty print JSON" in {
    Json.obj(
      "test1" -> true,
      "test2" -> 1,
      "test3" -> "s"
    ).pretty should equal(
"""{
  "test1" : true,
  "test2" : 1,
  "test3" : "s"
}"""

    )
  }
}
