package com.sk.hoteluserservice.service;

import com.sk.hoteluserservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);
    UserDto addClient(ClientCreateDto clientCreateDto);
    UserDto addManager(ManagerCreateDto managerCreateDto);
    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    boolean blockAccess(UserForbiddDto userForbiddDto);
    UserDto updateManager(ManagerUpdateDto managerUpdateDto);
    UserDto updateClient(ClientUpdateDto clientUpdateDto);
    DiscountDto findDiscount(Long id);

    Boolean incrementReservations(String authorization);

    Boolean decrementReservations(String authorization);

    UserDto findById(Long id);
}
