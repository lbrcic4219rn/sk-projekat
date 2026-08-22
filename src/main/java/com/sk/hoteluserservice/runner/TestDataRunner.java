package com.sk.hoteluserservice.runner;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private ClientRankRepository clientRankRepository;

    public TestDataRunner(RoleRepository roleRepository, UserRepository userRepository, ClientRankRepository clientRankRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.clientRankRepository = clientRankRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Insert roles
        Role roleClient = new Role("ROLE_CLIENT", "Client role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
        Role roleManager = new Role("ROLE_MANAGER", "Manager_role");

        roleRepository.save(roleClient);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleManager);
        //Insert admin
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(roleAdmin);
        admin.setFirstname("Takola");
        admin.setLastname("Nikolic");
        admin.setBirthDate(new GregorianCalendar(2000, Calendar.FEBRUARY, 11).getTime());
        admin.setPhone("0648983312");

        userRepository.save(admin);

        clientRankRepository.save(new ClientRank(0, 5, Rank.BRONZE, 10));
        clientRankRepository.save(new ClientRank(6, 10, Rank.SILVER, 20));
        clientRankRepository.save(new ClientRank(11, 20, Rank.GOLD, 30));
    }
}
