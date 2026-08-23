package com.sk.hoteluserservice.service.impl;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.hoteluserservice.domain.ClientRank;
import com.sk.hoteluserservice.domain.User;
import com.sk.hoteluserservice.dto.*;
import com.sk.hoteluserservice.exception.NotFoundException;
import com.sk.hoteluserservice.mapper.UserMapper;
import com.sk.hoteluserservice.repository.ClientRankRepository;
import com.sk.hoteluserservice.repository.UserRepository;
import com.sk.hoteluserservice.security.service.TokenService;
import com.sk.hoteluserservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_BY_ID = "User with id: %d not found.";
    private static final String USER_NOT_FOUND_BY_CREDENTIALS = "User with username: %s and password: %s not found.";

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ClientRankRepository clientRankRepository;
    private final UserMapper userMapper;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${destination.message}")
    private String userRegistratedMessageDestination;

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto addClient(ClientCreateDto clientCreateDto) {
        User user = userMapper.clientCreateDtoToUserClient(clientCreateDto);
        userRepository.save(user);
        try {
            jmsTemplate.convertAndSend(userRegistratedMessageDestination,
                    objectMapper.writeValueAsString(new NotificationDto(user.getId(), user.getEmail(), "activation email",
                            "ACTIVATION_EMAIL", user.getFirstname(), user.getLastname())));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize notification for user {}", user.getId(), e);
        }
        return userMapper.userToUserDto(user);
    }
    @Override
    public UserDto addManager(ManagerCreateDto managerCreateDto) {
        User user = userMapper.managerCreateDtoToUserManager(managerCreateDto);
        userRepository.save(user);
        try {
            jmsTemplate.convertAndSend(userRegistratedMessageDestination,
                    objectMapper.writeValueAsString(new NotificationDto(user.getId(), user.getEmail(), "activation email",
                            "ACTIVATION_EMAIL", user.getFirstname(), user.getLastname())));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize notification for user {}", user.getId(), e);
        }
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_CREDENTIALS, tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        if(user.isBlocked()){
            log.warn("Blocked user attempted login: username={}, email={}", tokenRequestDto.getUsername(), user.getEmail());
            return null;
        }
        Claims claims = Jwts.claims()
                .add("id", user.getId())
                .add("role", user.getRole().getName())
                .build();
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public boolean blockAccess(UserForbiddDto userForbiddDto) {
        User user = userRepository
                .findUserByUsernameAndEmail(userForbiddDto.getUsername(), userForbiddDto.getEmail())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and email: %s not found.", userForbiddDto.getUsername(),
                                userForbiddDto.getEmail())));
        user.setBlocked(userForbiddDto.isBlocked());
        return true;
    }

    @Override
    public UserDto updateManager(ManagerUpdateDto managerUpdateDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(managerUpdateDto.getOldUsername(), managerUpdateDto.getOldPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_CREDENTIALS, managerUpdateDto.getOldUsername(),
                                managerUpdateDto.getOldPassword())));
        user.setUsername(managerUpdateDto.getNewUsername());
        user.setPassword(managerUpdateDto.getNewPassword());
        user.setEmail(managerUpdateDto.getNewEmail());
        user.setPhone(managerUpdateDto.getNewPhone());
        user.setFirstname(managerUpdateDto.getNewFirstName());
        user.setLastname(managerUpdateDto.getNewLastName());
        user.setHotelName(managerUpdateDto.getNewHotelName());
        user.setHireDate(managerUpdateDto.getNewHireDate());

        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateClient(ClientUpdateDto clientUpdateDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(clientUpdateDto.getOldUsername(), clientUpdateDto.getOldPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_CREDENTIALS, clientUpdateDto.getOldUsername(),
                                clientUpdateDto.getOldPassword())));
        user.setUsername(clientUpdateDto.getNewUsername());
        user.setPassword(clientUpdateDto.getNewPassword());
        user.setEmail(clientUpdateDto.getNewEmail());
        user.setPhone(clientUpdateDto.getNewPhone());
        user.setFirstname(clientUpdateDto.getNewFirstName());
        user.setLastname(clientUpdateDto.getNewLastName());
        user.setNumberOfReservations(clientUpdateDto.getNewNumberOfReservations());
        user.setPassportNumber(clientUpdateDto.getNewPassportNumber());

        return userMapper.userToUserDto(user);
    }

    @Override
    public DiscountDto findDiscount(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_ID, id)));
        List<ClientRank> clientRankList = clientRankRepository.findAll();

        Integer discount = clientRankList.stream()
                .filter(clientRank -> clientRank.getMaxNumberOfReservations() >= user.getNumberOfReservations()
                        && clientRank.getMinNumberOfReservations() <= user.getNumberOfReservations())
                .findAny()
                .get()
                .getDiscount();
        return new DiscountDto(discount);
    }

    @Override
    public Boolean incrementReservations(String authorization) {
        String[] auth = authorization.split(" ");
        String token = auth[1];
        Claims claims = tokenService.parseToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid token."));
        Integer intId = claims.get("id", Integer.class);
        Long userId = Long.valueOf(intId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_ID, userId)));
        user.setNumberOfReservations(user.getNumberOfReservations() + 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean decrementReservations(String authorization) {
        String[] auth = authorization.split(" ");
        String token = auth[1];
        Claims claims = tokenService.parseToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid token."));
        Integer intId = claims.get("id", Integer.class);
        Long userId = Long.valueOf(intId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_ID, userId)));
        user.setNumberOfReservations(user.getNumberOfReservations() - 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_BY_ID, id)));
        return userMapper.userToUserDto(user);
    }

}