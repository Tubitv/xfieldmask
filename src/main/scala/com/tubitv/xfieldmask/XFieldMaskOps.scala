package com.tubitv.xfieldmask

import xfieldmask.xfieldmask.XFieldMask

trait XFieldMaskOps {
  def fields: Set[Int]
  def messages: Map[Int, XFieldMask]
  def inverse: Boolean

  def unary_-(): XFieldMask = XFieldMask(fields = fields, messages = messages, inverse = !inverse)

  def +(that: XFieldMaskOps): XFieldMask = {


    // TODO: add support for negative masks
    val newFields = this.fields ++ that.fields
    val (commonKeys, differentKeys) = commonAndDifferentMessageKeys(that)
    val newMessages = ((for {
      key <- commonKeys
      thisMessage = this.messages(key)
      thatMessage = that.messages(key)
    } yield key -> (thisMessage + thatMessage)) ++ (for {
      key <- differentKeys
      message = this.messages.getOrElse(key, that.messages(key))
    } yield key -> message)).toMap
    new XFieldMask(newFields, newMessages)
  }

  private def commonAndDifferentMessageKeys(that: XFieldMaskOps): (Set[Int], Set[Int]) = {
    val commonMessageKeys = this.messages.keySet.intersect(that.messages.keySet)
    val differentMessageKeys = this.messages.keySet.union(that.messages.keySet).diff(commonMessageKeys)
    (commonMessageKeys, differentMessageKeys)
  }
}
