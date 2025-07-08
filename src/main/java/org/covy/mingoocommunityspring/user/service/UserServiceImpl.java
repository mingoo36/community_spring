package org.covy.mingoocommunityspring.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.dto.UserUpdateRequestDto;
import org.covy.mingoocommunityspring.user.model.User;
import org.covy.mingoocommunityspring.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserRegisterDto dto) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 엔티티 생성 및 패스워드 암호화
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());

        // 저장 후 반환
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // username 수정 (null이 아닌 경우에만)
        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()) {
            user.setUsername(dto.getUsername().trim());
        }
        
        // image 수정 (null이 아닌 경우에만)
        if (dto.getImage() != null) {
            user.setImage(dto.getImage().trim().isEmpty() ? null : dto.getImage().trim());
        }
        
        // 수정 시간 업데이트
        user.setUpdatedAt(Instant.now());
        
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }




}
