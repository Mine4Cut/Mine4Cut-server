package io.github.Mine4Cut.Mine4Cut_server.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/custom")
    public void customError() {
        throw new CustomException(ErrorCode.NOT_FOUND);
    }

    @GetMapping("/runtime")
    public void runtimeError() {
        throw new RuntimeException("RunErr");
    }
}
