package io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity;

import io.github.Mine4Cut.Mine4Cut_server.common.entity.BaseEntity;
import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity.FrameLike;
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

    @Column(nullable = false/*, length = 몇 글자로 제한할 지 정해야 함*/)
    private String frameName;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private FrameLike frameLike;
}