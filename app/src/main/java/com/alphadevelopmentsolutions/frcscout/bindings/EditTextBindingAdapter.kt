package com.alphadevelopmentsolutions.frcscout.bindings

import android.text.TextWatcher

import android.widget.EditText

import androidx.databinding.BindingAdapter

object EditTextBindingAdapter {
    @BindingAdapter("textChangedListener")
    fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher?) {
        editText.addTextChangedListener(textWatcher)
    }
}
