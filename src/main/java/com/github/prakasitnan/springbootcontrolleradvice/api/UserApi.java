package com.github.prakasitnan.springbootcontrolleradvice.api;

import com.github.prakasitnan.springbootcontrolleradvice.dto.UserDto;
import com.github.prakasitnan.springbootcontrolleradvice.dto.req.CreateUserRequest;
import com.github.prakasitnan.springbootcontrolleradvice.entity.UserEntity;
import com.github.prakasitnan.springbootcontrolleradvice.exception.ResourceNotFoundException;
import com.github.prakasitnan.springbootcontrolleradvice.mapper.UserMapper;
import com.github.prakasitnan.springbootcontrolleradvice.repository.UserRepository;
import com.github.prakasitnan.springbootcontrolleradvice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserApi {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserApi(UserMapper userMapper, UserRepository userRepository, UserService userService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.userToUserDto(userEntity);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        UserEntity user = userService.upsertUser(userRequest);
        return userMapper.userToUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(-9);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userMapper::userToUserDto).toList();
    }
}
