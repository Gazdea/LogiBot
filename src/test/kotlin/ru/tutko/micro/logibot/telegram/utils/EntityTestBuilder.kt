package ru.tutko.micro.logibot.telegram.utils

import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.model.entity.*
import ru.tutko.micro.logibot.telegram.model.enums.*
import ru.tutko.micro.logibot.telegram.model.enums.role.DefaultRoleEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum

@Component
class EntityTestBuilder {

    fun getOrganization() : Organization {
        return Organization().apply {
            id = 1
            name = "name"
            chats = mutableListOf()
            userOrganizationLinks = mutableListOf()
        }
    }

    fun getChat(): Chat {
        return Chat().apply {
            id = 1
            chatId = 1
            organization = null
            type = ChatTypeEnum.GROUP.value
            title = "title"
            username = "username"
        }
    }

    fun getRole(): Role {
        return Role().apply {
            id = 1
            role = DefaultRoleEnum.ADMIN.value
            organizationId = 1
            rolePermissions = mutableSetOf()
        }
    }

    fun getRolePermission(): RolePermission {
        return RolePermission().apply {
            id = 1
            role = null
            permission = PermissionAccessEnum.MANAGE_EMPLOYEES.value
        }
    }

    fun getUserOrganizationLink() : UserOrganizationLink {
        return UserOrganizationLink().apply {
            id = getUserOrganizationLinkId()
            user = getUser()
            organization = getOrganization()
            roleId  = 1
        }
    }

    fun getUserOrganizationLinkId() : UserOrganizationLinkId {
        return UserOrganizationLinkId().apply {
            userId = 1
            organizationId = 1
        }
    }

    fun getUser() : User {
        return User().apply {
            id = 1
            userId = 1
            username = "username"
            firstName = "firstName"
            lastName = "lastName"
            auditLogs = mutableListOf()
            userOrganizationLinks = mutableListOf()
        }
    }

    fun getAuditLogs() : AuditLogs {
        return AuditLogs().apply {
            id = 1
            user = null
            table = null
            action = ActionLogsEnum.ADD_COLUMNS.value
        }
    }

    fun getTable() : Table {
        return Table().apply {
            id = 1
            chat = null
            tableName = "testTable"
        }
    }

    fun getTableAccessRole() : TableAccessRole {
        return TableAccessRole().apply {
            id = 1
            table = null
            role = null
            permission = TablePermissionAccessEnum.CONFIGURE_TABLE.value
        }
    }

    fun getTableColumn() : TableColumn {
        return TableColumn().apply {
            id = 1
            table = null
            columnName = "columnName"
            columnType = ColumnTypeEnum.STRING.value
        }
    }

    fun getTableDataMetadatum() : TableDataMetadatum {
        return TableDataMetadatum().apply {
            id = 1
            table = null
            mongoDocumentId = 1.toString()
            user = null
            organization = null
            action = ActionLogsEnum.ADD_COLUMNS.value
        }
    }

}