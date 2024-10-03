package com.anto.wp_sh.data.network

import com.anto.wp_sh.data.model.SchoolDetails
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/resource/s3k6-pzi2.json")
    suspend fun getSchoolData(): Response<SchoolDetails>

}