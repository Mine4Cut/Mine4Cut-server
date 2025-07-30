package io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity;

import io.github.Mine4Cut.Mine4Cut_server.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Frame extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String nicknameSnapshot;

    //TODO 프레임 이름 글자수 제한 논의해야함.
    @Column(nullable = false)
    private String frameName;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private int likeCount;

    public void decreaseLike() {
        this.likeCount--;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void renameFrame(String frameName) {
        this.frameName = frameName;
    }

    public void renameNickname(String nickname) {
        this.nicknameSnapshot = nickname;
    }
}