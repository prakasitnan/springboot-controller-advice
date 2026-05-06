package com.github.prakasitnan.springbootcontrolleradvice.mapper;

import com.github.prakasitnan.springbootcontrolleradvice.dto.UserDto;
import com.github.prakasitnan.springbootcontrolleradvice.dto.req.CreateUserRequest;
import com.github.prakasitnan.springbootcontrolleradvice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(UserEntity user);

    UserEntity createUserRequestToUserEntity(CreateUserRequest request);

}
