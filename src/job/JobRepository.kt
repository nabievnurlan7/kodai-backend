package com.futuris.job

import com.google.gson.Gson

class JobRepository {

    fun getJobs(): String = Gson().toJson(MockData.getForeignJobs())
}