package com.yo.webtoon.service;

import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.model.dto.UserDto.Authorization;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User ID Not found -> " + userId));
    }

    /**
     * 회원가입 :: userId 중복체크 후, userEntity에 추가한다.
     */
    public void signUp(UserDto.SignUp signUpInfo) {
        if (userRepository.existsByUserId(signUpInfo.getUserId())) {
            throw new RuntimeException("already exists ID -> " + signUpInfo.getUserId());
        }

        UserEntity userEntity = UserEntity.toEntity(signUpInfo);
        userEntity.setPassword(passwordEncoder.encode(signUpInfo.getPassword()));

        userRepository.save(userEntity);
    }

    /**
     * 아이디/비밀번호 검증 :: 로그인을 위해 일치하는 아이디, 비밀번호가 있는지 검증한 후, 아이디와 권한을 리턴한다.
     */
    public UserDto.Authorization authenticate(UserDto.Login loginInfo) {
        UserEntity userEntity = userRepository.findByUserId(loginInfo.getUserId())
            .orElseThrow(() -> new RuntimeException("failed login"));

        if (!passwordEncoder.matches(loginInfo.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("failed login");
        }

        return new Authorization(userEntity.getUserId(), userEntity.getRole().name());
    }
}
