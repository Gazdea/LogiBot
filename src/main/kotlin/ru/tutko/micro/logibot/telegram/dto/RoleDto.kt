package ru.tutko.micro.logibot.telegram.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
 */
data class RoleDto(
    var id: Long? = null,
    @field:Size(max = 50) var role: String? = null,
    @field:NotNull var organizationId: Long? = null,
    var rolePermissions: MutableSet<RolePermissionDto> = mutableSetOf()
) : Serializable