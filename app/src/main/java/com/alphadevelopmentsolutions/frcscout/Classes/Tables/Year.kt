package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class Year(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var name: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE,
        var imageUri: String = DEFAULT_STRING) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "years"
        const val COLUMN_NAME_NAME = "Name"
        const val COLUMN_NAME_START_DATE = "StartDate"
        const val COLUMN_NAME_END_DATE = "EndDate"
        const val COLUMN_NAME_IMAGE_URI = "ImageUri"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_START_DATE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_END_DATE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_IMAGE_URI, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [Year] with specified filters from [database]
         * @param year if specified, filters [Year] by [year] id
         * @param database used to load [Year]
         * @return [ArrayList] of [Year]
         */
        fun getObjects(year: Year?, database: Database): ArrayList<Year> {
            return ArrayList<Year>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (year != null) {
                    whereStatement.append("${if(year.localId != DEFAULT_LONG) COLUMN_NAME_LOCAL_ID else COLUMN_NAME_SERVER_ID} = ?")
                    whereArgs.add(
                            if(year.localId != DEFAULT_LONG)
                                year.localId.toString()
                            else
                                year.serverId.toString()
                        )
                }

                //add all object records to array list
                with(database.getObjects(
                        TABLE_NAME,
                        whereStatement.toString(),
                        whereArgs))
                {
                    if (this != null) {
                        while (moveToNext()) {
                            add(

                                 Year(
                                    getLong(COLUMN_NAME_LOCAL_ID),
                                    getLong(COLUMN_NAME_SERVER_ID),
                                    getString(COLUMN_NAME_NAME),
                                    getDate(COLUMN_NAME_START_DATE),
                                    getDate(COLUMN_NAME_END_DATE),
                                    getString(COLUMN_NAME_IMAGE_URI)
                                )
                            )
                        }

                        close()
                    }
                }
            }
        }
    }
    
    /**
     * Gets a bitmap version of the object
     * @return bitmap version of object
     */
    val imageBitmap: Bitmap
        get() = BitmapFactory.decodeFile(this.imageUri)


    override fun toString(): String
    {
        return "$serverId - $name"
    }

    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@Year.loadParentValues(this)
                    this@Year.name = name
                    this@Year.startDate = startDate
                    this@Year.endDate = endDate
                    this@Year.imageUri = imageUri
                    return true
                }
            }
        }

        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_NAME, name)
            put(COLUMN_NAME_START_DATE, startDate)
            put(COLUMN_NAME_END_DATE, endDate)
            put(COLUMN_NAME_IMAGE_URI, imageUri)
        }
}
