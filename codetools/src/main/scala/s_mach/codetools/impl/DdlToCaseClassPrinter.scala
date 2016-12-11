//package s_mach.codetools.impl
//
//import s_mach.codetools.codegen.CaseClassField
//import s_mach.string._
//
//object DdlToCaseClassPrinter {
//  case class Config(
//    formatTableName : String => String = {
//      import Lexer.Underscore
//      _.toCamelCase
//    },
//    formatColumnName : String => String = {
//      import Lexer.Underscore
//      _.toCamelCase
//    },
//    sqlToScalaTypeMap: Map[String, String] = stdSqlToScalaTypeMap
//  )
//
//  val scala_String = "String"
//  val scala_ArrayByte = "Array[Byte]"
//  val scala_Boolean = "Boolean"
//  val scala_Byte = "Byte"
//  val scala_Short = "Short"
//  val scala_Int = "Int"
//  val scala_Long = "Long"
//  val scala_BigInt = "BigInt"
//  val scala_Float = "Float"
//  val scala_Double = "Double"
//  val scala_BigDecimal = "BigDecimal"
//  val java_util_Date = "java.util.Date"
//
//  val stdSqlToScalaTypeMap : Map[String, String] = Map(
//    "char" -> scala_String,
//    "varchar" -> scala_String,
//    "tinytext" -> scala_String,
//    "text" -> scala_String,
//    "mediumtext" -> scala_String,
//    "longtext" -> scala_String,
//    "clob" -> scala_String,
//
//    "set" -> scala_String,
//
//    "enum" -> scala_String,
//
//    "blob" -> scala_ArrayByte,
//    "mediumblob" -> scala_ArrayByte,
//
//    "bit" -> scala_Boolean,
//    "tinyint(1)" -> scala_Boolean,
//    "unsigned tinyint(1)" -> scala_Boolean,
//
//    "tinyint" -> scala_Byte,
//    "unsigned tinyint" -> scala_Short, // scala has no concept of unsigned so need to promote
//    "smallint" -> scala_Short,
//    "unsigned smallint" -> scala_Int, // scala has no concept of unsigned so need to promote
//    "mediumint" -> scala_Int,
//    "unsigned mediumint" -> scala_Int, // scala has no concept of unsigned so need to promote
//    "int" -> scala_Int,
//    "unsigned int" -> scala_Long, // scala has no concept of unsigned so need to promote
//    "bigint" -> scala_BigInt,
//    "unsigned bigint" -> scala_BigInt,
//
//    "float" -> scala_Float,
//    "double" -> scala_Double,
//    "unsigned double" -> scala_Double,
//    "decimal" -> scala_BigDecimal,
//
//    "date" -> java_util_Date,
//    "datetime" -> java_util_Date,
//    "timestamp" -> scala_Long,
//    "time" -> java_util_Date,
//    "year" -> "Int"
//  )
//
//  // TODO: replace with real DDL parser
//  val parseCreateTableRegex = "(?i)CREATE TABLE [`]?(\\w+)[`]?\\s*\\((.+)\\)".r
//  val parseColumnDeclRegex = "(?i)[`]?(\\w+)[`]?\\s+(\\w+)\\s*(\\(.+?\\))?([^,]*)[,]".r
//  val parseColumnDeclFilter = "(?i)(?<=(\\s|^))(PRIMARY|KEY|CONSTRAINT|FOREIGN|USING)(?=(\\s|$))".r
//  val parseDefaultRegex = "(?i)DEFAULT (NULL|'.*?')".r
//
//  /** @return a case class for the given SQL DDL */
//  def print(
//    ddl: String,
//    cfg: Config = Config()
//  ) : String = {
//    import cfg._
//    val tidyDdl = ddl.replaceAllLiterally("\n"," ").replaceAll("\\s+"," ")
//    parseCreateTableRegex.findAllMatchIn(tidyDdl).map { tblMatch =>
//      val tableName = tblMatch.group(1)
//      val columns = tblMatch.group(2)
//      val fields : Vector[CaseClassField] = {
//        // Parse out column declarations and filter unintentional matches to lines like "PRIMARY KEY"
//        val columnDecls =
//          parseColumnDeclRegex
//            .findAllMatchIn(columns)
//            .filter(m =>
//              parseColumnDeclFilter.findFirstIn(m.group(0)).isEmpty
//            )
//        columnDecls.zipWithIndex.map { case (columnMatch,i) =>
//          val comment = columnMatch.group(0)
//          val columnName = columnMatch.group(1)
//          val rawColumnType = columnMatch.group(2)
//          val columnTypeMod = columnMatch.group(3)
//          val suffix = columnMatch.group(4)
//          val lcSuffix = suffix.toLowerCase
//          val isNullable = lcSuffix.contains("not null") == false
//          val columnType = {
//            {if(lcSuffix.contains("unsigned")) {
//              "unsigned "
//            } else {
//              ""
//            }} +
//            {if(rawColumnType.equalsIgnoreCase("tinyint") && columnTypeMod == "(1)") {
//              "tinyint(1)"
//            } else {
//              rawColumnType
//            }}
//          }
//          val baseScalaType = sqlToScalaTypeMap.getOrElse(
//            columnType,
//            throw new RuntimeException(s"Unmapped SQL type: $columnType! ${columnMatch.group(0)}")
//          )
//          // Parse the column default - some tricky logic here
//          val optDefault = {
//            parseDefaultRegex.findFirstMatchIn(suffix) match {
//              case Some(m) => Some {
//                // Translate NULL to None
//                if(m.group(1).equalsIgnoreCase("NULL")) {
//                  "None"
//                } else {
//                  // Strip quotes
//                  val rawDefault = m.group(1).tail.init
//                  // Adjust the scala value based on the baseScalaType
//                  val baseDefault =
//                    baseScalaType match {
//                      // Strings need to be double-quoted in scala
//                      case "String" => '"' + rawDefault.toString + '"'
//                      // Chars need to be single-quoted in scala
//                      case "Char" => s"'$rawDefault'"
//                      // SQL uses 0 for false and any other value as true
//                      case "Boolean" => rawDefault match {
//                        case "0" => "false"
//                        case _ => "true"
//                      }
//                      case _ => rawDefault
//                    }
//                  // If the column is nullable then wrap the default value in Some
//                  if(isNullable) {
//                    s"Some($baseDefault)"
//                  } else {
//                    baseDefault
//                  }
//                }
//              }
//              case None => None
//            }
//          }
//          val scalaType = if(isNullable) {
//            s"Option[$baseScalaType]"
//          } else {
//            baseScalaType
//          }
//          CaseClassField(
//            name = formatTableName(columnName),
//            _type = scalaType,
//            optDefault = optDefault,
//            optComment = Some(s"$i $comment")
//          )
//        }
//      }.toVector
//      val caseClassName = formatColumnName(tableName)
//      val caseClassStr = CaseClassPrinter.printCaseClass(caseClassName, fields)
//      val now = new java.util.Date()
//      s"""
//      |/**
//      | * Case class for a row in table $tableName
//      | * WARN: auto-generated file, modifications will be overwrriten
//      | * WARN: field order MUST correspond to SQL column order
//      | * Regex for quick find/replace:
//      | * ${"(\\w+)\\s*:\\s*(.+?)(\\s*=\\s*(.+?))*[,]*\\s* // (\\d+)"}
//      | * $now
//      | **/
//      |$caseClassStr
//      |/* Auto-generated from:
//      |$ddl
//      |*/
//      """.stripMargin.trim
//    }.mkString("\n")
//  }
//
//}
