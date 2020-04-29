package com.menard.ruralis.add_places

import androidx.annotation.StringRes
import com.menard.ruralis.R

enum class OpeningEnum(@StringRes val res: Int) {

    MONDAY_AM(R.string.monday_am),
    MONDAY_PM(R.string.monday_pm),
    TUESDAY_AM(R.string.tuesday_am),
    TUESDAY_PM(R.string.tuesday_pm),
    WEDNESDAY_AM(R.string.wednesday_am),
    WEDNESDAY_PM(R.string.wednesday_pm),
    THURSDAY_AM(R.string.thursday_am),
    THURSDAY_PM(R.string.thursday_pm),
    FRIDAY_AM(R.string.friday_am),
    FRIDAY_PM(R.string.friday_pm),
    SATURDAY_AM(R.string.saturday_am),
    SATURDAY_PM(R.string.saturday_pm),
    SUNDAY_AM(R.string.sunday_am),
    SUNDAY_PM(R.string.sunday_pm)
}