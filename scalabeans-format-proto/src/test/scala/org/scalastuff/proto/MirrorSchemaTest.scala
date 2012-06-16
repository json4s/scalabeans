/*
 * Copyright (c) 2011 ScalaStuff.org (joint venture of Alexander Dvorkovyy and Ruud Diterwich)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.scalastuff.proto

import org.scalastuff.scalabeans.Enum
import com.dyuproject.protostuff.{LinkedBuffer, ProtobufIOUtil}
import org.junit.{Assert, Test}

class MirrorSchemaTest {
  val linkedBuffer = LinkedBuffer.allocate(512)

  import TestFormat._

  @Test
  def testEmptyBean {
    checkSerDeserFormats(new EmptyTestBean) {Assert.assertEquals(_, _)}
  }

  @Test
  def testPrimitiveTypes {
    checkFormats(() => new PrimitiveTypesBean())
  }

  @Test
  def testSimpleTypes {
    checkFormats(() => new SimpleTypesBean())
  }

  @Test
  def testOption {
    checkFormats(() => new OptionTestBean())
  }

  @Test
  def testEnum {
    checkFormats(() => new EnumTestBean())
  }
  
  @Test
  def testJavaEnum {
    checkFormats(() => new JavaEnumTestBean())
  }

  @Test
  def testOptional {
    checkFormats(() => new OptionalTestBean())
  }

  @Test
  def testCompositeBean {
    checkFormats(() => new CompositeTestBean())
  }
}

case class EmptyTestBean

class PrimitiveTypesBean extends TestBean[PrimitiveTypesBean] {
  var bt: Byte = _
  var s: Short = _
  var i: Int = _
  var l: Long = _
  var bool: Boolean = _
  var f: Float = _
  var d: Double = _
  var c: Char = _

  def set1() = {
    bt = 1
    s = 2
    i = 3
    l = 4
    bool = true
    f = 5.0f
    d = 6.0
    c = 'A'
    this
  }

  def assertEquals(other: PrimitiveTypesBean) {
    Assert.assertEquals(bt, other.bt)
    Assert.assertEquals(s, other.s)
    Assert.assertEquals(i, other.i)
    Assert.assertEquals(l, other.l)
    Assert.assertEquals(bool, other.bool)
    Assert.assertEquals(f, other.f, 0.1)
    Assert.assertEquals(d, other.d, 0.1)
    Assert.assertEquals(c, other.c)
  }
}

class SimpleTypesBean extends TestBean[SimpleTypesBean] {
  var s:String = ""
  var bd:BigDecimal = 0
  var bi:BigInt = 0

  def set1() = {
    s = "whatever"
    bd = 1.0
    bi = 2
    this
  }

  def assertEquals(other: SimpleTypesBean) {
    Assert.assertEquals(s, other.s)
    Assert.assertEquals(bd, other.bd)
    Assert.assertEquals(bi, other.bi)
  }
}

class OptionTestBean extends TestBean[OptionTestBean] {
  var p: Option[Int] = None
  var r1: Option[String] = None
  var r2: Option[String] = None

  def set1() = {
    p = Some(1)
    r1 = Some("whatever")
    r2 = Some("")
    this
  }

  def assertEquals(other: OptionTestBean) {
    Assert.assertEquals(p, other.p)
    Assert.assertEquals(r1, other.r1)
    Assert.assertEquals(r2, other.r2)
  }
}

class Gender private ()
object Gender extends Enum[Gender] {
  val Unknown = new Gender
  val M = new Gender
  val F = new Gender
}

class EnumTestBean extends TestBean[EnumTestBean] {
  var e: Gender = Gender.Unknown

  def set1() = {
    e = Gender.M
    this
  }

  def assertEquals(other: EnumTestBean) {
    Assert.assertEquals(e, other.e)
  }
}

class JavaEnumTestBean extends TestBean[JavaEnumTestBean] {
  import java.lang.annotation.RetentionPolicy
  
  var e: RetentionPolicy = RetentionPolicy.CLASS

  def set1() = {
    e = RetentionPolicy.RUNTIME
    this
  }

  def assertEquals(other: JavaEnumTestBean) {
    Assert.assertEquals(e, other.e)
  }
}

class OptionalTestBean extends TestBean[OptionalTestBean] {
  var bt: Byte = 1
  var s: Short = 2
  var i: Int = 3
  var l: Long = 4
  var bool: Boolean = true
  var f: Float = 6.0f
  var d: Double = 7.0
  var c: Char = 'A'
  var str: String = "whatever"
  var bd:BigDecimal = 8.0
  var bi:BigInt = 9

  var timestamp: java.util.Date = new java.util.Date()
  var dateTime: java.sql.Timestamp = java.sql.Timestamp.valueOf("2011-01-30 10:30:59")
  var date: java.sql.Date = java.sql.Date.valueOf("2010-10-25")

  var op: Option[Long] = Some(10)
  var or1: Option[String] = Some("11")
  var or2: Option[String] = Some("12")

  def set1() = {
    bt = 0
    s = 0
    i = 0
    l = 0
    bool = false
    f = 0f
    d = 0.0
    c = 0
    str = ""
    bd = 0
    bi = 0

    timestamp = new java.util.Date(0)
    dateTime = new java.sql.Timestamp(0)
    date = new java.sql.Date(0)

    op = None
    or1 = None
    or2 = Some("")
    this
  }

  def assertEquals(other: OptionalTestBean) {
   Assert.assertEquals(bt, other.bt)
    Assert.assertEquals(s, other.s)
    Assert.assertEquals(i, other.i)
    Assert.assertEquals(l, other.l)
    Assert.assertEquals(bool, other.bool)
    Assert.assertEquals(f, other.f, 0.1)
    Assert.assertEquals(d, other.d, 0.1)
    Assert.assertEquals(c, other.c)
    Assert.assertEquals(str, other.str)
    Assert.assertEquals(bd, other.bd)
    Assert.assertEquals(bi, other.bi)
    Assert.assertEquals(timestamp, other.timestamp)
    Assert.assertEquals(dateTime, other.dateTime)
    Assert.assertEquals(date, other.date)
    Assert.assertEquals(op, other.op)
    Assert.assertEquals(or1, other.or1)
    Assert.assertEquals(or2, other.or2)
  }
}

class CompositeTestBean extends TestBean[CompositeTestBean] {
  var e: EmptyTestBean = new EmptyTestBean
  var pbt: PrimitiveTypesBean = new PrimitiveTypesBean
  var stp: SimpleTypesBean = new SimpleTypesBean
  var opt: OptionTestBean = new OptionTestBean
  var optl: OptionalTestBean = new OptionalTestBean
  var enumt: EnumTestBean = new EnumTestBean

  def set1() = {
    pbt.set1()
    stp.set1()
    opt.set1()
    optl.set1()
    enumt.set1()
    this
  }

  def assertEquals(other: CompositeTestBean) {
    pbt.assertEquals(other.pbt)
    stp.assertEquals(other.stp)
    opt.assertEquals(other.opt)
    optl.assertEquals(other.optl)
    enumt.assertEquals(other.enumt)
  }
}