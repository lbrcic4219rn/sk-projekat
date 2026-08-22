package com.sk.hoteluserservice.controller;

import com.sk.hoteluserservice.dto.*;
import com.sk.hoteluserservice.security.CheckSecurity;
import com.sk.hoteluserservice.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
//    @GetMapping
//    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) { //PRIMER ZA API GATEWAY
//
//        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
//    }
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
                                                @RequestBody @Valid UserForbiddDto userForbiddDto) {

        return new ResponseEntity<>(userService.blockAccess(userForbiddDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Register client")
    @PostMapping("/registerClient")
    public ResponseEntity<UserDto> saveClient(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        return new ResponseEntity<>(userService.addClient(clientCreateDto), HttpStatus.CREATED);
    }
    @ApiOperation(value = "Register manager")
    @PostMapping("/registerManager")
    public ResponseEntity<UserDto> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(userService.addManager(managerCreateDto), HttpStatus.CREATED);
    }
    @ApiOperation(value = "Update manager")
    @PutMapping("/updateManager")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<UserDto> updateManager(@RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ManagerUpdateDto managerUpdateDto) {
        return new ResponseEntity<>(userService.updateManager(managerUpdateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update client")
    @PutMapping("/updateClient")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<UserDto> updateClient(@RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ClientUpdateDto clientUpdateDto) {
        return new ResponseEntity<>(userService.updateClient(clientUpdateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = userService.login(tokenRequestDto);
        if(tokenResponseDto != null){
            return new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }




}

