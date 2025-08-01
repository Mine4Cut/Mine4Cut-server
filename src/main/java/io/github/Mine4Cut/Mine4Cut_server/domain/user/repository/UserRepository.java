package io.github.Mine4Cut.Mine4Cut_server.domain.user.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    @Modifying
    int deleteAllByDeletedTrueAndDeletedAtBefore(LocalDateTime cutoff);
}
