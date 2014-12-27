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
package s_mach.codetools.reflectPrint

trait ReflectPrintTupleImplicits {

  implicit def mkTuple2ReflectPrint[A,B](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B]
  ) : ReflectPrint[(A,B)] =
    ReflectPrint.forProductType[(A,B)]


  implicit def mkTuple3ReflectPrint[A,B,C](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C]
  ) : ReflectPrint[(A,B,C)] =
    ReflectPrint.forProductType[(A,B,C)]


  implicit def mkTuple4ReflectPrint[A,B,C,D](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D]
  ) : ReflectPrint[(A,B,C,D)] =
    ReflectPrint.forProductType[(A,B,C,D)]


  implicit def mkTuple5ReflectPrint[A,B,C,D,E](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E]
  ) : ReflectPrint[(A,B,C,D,E)] =
    ReflectPrint.forProductType[(A,B,C,D,E)]


  implicit def mkTuple6ReflectPrint[A,B,C,D,E,F](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F]
  ) : ReflectPrint[(A,B,C,D,E,F)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F)]


  implicit def mkTuple7ReflectPrint[A,B,C,D,E,F,G](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G]
  ) : ReflectPrint[(A,B,C,D,E,F,G)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G)]


  implicit def mkTuple8ReflectPrint[A,B,C,D,E,F,G,H](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H)]


  implicit def mkTuple9ReflectPrint[A,B,C,D,E,F,G,H,I](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I)]


  implicit def mkTuple10Diff[A,B,C,D,E,F,G,H,I,J](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J)]


  implicit def mkTuple11Diff[A,B,C,D,E,F,G,H,I,J,K](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K)]


  implicit def mkTuple12Diff[A,B,C,D,E,F,G,H,I,J,K,L](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L)]


  implicit def mkTuple13Diff[A,B,C,D,E,F,G,H,I,J,K,L,M](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M)]


  implicit def mkTuple14Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N)]


  implicit def mkTuple15Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O)]


  implicit def mkTuple16Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P)]


  implicit def mkTuple17Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q)]


  implicit def mkTuple18Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q],
    rReflectPrint: ReflectPrint[R]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R)]


  implicit def mkTuple19Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q],
    rReflectPrint: ReflectPrint[R],
    sReflectPrint: ReflectPrint[S]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S)]


  implicit def mkTuple20Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q],
    rReflectPrint: ReflectPrint[R],
    sReflectPrint: ReflectPrint[S],
    tReflectPrint: ReflectPrint[T]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T)]


  implicit def mkTuple21Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q],
    rReflectPrint: ReflectPrint[R],
    sReflectPrint: ReflectPrint[S],
    tReflectPrint: ReflectPrint[T],
    uReflectPrint: ReflectPrint[U]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U)]


  implicit def mkTuple22Diff[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V](implicit
    aReflectPrint: ReflectPrint[A],
    bReflectPrint: ReflectPrint[B],
    cReflectPrint: ReflectPrint[C],
    dReflectPrint: ReflectPrint[D],
    eReflectPrint: ReflectPrint[E],
    fReflectPrint: ReflectPrint[F],
    gReflectPrint: ReflectPrint[G],
    hReflectPrint: ReflectPrint[H],
    iReflectPrint: ReflectPrint[I],
    jReflectPrint: ReflectPrint[J],
    kReflectPrint: ReflectPrint[K],
    lReflectPrint: ReflectPrint[L],
    mReflectPrint: ReflectPrint[M],
    nReflectPrint: ReflectPrint[N],
    oReflectPrint: ReflectPrint[O],
    pReflectPrint: ReflectPrint[P],
    qReflectPrint: ReflectPrint[Q],
    rReflectPrint: ReflectPrint[R],
    sReflectPrint: ReflectPrint[S],
    tReflectPrint: ReflectPrint[T],
    uReflectPrint: ReflectPrint[U],
    vReflectPrint: ReflectPrint[V]
  ) : ReflectPrint[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)] =
    ReflectPrint.forProductType[(A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V)]

}

