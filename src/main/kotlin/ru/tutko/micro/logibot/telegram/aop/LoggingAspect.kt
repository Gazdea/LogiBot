package ru.tutko.micro.logibot.telegram.aop

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Aspect
@Component
class LoggingAspect {

    private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    // Определяем точку отсечения для метода dispatch в UpdateDispatcher
    @Pointcut("execution(* ru.tutko.micro.logibot.telegram.dispatcher.UpdateDispatcher.dispatch(..))")
    fun processUpdateMethods() {}

    // Логируем перед выполнением метода
    @Before("processUpdateMethods() && args(update,..)")
    fun logBefore(update: Update) {
        logger.info("Получен запрос: ${update.javaClass.simpleName} с параметрами: $update")
    }

    // Логируем после выполнения метода
    @AfterReturning(pointcut = "processUpdateMethods() && args(update,..)", returning = "response")
    fun logAfter(update: Update, response: Any?) {
        logger.info("Ответ для запроса: ${response?.javaClass?.simpleName ?: "null"}, с результатом: $response")
    }

}