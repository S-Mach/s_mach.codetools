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
         .t1i .,::;;; ;1tt        Copyright (c) 2016 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools

import org.scalatest.{FlatSpec, Matchers}
import s_mach.codetools.codegen.CaseClassCodegen
import s_mach.codetools.codegen.CaseClassCodegen.Field

class CaseClassCodegenTest extends FlatSpec with Matchers {
  import CaseClassCodegenTest._

  "genBigCaseClass" should "generate big case classes" in {
    CaseClassCodegen.genBigCaseClass("TestBig",testBigFields) should equal(testBigCaseClass)
  }

  "genCaseClass" should "should generate big case classes for case classes with more than 22 fields" in {
    CaseClassCodegen.genCaseClass("TestBig",testBigFields) should equal(testBigCaseClass)
  }

  "genCaseClass" should "should generate normal case classes for case classes with more than 22 fields" in {
    CaseClassCodegen.genCaseClass("TestNormal",testFields) should equal(testCaseClass)
  }
}

object CaseClassCodegenTest {

  val testFields =
    Vector(
      Field("id","Int", None, Some("auto-generated")),
      Field("placeCode","Option[String]",Some("None")),
      Field("placeName","Option[String]",Some("None")),
      Field("addr1","Option[String]",Some("None")),
      Field("addr2","Option[String]",Some("None")),
      Field("city","Option[String]",Some("None")),
      Field("stateCode","Option[String]",Some("None")),
      Field("zipCode","Option[String]",Some("None")),
      Field("countryCode","Option[String]",Some("None")),
      Field("phoneNumber","Option[String]",Some("None"))
    )

  val testBigFields =
    Vector(
      Field("id","Int", None, Some("auto-generated")),
      Field("placeCode","Option[String]",Some("None")),
      Field("placeName","Option[String]",Some("None")),
      Field("addr1","Option[String]",Some("None")),
      Field("addr2","Option[String]",Some("None")),
      Field("city","Option[String]",Some("None")),
      Field("stateCode","Option[String]",Some("None")),
      Field("zipCode","Option[String]",Some("None")),
      Field("countryCode","Option[String]",Some("None")),
      Field("phoneNumber","Option[String]",Some("None")),
      Field("primaryCityCode","Option[String]",Some("None")),
      Field("checkInTime","Option[String]",Some("None")),
      Field("imageUrl","Option[String]",Some("None")),
      Field("longitude","Option[BigDecimal]",Some("None")),
      Field("latitude","Option[BigDecimal]",Some("None")),
      Field("rating","Option[Short]",Some("None")),
      Field("checkOutTime","Option[String]",Some("None")),
      Field("code1","Option[String]",Some("None"),Some("comment 2")),
      Field("description","Option[String]",Some("None")),
      Field("code2","Option[String]",Some("None")),
      Field("code3","Option[String]",Some("None")),
      Field("code4","Option[String]",Some("None")),
      Field("code5","Option[String]",Some("None")),
      Field("code6","Option[Long]",Some("None")),
      Field("pleaseNote","Option[String]",Some("None")),
      Field("voucher","Option[String]",Some("None")),
      Field("popularity","Int",Some("0")),
      Field("propertyType","String",Some("\"test\"")),
      Field("sortOrder","Option[String]",Some("None")),
      Field("timeZone","Option[String]",Some("None"))
    )

