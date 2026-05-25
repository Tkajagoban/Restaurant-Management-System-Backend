package com.restaurent.RMS.services;

import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.repositories.UserRepository;
import com.restaurent.RMS.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoleAndPrivileges(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user, getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        if (user.getRole() == null) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add Role Authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().toUpperCase()));

        // Add Privilege Authorities (Additive logic with Status)
        if (user.getRole().getRolePrivileges() != null) {
            user.getRole().getRolePrivileges().forEach(rp -> {
                if (rp.getRestaurantPrivilege() != null && rp.getRestaurantPrivilege().getPrivilege() != null
                        && rp.getPrivilegeStatus() != null) {

                    String privName = rp.getRestaurantPrivilege().getPrivilege().getName();
                    String status = rp.getPrivilegeStatus().name();

                    // Create authority in "Name:Status" format
                    String combinedAuthority = privName + ":" + status;

                    authorities.add(new SimpleGrantedAuthority(combinedAuthority));

                    // Also keep the base privilege name as an authority for general access
                    authorities.add(new SimpleGrantedAuthority(privName));

                    System.out.println("DEBUG: Added authorities: [" + combinedAuthority + ", " + privName
                            + "] for user: " + user.getEmail());
                }
            });
        }

        System.out.println("DEBUG: Total authorities for " + user.getEmail() + ": " + authorities);
        return authorities;
    }

}