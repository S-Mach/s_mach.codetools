package s_mach.codetools.impl

import s_mach.codetools.reflectPrint._

class ReflectPrintOption[A](implicit
  pA:ReflectPrint[A]
) extends SimpleReflectPrintImpl[Option[A]] {
  def print(oa: Option[A])(implicit fmt: ReflectPrintFormat) : String = {
    oa match {
      case Some(a) => s"Some(${fmt.newSection(pA.printApply(a)(_))})"
      case None => "None"
    }
  }
}
