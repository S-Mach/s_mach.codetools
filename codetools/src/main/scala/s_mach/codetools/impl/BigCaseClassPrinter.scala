package s_mach.codetools.impl

import s_mach.codetools.codegen.CaseClassCodegen.Field
import s_mach.string._

object BigCaseClassPrinter {


  def printBigCaseClass(name: String, fields: Vector[Field]) : String = {
    require(fields.size <= 255,"JVM supports at most 255 parameters in a method")
    val subCaseClasses = fields.grouped(CASECLASS_MAX_FIELDS).toVector

    /*
    _1  :  Name.Name1,
    _2  :  Name.Name2
    */
    val fieldsStr = commaSep {
      subCaseClasses.zipWithIndex.map { case (s,i) =>
        Vector(s"_${i+1}",":",s"$name.$name${i+1}")
      }
    }.printGrid

    /*
    def field1             = _1.field1
    def field2             = _1.field2
    ...
    def field23            = _2.field23
    def field24            = _2.field24
    */
    val methodsStr = subCaseClasses.iterator.zipWithIndex.flatMap { case(subfields,i) =>
      subfields.map { f =>
        Vector("def",f.name,":",f._type,"=",s"_${i+1}.${f.name}") ++ f.optComment.map("// " + _).toVector
      }
    }.toVector.printGrid

    /*
    override def productElement(i: Int) : Any = i match {
      case n if n < 22 => _1.productElement(i)
      case n => _2.productElement(i - 22)
    }
    override def productArity : Int = 33
    override def productIterator = _1.productIterator ++ _2.productIterator
    */
    val productMethodsStr : String = {
      val cases = subCaseClasses.iterator.zipWithIndex.map { case (_,i) =>
        s"case n if n < ${(i+1)*CASECLASS_MAX_FIELDS} => _${i+1}.productElement(i - ${i*CASECLASS_MAX_FIELDS})"
      }.mkString("\n").indent(2)


      val iterators = subCaseClasses.indices.map(i => s"_${i+1}.productIterator").mkString(" ++ ")

      s"""
      |override def productElement(i: Int) : Any = i match {
      |$cases
      |  case _ => throw new IndexOutOfBoundsException
      |}
      |override def productArity : Int = ${fields.size}
      |override def productIterator = $iterators
      """.stripMargin.trim
    }
    /*
    case class Name1(
      field1       :  Long,
      field2       :  String,
      ...
    )
    case class Name2(
      field23             :  scala.Option[String],
      field24             :  scala.Option[Byte],
      ...
    )
    */
    val subCaseClassesStr = {
      subCaseClasses.iterator.zipWithIndex.map { case(subfields,i) =>
        CaseClassPrinter.printNormCaseClass(name + (i+1).toString,subfields)
      }.mkString("\n")
    }

    // Copy method that takes all parameters
    val bigCopyStr = {
      /*
      field1             : Long                 =   _1.field1,
      field2             : String               =   _1.field2,
      ...
      field23            : scala.Option[String] =   _2.field23,
      field24            : scala.Option[String] =   _2.field24,
      ...
      */
      val copyParmsStr = commaSep {
        subCaseClasses.iterator.zipWithIndex.flatMap { case(subfields,i) =>
          subfields.init.map { f =>
            Vector(f.name,":",f._type,"=",s"_${i+1}.${f.name}")
          }
        }.toVector
      }.printGrid

      val caseClassParmsStr = commaSep {
        subCaseClasses.iterator.zipWithIndex.flatMap { case(subfields,i) =>
          subfields.init.map { f =>
            Vector(f.name,"=",f.name)
          }
        }.toVector
      }.printGrid

      s"""
      |def copy(
      |${copyParmsStr.indent(2)}
      |) : $name = $name(
      |${caseClassParmsStr.indent(2)}
      |)
      """.stripMargin.trim
    }

    // Apply method that takes all parameters
    val bigApplyStr = {

      /*
      field1             : Long,
      field2             : String,
      ...
      field23            : scala.Option[String],
      field24            : scala.Option[String] = None,
      ...
      */
      val allFieldsStr = CaseClassPrinter.printFieldDecls(fields)

      /*
      _1 = Name1(
        field1      = field1,
        field2      = field2,
      ...
      ),
      2 = Name2(
        field23     = field23,
        field24     = field24,
      ...
      )
      */
      val applySubCaseClassesStr = subCaseClasses.iterator.zipWithIndex.map { case (subfields,i) =>
        val subFieldParms = commaSep {
          subfields.map { f =>
            Vector(f.name,"=",f.name)
          }
        }.printGrid

        s"""
        |_${i+1} = $name${i+1}(
        |${subFieldParms.indent(2)}
        |)
        """.stripMargin.trim

      }.mkString(",\n")

      s"""
      |def apply(
      |${allFieldsStr.indent(2)}
      |) : $name = $name(
      |${applySubCaseClassesStr.indent(2)}
      |)
      """.stripMargin.trim
    }


    s"""
    |case class $name(
    |${fieldsStr.indent(2)}
    |) {
    |${methodsStr.indent(2)}
    |
    |${bigCopyStr.indent(2)}
    |
    |${productMethodsStr.indent(2)}
    |}
    |
    |object $name {
    |${subCaseClassesStr.indent(2)}
    |${bigApplyStr.indent(2)}
    |}
    """.stripMargin.trim
  }
}
