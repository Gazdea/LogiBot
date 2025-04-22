package ru.tutko.micro.logibot.telegram.service.redis

import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.data.Payload
import java.time.Duration

interface CallbackRedisService {

	fun create(userId: String, value: CallbackData<Payload>, ttl: Duration = Duration.ofMinutes(5)): String

	fun createMany(userId: String, values: List<CallbackData<Payload>>, ttl: Duration = Duration.ofMinutes(5)): List<Pair<String, CallbackData<Payload>>>

	fun set(key: String, value: CallbackData<Payload>, ttl: Duration = Duration.ofMinutes(5))

	fun pop(key: String): CallbackData<Payload>?

	fun remove(key: String)

	fun containsKey(key: String): Boolean

	fun get(key: String): CallbackData<Payload>?

	fun clearUserId(userId: String)

	fun containsUserId(userId: String): Boolean
}