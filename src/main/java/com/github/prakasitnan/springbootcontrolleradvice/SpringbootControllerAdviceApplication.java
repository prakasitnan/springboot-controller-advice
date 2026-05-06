package com.github.prakasitnan.springbootcontrolleradvice;

import com.github.prakasitnan.springbootcontrolleradvice.entity.UserEntity;
import com.github.prakasitnan.springbootcontrolleradvice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringbootControllerAdviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootControllerAdviceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ADMIN");

                UserEntity user = new UserEntity();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user"));
                user.setRole("USER");

                userRepository.save(user);
                userRepository.save(admin);
            }
        };
    }

}
