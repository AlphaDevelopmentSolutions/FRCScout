package com.alphadevelopmentsolutions.frcscout.classes

import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar

class Menu(
    @MenuRes val menuResId: Int?,
    val onClick: Toolbar.OnMenuItemClickListener?
)