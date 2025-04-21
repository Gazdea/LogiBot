package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import ru.tutko.micro.logibot.telegram.service.redis.impl.CallbackRedisServiceImpl

@Serializable
data class CallbackData<T : Payload>(
	val handler: String,
	val className: String? = null,
	val data: T? = null
){
	constructor(enum: CallbackQueryEnum, data: T?) : this(
		handler = enum.value,
		className = data?.let { it::class.java.name },
		data = data
	)

	constructor(enum: InputEnum, data: T?) : this(
		handler = enum.value,
		className = data?.let { it::class.java.name },
		data = data
	)

	constructor(enum: InputEnum) : this(handler = enum.value)

	constructor(enum: CallbackQueryEnum) : this(handler = enum.value)

	companion object {
		fun <T : Payload> of(enum: CallbackQueryEnum, data: T?): CallbackData<T> {
			return CallbackData(
				handler = enum.value,
				className = data?.let { it::class.java.name },
				data = data
			)
		}
	}

}