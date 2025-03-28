package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.dto.UserDto
import ru.tutko.micro.logibot.telegram.repository.UserRepository
import ru.tutko.micro.logibot.telegram.service.UserService
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
): UserService {
    override fun getUserById(id: Long): Optional<UserDto> {
        return userRepository.findById(id).map { userMapper.toDto(it) }
    }

    override fun getUserByUserId(userId: Long): Optional<UserDto> {
        return userRepository.findByUserId(userId).map { userMapper.toDto(it) }
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
        val existingUser = userRepository.findByUserId(user.userId!!).orElse(null)

        return if(existingUser == null){
            userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
        }else{
            userMapper.toDto(existingUser)
        }
    }

    override fun exists(userId: Long): Boolean {
        return userRepository.existsByUserId(userId)
    }
}