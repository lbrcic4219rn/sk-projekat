package com.sk.hoteluserservice.service.impl;

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

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private ClientRankRepository clientRankRepository;
    private UserMapper userMapper;
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper;
    private String userRegistratedMessageDestination;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, TokenService tokenService,
                           ClientRankRepository clientRankRepository, JmsTemplate jmsTemplate,
                           ObjectMapper objectMapper, @Value("${destination.message}")String userRegistratedMessageDestination) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.clientRankRepository = clientRankRepository;
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.userRegistratedMessageDestination = userRegistratedMessageDestination;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto addClient(ClientCreateDto clientCreateDto) {
        User user = userMapper.clientCreateDtoToUserClient(clientCreateDto);
        userRepository.save(user);
        //send message to the notification service
        try {
            jmsTemplate.convertAndSend(userRegistratedMessageDestination,
                    objectMapper.writeValueAsString(new NotificationDto(user.getId(), user.getEmail(), "activation email",
                            "ACTIVATION_EMAIL", user.getFirstname(), user.getLastname())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userMapper.userToUserDto(user);
    }
    @Override
    public UserDto addManager(ManagerCreateDto managerCreateDto) {
        User user = userMapper.managerCreateDtoToUserManager(managerCreateDto);
        userRepository.save(user);
        //send message to the notification service
        try {
            jmsTemplate.convertAndSend(userRegistratedMessageDestination,
                    objectMapper.writeValueAsString(new NotificationDto(user.getId(), user.getEmail(), "activation email",
                            "ACTIVATION_EMAIL", user.getFirstname(), user.getLastname())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        if(user.isBlocked()){
            System.out.println("User with username:" +  tokenRequestDto.getUsername() + " and email: " + user.getEmail() + " is blocked");
            return null;
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
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
                        .format("User with username: %s and password: %s not found.", managerUpdateDto.getOldUsername(),
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
                        .format("User with username: %s and password: %s not found.", clientUpdateDto.getOldUsername(),
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
                        .format("User with id: %d not found.", id)));
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
        //auth[0] = "Bearer" -> zato splitujemo authorization
        String[] auth = authorization.split(" ");
        String token = auth[1];
        Claims claims = tokenService.parseToken(token);
        Integer intId = claims.get("id", Integer.class);
        Long userId = Long.valueOf(intId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", userId)));
        user.setNumberOfReservations(user.getNumberOfReservations() + 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean decrementReservations(String authorization) {
        //auth[0] = "Bearer" -> zato splitujemo authorization
        String[] auth = authorization.split(" ");
        String token = auth[1];
        Claims claims = tokenService.parseToken(token);
        Integer intId = claims.get("id", Integer.class);
        Long userId = Long.valueOf(intId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", userId)));
        user.setNumberOfReservations(user.getNumberOfReservations() - 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).get();
        return userMapper.userToUserDto(user);
    }


}