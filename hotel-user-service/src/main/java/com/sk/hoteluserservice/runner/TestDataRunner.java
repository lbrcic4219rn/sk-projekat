package com.sk.hoteluserservice.runner;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import com.sk.hoteluserservice.domain.ClientRank;
import com.sk.hoteluserservice.domain.Rank;
import com.sk.hoteluserservice.domain.Role;
import com.sk.hoteluserservice.domain.User;
import com.sk.hoteluserservice.repository.ClientRankRepository;
import com.sk.hoteluserservice.repository.RoleRepository;
import com.sk.hoteluserservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClientRankRepository clientRankRepository;

    @Override
    public void run(String... args) throws Exception {
        Role roleClient = new Role("ROLE_CLIENT", "Client role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
        Role roleManager = new Role("ROLE_MANAGER", "Manager_role");

        roleRepository.save(roleClient);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(roleAdmin);
        admin.setFirstname("Takola");
        admin.setLastname("Nikolic");
        admin.setBirthDate(LocalDate.of(2000, 2, 11));
        admin.setPhone("0648983312");

        userRepository.save(admin);

        clientRankRepository.save(new ClientRank(0, 5, Rank.BRONZE, 10));
        clientRankRepository.save(new ClientRank(6, 10, Rank.SILVER, 20));
        clientRankRepository.save(new ClientRank(11, 20, Rank.GOLD, 30));
    }
}
