package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import java.util.*

interface OrganizationRepository : JpaRepository<Organization, Long> {

    override fun findById(id: Long): Optional<Organization>

    override fun deleteById(id: Long)
}