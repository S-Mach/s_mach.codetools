package net.s_mach.codegen

case class CaseClassField(
  fieldName: String,
  scalaType: String,
  optDefaultValue: Option[String] = None,
  optComment: Option[String] = None
)