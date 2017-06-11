package cz.vojta.unidarchitecture.tracks

import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApiService {
    @GET("tracks")
    fun tracks(@Query("limit") limit: Int, @Query("client_id") cliendId: String): io.reactivex.Single<List<Track>>
}
