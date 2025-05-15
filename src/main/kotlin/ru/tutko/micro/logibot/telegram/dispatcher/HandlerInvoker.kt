package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

@Component
class HandlerInvoker {
	fun invoke(instance: Any, method: KFunction<*>, request: Request): Response {
		val args = method.parameters.associateWith { param ->
			when (param.kind) {
				KParameter.Kind.INSTANCE -> instance
				KParameter.Kind.VALUE -> when (param.type.classifier) {
					Request::class -> request
					Update::class -> request.update
					request.data?.data!!::class -> request.data.data
					else -> null
				}
				else -> null
			}
		}
		return method.callBy(args) as Response
	}
}
