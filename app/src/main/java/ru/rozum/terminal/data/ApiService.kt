package ru.rozum.terminal.data

import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2022-01-09/2023-02-10?adjusted=true&sort=asc&limit=50000&apiKey=69pSXc63ZkdTZXI9BOyMMMMUa8WJ_MWi")
    suspend fun loadBars(): Result
}