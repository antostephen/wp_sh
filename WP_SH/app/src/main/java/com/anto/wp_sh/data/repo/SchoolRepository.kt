package com.anto.wp_sh.data.repo

import com.anto.wp_sh.data.model.SchoolDetails
import com.anto.wp_sh.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class SchoolRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun fetchData(): Response<SchoolDetails> {
        return api.getSchoolData()
    }
}