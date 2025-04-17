package ru.tutko.micro.logibot.telegram.handler

import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService

@Handlers
class TableHandler(
	val organizationService: OrganizationService
) {


	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_TABLES)
	fun callbackGetTables(request: Request): Response {
		TODO()
	}
}