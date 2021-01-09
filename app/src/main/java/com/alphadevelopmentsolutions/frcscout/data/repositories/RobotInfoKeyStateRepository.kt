package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoKeyStateDao
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKeyState

class RobotInfoKeyStateRepository(private val dao: RobotInfoKeyStateDao) : MasterRepository<RobotInfoKeyState>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}