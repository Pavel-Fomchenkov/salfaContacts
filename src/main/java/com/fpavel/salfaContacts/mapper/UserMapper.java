package com.fpavel.salfaContacts.mapper;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getRole());
    }

    public User userCreateDtoToUser(UserCreateDto dto) {
        return new User(dto.login(), passwordEncoder.encode(dto.password()), dto.role());
    }
    public User userDtoToUser(UserDto userDto) {
        return new User(userDto.id(), userDto.login(), userDto.role());
    }

}
