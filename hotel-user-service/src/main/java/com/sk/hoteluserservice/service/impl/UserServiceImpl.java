package com.sk.hoteluserservice.service.impl;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.hoteluserservice.domain.ClientRank;
import com.sk.hoteluserservice.domain.User;
import com.sk.hoteluserservice.dto.*;
import com.sk.hoteluserservice.exception.BlockedAccountException;
import com.sk.hoteluserservice.exception.DuplicateResourceException;
import com.sk.hoteluserservice.exception.InvalidCredentialsException;
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
    private String userRegisteredMessageDestination;

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto addClient(ClientCreateDto clientCreateDto) {
        verifyUnique(clientCreateDto.username(), clientCreateDto.email());
        User user = userMapper.clientCreateDtoToUserClient(clientCreateDto);
        userRepository.save(user);
        try {
            jmsTemplate.convertAndSend(userRegisteredMessageDestination,
                    objectMapper.writeValueAsString(NotificationDto.builder()
                            .userId(user.getId())
                            .to(user.getEmail())
                            .subject("activation email")
                            .type("ACTIVATION_EMAIL")
                            .userFirstName(user.getFirstname())
                            .userLastName(user.getLastname())
                            .build()));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize notification for user {}", user.getId(), e);
        }
        return userMapper.userToUserDto(user);
    }
    @Override
    public UserDto addManager(ManagerCreateDto managerCreateDto) {
        verifyUnique(managerCreateDto.username(), managerCreateDto.email());
        User user = userMapper.managerCreateDtoToUserManager(managerCreateDto);
        userRepository.save(user);
        try {
            jmsTemplate.convertAndSend(userRegisteredMessageDestination,
                    objectMapper.writeValueAsString(NotificationDto.builder()
                            .userId(user.getId())
                            .to(user.getEmail())
                            .subject("activation email")
                            .type("ACTIVATION_EMAIL")
                            .userFirstName(user.getFirstname())
                            .userLastName(user.getLastname())
                            .build()));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize notification for user {}", user.getId(), e);
        }
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.username(), tokenRequestDto.password())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password."));
        if(user.isBlocked()){
            log.warn("Blocked user attempted login: username={}, email={}", tokenRequestDto.username(), user.getEmail());
            throw new BlockedAccountException(String.format("Account %s is blocked.", tokenRequestDto.username()));
        }
        Claims claims = Jwts.claims()
                .add("id", user.getId())
                .add("role", user.getRole().getName())
                .build();
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public boolean blockAccess(UserForbidDto userForbidDto) {
        User user = userRepository
                .findUserByUsernameAndEmail(userForbidDto.username(), userForbidDto.email())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and email: %s not found.", userForbidDto.username(),
                                userForbidDto.email())));
        user.setBlocked(userForbidDto.blocked());
        return true;
    }

    @Override
    public UserDto updateManager(ManagerUpdateDto managerUpdateDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(managerUpdateDto.oldUsername(), managerUpdateDto.oldPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_CREDENTIALS, managerUpdateDto.oldUsername(),
                                managerUpdateDto.oldPassword())));
        user.setUsername(managerUpdateDto.newUsername());
        user.setPassword(managerUpdateDto.newPassword());
        user.setEmail(managerUpdateDto.newEmail());
        user.setPhone(managerUpdateDto.newPhone());
        user.setFirstname(managerUpdateDto.newFirstName());
        user.setLastname(managerUpdateDto.newLastName());
        user.setHotelName(managerUpdateDto.newHotelName());
        user.setHireDate(managerUpdateDto.newHireDate());

        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateClient(ClientUpdateDto clientUpdateDto) {
        User user = userRepository
                .findUserByUsernameAndPassword(clientUpdateDto.oldUsername(), clientUpdateDto.oldPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format(USER_NOT_FOUND_BY_CREDENTIALS, clientUpdateDto.oldUsername(),
                                clientUpdateDto.oldPassword())));
        user.setUsername(clientUpdateDto.newUsername());
        user.setPassword(clientUpdateDto.newPassword());
        user.setEmail(clientUpdateDto.newEmail());
        user.setPhone(clientUpdateDto.newPhone());
        user.setFirstname(clientUpdateDto.newFirstName());
        user.setLastname(clientUpdateDto.newLastName());
        user.setNumberOfReservations(clientUpdateDto.newNumberOfReservations());
        user.setPassportNumber(clientUpdateDto.newPassportNumber());

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
                .orElseThrow(() -> new NotFoundException("Discount not found."))
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

    private void verifyUnique(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(String.format("Username %s is already taken.", username));
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(String.format("Email %s is already registered.", email));
        }
    }
}
