package com.alphadevelopmentsolutions.frcscout.bindings

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.alphadevelopmentsolutions.frcscout.data.models.DataType
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardInfoFormBinding
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutFieldInfoBinding
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoView

@BindingAdapter("robotInfoForm")
fun LinearLayout.setRobotInfoForm(robotInfoList: List<RobotInfoView>) {
    val layoutInflater = LayoutInflater.from(context)

    var previousRobotInfoView: RobotInfoView? = null
    robotInfoList.forEach { robotInfoView ->

        val fieldInfoLayout = LayoutFieldInfoBinding.inflate(layoutInflater).apply {
            this.robotInfoView = robotInfoView

            val robotInfoKey = robotInfoView.robotInfoKeyView.robotInfoKey

            when (robotInfoView.robotInfoKeyView.dataType.backendDataType){
                DataType.BackendDataType.STRING -> {
                    textEditText.addTextChangedListener {

                        val robotInfo = robotInfoView.robotInfo
                        robotInfo.value = it?.toString() ?: ""

                        launchIO {
                            RepositoryProvider.getInstance(context).robotInfoRepository.insert(robotInfo)
                        }
                    }
                }

                DataType.BackendDataType.INTEGER -> {
                    val robotInfo = robotInfoView.robotInfo
                    val min = robotInfoKey.min
                    val max = robotInfoKey.max

                    var currentValue =
                        try {
                            val parsedValue = robotInfo.value.toInt()

                            if (min != null && max != null) {
                                if (parsedValue >= min && parsedValue <= max)
                                    parsedValue
                                else
                                    min
                            }
                            else if (min != null) {
                                if (parsedValue >= min)
                                    parsedValue
                                else
                                    min
                            }
                            else if (max != null) {
                                if (parsedValue <= max)
                                    parsedValue
                                else
                                    0
                            }
                            else
                                0
                        }
                        catch (e: NumberFormatException) {
                            min ?: 0
                        }

                    plusButton.setOnClickListener {
                        val newValue = currentValue + 1

                        if (max != null && newValue > max)
                            Toast.makeText(context, "This field has a maximum value of $max", Toast.LENGTH_SHORT).show()
                        else {
                            currentValue = newValue

                            robotInfo.value = newValue.toString()

                            infoKeyValueTextView.text = robotInfo.value
                        }
                    }

                    minusButton.setOnClickListener {
                        val newValue = currentValue -1

                        if (min != null && newValue < min)
                            Toast.makeText(context, "This field has a minimum value of $min", Toast.LENGTH_SHORT).show()
                        else {
                            currentValue = newValue

                            robotInfo.value = newValue.toString()

                            infoKeyValueTextView.text = robotInfo.value
                        }
                    }
                }

                DataType.BackendDataType.BOOLEAN -> {
                    val robotInfo = robotInfoView.robotInfo

                    booleanCheckbox.setOnCheckedChangeListener { _, isChecked ->
                        robotInfo.value = isChecked.toString()

                        launchIO {
                            RepositoryProvider.getInstance(context).robotInfoRepository.insert(robotInfo)
                        }
                    }
                }
            }
        }

        if (!previousRobotInfoView?.robotInfoKeyView?.robotInfoKeyState?.id.contentEquals(robotInfoView.robotInfoKeyView.robotInfoKeyState.id)) {
            addView(
                LayoutCardInfoFormBinding.inflate(layoutInflater).apply {
                    cardTitle.text = robotInfoView.robotInfoKeyView.robotInfoKeyState.name
                    fieldsLayout.addView(fieldInfoLayout.root)
                }.root
            )
        }
        else {
            addView(fieldInfoLayout.root)
        }

        previousRobotInfoView = robotInfoView
    }
}