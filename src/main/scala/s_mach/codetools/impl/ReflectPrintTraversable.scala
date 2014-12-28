package s_mach.codetools.impl

import scala.language.higherKinds
import scala.reflect.ClassTag
import s_mach.codetools.reflectPrint.{ReflectPrint, ReflectPrintFormat}
import s_mach.codetools.reflectPrint.impl.SimpleReflectPrintImpl

class ReflectPrintTraversable[A, M[AA] <: Traversable[AA]](implicit
  pA:ReflectPrint[A],
  mClassTag:ClassTag[M[_]]
) extends SimpleReflectPrintImpl[M[A]] {
  val className = mClassTag.runtimeClass.getSimpleName
  override def print(
    ma: M[A]
  )(implicit
    fmt: ReflectPrintFormat
  ): String = {
    if(ma.isEmpty) {
      s"$className.empty"
    } else {
      s"$className(${fmt.newSection { sectionFmt =>
        ma
          .map(a => pA.printApply(a)(sectionFmt))
          .mkString(s",${sectionFmt.newLine}")
      }})"
    }
  }
}
