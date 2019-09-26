package com.xteamstudio.exam.oms.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xteamstudio.exam.oms.entity.UserConfigEntity;
import com.xteamstudio.exam.oms.lottery.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * see {@link ReactiveUserDetailsService}.
     *
     * @param username userId
     * @return {@link UserDetails}
     */
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> findFromDb(username))
                .map(this::toUserDetail);
    }

    private UserConfigEntity findFromDb(String username) {
        Optional<UserConfigEntity> o;
        String s = redisTemplate.opsForValue().get(RedisKeys.getUserConfig(username));
        UserConfigEntity userConfigEntity;
        try {
            if (!StringUtils.isEmpty(s)) {
                return toUserConfigEntity(s);
            }
            o = userRepository.findById(username);
            userConfigEntity = o.orElse(null);
            redisTemplate.opsForValue().set(RedisKeys.getUserConfig(username),
                    objectMapper.writeValueAsString(userConfigEntity), 60, TimeUnit.MINUTES);
        } catch (Throwable e) {
            logger.error("failed 2 find user", e);
            return null;
        }
        return userConfigEntity;
    }

    private UserConfigEntity toUserConfigEntity(String s) throws IOException {
        return objectMapper.readValue(s, UserConfigEntity.class);
    }

    private UserDetails toUserDetail(UserConfigEntity u) {
        return User.withUsername(u.getId())
                .password(passwordEncoder.encode(u.getPass()))
                .authorities("ROLE_" + u.getRole()).build();
    }
}
