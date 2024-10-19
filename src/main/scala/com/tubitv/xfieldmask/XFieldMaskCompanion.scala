package com.tubitv.xfieldmask

import xfieldmask.xfieldmask.XFieldMask

trait XFieldMaskCompanion {
  val Empty = XFieldMask()
  val Full = XFieldMask(inverse = true)
}
