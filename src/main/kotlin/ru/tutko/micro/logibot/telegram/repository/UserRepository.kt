package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


	fun findByExternalUserId(externalUserId: Long): Optional<User>


	fun findByUsername(username: String): Optional<User>


	fun existsByExternalUserId(externalUserId: Long): Boolean


}