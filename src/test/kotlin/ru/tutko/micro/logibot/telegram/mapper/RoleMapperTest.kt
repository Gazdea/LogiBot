package ru.tutko.micro.logibot.telegram.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.tutko.micro.logibot.telegram.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.utils.DtoTestBuilder
import ru.tutko.micro.logibot.telegram.utils.EntityTestBuilder

class RoleMapperTest {
    private val roleMapper = RoleMapperImpl()
    private val entityTestBuilder = EntityTestBuilder()
    private val dtoTestBuilder = DtoTestBuilder()

    private var role = Role()
    private var roleDto = RoleDto()

    @BeforeEach
    fun setUp() {
        role = entityTestBuilder.getRole()
        roleDto = dtoTestBuilder.getRoleDto()
    }

    @Test
    fun toEntity() {
        val mappedRole = roleMapper.toEntity(roleDto)

        assertAll({
            assertEquals(role.id, mappedRole.id)
            assertEquals(role.role, mappedRole.role)
            assertEquals(role.organizationId, mappedRole.organizationId)
        })
    }

    @Test
    fun toDto() {
        val mappedRole = roleMapper.toDto(role)

        assertAll({
            assertEquals(roleDto.id, mappedRole.id)
            assertEquals(roleDto.role, mappedRole.role)
            assertEquals(role.organizationId, mappedRole.organizationId)
        })
    }
}