package ru.tutko.micro.logibot.telegram.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.tutko.micro.logibot.telegram.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.utils.DtoTestBuilder
import ru.tutko.micro.logibot.telegram.utils.EntityTestBuilder

class UserOrganizationLinkMapperTest {
	private val userOrganizationLinkMapper = UserOrganizationLinkMapperImpl()
	private val entityTestBuilder = EntityTestBuilder()
	private val dtoTestBuilder = DtoTestBuilder()

	private var userOrganizationLink = UserOrganizationLink()
	private var userOrganizationLinkDto = UserOrganizationLinkDto()

    @BeforeEach
    fun setUp() {
	    userOrganizationLink = entityTestBuilder.getUserOrganizationLink()
	    userOrganizationLinkDto = dtoTestBuilder.getUserOrganizationLinkDto()
    }

    @Test
    fun toEntity() {
	    val mappedUserOrganizationLink = userOrganizationLinkMapper.toEntity(userOrganizationLinkDto)

	    assertAll({
		    assertEquals(userOrganizationLink.id, mappedUserOrganizationLink.id)
		    assertEquals(userOrganizationLink.roleId, mappedUserOrganizationLink.roleId)
		    assertEquals(userOrganizationLink.user!!.id, mappedUserOrganizationLink.user!!.id)
		    assertEquals(userOrganizationLink.organization!!.id, mappedUserOrganizationLink.organization!!.id)
	    })
    }

    @Test
    fun toDto() {
	    val mappedUserOrganizationLink = userOrganizationLinkMapper.toDto(userOrganizationLink)

	    assertAll({
		    assertEquals(userOrganizationLinkDto.id, mappedUserOrganizationLink.id)
		    assertEquals(userOrganizationLinkDto.roleId, mappedUserOrganizationLink.roleId)
		    assertEquals(userOrganizationLinkDto.user!!.id, mappedUserOrganizationLink.user!!.id)
		    assertEquals(userOrganizationLinkDto.organization!!.id, mappedUserOrganizationLink.organization!!.id)
	    })
    }
}