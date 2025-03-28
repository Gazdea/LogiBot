package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.tutko.micro.logibot.telegram.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import java.util.*

interface OrganizationRepository : JpaRepository<Organization, Long> {

    fun findByUserOrganizationLinks_User_UserId(userId: Long): MutableList<Organization>

}