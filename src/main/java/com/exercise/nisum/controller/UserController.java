package com.exercise.nisum.controller;

import com.exercise.nisum.mapper.PhoneMapper;
import com.exercise.nisum.mapper.UserMapper;
import com.exercise.nisum.model.Phone;
import com.exercise.nisum.model.User;
import com.exercise.nisum.request.user.CreateUserRequest;
import com.exercise.nisum.request.user.UpdateUserRequest;
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
import java.util.UUID;
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


    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = UserMapper.toEntity(request);
        List<Phone> phones = request.phones().stream()
                .map(PhoneMapper::toEntity)
                .collect(Collectors.toList());
        ;
        User savedUser = userService.saveWithPhones(user, phones);

        return new ResponseEntity<>(UserMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        User user = UserMapper.toEntity(request);
        List<Phone> phones = request.phones().stream()
                .map(PhoneMapper::toEntity)
                .collect(Collectors.toList());
        User updatedUser = userService.updateWithPhones(id, user, phones);
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar un usuario eliminado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario restaurado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restoreUser(@PathVariable UUID id) {
        User user = userService.restore(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }
}
