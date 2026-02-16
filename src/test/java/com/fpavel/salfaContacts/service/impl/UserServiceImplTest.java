package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final Long userId = 1L;
    private final String login = "TestUser";
    private final String password = "TestPassword";
    private final User.Role role = User.Role.USER;

    @Test
    void create_ShouldSaveUser_WhenValid() {
        User userToCreate = new User(null, login, password, role);
        User savedUser = new User(userId, login, password, role);
        when(userRepository.save(userToCreate)).thenReturn(savedUser);

        User result = userService.create(userToCreate);

        assertEquals(savedUser, result);
        verify(userRepository, times(1)).save(userToCreate);
    }

    @Test
    void create_ShouldThrowException_WhenIdNotNull() {
        User userToCreate = new User(userId, login, password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(userToCreate));
        assertEquals("ID must not be specified", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void create_ShouldThrowException_WhenLoginNull() {
        User userToCreate = new User(null, null, password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(userToCreate));
        assertEquals("Login cannot be null or empty", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void create_ShouldThrowException_WhenLoginEmpty() {
        User userToCreate = new User(null, "", password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(userToCreate));
        assertEquals("Login cannot be null or empty", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void create_ShouldThrowException_WhenPasswordNull() {
        User userToCreate = new User(null, login, null, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(userToCreate));
        assertEquals("Password cannot be null or empty", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void create_ShouldThrowException_WhenPasswordEmpty() {
        User userToCreate = new User(null, login, "", role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.create(userToCreate));
        assertEquals("Password cannot be null or empty", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void getById_ShouldReturnUser_WhenExists() {
        User user = new User(userId, login, password, role);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenNotExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getById(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAll_ShouldReturnListOfUsers() {
        List<User> users = List.of(
                new User(1L, "TestUser01", "TestPassword01", role),
                new User(2L, "TestUser02", "TestPassword02", role)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void update_ShouldUpdateAndReturnUser_WhenValid() {
        User existingUser = new User(userId, "oldLogin", "oldPass", role);
        User updateData = new User(userId, login, password, role);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.update(updateData);

        assertEquals(existingUser, result);
        assertEquals(login, existingUser.getLogin());
        assertEquals(role, existingUser.getRole());
        assertEquals("oldPass", existingUser.getPasswordEncrypted());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void update_ShouldThrowException_WhenIdNull() {
        User updateData = new User(null, login, password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.update(updateData));
        assertEquals("ID must not be null", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void update_ShouldThrowException_WhenLoginNull() {
        User updateData = new User(userId, null, password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.update(updateData));
        assertEquals("Login cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowException_WhenLoginEmpty() {
        User updateData = new User(userId, "", password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.update(updateData));
        assertEquals("Login cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        User updateData = new User(userId, login, password, role);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.update(updateData));
        assertEquals("User id " + userId + " not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void changePassword_ShouldUpdatePasswordAndReturnUser_WhenValid() {
        User existingUser = new User(userId, login, "oldPass", role);
        String newPassword = "newEncryptedPass";
        User changeData = new User(userId, login, newPassword, role);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.changePassword(changeData);

        assertEquals(existingUser, result);
        assertEquals(newPassword, existingUser.getPasswordEncrypted());
        assertEquals(login, existingUser.getLogin());
        assertEquals(role, existingUser.getRole());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void changePassword_ShouldThrowException_WhenIdNull() {
        User changeData = new User(null, login, password, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(changeData));
        assertEquals("ID must not be null", exception.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void changePassword_ShouldThrowException_WhenPasswordNull() {
        User changeData = new User(userId, login, null, role);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(changeData));
        assertEquals("Password must not be null", exception.getMessage());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void changePassword_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        User changeData = new User(userId, login, password, role);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.changePassword(changeData));
        assertEquals("User id " + userId + " not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }
}