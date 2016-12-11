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

import play.api.libs.json.{Format, Reads, Writes}

object DistinctTypeAliasJson {
  /** @return a Writes[V] that uses the implicit Writes[A] */
  def writes[V <: A,A](implicit
    aWrites:Writes[A]
  ) : Writes[V] =
    Writes[V](v => aWrites.writes(v))

  /** @return a Reads[V] that uses the implicit Reads[A] */
  def reads[V <: A,A](implicit
    f: A => V,
    aReads:Reads[A]
  ) : Reads[V] =
    Reads[V](js => aReads.reads(js).map(f))

  /** @return a Format[V] that uses the implicit Format[A] */
  def format[V <: A,A](implicit
    f: A => V,
    aReads: Reads[A],
    aWrites: Writes[A]
  ) : Format[V] = Format(reads(f,aReads),writes)
}

