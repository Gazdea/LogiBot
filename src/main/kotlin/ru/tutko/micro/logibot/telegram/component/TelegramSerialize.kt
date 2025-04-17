package ru.tutko.micro.logibot.telegram.component

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import java.util.*

class TelegramSerialize {
	companion object {

		fun extractData(
			update: Update,
			handlerType: HandlerTypeEnum,
			data: String?
		): CallbackData? {
			return when (handlerType) {
				HandlerTypeEnum.INPUT -> {
					data
						?.let { deserializeData(it) }
				}

				HandlerTypeEnum.CALLBACK -> {
					update.callbackQuery.data
						?.let { deserializeData(it) }
				}

				else -> null
			}
		}

		fun serializeData(callbackData: CallbackData): String {
			val bytes = ProtoBuf.encodeToByteArray(CallbackData.serializer(), callbackData)
			return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
		}

		fun deserializeData(data: String): CallbackData {
			val bytes = Base64.getUrlDecoder().decode(data)
			return ProtoBuf.decodeFromByteArray(CallbackData.serializer(), bytes)
		}

	}
}
