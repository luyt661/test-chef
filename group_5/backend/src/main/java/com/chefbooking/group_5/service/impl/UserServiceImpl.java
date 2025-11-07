package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.RegisterRequest;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService getUserDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Email not found: " + email);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getUserRoles().stream()
                            .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getRoleName()))
                            .toList()
            );
        };
    }



}
