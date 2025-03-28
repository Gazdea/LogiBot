package ru.tutko.micro.logibot.telegram.util

import ru.tutko.micro.logibot.telegram.component.TelegramSerialize
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum

class TelegramUtil {
    companion object {

	    fun getHandlerType(update: Update): HandlerTypeEnum {
		    val handlerType =  when{
			    update.hasMessage() && update.message.entities?.any { it.type == "bot_command" } == true -> HandlerTypeEnum.COMMAND
			    update.hasCallbackQuery() -> HandlerTypeEnum.CALLBACK
			    update.hasMessage() && !update.message.text.toString().startsWith("/") -> HandlerTypeEnum.INPUT
			    update.hasChatMember() -> HandlerTypeEnum.CHAT_MEMBER
			    update.hasMyChatMember() -> HandlerTypeEnum.MY_CHAT_MEMBER

			    else -> HandlerTypeEnum.UNKNOWN
		    }
		    println(handlerType)
		    return handlerType
	    }

        fun createInlineKeyboard(vararg buttons: Pair<String, CallbackData>): InlineKeyboardMarkup {
            return InlineKeyboardMarkup().apply {
                keyboard = buttons.map { (text, callback) ->
                    listOf(InlineKeyboardButton().apply {
                        this.text = text
                        this.callbackData = TelegramSerialize.serializeData(callback)
                    })
                }
            }
        }

        fun createInlineKeyboard(vararg buttonRows: List<Pair<String, CallbackData>>): InlineKeyboardMarkup {
            return InlineKeyboardMarkup().apply {
                keyboard = buttonRows.map { row ->
                    row.map { (text, callback) ->
                        InlineKeyboardButton().apply {
                            this.text = text
                            this.callbackData = TelegramSerialize.serializeData(callback)
                        }
                    }
                }
            }
        }

        fun getChatId(update: Update): Long {
            return when {
                update.hasMessage() -> update.message.chatId
                update.hasCallbackQuery() -> update.callbackQuery.message.chatId

                else -> update.message.chatId
            }
        }

        fun getUserId(update: Update): Long {
            return when {
                update.hasMessage() -> update.message.from.id
                update.hasCallbackQuery() -> update.callbackQuery.from.id
                update.hasChatJoinRequest() -> update.chatJoinRequest.user.id
                update.hasPollAnswer()-> update.pollAnswer.user.id
                update.hasEditedMessage() -> update.editedMessage.from.id
                update.hasEditedChannelPost() -> update.editedChannelPost.from.id
                update.hasChannelPost() -> update.channelPost.from.id

                else -> update.message.from.id
            }
        }

	    fun getMessage(update: Update): Message {
			return when {
				update.hasMessage() -> update.message
				update.hasCallbackQuery() -> update.callbackQuery.message
				update.hasEditedMessage() -> update.editedMessage

				else -> update.message
			}
		}

	    fun getFrom(update: Update): User  {
			return when {
				update.hasMessage() -> update.message.from
				update.hasCallbackQuery() -> update.callbackQuery.from

				else -> update.message.from
			}
		}

	    fun getChat(update: Update): Chat {
			return when {
				update.hasMessage() -> update.message.chat

				else -> update.message.chat
			}
		}
    }
}