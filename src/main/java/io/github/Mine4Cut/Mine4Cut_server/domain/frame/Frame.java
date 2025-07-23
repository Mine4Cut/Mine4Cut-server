package io.github.Mine4Cut.Mine4Cut_server.domain.frame;

import io.github.Mine4Cut.Mine4Cut_server.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Frame extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false/*, length = 몇 글자로 제한할 지 정해야 함*/)
    private String frameName;

    @Column(nullable = false)
    private String userName;

    @Column
    private int like = 0;
}