package com.tubitv.xfieldmask

import org.scalatest.wordspec.AnyWordSpec
import com.tubitv.xfieldmask.XFieldMask
import com.tubitv.xfieldmask.test.{InnerTestProto, TestProto}

class BasicSpec extends AnyWordSpec {
  "BasicSpec" should {
    "pass" in {
      println(TestProto(
        intField = 1,
        stringField = "string",
      ))
      println(XFieldMask(
        fields = Set(TestProto.INT_FIELD_FIELD_NUMBER, TestProto.STRING_FIELD_FIELD_NUMBER),
        messages = Map(
          TestProto.INNER_FIELD_FIELD_NUMBER -> XFieldMask(
            fields = Set(InnerTestProto.STRING_FIELD_FIELD_NUMBER),
          ),
        ),
      ))
    }
  }
}
