package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.classes.SuccessErrorCode
import com.google.gson.annotations.SerializedName

interface ApiResponse {

    interface Set {
        class Base(
            @SerializedName("checklist_item_result") val checklistItemResult: SuccessErrorCode,
            @SerializedName("robot_info") val robotInfo: SuccessErrorCode,
            @SerializedName("robot_media") val robotMedia: SuccessErrorCode,
            @SerializedName("scout_card_info") val scoutCardInfo: SuccessErrorCode
        ) {

            fun fullSuccess() = checklistItemResult.success &&
                robotInfo.success &&
                robotMedia.success &&
                scoutCardInfo.success

            /**
             * Get any errors that were returned from the [Api] call
             * @return [List] [SuccessErrorCode]
             */
            fun getErrors(): List<SuccessErrorCode> {

                val errorCodes = mutableListOf<SuccessErrorCode>()

                if (!checklistItemResult.success)
                    errorCodes.add(checklistItemResult)

                if (!robotInfo.success)
                    errorCodes.add(robotInfo)

                if (!robotMedia.success)
                    errorCodes.add(robotMedia)

                if (!scoutCardInfo.success)
                    errorCodes.add(scoutCardInfo)

                // Iterate over all errors and remove duplicates
                val nonDuplicatedErrorCodes = mutableListOf<SuccessErrorCode>()
                errorCodes.iterator().let {

                    while (it.hasNext()) {

                        val errorCode = it.next()

                        var exists = false

                        nonDuplicatedErrorCodes.forEach { nonDuplicatedErrorCode ->

                            if (nonDuplicatedErrorCode.error == errorCode.error && nonDuplicatedErrorCode.message == errorCode.message) {
                                exists = true
                                return@let
                            }
                        }

                        if (!exists)
                            nonDuplicatedErrorCodes.add(errorCode)
                    }
                }

                return nonDuplicatedErrorCodes.toList()
            }
        }
    }
}