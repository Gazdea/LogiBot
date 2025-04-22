package ru.tutko.micro.logibot.telegram.component

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.data.Payload

import java.util.*

object TelegramSerialize {
	private val mapper = jacksonObjectMapper()

	//Base64
	fun <T : Payload> serializeData(callbackData: CallbackData<T>): String {
		val json = mapper.writeValueAsString(callbackData)
		return Base64.getUrlEncoder().withoutPadding().encodeToString(json.toByteArray())
	}

	fun deserializeData(data: String): CallbackData<out Payload> {
		val json = String(Base64.getUrlDecoder().decode(data))
		val rawNode = mapper.readTree(json)

		val classNameNode = rawNode.get("className")
		if (classNameNode == null || classNameNode.isNull || classNameNode.asText().isBlank()) {
			return mapper.readValue(json, CallbackData::class.java)
		}

		val className = classNameNode.asText()
		val payloadClass = Class.forName(className) as Class<out Payload>

		val callbackDataType = mapper.typeFactory
			.constructParametricType(CallbackData::class.java, payloadClass)

		return mapper.readValue(json, callbackDataType)
	}

//	fun <T : Payload> serializeData(callbackData: CallbackData<T>): String {
//		return mapper.writeValueAsString(callbackData)
//	}
//
//	fun deserializeData(json: String): CallbackData<out Payload> {
//		val rootNode = mapper.readTree(json)
//		val className = rootNode.get("className")?.asText()
//			?: throw IllegalArgumentException("Missing className")
//
//		val payloadClass = Class.forName(className) as Class<out Payload>
//
//		val typeRef = mapper.typeFactory
//			.constructParametricType(CallbackData::class.java, payloadClass)
//
//		return mapper.readValue(json, typeRef)
//	}
}