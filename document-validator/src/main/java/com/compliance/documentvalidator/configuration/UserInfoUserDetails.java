package com.compliance.documentvalidator.configuration;

import com.compliance.documentvalidator.entity.UserCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoUserDetails implements UserDetails {

    private String clientId;

    private String clientSecret;

    private String companyId;

    private List<GrantedAuthority> roles;

    public UserInfoUserDetails(UserCredentials userCredentials) {
        this.clientId = userCredentials.getClientId();
        this.clientSecret = userCredentials.getClientSecret();
        this.companyId = userCredentials.getCompanyId();
        this.roles= Arrays.stream(userCredentials.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return clientSecret;
    }

    @Override
    public String getUsername() {
        return clientId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
