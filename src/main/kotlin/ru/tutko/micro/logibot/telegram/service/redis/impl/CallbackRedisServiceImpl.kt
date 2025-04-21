package ru.tutko.micro.logibot.telegram.service.redis.impl

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.component.TelegramSerialize
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import java.time.Duration
import java.util.*

@Service
class CallbackRedisServiceImpl(
	private val redisTemplate: RedisTemplate<String, Any>
) : CallbackRedisService {

	private fun generateUniqueKey(): String {
		var key: String
		do {
			key = UUID.randomUUID().toString()
		} while (redisTemplate.hasKey(key))
		return key
	}

	override fun create(userId: String, value: CallbackData<Payload>, ttl: Duration): String {
		val key = generateUniqueKey()
		val serialized = TelegramSerialize.serializeData(value)
		redisTemplate.opsForValue().set(key, serialized, ttl)
		redisTemplate.opsForList().rightPush(userId, key)
		return key
	}

	override fun set(key: String, value: CallbackData<Payload>, ttl: Duration) {
		val serialized = TelegramSerialize.serializeData(value)
		redisTemplate.opsForValue().set(key, serialized, ttl)
	}

	@Suppress("UNCHECKED_CAST")
	override fun pop(key: String): CallbackData<Payload>? {
		val raw = redisTemplate.opsForValue().get(key) ?: return null
		val callbackData = TelegramSerialize.deserializeData(raw.toString()) as CallbackData<Payload>
		redisTemplate.delete(key)
		return callbackData
	}

	@Suppress("UNCHECKED_CAST")
	override fun get(key: String): CallbackData<Payload>? {
		val raw = redisTemplate.opsForValue().get(key) ?: return null
		return TelegramSerialize.deserializeData(raw.toString()) as CallbackData<Payload>
	}

	override fun clearChatIdUserId(userId: String) {
		val keys = redisTemplate.opsForList().range(userId, 0, -1) ?: return
		keys.forEach { redisTemplate.delete(it.toString()) }
		redisTemplate.delete(userId)
	}

	override fun remove(key: String) {
		redisTemplate.delete(key)
	}

	override fun containsKey(key: String): Boolean {
		return redisTemplate.hasKey(key)
	}
}