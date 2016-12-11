package s_mach.codetools

package object impl {
  val CASECLASS_MAX_FIELDS = 22

  def commaSep(grid : Vector[Vector[String]]) : Vector[Vector[String]]  = {
    grid.init.map { row =>
      row.init :+ row.last + ","
    } :+ grid.last
  }
}
