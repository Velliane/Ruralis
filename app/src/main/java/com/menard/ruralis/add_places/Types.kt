package com.menard.ruralis.add_places

import androidx.annotation.StringRes
import com.menard.ruralis.R

enum class Types(@StringRes val res: Int) {

    DAIRY(R.string.type_dairy_farmer),
    VEGETABLE(R.string.type_vegetable);
}