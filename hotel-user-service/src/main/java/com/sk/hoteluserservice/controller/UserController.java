package com.sk.hoteluserservice.controller;

import lombok.RequiredArgsConstructor;

import com.sk.hoteluserservice.dto.*;
import com.sk.hoteluserservice.security.CheckSecurity;
import com.sk.hoteluserservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_CLIENT", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> getUserByID(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable("id") Long id) {

        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/discount")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDiscount(id), HttpStatus.OK);
    }
    @GetMapping("/incrementReservationNumber")
    public ResponseEntity<Boolean> incrementReservationNumber(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(userService.incrementReservations(authorization), HttpStatus.OK);
    }
    @GetMapping("/decrementReservationNumber")
    public ResponseEntity<Boolean> decrementReservationNumber(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(userService.decrementReservations(authorization), HttpStatus.OK);
    }

    @PostMapping("/forbidAccess")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Boolean> forbidAccess(@RequestHeader("Authorization") String authorization,
                                                @RequestBody @Valid UserForbidDto userForbidDto) {

        return new ResponseEntity<>(userService.blockAccess(userForbidDto), HttpStatus.OK);
    }

    @Operation(summary = "Register client")
    @PostMapping("/registerClient")
    public ResponseEntity<UserDto> saveClient(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        return new ResponseEntity<>(userService.addClient(clientCreateDto), HttpStatus.CREATED);
    }
    @Operation(summary = "Register manager")
    @PostMapping("/registerManager")
    public ResponseEntity<UserDto> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(userService.addManager(managerCreateDto), HttpStatus.CREATED);
    }
    @Operation(summary = "Update manager")
    @PutMapping("/updateManager")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<UserDto> updateManager(@RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ManagerUpdateDto managerUpdateDto) {
        return new ResponseEntity<>(userService.updateManager(managerUpdateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update client")
    @PutMapping("/updateClient")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<UserDto> updateClient(@RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ClientUpdateDto clientUpdateDto) {
        return new ResponseEntity<>(userService.updateClient(clientUpdateDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }

}

