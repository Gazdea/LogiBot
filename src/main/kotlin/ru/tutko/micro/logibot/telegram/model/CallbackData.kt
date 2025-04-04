package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import kotlin.reflect.full.starProjectedType

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class CallbackData(
	val handler: String,
	val data: String = "{}"
) {
	constructor(handler: CallbackQueryEnum, params: Any? = null) : this(
		handler = handler.value,
		data = params?.let {
			ProtoBuf.encodeToHexString(ProtoBuf.serializersModule.serializer(it::class.starProjectedType), it)
		} ?: "{}"
	)

	constructor(handler: InputEnum, params: Any? = null) : this(
		handler = handler.value,
		data = params?.let {
			ProtoBuf.encodeToHexString(ProtoBuf.serializersModule.serializer(it::class.starProjectedType), it)
		} ?: "{}"
	)

	inline fun <reified T> getData(): T? =
		runCatching { ProtoBuf.decodeFromHexString<T>(data) }.getOrNull()
}