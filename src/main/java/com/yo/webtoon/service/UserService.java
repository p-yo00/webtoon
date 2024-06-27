package com.yo.webtoon.service;

import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("User ID Not found -> " + username));
    }
}
