package com.alphadevelopmentsolutions.frcscout.bindings

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.*
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardInfoFormBinding
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutFieldInfoBinding
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.extensions.runOnUiThread
import com.alphadevelopmentsolutions.frcscout.extensions.toByteArray
import com.alphadevelopmentsolutions.frcscout.interfaces.Constant
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoKeyView
import com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard.ScoutCardInfoKeyView
import java.util.concurrent.CountDownLatch

@BindingAdapter(value = ["robotInfoForm", "event", "team"])
fun LinearLayout.setRobotInfoForm(robotInfoKeyViewList: List<RobotInfoKeyView>, event: Event?, team: Team) {
    if (robotInfoKeyViewList.isNotEmpty()) {
        val layoutInflater = LayoutInflater.from(context)

        var previousRobotInfoKeyView: RobotInfoKeyView? = null
        var currentLayoutCardInfoFormBinding: LayoutCardInfoFormBinding? = null

        val account = Account.getInstance(context)

        if (account != null && event != null) {
            launchIO {
                val robotInfoList = RepositoryProvider.getInstance(context).robotInfoRepository.getForTeamAtEvent(team, event)

                robotInfoKeyViewList.forEach { robotInfoKeyView ->

                    // Robot info doesn't get updated, the deleted time gets set
                    // and a new record is created
                    var tempCurrentRobotInfo: RobotInfo? = null

                    robotInfoList.forEach { potentialRobotInfo ->
                        if (potentialRobotInfo.keyId.contentEquals(robotInfoKeyView.robotInfoKey.id)) {
                            tempCurrentRobotInfo = potentialRobotInfo
                        }
                    }

                    val finalCurrentRobotInfo = tempCurrentRobotInfo

                    val latch = CountDownLatch(1)

                    lateinit var fieldInfoLayout: LayoutFieldInfoBinding

                    runOnUiThread {
                        fieldInfoLayout = LayoutFieldInfoBinding.inflate(layoutInflater)

                        latch.countDown()
                    }

                    latch.await()

                    fieldInfoLayout.apply {
                        val newRobotInfo: RobotInfo by lazy {
                            if (finalCurrentRobotInfo?.isDraft == false || finalCurrentRobotInfo == null)
                                RobotInfo.create().apply {
                                    id = Constant.UUID_GENERATOR.generate().toByteArray()
                                    this.eventId = event.id
                                    this.teamId = team.id
                                    keyId = robotInfoKeyView.robotInfoKey.id
                                    completedById = account.id
                                }
                            else
                                finalCurrentRobotInfo
                        }

                        this.title = robotInfoKeyView.robotInfoKey.name
                        this.backendDataType = robotInfoKeyView.dataType.backendDataType

                        val robotInfoKey = robotInfoKeyView.robotInfoKey

                        when (robotInfoKeyView.dataType.backendDataType) {
                            DataType.BackendDataType.STRING -> {
                                textEditText.addTextChangedListener {

                                    newRobotInfo.value = it?.toString() ?: ""
                                    newRobotInfo.markModified(account)

                                    launchIO {
                                        RepositoryProvider.getInstance(context).robotInfoRepository.insert(newRobotInfo)

                                        if (finalCurrentRobotInfo?.isDraft == false && finalCurrentRobotInfo.deletedDate == null) {
                                            finalCurrentRobotInfo.markDeleted(account)
                                            finalCurrentRobotInfo.let {
                                                RepositoryProvider.getInstance(context).robotInfoRepository.delete(finalCurrentRobotInfo)
                                            }
                                        }
                                    }
                                }
                            }

                            DataType.BackendDataType.INTEGER -> {
                                val min = robotInfoKey.min
                                val max = robotInfoKey.max

                                var currentValue =
                                    try {
                                        val parsedValue = finalCurrentRobotInfo?.value?.toInt()
                                            ?: min ?: 0

                                        if (min != null && max != null) {
                                            if (parsedValue >= min && parsedValue <= max)
                                                parsedValue
                                            else
                                                min
                                        } else if (min != null) {
                                            if (parsedValue >= min)
                                                parsedValue
                                            else
                                                min
                                        } else if (max != null) {
                                            if (parsedValue <= max)
                                                parsedValue
                                            else
                                                0
                                        } else
                                            0
                                    } catch (e: NumberFormatException) {
                                        min ?: 0
                                    }

                                newRobotInfo.value = currentValue.toString()

                                plusButton.setOnClickListener {
                                    val newValue = currentValue + 1

                                    if (max != null && newValue > max)
                                        Toast.makeText(context, "This field has a maximum value of $max", Toast.LENGTH_SHORT).show()
                                    else {
                                        currentValue = newValue

                                        newRobotInfo.value = newValue.toString()
                                        newRobotInfo.markModified(account)

                                        infoKeyValueTextView.text = newRobotInfo.value

                                        launchIO {
                                            RepositoryProvider.getInstance(context).robotInfoRepository.insert(newRobotInfo)

                                            if (finalCurrentRobotInfo?.isDraft == false && finalCurrentRobotInfo.deletedDate == null) {
                                                finalCurrentRobotInfo.markDeleted(account)
                                                finalCurrentRobotInfo.let {
                                                    RepositoryProvider.getInstance(context).robotInfoRepository.delete(finalCurrentRobotInfo)
                                                }
                                            }
                                        }
                                    }
                                }

                                minusButton.setOnClickListener {
                                    val newValue = currentValue - 1

                                    if (min != null && newValue < min)
                                        Toast.makeText(context, "This field has a minimum value of $min", Toast.LENGTH_SHORT).show()
                                    else {
                                        currentValue = newValue

                                        newRobotInfo.value = newValue.toString()
                                        newRobotInfo.markModified(account)
                                        infoKeyValueTextView.text = newRobotInfo.value

                                        launchIO {
                                            RepositoryProvider.getInstance(context).robotInfoRepository.insert(newRobotInfo)

                                            if (finalCurrentRobotInfo?.isDraft == false && finalCurrentRobotInfo.deletedDate == null) {
                                                finalCurrentRobotInfo.markDeleted(account)
                                                finalCurrentRobotInfo.let {
                                                    RepositoryProvider.getInstance(context).robotInfoRepository.delete(finalCurrentRobotInfo)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            DataType.BackendDataType.BOOLEAN -> {
                                booleanCheckbox.setOnCheckedChangeListener { _, isChecked ->
                                    newRobotInfo.value = isChecked.toString()

                                    launchIO {
                                        RepositoryProvider.getInstance(context).robotInfoRepository.insert(newRobotInfo)

                                        if (finalCurrentRobotInfo?.isDraft == false && finalCurrentRobotInfo.deletedDate == null) {
                                            finalCurrentRobotInfo.markDeleted(account)
                                            finalCurrentRobotInfo.let {
                                                RepositoryProvider.getInstance(context).robotInfoRepository.delete(finalCurrentRobotInfo)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        this.value = newRobotInfo.value
                    }

                    val uiLatch = CountDownLatch(1)

                    if (!previousRobotInfoKeyView?.robotInfoKeyState?.id.contentEquals(robotInfoKeyView.robotInfoKeyState.id)) {
                        runOnUiThread {
                            LayoutCardInfoFormBinding.inflate(layoutInflater).apply {
                                currentLayoutCardInfoFormBinding = this

                                cardTitle.text = robotInfoKeyView.robotInfoKeyState.name
                                fieldsLayout.addView(fieldInfoLayout.root)

                                addView(root)
                            }


                            uiLatch.countDown()
                        }
                    } else {
                        runOnUiThread {
                            currentLayoutCardInfoFormBinding?.fieldsLayout?.addView(fieldInfoLayout.root)

                            uiLatch.countDown()
                        }
                    }

                    uiLatch.await()

                    previousRobotInfoKeyView = robotInfoKeyView
                }
            }
        }
    }
}

@BindingAdapter(value = ["scoutCardInfoForm", "match", "team"])
fun LinearLayout.setScoutCardInfoForm(scoutCardInfoKeyViewList: List<ScoutCardInfoKeyView>, match: Match, team: Team) {
    if (scoutCardInfoKeyViewList.isNotEmpty()) {
        val layoutInflater = LayoutInflater.from(context)

        var previousScoutCardInfoKeyView: ScoutCardInfoKeyView? = null
        var currentLayoutCardInfoFormBinding: LayoutCardInfoFormBinding? = null

        val account = Account.getInstance(context)

        if (account != null) {
            launchIO {
                val scoutCardInfoList = RepositoryProvider.getInstance(context).scoutCardInfoRepository.getForTeamInMatch(team, match)

                scoutCardInfoKeyViewList.forEach { scoutCardInfoKeyView ->

                    // Robot info doesn't get updated, the deleted time gets set
                    // and a new record is created
                    var tempCurrentScoutCardInfo: ScoutCardInfo? = null

                    scoutCardInfoList.forEach { potentialScoutCardInfo ->
                        if (potentialScoutCardInfo.keyId.contentEquals(scoutCardInfoKeyView.scoutCardInfoKey.id)) {
                            tempCurrentScoutCardInfo = potentialScoutCardInfo
                        }
                    }

                    val finalCurrentScoutCardInfo = tempCurrentScoutCardInfo

                    val latch = CountDownLatch(1)

                    lateinit var fieldInfoLayout: LayoutFieldInfoBinding

                    runOnUiThread {
                        fieldInfoLayout = LayoutFieldInfoBinding.inflate(layoutInflater)

                        latch.countDown()
                    }

                    latch.await()

                    fieldInfoLayout.apply {
                        val newScoutCardInfo: ScoutCardInfo by lazy {
                            if (finalCurrentScoutCardInfo?.isDraft == false || finalCurrentScoutCardInfo == null)
                                ScoutCardInfo.create().apply {
                                    id = Constant.UUID_GENERATOR.generate().toByteArray()
                                    this.matchId = match.id
                                    this.teamId = team.id
                                    keyId = scoutCardInfoKeyView.scoutCardInfoKey.id
                                    completedById = account.id
                                }
                            else
                                finalCurrentScoutCardInfo
                        }

                        this.title = scoutCardInfoKeyView.scoutCardInfoKey.name
                        this.backendDataType = scoutCardInfoKeyView.dataType.backendDataType

                        val scoutCardInfoKey = scoutCardInfoKeyView.scoutCardInfoKey

                        when (scoutCardInfoKeyView.dataType.backendDataType) {
                            DataType.BackendDataType.STRING -> {
                                textEditText.addTextChangedListener {

                                    newScoutCardInfo.value = it?.toString() ?: ""
                                    newScoutCardInfo.markModified(account)

                                    launchIO {
                                        RepositoryProvider.getInstance(context).scoutCardInfoRepository.insert(newScoutCardInfo)

                                        if (finalCurrentScoutCardInfo?.isDraft == false && finalCurrentScoutCardInfo.deletedDate == null) {
                                            finalCurrentScoutCardInfo.markDeleted(account)
                                            finalCurrentScoutCardInfo.let {
                                                RepositoryProvider.getInstance(context).scoutCardInfoRepository.delete(finalCurrentScoutCardInfo)
                                            }
                                        }
                                    }
                                }
                            }

                            DataType.BackendDataType.INTEGER -> {
                                val min = scoutCardInfoKey.min
                                val max = scoutCardInfoKey.max

                                var currentValue =
                                    try {
                                        val parsedValue = finalCurrentScoutCardInfo?.value?.toInt()
                                            ?: min ?: 0

                                        if (min != null && max != null) {
                                            if (parsedValue >= min && parsedValue <= max)
                                                parsedValue
                                            else
                                                min
                                        } else if (min != null) {
                                            if (parsedValue >= min)
                                                parsedValue
                                            else
                                                min
                                        } else if (max != null) {
                                            if (parsedValue <= max)
                                                parsedValue
                                            else
                                                0
                                        } else
                                            0
                                    } catch (e: NumberFormatException) {
                                        min ?: 0
                                    }

                                newScoutCardInfo.value = currentValue.toString()

                                plusButton.setOnClickListener {
                                    val newValue = currentValue + 1

                                    if (max != null && newValue > max)
                                        Toast.makeText(context, "This field has a maximum value of $max", Toast.LENGTH_SHORT).show()
                                    else {
                                        currentValue = newValue

                                        newScoutCardInfo.value = newValue.toString()
                                        newScoutCardInfo.markModified(account)

                                        infoKeyValueTextView.text = newScoutCardInfo.value

                                        launchIO {
                                            RepositoryProvider.getInstance(context).scoutCardInfoRepository.insert(newScoutCardInfo)

                                            if (finalCurrentScoutCardInfo?.isDraft == false && finalCurrentScoutCardInfo.deletedDate == null) {
                                                finalCurrentScoutCardInfo.markDeleted(account)
                                                finalCurrentScoutCardInfo.let {
                                                    RepositoryProvider.getInstance(context).scoutCardInfoRepository.delete(finalCurrentScoutCardInfo)
                                                }
                                            }
                                        }
                                    }
                                }

                                minusButton.setOnClickListener {
                                    val newValue = currentValue - 1

                                    if (min != null && newValue < min)
                                        Toast.makeText(context, "This field has a minimum value of $min", Toast.LENGTH_SHORT).show()
                                    else {
                                        currentValue = newValue

                                        newScoutCardInfo.value = newValue.toString()
                                        newScoutCardInfo.markModified(account)
                                        infoKeyValueTextView.text = newScoutCardInfo.value

                                        launchIO {
                                            RepositoryProvider.getInstance(context).scoutCardInfoRepository.insert(newScoutCardInfo)

                                            if (finalCurrentScoutCardInfo?.isDraft == false && finalCurrentScoutCardInfo.deletedDate == null) {
                                                finalCurrentScoutCardInfo.markDeleted(account)
                                                finalCurrentScoutCardInfo.let {
                                                    RepositoryProvider.getInstance(context).scoutCardInfoRepository.delete(finalCurrentScoutCardInfo)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            DataType.BackendDataType.BOOLEAN -> {
                                booleanCheckbox.setOnCheckedChangeListener { _, isChecked ->
                                    newScoutCardInfo.value = isChecked.toString()

                                    launchIO {
                                        RepositoryProvider.getInstance(context).scoutCardInfoRepository.insert(newScoutCardInfo)

                                        if (finalCurrentScoutCardInfo?.isDraft == false && finalCurrentScoutCardInfo.deletedDate == null) {
                                            finalCurrentScoutCardInfo.markDeleted(account)
                                            finalCurrentScoutCardInfo.let {
                                                RepositoryProvider.getInstance(context).scoutCardInfoRepository.delete(finalCurrentScoutCardInfo)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        this.value = newScoutCardInfo.value
                    }

                    val uiLatch = CountDownLatch(1)

                    if (!previousScoutCardInfoKeyView?.scoutCardInfoKeyState?.id.contentEquals(scoutCardInfoKeyView.scoutCardInfoKeyState.id)) {
                        runOnUiThread {
                            LayoutCardInfoFormBinding.inflate(layoutInflater).apply {
                                currentLayoutCardInfoFormBinding = this

                                cardTitle.text = scoutCardInfoKeyView.scoutCardInfoKeyState.name
                                fieldsLayout.addView(fieldInfoLayout.root)

                                addView(root)
                            }


                            uiLatch.countDown()
                        }
                    } else {
                        runOnUiThread {
                            currentLayoutCardInfoFormBinding?.fieldsLayout?.addView(fieldInfoLayout.root)

                            uiLatch.countDown()
                        }
                    }

                    uiLatch.await()

                    previousScoutCardInfoKeyView = scoutCardInfoKeyView
                }
            }
        }
    }
}