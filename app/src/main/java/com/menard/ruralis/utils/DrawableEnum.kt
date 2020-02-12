package com.menard.ruralis.utils

import androidx.annotation.DrawableRes
import com.menard.ruralis.R

enum class DrawableEnum(@DrawableRes val drawableId: Int){

    EGG(R.drawable.egg),
    RABBIT(R.drawable.rabbit),
    COW(R.drawable.cow),
    GOAT( R.drawable.goat ),
    SHEEP(R.drawable.sheep);

}
