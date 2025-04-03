package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId

interface UserOrganizationLinkRepository : JpaRepository<UserOrganizationLink, UserOrganizationLinkId>,
	JpaSpecificationExecutor<UserOrganizationLink> {
}