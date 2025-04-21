package ru.tutko.micro.logibot.telegram.model.data

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
	use = JsonTypeInfo.Id.CLASS,
	include = JsonTypeInfo.As.PROPERTY,
	property = "@class"
)
abstract class Payload