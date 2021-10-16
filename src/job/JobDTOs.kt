package com.futuris.job

data class Job(
        val id: String,
        val company: Company,
        val salary: String,
        val location: String,
        val teammates: List<Teammate>,
        val positionName: String,
        val positionDescription: String,
        val isActive: Boolean
)

data class Company(
        val name: String,
        val established: String = "2001",
        val employees: String = "120",
        val headquarterLocation: String = "Moscow, Russia",
        val slides: List<String>,
        val website: String = "",
        val description: String = ""
)

data class Teammate(
        val name: String,
        val position: String,
        val experience: String,
        val photo: String
)