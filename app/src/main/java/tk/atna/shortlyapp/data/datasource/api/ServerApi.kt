package tk.atna.shortlyapp.data.datasource.api

import retrofit2.http.POST
import retrofit2.http.Query
import tk.atna.shortlyapp.data.model.ServerResponse
import tk.atna.shortlyapp.data.model.ShortenedUrlDto

interface ServerApi {

    @POST("shorten")
    suspend fun shortenUrl(
        @Query("url") url: String
    ): ServerResponse<ShortenedUrlDto>
}