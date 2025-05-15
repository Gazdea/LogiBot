package ru.tutko.micro.logibot.telegram.filter.update_impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.dto.request.FindOrMigrateChatDtoRequest
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.service.ChatService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter

@Component
class ChatValidateFilter(
	private val chatService: ChatService,
): UpdateValidationFilter {

	override fun validate(update: Update): Boolean {
		val chat = UpdateUtil(update).getChat()
		return ChatTypeEnum.fromValue(chat.type) != ChatTypeEnum.PRIVATE
	}

	override fun process(update: Update) {
		val migrateChat = UpdateUtil(update).getMigrateChat()
		val chat = UpdateUtil(update).getChat()
		chatService.findOrMigrateChat(
			FindOrMigrateChatDtoRequest(
				chatId = chat.id,
				type = ChatTypeEnum.fromValue(chat.type),
				title = chat.title,
				username = chat.userName,
				fromChatId = migrateChat.first,
				toChatId = migrateChat.second,
			)
		)
	}
}