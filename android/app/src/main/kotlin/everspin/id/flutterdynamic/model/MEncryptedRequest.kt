package everspin.id.flutterdynamic.model

import com.google.gson.annotations.SerializedName

data class MEncryptedRequest(
    @SerializedName("payload")
    val payload : String,
    @SerializedName("evToken")
    val evToken : String,
    @SerializedName("evEncDesc")
    val evEncDesc : String
)
