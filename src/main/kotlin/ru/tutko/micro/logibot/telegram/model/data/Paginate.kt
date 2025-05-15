package ru.tutko.micro.logibot.telegram.model.data

class Paginate<T>(
	private val items: List<T>,
	private val page: Int = 0,
	private val size: Int = 8
) {
	fun currentPage(): List<T> {
		val fromIndex = (page * size).coerceAtMost(items.size)
		val toIndex = ((page + 1) * size).coerceAtMost(items.size)
		return items.subList(fromIndex, toIndex)
	}

	fun hasNext(): Boolean = (page + 1) * size < items.size

	fun hasPrevious(): Boolean = page > 0

	fun increasePage(): Paginate<T> = Paginate(items, page + 1, size)

	fun decreasePage(): Paginate<T> = Paginate(items, page - 1, size)
}
