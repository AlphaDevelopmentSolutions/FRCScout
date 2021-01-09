package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity()
abstract class SubmittableTable protected constructor(
    @SerializedName("deleted_date") @ColumnInfo(name = "deleted_date") var deletedDate: Date? = null,
    @SerializedName("deleted_by_id") @ColumnInfo(name = "deleted_by_id", index = true) var deletedById: ByteArray? = null,
    @SerializedName("modified_by_id") @ColumnInfo(name = "modified_by_id", index = true) var modifiedById: ByteArray
) : Table() {

    abstract override fun toString(): String

    fun markDeleted(account: Account) {
        deletedDate = Date()
        deletedById = account.id

        markModified(account)
    }

    fun markModified(account: Account) {
        lastModifiedDate = Date()
        modifiedById = account.id
    }
}
