package ru.tutko.micro.logibot.telegram.dto

import jakarta.validation.constraints.Size
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RolePermission}
 */
data class RolePermissionDto(var id: Long? = null, @field:Size(max = 100) var permission: String? = null) : Serializable