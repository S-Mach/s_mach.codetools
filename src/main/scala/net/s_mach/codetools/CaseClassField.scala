package net.s_mach.codetools

case class CaseClassField(
  fieldName: String,
  scalaType: String,
  optDefaultValue: Option[String] = None,
  optComment: Option[String] = None
)