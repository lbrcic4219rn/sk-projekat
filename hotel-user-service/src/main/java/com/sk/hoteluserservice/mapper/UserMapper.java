package com.sk.hoteluserservice.mapper;


import com.sk.hoteluserservice.domain.User;
import org.springframework.http.HttpStatus;
import com.sk.hoteluserservice.exception.ErrorCode;
import com.sk.hoteluserservice.exception.CustomException;
import com.sk.hoteluserservice.domain.Role;
import com.sk.hoteluserservice.dto.ClientCreateDto;
import com.sk.hoteluserservice.dto.ManagerCreateDto;
import com.sk.hoteluserservice.dto.UserDto;
import com.sk.hoteluserservice.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public record UserMapper(RoleRepository roleRepository) {

        private static final String ROLE_CLIENT = "ROLE_CLIENT";
        private static final String ROLE_MANAGER = "ROLE_MANAGER";


        public UserDto userToUserDto(User user) {
            return UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .username(user.getUsername())
                    .birthDate(user.getBirthDate())
                    .phone(user.getPhone())
                    .passportNumber(user.getPassportNumber())
                    .numberOfReservations(user.getNumberOfReservations())
                    .hotelName(user.getHotelName())
                    .hireDate(user.getHireDate())
                    .build();
        }

        public User clientCreateDtoToUserClient(ClientCreateDto clientCreateDto) {
            User user = new User();
            user.setEmail(clientCreateDto.email());
            user.setFirstname(clientCreateDto.firstName());
            user.setLastname(clientCreateDto.lastName());
            user.setUsername(clientCreateDto.username());
            user.setPassword(clientCreateDto.password());

            user.setBirthDate(clientCreateDto.birthDate());
            user.setPhone(clientCreateDto.phone());
            user.setPassportNumber(clientCreateDto.passportNumber());
            user.setNumberOfReservations(clientCreateDto.numberOfReservations());

            user.setRole(role(ROLE_CLIENT));
            user.setNumberOfReservations(0);

            return user;
        }
        public User managerCreateDtoToUserManager(ManagerCreateDto managerCreateDto) {
            User user = new User();
            user.setEmail(managerCreateDto.email());
            user.setFirstname(managerCreateDto.firstName());
            user.setLastname(managerCreateDto.lastName());
            user.setUsername(managerCreateDto.username());
            user.setPassword(managerCreateDto.password());

            user.setBirthDate(managerCreateDto.birthDate());
            user.setPhone(managerCreateDto.phone());
            user.setHotelName(managerCreateDto.hotelName());
            user.setHireDate(managerCreateDto.hireDate());

            user.setRole(role(ROLE_MANAGER));
            user.setNumberOfReservations(0);

            return user;
        }

        private Role role(String name) {
            return roleRepository.findRoleByName(name)
                    .orElseThrow(() -> new CustomException(
                            String.format("Role %s is not configured.", name),
                            ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR));
        }
}
