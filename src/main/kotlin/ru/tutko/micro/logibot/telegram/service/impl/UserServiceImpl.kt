package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.repository.UserOrganizationLinkRepository
import ru.tutko.micro.logibot.telegram.repository.UserRepository
import ru.tutko.micro.logibot.telegram.service.UserService
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userOrganizationLinkRepository: UserOrganizationLinkRepository,
    private val userMapper: UserMapper,
): UserService {

    override fun getUserById(id: Long): UserDto {
        return userRepository.findById(id).map { userMapper.toDto(it) }.orElseThrow {
            throw NotFoundException("Пользователь не найден")
        }
    }

    override fun getUserByUserExternalId(userId: Long): UserDto {
        return userRepository.findByExternalUserId(userId).map { userMapper.toDto(it) }.orElseThrow {
            throw NotFoundException("Пользователь не найден")
        }
    }

    override fun getUserByUsername(username: String): Optional<UserDto> {
        return userRepository.findByUsername(username).map { userMapper.toDto(it) }
    }

    override fun createUser(user: UserDto): UserDto {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
    }

    override fun updateUser(user: UserDto): UserDto {

        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
    }

    override fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    override fun createIfNotExists(user: UserDto): UserDto {
        val existingUser = userRepository.findByExternalUserId(user.externalUserId!!).orElse(null)

        return if(existingUser == null){
            userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
        }else{
            userMapper.toDto(existingUser)
        }
    }

    override fun exists(userId: Long): Boolean {
        return userRepository.existsByExternalUserId(userId)
    }

    @Transactional
    override fun getUsersByOrganization(organizationId: Long, page: Int, size: Int): Page<UserDto> {
        val pageable = PageRequest.of(page, size)
        return userOrganizationLinkRepository.findById_OrganizationId(organizationId, pageable).map {
            it.user?.let { it1 -> userMapper.toDto(it1) }!!
        }
    }
}