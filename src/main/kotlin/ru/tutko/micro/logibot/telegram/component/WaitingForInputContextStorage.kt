package ru.tutko.micro.logibot.telegram.component

import org.springframework.stereotype.Component

@Component
class WaitingForInputContextStorage {
	private val waitingForInputContext: MutableMap<String, String> = mutableMapOf()

	fun set(chatIdUserId: String, value: String) {
		waitingForInputContext[chatIdUserId] = value
	}

	fun pop(chatIdUserId: String, key: String): String? {
		return waitingForInputContext.remove(chatIdUserId)
	}

	fun remove(chatIdUserId: String) {
		waitingForInputContext.remove(chatIdUserId)
	}

	fun containsKey(key: String): Boolean {
		return waitingForInputContext.containsKey(key)
	}

	fun get(key: String): String? {
		return waitingForInputContext[key]
	}
}
