package uz.max.anychart.api

data class ResponseData <T>(
    val status:String,
    val message:String = "Users",
    val data: T? = null
)