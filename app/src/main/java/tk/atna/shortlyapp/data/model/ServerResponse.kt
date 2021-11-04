package tk.atna.shortlyapp.data.model

import com.google.gson.annotations.SerializedName

data class ServerResponse<T>(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("result")
    val result: T
)