package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import kotlin.reflect.full.starProjectedType

@Serializable
data class CallbackData(
	val handler: String,
	val data: String = "{}"
) {
	constructor(handler: CallbackQueryEnum, params: Any? = null) : this(
		handler = handler.value,
		data = params?.let { Json.encodeToString(Json.serializersModule.serializer(it::class.starProjectedType), it) } ?: "{}"
	)

	constructor(handler: InputEnum, params: Any? = null) : this(
		handler = handler.value,
		data = params?.let { Json.encodeToString(Json.serializersModule.serializer(it::class.starProjectedType), it) } ?: "{}"
	)

	inline fun <reified T> getData(): T? = runCatching { Json.decodeFromString<T>(data) }.getOrNull()
}
