package ru.tutko.micro.logibot.telegram.config

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.tutko.micro.logibot.telegram.annotation.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

@Configuration
class HandlerMethodConfig(private val applicationContext: ApplicationContext) {

	@Bean
	fun handlerMethods(): List<Pair<Any, KFunction<*>>> {
		return applicationContext.getBeansWithAnnotation(Handlers::class.java)
			.values
			.flatMap { handler ->
				handler::class.declaredMemberFunctions
					.filter { method ->
						method.hasAnnotation<CommandMapping>() ||
								method.hasAnnotation<CallbackMapping>() ||
								method.hasAnnotation<InputMapping>() ||
								method.hasAnnotation<ChatMemberMapping>() ||
								method.hasAnnotation<MyChatMemberMapping>()
					}
					.map { method -> handler to method }
			}
	}
}