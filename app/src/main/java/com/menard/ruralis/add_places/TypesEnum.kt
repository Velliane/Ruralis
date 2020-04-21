package com.menard.ruralis.add_places

import androidx.annotation.StringRes
import com.menard.ruralis.R

enum class TypesEnum(@StringRes val res: Int) {

    DAIRY(R.string.type_dairy_farmer),
    VEGETABLE(R.string.type_vegetable),
    HONEY(R.string.type_honey),
    EDUCATIONAL_FARM(R.string.type_educational_farm),
    POULTRY(R.string.type_egg_poultry),
    BEEF_MEAT(R.string.type_beef_meat),
    MIXED_LIVESTOCK(R.string.type_mixed_livestock),
    FISH(R.string.type_fish),
    OTHER(R.string.type_other)
}