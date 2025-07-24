package io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import jakarta.persistence.*;

@Entity
public class FrameLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_id")
    private Frame frame;
}
