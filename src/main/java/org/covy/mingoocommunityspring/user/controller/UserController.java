package org.covy.mingoocommunityspring.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.covy.mingoocommunityspring.config.jwt.JwtTokenProvider;
import org.covy.mingoocommunityspring.user.dto.LoginRequestDto;
import org.covy.mingoocommunityspring.user.dto.LoginResponseDto;
import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.dto.UserResponseDto;
import org.covy.mingoocommunityspring.user.model.User;

import java.util.Optional;

import org.covy.mingoocommunityspring.user.service.LogoutService;
import org.covy.mingoocommunityspring.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LogoutService logoutService;

    /**
     * POST /api/users/register
     * 회원가입 요청 처리
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid UserRegisterDto dto
    ) {
        User created = userService.register(dto);
        UserResponseDto response = new UserResponseDto(
                created.getId(),
                created.getEmail(),
                created.getUsername()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        Optional<User> userOpt = userService.login(request.getEmail(), request.getPassword());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = userOpt.get();
        String token = jwtTokenProvider.createToken(user.getId());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logoutService.logout(token);
        }

        return ResponseEntity.ok().build();
    }

}
