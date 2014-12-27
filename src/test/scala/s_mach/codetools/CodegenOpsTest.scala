///*
//                    ,i::,
//               :;;;;;;;
//              ;:,,::;.
//            1ft1;::;1tL
//              t1;::;1,
//               :;::;               _____        __  ___              __
//          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
//         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
//         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
//       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
//       Lft1,:;:       , 1tfL:
//       ;it1i ,,,:::;;;::1tti      s_mach.codetools
//         .t1i .,::;;; ;1tt        Copyright (c) 2014 S-Mach, Inc.
//         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
//          .L1 1tt1ttt,,Li
//            ...1LLLL...
//*/
//package s_mach.codetools
//
//import s_mach.codetools.impl.CodegenOps
//import org.scalatest.{Matchers, FlatSpec}
//
//class CodegenOpsTest extends FlatSpec with Matchers {
//  "genBigCaseClass" should "generate big case classes" in {
//    CodegenOps.genBigCaseClass(
//      caseClassName = "Place",
//      oomCaseClassField = testBigCaseClassFields
//    ) should equal(testBigCaseClass)
//  }
//
//  "genCaseClass" should "should generate big case classes for case classes with more than 22 fields" in {
//    CodegenOps.genBigCaseClass(
//      caseClassName = "Place",
//      oomCaseClassField = testBigCaseClassFields
//    ) should equal(testBigCaseClass)
//  }
//
//  "genCaseClass" should "should generate normal case classes for case classes with more than 22 fields" in {
//    CodegenOps.genBigCaseClass(
//      caseClassName = "Place",
//      oomCaseClassField = testCaseClassFields
//    ) should equal(testCaseClass)
//  }
//
//  val testCaseClassFields =
//    Seq(
//      CaseClassField("id","Int", None, Some("auto-generated")),
//      CaseClassField("placeCode","Option[String]",Some("None")),
//      CaseClassField("placeName","Option[String]",Some("None")),
//      CaseClassField("addr1","Option[String]",Some("None")),
//      CaseClassField("addr2","Option[String]",Some("None")),
//      CaseClassField("city","Option[String]",Some("None")),
//      CaseClassField("stateCode","Option[String]",Some("None")),
//      CaseClassField("zipCode","Option[String]",Some("None")),
//      CaseClassField("countryCode","Option[String]",Some("None")),
//      CaseClassField("phoneNumber","Option[String]",Some("None"))
//    )
//
//  val testBigCaseClassFields =
//    Seq(
//      CaseClassField("id","Int", None, Some("auto-generated")),
//      CaseClassField("placeCode","Option[String]",Some("None")),
//      CaseClassField("placeName","Option[String]",Some("None")),
//      CaseClassField("addr1","Option[String]",Some("None")),
//      CaseClassField("addr2","Option[String]",Some("None")),
//      CaseClassField("city","Option[String]",Some("None")),
//      CaseClassField("stateCode","Option[String]",Some("None")),
//      CaseClassField("zipCode","Option[String]",Some("None")),
//      CaseClassField("countryCode","Option[String]",Some("None")),
//      CaseClassField("phoneNumber","Option[String]",Some("None")),
//      CaseClassField("primaryCityCode","Option[String]",Some("None")),
//      CaseClassField("checkInTime","Option[String]",Some("None")),
//      CaseClassField("imageUrl","Option[String]",Some("None")),
//      CaseClassField("longitude","Option[BigDecimal]",Some("None")),
//      CaseClassField("latitude","Option[BigDecimal]",Some("None")),
//      CaseClassField("rating","Option[Short]",Some("None")),
//      CaseClassField("checkOutTime","Option[String]",Some("None")),
//      CaseClassField("code1","Option[String]",Some("None"),Some("provider code 1")),
//      CaseClassField("description","Option[String]",Some("None")),
//      CaseClassField("code2","Option[String]",Some("None")),
//      CaseClassField("code3","Option[String]",Some("None")),
//      CaseClassField("code4","Option[String]",Some("None")),
//      CaseClassField("code5","Option[String]",Some("None")),
//      CaseClassField("code6","Option[Long]",Some("None")),
//      CaseClassField("pleaseNote","Option[String]",Some("None")),
//      CaseClassField("voucher","Option[String]",Some("None")),
//      CaseClassField("popularity","Int",Some("0")),
//      CaseClassField("propertyType","String",Some("Hotel")),
//      CaseClassField("sortOrder","Option[String]",Some("None")),
//      CaseClassField("timeZone","Option[String]",Some("None"))
//    )
//
//  val testBigCaseClass =
//"""
//case class Place(
//  _1  :  Place.Place1,
//  _2  :  Place.Place2
//) {
//  def id                       : Int                = _1.id               // auto-generated
//  def placeCode                : Option[String]     = _1.placeCode
//  def placeName                : Option[String]     = _1.placeName
//  def addr1                    : Option[String]     = _1.addr1
//  def addr2                    : Option[String]     = _1.addr2
//  def city                     : Option[String]     = _1.city
//  def stateCode                : Option[String]     = _1.stateCode
//  def zipCode                  : Option[String]     = _1.zipCode
//  def countryCode              : Option[String]     = _1.countryCode
//  def phoneNumber              : Option[String]     = _1.phoneNumber
//  def primaryCityCode          : Option[String]     = _1.primaryCityCode
//  def checkInTime              : Option[String]     = _1.checkInTime
//  def imageUrl                 : Option[String]     = _1.imageUrl
//  def longitude                : Option[BigDecimal] = _1.longitude
//  def latitude                 : Option[BigDecimal] = _1.latitude
//  def rating                   : Option[Short]      = _1.rating
//  def checkOutTime             : Option[String]     = _1.checkOutTime
//  def code1                    : Option[String]     = _1.code1            // provider code 1
//  def description              : Option[String]     = _1.description
//  def code2                    : Option[String]     = _1.code2
//  def code3                    : Option[String]     = _1.code3
//  def code4                    : Option[String]     = _2.code4
//  def code5                    : Option[String]     = _2.code5
//  def code6                    : Option[Long]       = _2.code6
//  def pleaseNote               : Option[String]     = _2.pleaseNote
//  def voucher                  : Option[String]     = _2.voucher
//  def popularity               : Int                = _2.popularity
//  def propertyType             : String             = _2.propertyType
//  def sortOrder                : Option[String]     = _2.sortOrder
//  def timeZone                 : Option[String]     = _2.timeZone
//
//  def copy(
//    id                       : Int                = id
//    placeCode                : Option[String]     = placeCode
//    placeName                : Option[String]     = placeName
//    addr1                    : Option[String]     = addr1
//    addr2                    : Option[String]     = addr2
//    city                     : Option[String]     = city
//    stateCode                : Option[String]     = stateCode
//    zipCode                  : Option[String]     = zipCode
//    countryCode              : Option[String]     = countryCode
//    phoneNumber              : Option[String]     = phoneNumber
//    primaryCityCode          : Option[String]     = primaryCityCode
//    checkInTime              : Option[String]     = checkInTime
//    imageUrl                 : Option[String]     = imageUrl
//    longitude                : Option[BigDecimal] = longitude
//    latitude                 : Option[BigDecimal] = latitude
//    rating                   : Option[Short]      = rating
//    checkOutTime             : Option[String]     = checkOutTime
//    code1                    : Option[String]     = code1
//    description              : Option[String]     = description
//    code2                    : Option[String]     = code2
//    code3                    : Option[String]     = code3
//    code4                    : Option[String]     = code4
//    code5                    : Option[String]     = code5
//    code6                    : Option[Long]       = code6
//    pleaseNote               : Option[String]     = pleaseNote
//    voucher                  : Option[String]     = voucher
//    popularity               : Int                = popularity
//    propertyType             : String             = propertyType
//    sortOrder                : Option[String]     = sortOrder
//    timeZone                 : Option[String]     = timeZone
//  ) : Place = Place(
//    id = id,
//    placeCode = placeCode,
//    placeName = placeName,
//    addr1 = addr1,
//    addr2 = addr2,
//    city = city,
//    stateCode = stateCode,
//    zipCode = zipCode,
//    countryCode = countryCode,
//    phoneNumber = phoneNumber,
//    primaryCityCode = primaryCityCode,
//    checkInTime = checkInTime,
//    imageUrl = imageUrl,
//    longitude = longitude,
//    latitude = latitude,
//    rating = rating,
//    checkOutTime = checkOutTime,
//    code1 = code1,
//    description = description,
//    code2 = code2,
//    code3 = code3,
//    code4 = code4,
//    code5 = code5,
//    code6 = code6,
//    pleaseNote = pleaseNote,
//    voucher = voucher,
//    popularity = popularity,
//    propertyType = propertyType,
//    sortOrder = sortOrder,
//    timeZone = timeZone
//  )
//
//  override def productElement(i: Int) : Any = i match {
//    case n if n < 22 => _1.productElement(i - 0)
//    case n if n < 44 => _2.productElement(i - 22)
//    case _ => throw new IndexOutOfBoundsException
//  }
//  override def productArity : Int = 30
//  override def productIterator = _1.productIterator ++ _2.productIterator
//}
//
//object Place {
//  case class Place1(
//    id              : Int,
//    placeCode       : Option[String],
//    placeName       : Option[String],
//    addr1           : Option[String],
//    addr2           : Option[String],
//    city            : Option[String],
//    stateCode       : Option[String],
//    zipCode         : Option[String],
//    countryCode     : Option[String],
//    phoneNumber     : Option[String],
//    primaryCityCode : Option[String],
//    checkInTime     : Option[String],
//    imageUrl        : Option[String],
//    longitude       : Option[BigDecimal],
//    latitude        : Option[BigDecimal],
//    rating          : Option[Short],
//    checkOutTime    : Option[String],
//    code1           : Option[String],
//    description     : Option[String],
//    code2           : Option[String],
//    code3           : Option[String],
//    code4           : Option[String]
//  )
//  case class Place2(
//    code5        : Option[String],
//    code6        : Option[Long],
//    pleaseNote   : Option[String],
//    voucher      : Option[String],
//    popularity   : Int,
//    propertyType : String,
//    sortOrder    : Option[String],
//    timeZone     : Option[String]
//  )
//  def apply(
//    id              : Int,
//    placeCode       : Option[String]      = None,
//    placeName       : Option[String]      = None,
//    addr1           : Option[String]      = None,
//    addr2           : Option[String]      = None,
//    city            : Option[String]      = None,
//    stateCode       : Option[String]      = None,
//    zipCode         : Option[String]      = None,
//    countryCode     : Option[String]      = None,
//    phoneNumber     : Option[String]      = None,
//    primaryCityCode : Option[String]      = None,
//    checkInTime     : Option[String]      = None,
//    imageUrl        : Option[String]      = None,
//    longitude       : Option[BigDecimal]  = None,
//    latitude        : Option[BigDecimal]  = None,
//    rating          : Option[Short]       = None,
//    checkOutTime    : Option[String]      = None,
//    code1           : Option[String]      = None,
//    description     : Option[String]      = None,
//    code2           : Option[String]      = None,
//    code3           : Option[String]      = None,
//    code4           : Option[String]      = None,
//    code5           : Option[String]      = None,
//    code6           : Option[Long]        = None,
//    pleaseNote      : Option[String]      = None,
//    voucher         : Option[String]      = None,
//    popularity      : Int                 = 0,
//    propertyType    : String              = "Hotel",
//    sortOrder       : Option[String]      = None,
//    timeZone        : Option[String]      = None
//  ) : Place = Place(
//    _1 = Place1(
//      id = id,
//      placeCode = placeCode,
//      placeName = placeName,
//      addr1 = addr1,
//      addr2 = addr2,
//      city = city,
//      stateCode = stateCode,
//      zipCode = zipCode,
//      countryCode = countryCode,
//      phoneNumber = phoneNumber,
//      primaryCityCode = primaryCityCode,
//      checkInTime = checkInTime,
//      imageUrl = imageUrl,
//      longitude = longitude,
//      latitude = latitude,
//      rating = rating,
//      checkOutTime = checkOutTime,
//      code1 = code1,
//      description = description,
//      code2 = code2,
//      code3 = code3,
//      code4 = code4,
//    ),
//    _2 = Place2(
//      code5 = code5,
//      code6 = code6,
//      pleaseNote = pleaseNote,
//      voucher = voucher,
//      popularity = popularity,
//      propertyType = propertyType,
//      sortOrder = sortOrder,
//      timeZone = timeZone
//    )
//  )
//}
//""".trim
//
//  val testCaseClass =
//"""
//case class Place(
//  id              : Int,             // auto-generated
//  placeCode       : Option[String],
//  placeName       : Option[String],
//  addr1           : Option[String],
//  addr2           : Option[String],
//  city            : Option[String],
//  stateCode       : Option[String],
//  zipCode         : Option[String],
//  countryCode     : Option[String],
//  phoneNumber     : Option[String],
//)
//""".trim
//
//}
