package com.sk.hoteluserservice.mapper;

import com.sk.hoteluserservice.domain.User;
import com.sk.hoteluserservice.dto.ClientCreateDto;
import com.sk.hoteluserservice.dto.ManagerCreateDto;
import com.sk.hoteluserservice.dto.UserDto;
import com.sk.hoteluserservice.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

        private RoleRepository roleRepository;

        public UserMapper(RoleRepository roleRepository) {
            this.roleRepository = roleRepository;
        }

        public UserDto userToUserDto(User user) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstname());
            userDto.setLastName(user.getLastname());
            userDto.setUsername(user.getUsername());
            userDto.setBirthDate(user.getBirthDate());
            userDto.setPhone(user.getPhone());
            userDto.setPassportNumber(user.getPassportNumber());
            userDto.setNumberOfReservations(user.getNumberOfReservations());
            userDto.setHotelName(user.getHotelName());
            userDto.setHireDate(user.getHireDate());
            return userDto;
        }

        public User clientCreateDtoToUserClient(ClientCreateDto clientCreateDto) {
            User user = new User();
            user.setEmail(clientCreateDto.getEmail());
            user.setFirstname(clientCreateDto.getFirstName());
            user.setLastname(clientCreateDto.getLastName());
            user.setUsername(clientCreateDto.getUsername());
            user.setPassword(clientCreateDto.getPassword());

            user.setBirthDate(clientCreateDto.getBirthDate());
            user.setPhone(clientCreateDto.getPhone());
            user.setPassportNumber(clientCreateDto.getPassportNumber());
            user.setNumberOfReservations(clientCreateDto.getNumberOfReservations());

            user.setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());
            user.setNumberOfReservations(0);

            return user;
        }
        public User managerCreateDtoToUserManager(ManagerCreateDto managerCreateDto) {
            User user = new User();
            user.setEmail(managerCreateDto.getEmail());
            user.setFirstname(managerCreateDto.getFirstName());
            user.setLastname(managerCreateDto.getLastName());
            user.setUsername(managerCreateDto.getUsername());
            user.setPassword(managerCreateDto.getPassword());

            user.setBirthDate(managerCreateDto.getBirthDate());
            user.setPhone(managerCreateDto.getPhone());
            user.setHotelName(managerCreateDto.getHotelName());
            user.setHireDate(managerCreateDto.getHireDate());

            user.setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());
            user.setNumberOfReservations(0);

            return user;
        }
}
