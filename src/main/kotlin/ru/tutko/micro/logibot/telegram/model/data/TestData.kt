package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TestData(val rndInt: Int, val rndLong: Long): Payload()