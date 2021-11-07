package tk.atna.shortlyapp.domain.model

import retrofit2.HttpException
import retrofit2.Response

class ApiException(
    val errorMessage: String,
    response: Response<*>
) : HttpException(response)