package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.extension.toByteArray
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import java.util.*

@Entity(
    indices = [
        Index(
            "id"
        )
    ]
)
abstract class Table protected constructor(
    @PrimaryKey var id: ByteArray = Constants.UUID_GENERATOR.generate().toByteArray(),
    var isDraft: Boolean = false
) {

    abstract override fun toString(): String
}
