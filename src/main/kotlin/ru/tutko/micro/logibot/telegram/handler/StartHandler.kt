package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class StartHandler(
    private val organizationService: OrganizationService,
    private val telegramKeyboard: TelegramKeyboard
) {

    @CommandMapping(command = CommandEnum.START)
    fun handleStart(request: Request): Response {
        val buttons = getPaginateOrganizations(userId = request.userId)

        return Response(
            botApiMethods = listOf(SendMessage().apply {
                chatId = request.chatId.toString()
                text = "👋 Привет! Вот организации в которых ты есть."
                replyMarkup = buttons
            })
        )
    }

    @CallbackMapping(CallbackQueryEnum.PAGINATE_ORGANIZATIONS)
    fun paginateOrganizations(request: Request, paginate: Paginate): Response {
        val buttons = getPaginateOrganizations(paginate, request.userId)

        return Response(
            botApiMethods = listOf(EditMessageReplyMarkup().apply {
                messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
                chatId = request.chatId.toString()
                replyMarkup = buttons
            })
        )
    }

    private fun getPaginateOrganizations(paginate: Paginate = Paginate(0), userId: Long): InlineKeyboardMarkup {
        val organizations = organizationService.getOrganizationsByUserId(userId, paginate.page)
        val organizationButtons: List<List<Pair<String, CallbackData<Payload>>>> = organizations.content.map { org ->
            listOf(org.name!! to CallbackData(CallbackQueryEnum.GET_ORGANIZATION, OrganizationId(org.id!!)))
        }

        val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

        navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.CREATE_ORGANIZATION))
        if (organizations.hasPrevious()) {
            navigationButtons.add("<-" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, paginate.decreasePage()))
        }
        if (organizations.hasNext()) {
            navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, paginate.increasePage()))
        }

        return  telegramKeyboard.createInlineKeyboard("$userId", listOf(navigationButtons) + organizationButtons)
    }
}