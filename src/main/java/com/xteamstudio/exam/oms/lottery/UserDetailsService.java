package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.entity.UserConfigEntity;
import com.xteamstudio.exam.oms.lottery.repository.UserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

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
        try {
            o = userRepository.findById(username);
        } catch (Throwable e) {
            logger.error("failed 2 find user", e);
            return null;
        }
        return o.orElse(null);
    }

    private UserDetails toUserDetail(UserConfigEntity u) {
        return User.withUsername(u.getId())
                .password(passwordEncoder.encode(u.getPass()))
                .authorities("ROLE_" + u.getRole()).build();
    }
}
