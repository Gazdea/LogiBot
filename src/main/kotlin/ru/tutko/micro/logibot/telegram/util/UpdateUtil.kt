package ru.tutko.micro.logibot.telegram.util

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum


class UpdateUtil(private val update: Update) {

	fun getHandlerType(): HandlerTypeEnum {
		val handlerType = when {
			update.hasMessage() && update.message.entities?.any { it.type == "bot_command" } == true -> HandlerTypeEnum.COMMAND
			update.hasCallbackQuery() -> HandlerTypeEnum.CALLBACK
			update.hasMessage() && update.message.text?.startsWith("/") != true -> HandlerTypeEnum.INPUT
			update.hasChatMember() -> HandlerTypeEnum.CHAT_MEMBER
			update.hasMyChatMember() -> HandlerTypeEnum.MY_CHAT_MEMBER
			else -> HandlerTypeEnum.UNKNOWN
		}
		return handlerType
	}

	fun getMessage(): Message {
		return when {
			update.hasMessage() -> update.message
			update.hasCallbackQuery() -> update.callbackQuery.message
			update.hasEditedMessage() -> update.editedMessage
			update.hasMyChatMember() -> throw ValidationException("")
			update.hasChatMember() -> throw ValidationException("")

			else -> update.message
		}
	}

	fun getMigrateChat(): Pair<Long, Long> {
		return when {
			update.hasMessage() -> Pair(update.message.migrateFromChatId, update.message.migrateToChatId)
			update.hasMyChatMember() -> Pair(update.myChatMember.chat.id, update.myChatMember.chat.id)
			update.hasChatMember() -> Pair(update.chatMember.chat.id, update.chatMember.chat.id)
			update.hasCallbackQuery() -> Pair(
				update.callbackQuery.message.migrateFromChatId,
				update.callbackQuery.message.migrateToChatId
			)

			else -> Pair(update.message.migrateFromChatId, update.message.migrateToChatId)
		}
	}

	fun getUser(): User {
		return when {
			update.hasMessage() -> update.message.from
			update.hasCallbackQuery() -> update.callbackQuery.from
			update.hasMyChatMember() -> update.myChatMember.from
			update.hasChatMember() -> update.chatMember.from

			else -> update.message.from
		}
	}

	fun getChat(): Chat {
		return when {
			update.hasMessage() -> update.message.chat
			update.hasMyChatMember() -> update.myChatMember.chat
			update.hasChatMember() -> update.chatMember.chat
			update.hasCallbackQuery() -> update.callbackQuery.message.chat

			else -> update.message.chat
		}
	}
}
