package io.github.Mine4Cut.Mine4Cut_server.api.frame.controller;

import io.github.Mine4Cut.Mine4Cut_server.service.frame.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FrameController {
    private final FrameService frameService;


}
