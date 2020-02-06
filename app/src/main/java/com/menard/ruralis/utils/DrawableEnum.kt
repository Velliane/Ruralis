package com.menard.ruralis.utils

import com.menard.ruralis.R

enum class DrawableEnum: ImageShowed{

    EGG{
        override fun getImageResource(): Int = R.drawable.egg
    },
    RABBIT{
        override fun getImageResource(): Int = R.drawable.rabbit
    },
    COW{
        override fun getImageResource(): Int = R.drawable.cow
    },
    GOAT{
        override fun getImageResource(): Int = R.drawable.goat
    },
    SHEEP{
        override fun getImageResource(): Int = R.drawable.sheep
    };

}

interface ImageShowed {
    fun getImageResource(): Int
}