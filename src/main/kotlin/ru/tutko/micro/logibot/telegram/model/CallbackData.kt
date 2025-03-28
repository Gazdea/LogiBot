package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.Serializable
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum

@Serializable
data class CallbackData(
	val handler: String,
	val data: Map<String, String> = mapOf(),
)
{
	constructor(handler: CallbackQueryEnum, vararg params: Pair<String, Any>?) : this(
		handler = handler.value,
		data = params.filterNotNull().associate { it.first to it.second.toString() }
	)

	constructor(handler: InputEnum, vararg params: Pair<String, Any>?) : this(
		handler = handler.value,
		data = params.filterNotNull().associate { it.first to it.second.toString() }
	)
}
