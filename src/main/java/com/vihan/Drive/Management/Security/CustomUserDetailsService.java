package com.vihan.Drive.Management.Security;

import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Repository.AuthRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        
        AuthUserModel authUser = authRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Auth user not found for user id: " + userId));
        
        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                authUser.getEncryptedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public UserDetails loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        AuthUserModel authUser = authRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Auth user not found for username: " + username));
        
        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                authUser.getEncryptedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}