package com.exercise.nisum.controller;

import com.exercise.nisum.mapper.PhoneMapper;
import com.exercise.nisum.mapper.UserMapper;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.request.user.CreateUserRequest;
import com.exercise.nisum.response.user.UserResponse;
import com.exercise.nisum.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obtener todas los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponse> userResponseList = users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseList);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Usuario inválida")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = UserMapper.toEntity(request);
        List<Phone> phones = request.phones().stream()
                .map(PhoneMapper::toEntity)
                .collect(Collectors.toList());;
        User savedUser = userService.save(user, phones);

        return new ResponseEntity<>(UserMapper.toDto(savedUser), HttpStatus.CREATED);
    }
}
