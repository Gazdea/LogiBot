package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tutko.micro.logibot.telegram.model.entity.OrganizationSettings

interface OrganizationSettingsRepository : JpaRepository<OrganizationSettings, Long> {
}