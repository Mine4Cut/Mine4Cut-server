package io.github.Mine4Cut.Mine4Cut_server.api.config;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.exception.AuthorizationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserArgumentResolver 테스트")
class UserArgumentResolverTest {

    private UserArgumentResolver resolver;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private WebDataBinderFactory binderFactory;

    private User testUser;

    @BeforeEach
    void setUp() {
        resolver = new UserArgumentResolver();
        
        // 테스트용 사용자 생성
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .nickname("testnick")
                .email("test@example.com")
                .build();
    }

    @AfterEach
    void tearDown() {
        // ThreadLocal 정리
        UserContextHolder.clear();
    }

    @Test
    @DisplayName("User 타입 파라미터를 지원한다")
    void supportsParameter_UserType_ReturnsTrue() {
        // given
        when(methodParameter.getParameterType()).thenReturn((Class) User.class);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("User 타입이 아닌 파라미터는 지원하지 않는다")
    void supportsParameter_NonUserType_ReturnsFalse() {
        // given
        when(methodParameter.getParameterType()).thenReturn((Class) String.class);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("ThreadLocal에 사용자가 있을 때 해당 사용자를 반환한다")
    void resolveArgument_UserExists_ReturnsUser() throws Exception {
        // given
        UserContextHolder.setUser(testUser);

        // when
        Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result).isEqualTo(testUser);
        assertThat(result).isInstanceOf(User.class);
        User returnedUser = (User) result;
        assertThat(returnedUser.getId()).isEqualTo(1L);
        assertThat(returnedUser.getUsername()).isEqualTo("testuser");
        assertThat(returnedUser.getNickname()).isEqualTo("testnick");
        assertThat(returnedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("ThreadLocal에 사용자가 없을 때 null을 반환한다")
    void resolveArgument_NoUser_ReturnsNull() throws Exception {
        // given
        UserContextHolder.clear(); // 명시적으로 사용자 정보 제거

        // when
        Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("UserContextHolder.getUserId() 호출 시 사용자가 있으면 ID를 반환한다")
    void userContextHolder_GetUserId_UserExists_ReturnsUserId() {
        // given
        UserContextHolder.setUser(testUser);

        // when
        Long userId = UserContextHolder.getUserId();

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("UserContextHolder.getUserId() 호출 시 사용자가 없으면 AuthorizationException을 발생시킨다")
    void userContextHolder_GetUserId_NoUser_ThrowsAuthorizationException() {
        // given
        UserContextHolder.clear(); // 사용자 정보 제거

        // when & then
        assertThatThrownBy(() -> UserContextHolder.getUserId())
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("UserContextHolder.getUser() 호출 시 사용자가 있으면 User 객체를 반환한다")
    void userContextHolder_GetUser_UserExists_ReturnsUser() {
        // given
        UserContextHolder.setUser(testUser);

        // when
        User user = UserContextHolder.getUser();

        // then
        assertThat(user).isEqualTo(testUser);
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("UserContextHolder.getUser() 호출 시 사용자가 없으면 null을 반환한다")
    void userContextHolder_GetUser_NoUser_ReturnsNull() {
        // given
        UserContextHolder.clear(); // 사용자 정보 제거

        // when
        User user = UserContextHolder.getUser();

        // then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("UserContextHolder.clear() 호출 시 ThreadLocal이 정리된다")
    void userContextHolder_Clear_ClearsThreadLocal() {
        // given
        UserContextHolder.setUser(testUser);
        assertThat(UserContextHolder.getUser()).isNotNull(); // 사용자가 설정되었는지 확인

        // when
        UserContextHolder.clear();

        // then
        assertThat(UserContextHolder.getUser()).isNull();
    }

}