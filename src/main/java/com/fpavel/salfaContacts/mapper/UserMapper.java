package com.fpavel.salfaContacts.mapper;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getRole());
    }

    public User userCreateDtoToUser(UserCreateDto dto) {
        return new User(dto.login(), dto.password(), dto.role());
    }

}
