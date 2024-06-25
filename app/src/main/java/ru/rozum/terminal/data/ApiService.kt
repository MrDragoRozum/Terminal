package ru.rozum.terminal.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeFrame}/2022-01-09/2023-02-10?adjusted=true&sort=desc&limit=50000&apiKey=69pSXc63ZkdTZXI9BOyMMMMUa8WJ_MWi")
    suspend fun loadBars(@Path("timeFrame") timeFrame: String): Result
}