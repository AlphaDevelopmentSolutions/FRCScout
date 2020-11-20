package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.RDatabase

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
            RDatabase.destroyInstance()
            INSTANCE = null
        }
    }

    val checklistItemRepository by lazy {
        ChecklistItemRepository(
            rDatabase.getChecklistItemDao()
        )
    }

    val checklistItemResultRepository by lazy {
        ChecklistItemResultRepository(
            rDatabase.getChecklistItemResultDao()
        )
    }

    val dataTypeRepository by lazy {
        DataTypeRepository(
            rDatabase.getDataTypeDao()
        )
    }

    val eventRepository by lazy {
        EventRepository(
            rDatabase.getEventDao()
        )
    }

    val eventTeamListRepository by lazy {
        EventTeamListRepository(
            rDatabase.getEventTeamListDao()
        )
    }

    val matchRepo by lazy {
        MatchRepository(
            rDatabase.getMatchDao()
        )
    }

    val robotInfoKeRepository by lazy {
        RobotInfoKeRepository(
            rDatabase.getRobotInfoKeyDao()
        )
    }

    val robotInfoRepository by lazy {
        RobotInfoRepository(
            rDatabase.getRobotInfoDao()
        )
    }

    val robotMediaRepository by lazy {
        RobotMediaRepository(
            rDatabase.getRobotMediaDao()
        )
    }

    val roleRepository by lazy {
        RoleRepository(
            rDatabase.getRoleDao()
        )
    }

    val scoutCardInfoKeyRepository by lazy {
        ScoutCardInfoKeyRepository(
            rDatabase.geScoutCardInfoKeyDao()
        )
    }

    val scoutCardInfoKeyStateRepository by lazy {
        ScoutCardInfoKeyStateRepository(
            rDatabase.getScoutCardInfoKeyStateDao()
        )
    }

    val scoutCardInfoRepository by lazy {
        ScoutCardInfoRepository(
            rDatabase.getScoutCardInfoDao()
        )
    }

    val teamAccountRepository by lazy {
        TeamAccountRepository(
            rDatabase.getTeamAccountDao()
        )
    }

    val userRepository by lazy {
        UserRepository(
            rDatabase.getUserDao()
        )
    }

    val userRoleRepository by lazy {
        UserRoleRepository(
            rDatabase.getUserRoleDao()
        )
    }

    val userTeamAccountRepository by lazy {
        UserTeamAccountRepository(
            rDatabase.getUserTeamAccountDao()
        )
    }

    val yearRepository by lazy {
        YearRepository(
            rDatabase.getYearDao()
        )
    }
}