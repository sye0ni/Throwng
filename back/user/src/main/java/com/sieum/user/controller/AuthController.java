package com.sieum.user.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

import com.sieum.user.domain.User;
import com.sieum.user.dto.MemberTokens;
import com.sieum.user.dto.response.AccessTokenResponse;
import com.sieum.user.service.LoginService;
import com.sieum.user.util.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final int COOKIE_AGE_SECONDS = 604800;

    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login/{provider}")
    public ResponseEntity<AccessTokenResponse> login(
            HttpServletRequest request,
            @PathVariable final String provider,
            @RequestParam("code") String code,
            final HttpServletResponse response) {
        final User user = loginService.login(provider, code);
        final MemberTokens memberTokens = jwtUtil.createJwtToken(user);
        final ResponseCookie cookie =
                ResponseCookie.from("refresh-token", memberTokens.getRefreshToken())
                        .maxAge(COOKIE_AGE_SECONDS)
                        .sameSite("None")
                        .secure(true)
                        .httpOnly(true)
                        .path("/")
                        .build();
        response.addHeader(SET_COOKIE, cookie.toString());
        return ResponseEntity.status(CREATED)
                .body(new AccessTokenResponse(memberTokens.getAccessToken()));
    }
}
