package com.restaurent.RMS.config;

import com.restaurent.RMS.entities.*;
import com.restaurent.RMS.enums.PrivilegeStatus;
import com.restaurent.RMS.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrivilegeRepository privilegeRepository;
    private final RestaurantPrivilegeRepository restaurantPrivilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("Mock data already exists. Skipping...");
            return;
        }

        System.out.println("loading mock data");

        // Restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("KBC Restaurant");
        restaurant.setAddress("Jaffna-Manipay-Karainagar Rd, Jaffna");
        restaurant.setCity("Manipay");
        restaurant.setEmail("kbc.rms018@gmail.com");
        restaurant.setLogoImage(null);
        restaurant.setPhoneNumber("0217218865");
        restaurant.setWebSite("www.kbc.lk");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Role
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRestaurant(savedRestaurant);
        adminRole = roleRepository.save(adminRole);

        Role chefRole = new Role();
        chefRole.setRoleName("CHEF");
        chefRole.setRestaurant(savedRestaurant);
        roleRepository.save(chefRole);

        Role stewardRole = new Role();
        stewardRole.setRoleName("STEWARD");
        stewardRole.setRestaurant(savedRestaurant);
        roleRepository.save(stewardRole);

        // User
        User user = new User();
        user.setAddress("Near the kalikovil thavady north kokuvil.");
        user.setCity("Kokuvil");
        user.setEmail("tkajagoban19@gmail.com");
        user.setFirstName("owner");
        user.setLastName("koban");
        user.setNic("200001801396");
        user.setPassword(bCryptPasswordEncoder.encode("Koban@123"));
        user.setPhoneNumber("0751880587");
        user.setRole(adminRole);
        userRepository.save(user);

        User user2 = new User();
        user2.setAddress("Mullaitheevu.");
        user2.setCity("Murasumoottai");
        user2.setEmail("sgicrms15@gmail.com");
        user2.setFirstName("Kj");
        user2.setLastName("koban");
        user2.setNic("201020384643");
        user2.setPassword(bCryptPasswordEncoder.encode("Kj1234@"));
        user2.setPhoneNumber("0761880587");
        user2.setRole(chefRole);
        userRepository.save(user2);

        User user3 = new User();
        user3.setAddress("kalmunai.");
        user3.setCity("natpitty");
        user3.setEmail("kalainilavan2002@gmail.com");
        user3.setFirstName("kaja");
        user3.setLastName("Jenagan");
        user3.setNic("201645384643");
        user3.setPassword(bCryptPasswordEncoder.encode("kalai1234@"));
        user3.setPhoneNumber("0721880587");
        user3.setRole(stewardRole);
        userRepository.save(user3);


        // Privilege
        List<String> privilegeList = List.of(
                "Role Management",
                "User Management",
                "Role Privileges",
                "Restaurant Privilege",
                "Email Settings",
                "Tax Settings",
                "Restaurant Management",
                "Food Management",
                "Table Management",
                "Order Management",
                "Admin Dashboard",
                "Chef Dashboard",
                "Steward Dashboard",
                "Invoice Management");

        for (String privilegeName : privilegeList) {
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            Privilege savedPrivilege = privilegeRepository.save(privilege);

            // Restaurant privilege
            RestaurantPrivilege restaurantPrivilege = new RestaurantPrivilege();
            restaurantPrivilege.setRestaurant(savedRestaurant);
            restaurantPrivilege.setPrivilege(savedPrivilege);
            restaurantPrivilege.setActive(true);
            restaurantPrivilegeRepository.save(restaurantPrivilege);
        }

        // RolePrivilege

        Role admin = roleRepository.findByRoleName("ADMIN");
        Role chef = roleRepository.findByRoleName("CHEF");
        Role steward = roleRepository.findByRoleName("STEWARD");

        List<RestaurantPrivilege> allRestaurantPrivileges = restaurantPrivilegeRepository.findAll();

        for (RestaurantPrivilege rp : allRestaurantPrivileges) {
            // Admin gets everything
            RolePrivilege adminRP = new RolePrivilege();
            adminRP.setPrivilegeStatus(PrivilegeStatus.MAINTAIN);
            adminRP.setRestaurantPrivilege(rp);
            adminRP.setRole(admin);
            rolePrivilegeRepository.save(adminRP);

            // Chef gets Chef Dashboard
            if ("Chef Dashboard".equals(rp.getPrivilege().getName())) {
                RolePrivilege chefRP = new RolePrivilege();
                chefRP.setPrivilegeStatus(PrivilegeStatus.MAINTAIN);
                chefRP.setRestaurantPrivilege(rp);
                chefRP.setRole(chef);
                rolePrivilegeRepository.save(chefRP);
            }

            // Steward gets Steward Dashboard
            if ("Steward Dashboard".equals(rp.getPrivilege().getName())) {
                RolePrivilege stewardRP = new RolePrivilege();
                stewardRP.setPrivilegeStatus(PrivilegeStatus.MAINTAIN);

                stewardRP.setRole(steward);
                rolePrivilegeRepository.save(stewardRP);
            }
        }

        System.out.println("Data loaded successfully");
    }
}
