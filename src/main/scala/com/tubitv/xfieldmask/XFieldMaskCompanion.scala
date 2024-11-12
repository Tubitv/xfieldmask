package com.tubitv.xfieldmask

import com.tubitv.xfieldmask.XFieldMask

trait XFieldMaskCompanion {
  val Empty = XFieldMask.defaultInstance
  val Full = Empty.copy(inverse = true)
}
