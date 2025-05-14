package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
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
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.UserService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class StartHandler(
    private val organizationService: OrganizationService,
    private val userService: UserService,
    private val telegramKeyboard: TelegramKeyboard
) {

    @CommandMapping(command = CommandEnum.START)
    fun handleStart(request: Request): Response {
        val messageText = UpdateUtil(request.update).getMessage()?.text ?: ""
        val payload = messageText.removePrefix("/start").trim()

        if (payload.startsWith("org_")) {
            val orgId = payload.removePrefix("org_").toLongOrNull()
            if (orgId != null) {

                userService.joinUserOrganizationIfNeeded(orgId, request.userId)
                return Response(
                    botApiMethods = listOf(SendMessage().apply {
                        chatId = request.chatId.toString()
                        text = "🎉 Ты успешно присоединился к организации!"
                    })
                )
            }
        }
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
    fun paginateOrganizations(request: Request, pageable: Pageable): Response {
        val buttons = getPaginateOrganizations(pageable, request.userId)

        return Response(
            botApiMethods = listOf(EditMessageText().apply {
                messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
                chatId = request.chatId.toString()
                text = "👋 Привет! Вот организации в которых ты есть."
                replyMarkup = buttons
            })
        )
    }

    private fun getPaginateOrganizations(pageable: Pageable = Pageable(0), userId: Long): InlineKeyboardMarkup {
        val organizations = organizationService.getOrganizationsByUserId(userId, pageable.page)
        val organizationButtons: List<List<Pair<String, CallbackData<Payload>>>> = organizations.content.map { org ->
            listOf(org.name!! to CallbackData(CallbackQueryEnum.GET_ORGANIZATION, OrganizationId(org.id!!)))
        }

        val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

        navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.CREATE_ORGANIZATION))
        if (organizations.hasPrevious()) {
            navigationButtons.add("⬅️ Назад" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, pageable.decreasePage()))
        }
        if (organizations.hasNext()) {
            navigationButtons.add("Вперёд ➡️" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, pageable.increasePage()))
        }

        return  telegramKeyboard.createInlineKeyboard("$userId", listOf(navigationButtons) + organizationButtons)
    }
}