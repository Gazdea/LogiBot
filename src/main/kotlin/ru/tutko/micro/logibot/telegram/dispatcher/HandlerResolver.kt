package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.annotation.mapping.*
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Component
class HandlerResolver(private val handlerMethods: List<Pair<Any, KFunction<*>>>) {
	fun resolve(request: Request, type: HandlerTypeEnum): Pair<Any, KFunction<*>>? {
		return handlerMethods
			.filter { (_, method) -> method.hasHandlerType(type) }
			.firstOrNull { (_, method) ->
				val annotation = method.getAnnotationByType(type)
				annotation != null && annotation.matches(request)
			}
	}

	private fun KFunction<*>.getAnnotationByType(type: HandlerTypeEnum): Annotation? {
		return when (type) {
			HandlerTypeEnum.COMMAND -> findAnnotation<CommandMapping>()
			HandlerTypeEnum.CALLBACK -> findAnnotation<CallbackMapping>()
			HandlerTypeEnum.INPUT -> findAnnotation<InputMapping>()
			HandlerTypeEnum.CHAT_MEMBER -> findAnnotation<ChatMemberMapping>()
			HandlerTypeEnum.MY_CHAT_MEMBER -> findAnnotation<MyChatMemberMapping>()
			else -> null
		}
	}

	private fun KFunction<*>.hasHandlerType(type: HandlerTypeEnum): Boolean {
		return when (type) {
			HandlerTypeEnum.COMMAND -> hasAnnotation<CommandMapping>()
			HandlerTypeEnum.CALLBACK -> hasAnnotation<CallbackMapping>()
			HandlerTypeEnum.INPUT -> hasAnnotation<InputMapping>()
			HandlerTypeEnum.CHAT_MEMBER -> hasAnnotation<ChatMemberMapping>()
			HandlerTypeEnum.MY_CHAT_MEMBER -> hasAnnotation<MyChatMemberMapping>()
			else -> false
		}
	}

	private fun Annotation.matches(request: Request): Boolean {
		return when (this) {
			is CommandMapping -> request.update.message.text == this.command.value
			is CallbackMapping -> request.data?.handler == this.callbackQuery.value
			is InputMapping -> request.data?.handler == this.input.value
			is ChatMemberMapping -> request.update.chatMember?.newChatMember?.status == this.chatMember.value
			is MyChatMemberMapping -> request.update.myChatMember?.newChatMember?.status == this.myChatMember.value
			else -> false
		}
	}
}