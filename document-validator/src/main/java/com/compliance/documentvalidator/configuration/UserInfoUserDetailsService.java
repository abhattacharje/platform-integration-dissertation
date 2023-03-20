package com.compliance.documentvalidator.configuration;

import com.compliance.documentvalidator.entity.UserCredentials;
import com.compliance.documentvalidator.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {

        Optional<UserCredentials> userInfo = userCredentialsRepository.findById(clientId);
        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + clientId));
    }
}
