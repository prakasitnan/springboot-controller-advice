package com.github.prakasitnan.springbootcontrolleradvice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @Positive(message = "userId must be greater than 0")
    private Long userId;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Email must be in the format mail@mail.com")
    private String email;

}
