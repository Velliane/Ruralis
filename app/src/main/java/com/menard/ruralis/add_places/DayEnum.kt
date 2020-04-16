package com.menard.ruralis.add_places

import androidx.annotation.StringRes
import com.menard.ruralis.R

enum class DayEnum(@StringRes val res: Int) {

    MONDAY(R.string.monday),
    TUESDAY(R.string.tuesday),
    WEDNESDAY(R.string.wednesday),
    THURSDAY(R.string.thursday),
    FRIDAY(R.string.friday),
    SATURDAY(R.string.saturday),
    SUNDAY(R.string.sunday)
}