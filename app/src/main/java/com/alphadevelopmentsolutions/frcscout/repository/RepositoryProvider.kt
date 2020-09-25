package com.alphadevelopmentsolutions.frcscout.repository

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase

class RepositoryProvider private constructor(context: Context) {

    private val rDatabase: RDatabase by lazy {
        RDatabase.getInstance(context)
    }

    companion object {

        private var INSTANCE: RepositoryProvider? = null

        fun getInstance(context: Context): RepositoryProvider {

            return INSTANCE ?: let {

                val tempInstance = RepositoryProvider(context)

                INSTANCE = tempInstance

                tempInstance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    val matchRepo by lazy {
        MatchRepository(
            rDatabase.getMatchDao(),
            context
        )
    }
}