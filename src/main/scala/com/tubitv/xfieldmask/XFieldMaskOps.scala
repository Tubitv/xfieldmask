package com.tubitv.xfieldmask

import xfieldmask.xfieldmask.XFieldMask

trait XFieldMaskOps {
  def fields: Set[Int]
  def messages: Map[Int, XFieldMask]
  def inverse: Boolean

  def unary_-(): XFieldMask = XFieldMask(fields = fields, messages = messages, inverse = !inverse)

  def union(that: XFieldMaskOps): XFieldMask =  (this.inverse, that.inverse) match {
    case (false, false) => this.signlessUnion(that, inverse = false)
    case (true, true) => this.signlessIntersection(that, inverse = false)
    case (false, true) => that.signlessDifference(this, inverse = true)
    case (true, false) => this.signlessDifference(that, inverse = true)
  }

  def |(that: XFieldMaskOps): XFieldMask = union(that)
  def +(that: XFieldMaskOps): XFieldMask = union(that)

  def intersection(that: XFieldMaskOps): XFieldMask = (this.inverse, that.inverse) match {
    case (false, false) => this.signlessIntersection(that, inverse = false)
    case (true, true) => this.signlessUnion(that, inverse = false)
    case (false, true) => this.signlessDifference(that, inverse = false)
    case (true, false) => that.signlessDifference(this, inverse = false)
  }

  def &(that: XFieldMaskOps): XFieldMask = intersection(that)

  def diff(that: XFieldMaskOps): XFieldMask = (this.inverse, that.inverse) match {
    case (false, false) => this.signlessDifference(that, inverse = false)
    case (true, true) => this.signlessUnion(that, inverse = true)
    case (false, true) => this.signlessIntersection(that, inverse = false)
    case (true, false) => this.signlessDifference(that, inverse = true)
  }

  def &~(that: XFieldMaskOps): XFieldMask = diff(that)

  private def signlessUnion(that: XFieldMaskOps, inverse: Boolean): XFieldMask = {
    val newFields = this.fields union that.fields
    val commonKeys = this.messages.keySet intersect that.messages.keySet
    val differentKeys = (this.messages.keySet union that.messages.keySet) diff commonKeys
    val newMessages = ((for {
      key <- commonKeys
      thisMessage = this.messages(key)
      thatMessage = that.messages(key)
    } yield key -> (thisMessage union thatMessage)) ++ (for {
      key <- differentKeys
      message = this.messages.getOrElse(key, that.messages(key))
    } yield key -> message)).toMap
    new XFieldMask(fields = newFields, messages = newMessages, inverse = inverse)
  }

  private def signlessIntersection(that: XFieldMaskOps, inverse: Boolean): XFieldMask = {
    val newFields = this.fields & that.fields
    val commonKeys = this.messages.keySet & that.messages.keySet
    val newMessages = (for {
      key <- commonKeys
      thisMessage = this.messages(key)
      thatMessage = that.messages(key)
    } yield key -> (thisMessage & thatMessage)).toMap
    new XFieldMask(fields = newFields, messages = newMessages, inverse = inverse)
  }

  private def signlessDifference(that: XFieldMaskOps, inverse: Boolean): XFieldMask = {
    val newFields = this.fields diff that.fields
    val commonKeys = this.messages.keySet.intersect(that.messages.keySet)
    val onlyThisKeys = this.messages.keySet &~ that.messages.keySet
    val newMessages = ((for {
      key <- commonKeys
      thisMessage = this.messages(key)
      thatMessage = that.messages(key)
    } yield key -> (thisMessage diff thatMessage)) ++ (for {
      key <- onlyThisKeys
      message = this.messages(key)
    } yield key -> message)).toMap
    new XFieldMask(fields = newFields, messages = newMessages, inverse = inverse)
  }
}
