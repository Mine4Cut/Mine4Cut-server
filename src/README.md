AI 툴 있다 가정

### 1. 개발할거 기능 범위 정하기

- 최대한 PR은 작게.
- 기능 구현 단위

### 2. AI한테 xxx 만들거야~~

- 시니어 엔지니어로....
- SRS(요구사항명세서)를 작성해줘.
- 개발 순서

### 3. 읽어봐. 그리고 공식문서

- 개발을해. << AI를 써서 우선 시켜도됨. 처음에는
- SRS대로 만들어 << 코드를짤거아님.
- 그 코드에서 쓴 기술들이 있잖아. << 이건 이제 너가 보고


```

백엔드 시니어 엔지니어로서 대답해.

SRS를 작성해야해. 지금 프로젝트 초기세팅중이고 SpringBoot사용중이야. 전역 에러 핸들러와 공통 응답 형식 DTO들을 만들거야. 코드는 작성하지말고 설계하고 리뷰받아.
```

```
백엔드 시니어 엔지니어 관점에서 전역 에러 핸들러와 공통 응답 형식 설계를 검토해드리겠습니다.

1. 공통 응답 형식 설계
기본 응답 구조
{
  "success": boolean,
  "code": string,
  "message": string,
  "data": T,
  "timestamp": LocalDateTime,
  "path": string
}
설계 고려사항
일관성: 모든 API 응답이 동일한 구조를 가져야 함
제네릭 타입: ApiResponse<T> 형태로 데이터 타입 안정성 확보
메타데이터: 응답 시간, 요청 경로 등 디버깅에 유용한 정보 포함
페이징: 리스트 응답 시 페이징 정보를 포함할 별도 응답 클래스 필요
2. 에러 응답 구조
에러 응답 확장
{
  "success": false,
  "code": "ERROR_CODE",
  "message": "사용자 친화적 메시지",
  "errors": [
    {
      "field": "fieldName",
      "value": "rejectedValue",
      "reason": "상세 에러 사유"
    }
  ],
  "timestamp": LocalDateTime,
  "path": string
}
3. 전역 에러 핸들러 설계
처리해야 할 예외 유형
비즈니스 예외: 커스텀 BusinessException
Validation 예외: MethodArgumentNotValidException, ConstraintViolationException
HTTP 관련 예외: 404, 405, 415 등
시스템 예외: 500 Internal Server Error
보안 예외: 인증/인가 실패
에러 코드 체계
비즈니스 도메인별 prefix (USER_001, ORDER_001)
HTTP 상태 코드와 매핑
4. 설계 검토 포인트

주의사항
⚠️ 민감정보 노출 방지: 운영환경에서 스택트레이스 숨김 ⚠️ 성능 고려: 불필요한 메타데이터로 인한 응답 크기 증가 ⚠️ 확장성: 향후 요구사항 변경에 대비한 유연한 구조

5. 추가 고려사항
**로깅 전략**

**보안 고려사항**
시스템 에러 정보 노출 최소화
**문서화**
API 문서에 에러 코드 명세 포함
각 도메인별 에러 코드 가이드 작성


```


```declarative
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String path;
    
    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> failure(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> failure(String code, String message, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
```

AI쓸때 중요한 점 >> 클로드 우선 이<<새끼 잘함.
GPT >> o3 제일좋음 >> 사고용임. << SRS를쓸때 o3
- SRS + 질문 + 설계 << o3, claude sonnet4
- 빠른 구현 + 코드 << 4o

커밋은 진자 작은단위임. << 구현단위

- feat (common) : 공통응답 DTO 생성
- refactor : 
- fix : 
- chore :
- test : 

정말 중요한 거 < 민감정보 올리면 ㅈ됨.
- DB Password
- DB username
