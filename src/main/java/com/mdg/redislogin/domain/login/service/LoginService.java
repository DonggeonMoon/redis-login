package com.mdg.redislogin.domain.login.service;

import com.mdg.redislogin.domain.login.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void login(UserVo userVo, HttpSession session) {
        setUserInfoOnRedis(userVo, session);
        session.setAttribute("user", userVo);
    }

    private void setUserInfoOnRedis(UserVo userVo, HttpSession session) {
        redisTemplate.opsForHash()
                .putAll("user:" + getNewUserId(),
                        Map.of(
                                "userId", userVo.userId(),
                                "sessionId", session.getId()
                        )
                );
    }

    private String getNewUserId() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment("userId");

        return Objects.requireNonNull(valueOperations.get("userId")).toString();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
