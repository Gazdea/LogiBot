package ru.tutko.micro.logibot.telegram.component

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum

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
			val encode = Json.encodeToString(callbackData)
			return encode
		}

		private fun deserializeData(data: String): CallbackData {
			val decode = Json.decodeFromString<CallbackData>(data)
			return decode
		}
	}
}
