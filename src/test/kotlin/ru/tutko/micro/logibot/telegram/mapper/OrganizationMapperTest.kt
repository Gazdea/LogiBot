package ru.tutko.micro.logibot.telegram.mapper

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import ru.tutko.micro.logibot.telegram.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.utils.DtoTestBuilder
import ru.tutko.micro.logibot.telegram.utils.EntityTestBuilder

class OrganizationMapperTest {
     private val organizationMapper = OrganizationMapperImpl()
     private val entityTestBuilder = EntityTestBuilder()
     private val dtoTestBuilder = DtoTestBuilder()

     private var organization = Organization()
     private var organizationDto = OrganizationDto()

     @BeforeEach
     fun setUp() {
          organization = entityTestBuilder.getOrganization()
          organizationDto = dtoTestBuilder.getOrganizationDto()
     }

     @Test
     fun toEntity() {
          val mappedOrganization = organizationMapper.toEntity(organizationDto)

          assertAll({
                assertEquals(organizationDto.id, mappedOrganization.id)
                assertEquals(organizationDto.name, mappedOrganization.name)
		  })
     }

    @Test
     fun toDto() {
     val mappedOrganization = organizationMapper.toDto(organization)

     assertAll({
           assertEquals(organization.id, mappedOrganization.id)
           assertEquals(organization.name, mappedOrganization.name)
	 })
     }

    @Test
     fun partialUpdate() {}
}
