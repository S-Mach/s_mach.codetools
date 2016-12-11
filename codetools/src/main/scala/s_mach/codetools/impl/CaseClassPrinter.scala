package s_mach.codetools.impl

import s_mach.codetools.codegen.CaseClassCodegen.Field
import s_mach.string._

object CaseClassPrinter {
  def printCaseClass(name: String, fields: Vector[Field]) : String = {
    if(fields.size <= CASECLASS_MAX_FIELDS) {
      printNormCaseClass(name, fields)
    } else {
      BigCaseClassPrinter.printBigCaseClass(name, fields)
    }
  }

  def printNormCaseClass(name: String, fields: Vector[Field]) : String = {
    val fieldsStr = printFieldDecls(fields)
    s"""
    |case class $name(
    |${fieldsStr.indent(2)}
    |)
    """.stripMargin.trim
  }

  def printFieldDecls(fields: Vector[Field]) : String = {
    val atLeastOneDefault = fields.exists(_.optDefault.nonEmpty)
    fields.indices.map { j =>
      val baseFieldDecl = Vector(
        fields(j).name,
        ":",
        fields(j)._type
      ) ++ fields(j).optDefault.map("= " + _).toVector
      val fieldDeclWithComma =
        if(j == fields.indices.last) {
          baseFieldDecl
        } else {
          baseFieldDecl.updated(baseFieldDecl.indices.last,baseFieldDecl.last + ",")
        }
      if(fields(j).optDefault.isEmpty && atLeastOneDefault) {
        fieldDeclWithComma ++ Vector("") ++ fields(j).optComment.map("// " + _).toVector
      } else {
        fieldDeclWithComma ++ fields(j).optComment.map("// " + _).toVector
      }
    }.printGrid
  }


}

