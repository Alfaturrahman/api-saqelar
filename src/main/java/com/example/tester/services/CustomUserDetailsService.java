package com.example.tester.services;

import com.example.tester.models.User;
import com.example.tester.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User dbUser = user.get();
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(dbUser.getUsername());
        builder.password(dbUser.getPassword());

        // Role sudah ada di DB dalam format "ROLE_USER", langsung gunakan tanpa modifikasi
        String[] roles = dbUser.getRole().split(",");
        builder.roles(roles);

        return builder.build();
    }
}
