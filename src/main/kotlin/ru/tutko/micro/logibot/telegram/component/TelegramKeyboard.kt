package ru.tutko.micro.logibot.telegram.component

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService

@Component
class TelegramKeyboard(
	private val callbackRedisService: CallbackRedisService
) {

	companion object {
		fun checkAccessCreateButton(
			role: RoleDto,
			callbackQueryEnum: CallbackQueryEnum,
			data: Payload?,
			label: String
		): Pair<String, CallbackData<Payload>>? {
			return if (role.roleOrganizationPermissions.any { it.permission == callbackQueryEnum.permission || it.permission == PermissionAccessEnum.CREATOR }) {
				val callbackData = if (data != null) {
					CallbackData(callbackQueryEnum.toString(), data::class.java.name, data)
				} else {
					CallbackData(callbackQueryEnum.toString())
				}
				label to callbackData
			} else {
				null
			}
		}
	}

	// 1. Одинарные кнопки на каждой строке (vararg пар)
	fun createInlineKeyboardRow(userId: String, vararg buttons: Pair<String, CallbackData<Payload>>): InlineKeyboardMarkup {
		return InlineKeyboardMarkup().apply {
			keyboard = buttons.map { (text, callback) ->
				listOf(InlineKeyboardButton().apply {
					this.text = text
					this.callbackData = callbackRedisService.create(userId, callback)
				})
			}
		}
	}

	// 2. Одинарные кнопки на каждой строке (список пар)
	fun createInlineKeyboardRow(userId: String, buttons: List<Pair<String, CallbackData<Payload>>>): InlineKeyboardMarkup {
		return InlineKeyboardMarkup().apply {
			keyboard = buttons.map { (text, callback) ->
				listOf(InlineKeyboardButton().apply {
					this.text = text
					this.callbackData = callbackRedisService.create(userId, callback)
				})
			}
		}
	}

	// 3. Несколько рядов кнопок (vararg списков пар)
	fun createInlineKeyboard(userId: String, vararg rows: List<Pair<String, CallbackData<Payload>>>): InlineKeyboardMarkup {
		return InlineKeyboardMarkup().apply {
			keyboard = rows.map { row ->
				row.map { (text, callback) ->
					InlineKeyboardButton().apply {
						this.text = text
						this.callbackData = callbackRedisService.create(userId, callback)
					}
				}
			}
		}
	}

	// 4. Несколько рядов кнопок (список списков пар)
	fun createInlineKeyboard(userId: String, rows: List<List<Pair<String, CallbackData<Payload>>>>): InlineKeyboardMarkup {
		return InlineKeyboardMarkup().apply {
			keyboard = rows.map { row ->
				row.map { (text, callback) ->
					InlineKeyboardButton().apply {
						this.text = text
						this.callbackData = callbackRedisService.create(userId, callback)
					}
				}
			}
		}
	}
}