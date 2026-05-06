package com.github.prakasitnan.springbootcontrolleradvice.service;

import com.github.prakasitnan.springbootcontrolleradvice.dto.UserDto;
import com.github.prakasitnan.springbootcontrolleradvice.dto.req.CreateUserRequest;
import com.github.prakasitnan.springbootcontrolleradvice.entity.UserEntity;
import com.github.prakasitnan.springbootcontrolleradvice.exception.ResourceNotFoundException;
import com.github.prakasitnan.springbootcontrolleradvice.mapper.UserMapper;
import com.github.prakasitnan.springbootcontrolleradvice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserEntity upsertUser(CreateUserRequest request) {
        Instant now = Instant.now();
        UserEntity mappedUser = userMapper.createUserRequestToUserEntity(request);

        Long userId = request.getUserId();
        boolean isCreate = userId == null;

        UserEntity userEntity = isCreate
                ? new UserEntity()
                : userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userEntity.setUsername(mappedUser.getUsername());
        userEntity.setPassword(mappedUser.getPassword());
        userEntity.setName(mappedUser.getName());
        userEntity.setEmail(mappedUser.getEmail());
        userEntity.setUpdatedDate(now);

        if (isCreate) {
            userEntity.setCreatedDate(now);
            userEntity.setStatus(1);
        }

        return userRepository.save(userEntity);
    }


    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .toList();
    }
}
