package com.menard.ruralis.model.textsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlusCode {
    @SerializedName("compound_code")
    @Expose
    var compoundCode: String? = null
    @SerializedName("global_code")
    @Expose
    var globalCode: String? = null

}