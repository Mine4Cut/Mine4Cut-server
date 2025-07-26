package io.github.Mine4Cut.Mine4Cut_server.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TestController.class)
@Import({GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void customException_isHandledByGlobalHandler() throws Exception {
        mockMvc.perform(get("/test/custom"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errorCode").value("1001"))    // enum의 code 값
                .andExpect(jsonPath("$.message").value("존재하지 않는 유저입니다."))
                .andExpect(jsonPath("$.path").value("/test/custom"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void runtimeException_isHandledAsInternalError() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("서버 내부 오류"))
                .andExpect(jsonPath("$.path").value("/test/runtime"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
