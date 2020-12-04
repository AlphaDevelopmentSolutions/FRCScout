package com.alphadevelopmentsolutions.frcscout.callbacks

import java.io.Serializable

interface OnItemSelectedListener : Serializable {
    fun onItemSelected(selectedItem: Any)
}