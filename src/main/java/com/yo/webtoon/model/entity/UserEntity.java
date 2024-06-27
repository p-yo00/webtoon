package com.yo.webtoon.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    private String userId;
    private String password;
    private String userName;
    private int point;
    private LocalDateTime registerDate;
    private LocalDateTime deleteDate;
    private LocalDateTime adultCertificationDate;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isEnabled() {
        return deleteDate == null;
    }
}
