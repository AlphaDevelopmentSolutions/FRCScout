package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter(value = ["fabShowOwner", "fabShowData"])
fun FloatingActionButton.observeShow(lifecycleOwner: LifecycleOwner, shouldShow: LiveData<Boolean>) {
    shouldShow.observe(lifecycleOwner) {
        if (it)
            show()
        else
            hide()
    }
}
