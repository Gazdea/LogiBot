package ru.tutko.micro.logibot.telegram.filter.update_impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.dto.request.FindOrMigrateChatDtoRequest
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.service.ChatService
import ru.tutko.micro.logibot.telegram.util.TelegramUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter

@Component
class ChatValidateFilter(
	private val chatService: ChatService,
): UpdateValidationFilter {

	override fun validate(update: Update): Boolean {
		val message = TelegramUtil.getMessage(update)
		return ChatTypeEnum.fromValue(message.chat.type) != ChatTypeEnum.PRIVATE
	}

	override fun process(update: Update) {
		val message = TelegramUtil.getMessage(update)
		chatService.findOrMigrateChat(
			FindOrMigrateChatDtoRequest(
				chatId = message.chat.id,
				type = ChatTypeEnum.fromValue(message.chat.type),
				title = message.chat.title,
				username = message.chat.userName,
				fromChatId = message.migrateFromChatId,
				toChatId = message.migrateToChatId,
			)
		)

	}
}