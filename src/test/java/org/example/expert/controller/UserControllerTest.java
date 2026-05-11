package org.example.expert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.controller.UserController;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private UserChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(1L, "test@example.com");
        changePasswordRequest =
                new UserChangePasswordRequest("oldPassword123", "newPassword123");
    }

    @Test
    void getUser_성공() throws Exception {

        // given
        long userId = 1L;

        when(userService.getUser(userId))
                .thenReturn(userResponse);

        // when & then
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void changePassword_성공() throws Exception {
        // given
        AuthUser authUser = new AuthUser(1L,"test@example.com", UserRole.USER);
        doNothing().when(userService).changePassword(eq(1L),any(UserChangePasswordRequest.class));

        //when & then
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest))
                .requestAttr("userId",1L)
                .requestAttr("email","test@example.com")
                .requestAttr("userRole","USER"))
                .andExpect(status().isOk());

        verify(userService, times(1)).changePassword(eq(1L), any(UserChangePasswordRequest.class));
    }
}
