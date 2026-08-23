package com.sk.hotelreservationservice.client.userservice;

import com.sk.hotelreservationservice.dto.DiscountDto;
import com.sk.hotelreservationservice.dto.UserDto;
import com.sk.hotelreservationservice.exception.UserServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate userServiceRestTemplate;

    public UserDto findUser(Long userId, String authorization) {
        try {
            return userServiceRestTemplate
                    .exchange("/user/" + userId, HttpMethod.GET, withToken(authorization), UserDto.class)
                    .getBody();
        } catch (RestClientException e) {
            log.error("Failed to load user {} from the user service", userId, e);
            throw new UserServiceUnavailableException("User service is not reachable.");
        }
    }

    public Integer findDiscount(Long userId) {
        try {
            DiscountDto discountDto = userServiceRestTemplate
                    .getForObject("/user/" + userId + "/discount", DiscountDto.class);
            return discountDto == null || discountDto.discount() == null ? 0 : discountDto.discount();
        } catch (RestClientException e) {
            log.warn("Could not read discount for user {}, continuing without one", userId, e);
            return 0;
        }
    }

    public void incrementReservations(String authorization) {
        call("/user/incrementReservationNumber", authorization);
    }

    public void decrementReservations(String authorization) {
        call("/user/decrementReservationNumber", authorization);
    }

    private void call(String path, String authorization) {
        try {
            userServiceRestTemplate.exchange(path, HttpMethod.GET, withToken(authorization), Boolean.class);
        } catch (RestClientException e) {
            log.error("Call to {} failed", path, e);
            throw new UserServiceUnavailableException("User service is not reachable.");
        }
    }

    private HttpEntity<Void> withToken(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        return new HttpEntity<>(headers);
    }
}