  val testBigCaseClass =
"""
case class TestBig(
  _1 : TestBig.TestBig1,
  _2 : TestBig.TestBig2
) {
  def id              : Int                = _1.id              // auto-generated
  def placeCode       : Option[String]     = _1.placeCode
  def placeName       : Option[String]     = _1.placeName
  def addr1           : Option[String]     = _1.addr1
  def addr2           : Option[String]     = _1.addr2
  def city            : Option[String]     = _1.city
  def stateCode       : Option[String]     = _1.stateCode
  def zipCode         : Option[String]     = _1.zipCode
  def countryCode     : Option[String]     = _1.countryCode
  def phoneNumber     : Option[String]     = _1.phoneNumber
  def primaryCityCode : Option[String]     = _1.primaryCityCode
  def checkInTime     : Option[String]     = _1.checkInTime
  def imageUrl        : Option[String]     = _1.imageUrl
  def longitude       : Option[BigDecimal] = _1.longitude
  def latitude        : Option[BigDecimal] = _1.latitude
  def rating          : Option[Short]      = _1.rating
  def checkOutTime    : Option[String]     = _1.checkOutTime
  def code1           : Option[String]     = _1.code1           // comment 2
  def description     : Option[String]     = _1.description
  def code2           : Option[String]     = _1.code2
  def code3           : Option[String]     = _1.code3
  def code4           : Option[String]     = _1.code4
  def code5           : Option[String]     = _2.code5
  def code6           : Option[Long]       = _2.code6
  def pleaseNote      : Option[String]     = _2.pleaseNote
  def voucher         : Option[String]     = _2.voucher
  def popularity      : Int                = _2.popularity
  def propertyType    : String             = _2.propertyType
  def sortOrder       : Option[String]     = _2.sortOrder
  def timeZone        : Option[String]     = _2.timeZone

  def copy(
    id              : Int                = _1.id,
    placeCode       : Option[String]     = _1.placeCode,
    placeName       : Option[String]     = _1.placeName,
    addr1           : Option[String]     = _1.addr1,
    addr2           : Option[String]     = _1.addr2,
    city            : Option[String]     = _1.city,
    stateCode       : Option[String]     = _1.stateCode,
    zipCode         : Option[String]     = _1.zipCode,
    countryCode     : Option[String]     = _1.countryCode,
    phoneNumber     : Option[String]     = _1.phoneNumber,
    primaryCityCode : Option[String]     = _1.primaryCityCode,
    checkInTime     : Option[String]     = _1.checkInTime,
    imageUrl        : Option[String]     = _1.imageUrl,
    longitude       : Option[BigDecimal] = _1.longitude,
    latitude        : Option[BigDecimal] = _1.latitude,
    rating          : Option[Short]      = _1.rating,
    checkOutTime    : Option[String]     = _1.checkOutTime,
    code1           : Option[String]     = _1.code1,
    description     : Option[String]     = _1.description,
    code2           : Option[String]     = _1.code2,
    code3           : Option[String]     = _1.code3,
    code5           : Option[String]     = _2.code5,
    code6           : Option[Long]       = _2.code6,
    pleaseNote      : Option[String]     = _2.pleaseNote,
    voucher         : Option[String]     = _2.voucher,
    popularity      : Int                = _2.popularity,
    propertyType    : String             = _2.propertyType,
    sortOrder       : Option[String]     = _2.sortOrder
  ) : TestBig = TestBig(
    id              = id,
    placeCode       = placeCode,
    placeName       = placeName,
    addr1           = addr1,
    addr2           = addr2,
    city            = city,
    stateCode       = stateCode,
    zipCode         = zipCode,
    countryCode     = countryCode,
    phoneNumber     = phoneNumber,
    primaryCityCode = primaryCityCode,
    checkInTime     = checkInTime,
    imageUrl        = imageUrl,
    longitude       = longitude,
    latitude        = latitude,
    rating          = rating,
    checkOutTime    = checkOutTime,
    code1           = code1,
    description     = description,
    code2           = code2,
    code3           = code3,
    code5           = code5,
    code6           = code6,
    pleaseNote      = pleaseNote,
    voucher         = voucher,
    popularity      = popularity,
    propertyType    = propertyType,
    sortOrder       = sortOrder
  )

  override def productElement(i: Int) : Any = i match {
    case n if n < 22 => _1.productElement(i - 0)
    case n if n < 44 => _2.productElement(i - 22)
    case _ => throw new IndexOutOfBoundsException
  }
  override def productArity : Int = 30
  override def productIterator = _1.productIterator ++ _2.productIterator
}

object TestBig {
  case class TestBig1(
    id              : Int,                       // auto-generated
    placeCode       : Option[String]     = None,
    placeName       : Option[String]     = None,
    addr1           : Option[String]     = None,
    addr2           : Option[String]     = None,
    city            : Option[String]     = None,
    stateCode       : Option[String]     = None,
    zipCode         : Option[String]     = None,
    countryCode     : Option[String]     = None,
    phoneNumber     : Option[String]     = None,
    primaryCityCode : Option[String]     = None,
    checkInTime     : Option[String]     = None,
    imageUrl        : Option[String]     = None,
    longitude       : Option[BigDecimal] = None,
    latitude        : Option[BigDecimal] = None,
    rating          : Option[Short]      = None,
    checkOutTime    : Option[String]     = None,
    code1           : Option[String]     = None, // comment 2
    description     : Option[String]     = None,
    code2           : Option[String]     = None,
    code3           : Option[String]     = None,
    code4           : Option[String]     = None
  )
  case class TestBig2(
    code5        : Option[String] = None,
    code6        : Option[Long]   = None,
    pleaseNote   : Option[String] = None,
    voucher      : Option[String] = None,
    popularity   : Int            = 0,
    propertyType : String         = "test",
    sortOrder    : Option[String] = None,
    timeZone     : Option[String] = None
  )
  def apply(
    id              : Int,                         // auto-generated
    placeCode       : Option[String]     = None,
    placeName       : Option[String]     = None,
    addr1           : Option[String]     = None,
    addr2           : Option[String]     = None,
    city            : Option[String]     = None,
    stateCode       : Option[String]     = None,
    zipCode         : Option[String]     = None,
    countryCode     : Option[String]     = None,
    phoneNumber     : Option[String]     = None,
    primaryCityCode : Option[String]     = None,
    checkInTime     : Option[String]     = None,
    imageUrl        : Option[String]     = None,
    longitude       : Option[BigDecimal] = None,
    latitude        : Option[BigDecimal] = None,
    rating          : Option[Short]      = None,
    checkOutTime    : Option[String]     = None,
    code1           : Option[String]     = None,   // comment 2
    description     : Option[String]     = None,
    code2           : Option[String]     = None,
    code3           : Option[String]     = None,
    code4           : Option[String]     = None,
    code5           : Option[String]     = None,
    code6           : Option[Long]       = None,
    pleaseNote      : Option[String]     = None,
    voucher         : Option[String]     = None,
    popularity      : Int                = 0,
    propertyType    : String             = "test",
    sortOrder       : Option[String]     = None,
    timeZone        : Option[String]     = None
  ) : TestBig = TestBig(
    _1 = TestBig1(
      id              = id,
      placeCode       = placeCode,
      placeName       = placeName,
      addr1           = addr1,
      addr2           = addr2,
      city            = city,
      stateCode       = stateCode,
      zipCode         = zipCode,
      countryCode     = countryCode,
      phoneNumber     = phoneNumber,
      primaryCityCode = primaryCityCode,
      checkInTime     = checkInTime,
      imageUrl        = imageUrl,
      longitude       = longitude,
      latitude        = latitude,
      rating          = rating,
      checkOutTime    = checkOutTime,
      code1           = code1,
      description     = description,
      code2           = code2,
      code3           = code3,
      code4           = code4
    ),
    _2 = TestBig2(
      code5        = code5,
      code6        = code6,
      pleaseNote   = pleaseNote,
      voucher      = voucher,
      popularity   = popularity,
      propertyType = propertyType,
      sortOrder    = sortOrder,
      timeZone     = timeZone
    )
  )
}
""".trim
case class TestBig(
  _1 : TestBig.TestBig1,
  _2 : TestBig.TestBig2
) {
  def id              : Int                = _1.id              // auto-generated
  def placeCode       : Option[String]     = _1.placeCode       
  def placeName       : Option[String]     = _1.placeName       
  def addr1           : Option[String]     = _1.addr1           
  def addr2           : Option[String]     = _1.addr2           
  def city            : Option[String]     = _1.city            
  def stateCode       : Option[String]     = _1.stateCode       
  def zipCode         : Option[String]     = _1.zipCode         
  def countryCode     : Option[String]     = _1.countryCode     
  def phoneNumber     : Option[String]     = _1.phoneNumber     
  def primaryCityCode : Option[String]     = _1.primaryCityCode 
  def checkInTime     : Option[String]     = _1.checkInTime     
  def imageUrl        : Option[String]     = _1.imageUrl        
  def longitude       : Option[BigDecimal] = _1.longitude       
  def latitude        : Option[BigDecimal] = _1.latitude        
  def rating          : Option[Short]      = _1.rating          
  def checkOutTime    : Option[String]     = _1.checkOutTime    
  def code1           : Option[String]     = _1.code1           // comment 2
  def description     : Option[String]     = _1.description     
  def code2           : Option[String]     = _1.code2           
  def code3           : Option[String]     = _1.code3           
  def code4           : Option[String]     = _1.code4           
  def code5           : Option[String]     = _2.code5           
  def code6           : Option[Long]       = _2.code6           
  def pleaseNote      : Option[String]     = _2.pleaseNote      
  def voucher         : Option[String]     = _2.voucher         
  def popularity      : Int                = _2.popularity      
  def propertyType    : String             = _2.propertyType    
  def sortOrder       : Option[String]     = _2.sortOrder       
  def timeZone        : Option[String]     = _2.timeZone        

  def copy(
    id              : Int                = _1.id,
    placeCode       : Option[String]     = _1.placeCode,
    placeName       : Option[String]     = _1.placeName,
    addr1           : Option[String]     = _1.addr1,
    addr2           : Option[String]     = _1.addr2,
    city            : Option[String]     = _1.city,
    stateCode       : Option[String]     = _1.stateCode,
    zipCode         : Option[String]     = _1.zipCode,
    countryCode     : Option[String]     = _1.countryCode,
    phoneNumber     : Option[String]     = _1.phoneNumber,
    primaryCityCode : Option[String]     = _1.primaryCityCode,
    checkInTime     : Option[String]     = _1.checkInTime,
    imageUrl        : Option[String]     = _1.imageUrl,
    longitude       : Option[BigDecimal] = _1.longitude,
    latitude        : Option[BigDecimal] = _1.latitude,
    rating          : Option[Short]      = _1.rating,
    checkOutTime    : Option[String]     = _1.checkOutTime,
    code1           : Option[String]     = _1.code1,
    description     : Option[String]     = _1.description,
    code2           : Option[String]     = _1.code2,
    code3           : Option[String]     = _1.code3,
    code5           : Option[String]     = _2.code5,
    code6           : Option[Long]       = _2.code6,
    pleaseNote      : Option[String]     = _2.pleaseNote,
    voucher         : Option[String]     = _2.voucher,
    popularity      : Int                = _2.popularity,
    propertyType    : String             = _2.propertyType,
    sortOrder       : Option[String]     = _2.sortOrder
  ) : TestBig = TestBig(
    id              = id,
    placeCode       = placeCode,
    placeName       = placeName,
    addr1           = addr1,
    addr2           = addr2,
    city            = city,
    stateCode       = stateCode,
    zipCode         = zipCode,
    countryCode     = countryCode,
    phoneNumber     = phoneNumber,
    primaryCityCode = primaryCityCode,
    checkInTime     = checkInTime,
    imageUrl        = imageUrl,
    longitude       = longitude,
    latitude        = latitude,
    rating          = rating,
    checkOutTime    = checkOutTime,
    code1           = code1,
    description     = description,
    code2           = code2,
    code3           = code3,
    code5           = code5,
    code6           = code6,
    pleaseNote      = pleaseNote,
    voucher         = voucher,
    popularity      = popularity,
    propertyType    = propertyType,
    sortOrder       = sortOrder
  )

  override def productElement(i: Int) : Any = i match {
    case n if n < 22 => _1.productElement(i - 0)
    case n if n < 44 => _2.productElement(i - 22)
    case _ => throw new IndexOutOfBoundsException
  }
  override def productArity : Int = 30
  override def productIterator = _1.productIterator ++ _2.productIterator
}

object TestBig {
  case class TestBig1(
    id              : Int,                       // auto-generated
    placeCode       : Option[String]     = None,
    placeName       : Option[String]     = None,
    addr1           : Option[String]     = None,
    addr2           : Option[String]     = None,
    city            : Option[String]     = None,
    stateCode       : Option[String]     = None,
    zipCode         : Option[String]     = None,
    countryCode     : Option[String]     = None,
    phoneNumber     : Option[String]     = None,
    primaryCityCode : Option[String]     = None,
    checkInTime     : Option[String]     = None,
    imageUrl        : Option[String]     = None,
    longitude       : Option[BigDecimal] = None,
    latitude        : Option[BigDecimal] = None,
    rating          : Option[Short]      = None,
    checkOutTime    : Option[String]     = None,
    code1           : Option[String]     = None, // comment 2
    description     : Option[String]     = None,
    code2           : Option[String]     = None,
    code3           : Option[String]     = None,
    code4           : Option[String]     = None
  )
  case class TestBig2(
    code5        : Option[String] = None,
    code6        : Option[Long]   = None,
    pleaseNote   : Option[String] = None,
    voucher      : Option[String] = None,
    popularity   : Int            = 0,
    propertyType : String         = "test",
    sortOrder    : Option[String] = None,
    timeZone     : Option[String] = None
  )
  def apply(
    id              : Int,                         // auto-generated
    placeCode       : Option[String]     = None,
    placeName       : Option[String]     = None,
    addr1           : Option[String]     = None,
    addr2           : Option[String]     = None,
    city            : Option[String]     = None,
    stateCode       : Option[String]     = None,
    zipCode         : Option[String]     = None,
    countryCode     : Option[String]     = None,
    phoneNumber     : Option[String]     = None,
    primaryCityCode : Option[String]     = None,
    checkInTime     : Option[String]     = None,
    imageUrl        : Option[String]     = None,
    longitude       : Option[BigDecimal] = None,
    latitude        : Option[BigDecimal] = None,
    rating          : Option[Short]      = None,
    checkOutTime    : Option[String]     = None,
    code1           : Option[String]     = None,   // comment 2
    description     : Option[String]     = None,
    code2           : Option[String]     = None,
    code3           : Option[String]     = None,
    code4           : Option[String]     = None,
    code5           : Option[String]     = None,
    code6           : Option[Long]       = None,
    pleaseNote      : Option[String]     = None,
    voucher         : Option[String]     = None,
    popularity      : Int                = 0,
    propertyType    : String             = "test",
    sortOrder       : Option[String]     = None,
    timeZone        : Option[String]     = None
  ) : TestBig = TestBig(
    _1 = TestBig1(
      id              = id,
      placeCode       = placeCode,
      placeName       = placeName,
      addr1           = addr1,
      addr2           = addr2,
      city            = city,
      stateCode       = stateCode,
      zipCode         = zipCode,
      countryCode     = countryCode,
      phoneNumber     = phoneNumber,
      primaryCityCode = primaryCityCode,
      checkInTime     = checkInTime,
      imageUrl        = imageUrl,
      longitude       = longitude,
      latitude        = latitude,
      rating          = rating,
      checkOutTime    = checkOutTime,
      code1           = code1,
      description     = description,
      code2           = code2,
      code3           = code3,
      code4           = code4
    ),
    _2 = TestBig2(
      code5        = code5,
      code6        = code6,
      pleaseNote   = pleaseNote,
      voucher      = voucher,
      popularity   = popularity,
      propertyType = propertyType,
      sortOrder    = sortOrder,
      timeZone     = timeZone
    )
  )
}
  val testCaseClass =
"""
case class TestNormal(
  id          : Int,                   // auto-generated
  placeCode   : Option[String] = None,
  placeName   : Option[String] = None,
  addr1       : Option[String] = None,
  addr2       : Option[String] = None,
  city        : Option[String] = None,
  stateCode   : Option[String] = None,
  zipCode     : Option[String] = None,
  countryCode : Option[String] = None,
  phoneNumber : Option[String] = None
)
""".trim
// Ensure code above compiles
case class Test2(
  id          : Int,                   // auto-generated
  placeCode   : Option[String] = None,
  placeName   : Option[String] = None,
  addr1       : Option[String] = None,
  addr2       : Option[String] = None,
  city        : Option[String] = None,
  stateCode   : Option[String] = None,
  zipCode     : Option[String] = None,
  countryCode : Option[String] = None,
  phoneNumber : Option[String] = None
)

}
