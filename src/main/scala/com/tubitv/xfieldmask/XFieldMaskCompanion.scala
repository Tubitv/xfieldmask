package com.tubitv.xfieldmask

import com.tubitv.xfieldmask.XFieldMask

trait XFieldMaskCompanion {
  val Empty = XFieldMask()
  val Full = XFieldMask(inverse = true)
}
