package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.api.AppData
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.repository.*

class ApiViewModel(application: Application) : AndroidViewModel(application) {

    private val checklistItemRepo by lazy {
        ChecklistItemRepository(
                RDatabase
                        .getInstance(application)
                        .checklistItemDao()
        )
    }

    private val checklistItemResultRepo by lazy {
        ChecklistItemResultRepository(
                RDatabase
                        .getInstance(application)
                        .checklistItemResultDao()
        )
    }

    private val eventRepo by lazy {
        EventRepository(
                RDatabase
                        .getInstance(application)
                        .eventDao()
        )
    }

    private val eventTeamListRepo by lazy {
        EventTeamListRepository(
                RDatabase
                        .getInstance(application)
                        .eventTeamListDao()
        )
    }

    private val matchRepo by lazy {
        MatchRepository(
                RDatabase
                        .getInstance(application)
                        .matchDao()
        )
    }

    private val robotInfoRepo by lazy {
        RobotInfoRepository(
                RDatabase
                        .getInstance(application)
                        .robotInfoDao()
        )
    }

    private val robotInfoKeyRepo by lazy {
        RobotInfoKeyRepository(
                RDatabase
                        .getInstance(application)
                        .robotInfoKeyDao()
        )
    }

    private val robotMediaRepo by lazy {
        RobotMediaRepository(
                RDatabase
                        .getInstance(application)
                        .robotMediaDao()
        )
    }

    private val scoutCardInfoRepo by lazy {
        ScoutCardInfoRepository(
                RDatabase
                        .getInstance(application)
                        .scoutCardInfoDao()
        )
    }

    private val scoutCardInfoKeyRepo by lazy {
        ScoutCardInfoKeyRepository(
                RDatabase
                        .getInstance(application)
                        .scoutCardInfoKeyDao()
        )
    }

    private val teamRepo by lazy {
        TeamRepository(
                RDatabase
                        .getInstance(application)
                        .teamDao()
        )
    }

    private val userRepo by lazy {
        UserRepository(
                RDatabase
                        .getInstance(application)
                        .userDao()
        )
    }

    private val yearRepo by lazy {
        YearRepository(
                RDatabase
                        .getInstance(application)
                        .yearDao()
        )
    }

    /**
     * Fetches data from the server
     * @return [Boolean] if successful
     */
    suspend fun fetchData(): Boolean {

        val response =
                Api
                    .getInstance()
                    .getData()
                    .execute()

        if(response.isSuccessful) {
            response.body()?.let { data ->

                checklistItemRepo.insertAll(data.checklistItems)
                checklistItemResultRepo.insertAll(data.checklistItemResults)
                eventRepo.insertAll(data.events)
                eventTeamListRepo.insertAll(data.eventTeamList)
                matchRepo.insertAll(data.matches)
                robotInfoRepo.insertAll(data.robotInfo)
                robotInfoKeyRepo.insertAll(data.robotInfoKeys)
                robotMediaRepo.insertAll(data.robotMedia)
                scoutCardInfoRepo.insertAll(data.scoutCardInfo)
                scoutCardInfoKeyRepo.insertAll(data.scoutCardInfoKeys)
                teamRepo.insertAll(data.teams)
                userRepo.insertAll(data.users)
                yearRepo.insertAll(data.years)

                return true
            }
        }

        return false
    }

    /**
     * Submits data to the server
     */
    suspend fun submitData(): Boolean {

        //TODO: Submit Data

        val appData = AppData(

        )



        return false
    }
}