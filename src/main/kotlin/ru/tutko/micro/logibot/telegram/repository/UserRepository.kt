package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tutko.micro.logibot.telegram.model.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>

    fun findByUserId(userId: Long): Optional<User>

    fun existsByUserId(userId: Long): Boolean
}