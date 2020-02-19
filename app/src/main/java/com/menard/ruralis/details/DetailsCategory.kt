package com.menard.ruralis.details

import androidx.annotation.StringRes
import com.menard.ruralis.R

enum class DetailsCategory(@StringRes val title: Int) {

    INFO(R.string.tab_info),
    PHOTOS(R.string.tab_photos),
    CONTACT(R.string.tab_contact);

}