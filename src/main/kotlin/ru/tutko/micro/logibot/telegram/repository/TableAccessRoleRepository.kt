package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tutko.micro.logibot.telegram.model.entity.TableAccessRole

interface TableAccessRoleRepository : JpaRepository<TableAccessRole, Long> {
}