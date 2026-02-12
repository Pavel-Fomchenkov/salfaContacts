package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.mapper.UserMapper;
import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.repository.UserRepository;
import com.fpavel.salfaContacts.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;

    public UserServiceImpl(UserMapper mapper, UserRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public User create(UserCreateDto userCreateDto) {
        return repository.save(mapper.userCreateDtoToUser(userCreateDto));
    }

    @Override
    public UserDto getById(long id) {
        User user = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream().map(mapper::userToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> opt = repository.findById(userDto.id());
        if (opt.isEmpty()) {
            throw new EntityNotFoundException("User id " + userDto.id() + " not found.");
        }
        User user = opt.get();

        user.setLogin(userDto.login());
        user.setRole(userDto.role());
        return mapper.userToUserDto(repository.save(user));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
