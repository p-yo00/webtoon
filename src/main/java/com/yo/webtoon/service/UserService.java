package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.constant.Role;
import com.yo.webtoon.model.dto.UserDetail;
import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public UserDetail loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(
                () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        return UserDetail.builder()
            .id(userEntity.getId())
            .userId(userEntity.getUserId())
            .password(userEntity.getPassword())
            .deleteDatetime(userEntity.getDeleteDatetime())
            .role(userEntity.getRole())
            .build();
    }

    /**
     * 회원가입 :: userId 중복체크 후, userEntity에 추가한다.
     */
    public void signUp(UserDto.SignUp signUpInfo) {
        if (userRepository.existsByUserId(signUpInfo.getUserId())) {
            throw new WebtoonException(ErrorCode.ALREADY_EXIST_USER_ID);
        }

        UserEntity userEntity = UserEntity.toEntity(signUpInfo);
        userEntity.setPassword(passwordEncoder.encode(signUpInfo.getPassword()));

        userRepository.save(userEntity);
    }

    /**
     * 아이디/비밀번호 검증 :: 로그인을 위해 일치하는 아이디, 비밀번호가 있는지 검증한 후, 아이디와 권한을 리턴한다.
     */
    public String authenticate(UserDto.Login loginInfo) {
        UserEntity userEntity = userRepository.findByUserId(loginInfo.getUserId())
            .orElseThrow(
                () -> new WebtoonException(ErrorCode.FAILED_LOGIN));

        if (!passwordEncoder.matches(loginInfo.getPassword(), userEntity.getPassword())) {
            throw new WebtoonException(ErrorCode.FAILED_LOGIN);
        }

        return userEntity.getUserId();
    }

    /**
     * 회원 탈퇴 :: delete_datetime를 변경하며, 관리자 권한인 경우에만 자신이 아닌 다른 사용자를 삭제할 수 있다.
     */
    public void deleteUser(Long loginId, Long deleteId) {
        UserEntity loginUser = userRepository.findById(loginId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        if (loginUser.getRole().equals(Role.ROLE_MANAGER)) { // 관리자는 모두 삭제할 수 있다.
            UserEntity deleteUser = userRepository.findById(deleteId)
                .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

            deleteUser.setDeleteDatetime(LocalDateTime.now());
            userRepository.save(deleteUser);
        } else { // 관리자 외 사용자는 로그인 사용자와 일치해야 삭제할 수 있다.
            if (!loginId.equals(deleteId)) {
                throw new WebtoonException(ErrorCode.FORBIDDEN);
            }
            loginUser.setDeleteDatetime(LocalDateTime.now());
            userRepository.save(loginUser);
        }
    }

    /**
     * 회원 수정 :: 기존 비밀번호가 일치하면 새로운 비밀번호와 이름으로 업데이트할 수 있다.
     */
    public void editUser(UserDto.Edit editUser) {
        UserEntity userEntity = userRepository.findById(editUser.getUserId())
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(editUser.getOldPassword(), userEntity.getPassword())) {
            throw new WebtoonException(ErrorCode.WRONG_PASSWORD);
        }

        userEntity.setPassword(passwordEncoder.encode(editUser.getNewPassword()));
        userEntity.setUserName(editUser.getUserName());

        userRepository.save(userEntity);
    }

    /**
     * 사용자 조회 :: 사용자 id와 일치하고 탈퇴하지 않은 사용자를 조회한다.
     */
    public UserDto.Get getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        return UserDto.Get.builder()
            .userName(userEntity.getUserName())
            .userId(userEntity.getUserId())
            .point(userEntity.getPoint())
            .registerDatetime(userEntity.getRegisterDatetime())
            .adultCertificationDate(userEntity.getAdultCertificationDate())
            .build();
    }

    /**
     * 성인 인증 :: 로그인한 사용자의 성인 인증 컬럼에 현재 날짜를 저장한다.
     */
    public void certifyAdult(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        userEntity.setAdultCertificationDate(LocalDate.now());
        userRepository.save(userEntity);
    }

}
