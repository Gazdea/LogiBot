package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class OrganizationHandler(
    private val organizationService: OrganizationService,
    private val roleService: RoleService,
) {
    @CallbackMapping(CallbackQueryEnum.GET_ORGANIZATION)
    fun callbackQueryGetOrganization(request: Request): Response {
        val organizationId = request.data?.getData<OrganizationId>()
            ?: throw ValidationException("Не найдена OrganizationId")

        val organization = organizationService.getOrganizationById(organizationId.orgId)
        val role = roleService.getRoleByUserOrganization(organizationId.orgId, request.userId)

        val buttons = UpdateUtil.createInlineKeyboardRow(
            listOfNotNull(
            ("Назад" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, Paginate(0))),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.SETTINGS,
                    null, "Настройки"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_ROLES,
                    OrganizationPaginate(organizationId.orgId, Paginate(0)), "Роли организации"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_USERS,
                    OrganizationPaginate(organizationId.orgId, Paginate(0)), "Сотрудники организации"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_TABLES,
                    OrganizationPaginate(organizationId.orgId, Paginate(0)), "Таблицы организации"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_CHATS,
                    OrganizationPaginate(organizationId.orgId, Paginate(0)), "Чаты организации"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.LOGS_ORGANIZATION,
                    organizationId, "Логи организации"),
                UpdateUtil.checkAccessCreateButton(role, CallbackQueryEnum.REPORT_ORGANIZATION,
                    organizationId, "Отчеты организации")
        ))

        // Возвращаем ответ с кнопками
        return Response(
            botApiMethods = listOf(
                EditMessageText().apply {
                    messageId = UpdateUtil(request.update).getMessage()!!.messageId
                    chatId = request.chatId.toString()
                    text = "Ваша организация ${organization!!.name}"
                    replyMarkup = buttons
                }
            )
        )

    }


}