package com.fpavel.salfaContacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.dto.UserPasswordDto;
import com.fpavel.salfaContacts.mapper.UserMapper;
import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.security.JwtUtils;
import com.fpavel.salfaContacts.security.UserDetailsServiceImpl;
import com.fpavel.salfaContacts.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private UserDto userDto;
    private UserCreateDto userCreateDto;
    private UserPasswordDto userPasswordDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "TestUser", "TestEncodedPassword", User.Role.USER);
        userDto = new UserDto(1L, "TestUser", User.Role.USER);
        userCreateDto = new UserCreateDto("TestUser", "TestPassword");
        userPasswordDto = new UserPasswordDto(1L, "TestPasswordNew");
    }

    @Test
    void create_ShouldReturnUserDto_WhenValidInput() throws Exception {
        when(userMapper.userCreateDtoToUser(any(UserCreateDto.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.login").value(userDto.login()));

        verify(userMapper).userCreateDtoToUser(eq(userCreateDto));
        verify(userService).create(same(user));
        verify(userMapper).userToUserDto(same(user));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        UserCreateDto invalidDto = new UserCreateDto("", "");

        mockMvc.perform(post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void getById_ShouldReturnUserDto_WhenExists() throws Exception {
        when(userService.getById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.login").value(userDto.login()));

        verify(userService).getById(1L);
        verify(userMapper).userToUserDto(user);
    }

    @Test
    void getById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(userService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(userService).getById(99L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getAll_ShouldReturnListOfUserDto() throws Exception {
        List<User> users = List.of(user);
        when(userService.getAll()).thenReturn(users);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(userDto.id()))
                .andExpect(jsonPath("$[0].login").value(userDto.login()));

        verify(userService).getAll();
        verify(userMapper).userToUserDto(user);
    }

    @Test
    void update_ShouldReturnUpdatedUserDto_WhenValidInput() throws Exception {
        when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(user);
        when(userService.update(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.login").value(userDto.login()));

        verify(userMapper).userDtoToUser(eq(userDto));
        verify(userService).update(same(user));
        verify(userMapper).userToUserDto(same(user));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        UserDto invalidDto = new UserDto(null, "", User.Role.ADMIN);

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void changePassword_ShouldReturnUserDto_WhenValidInput() throws Exception {
        when(userMapper.userPasswordDtoToUser(any(UserPasswordDto.class))).thenReturn(user);
        when(userService.changePassword(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.login").value(userDto.login()));

        verify(userMapper).userPasswordDtoToUser(eq(userPasswordDto));
        verify(userService).changePassword(same(user));
        verify(userMapper).userToUserDto(same(user));
    }

    @Test
    void changePassword_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        UserPasswordDto invalidDto = new UserPasswordDto(null, "");

        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void delete_ShouldReturnOk_WhenDeleted() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/user")
                        .param("userId", "1"))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenUserNotExist() throws Exception {
        doThrow(new EntityNotFoundException("User not found")).when(userService).delete(99L);

        mockMvc.perform(delete("/user")
                        .param("userId", "99"))
                .andExpect(status().isNotFound());

        verify(userService).delete(99L);
    }
}