## 🚀 프로젝트 소개

나만의 네컷사진 프레임을 만들고 공유하는 플랫폼

🛠 기술 스택

- Spring Boot 3.5.3
- Spring Data JPA
- Spring Security
- PostgreSQL
- Test Containers

## 📝 개발 컨벤션

커밋 컨벤션
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅, 세미콜론 누락 등
refactor: 코드 리팩토링
test: 테스트 코드 추가/수정
chore: 빌드 업무 수정, 패키지 매니저 수정
dependency : 의존성 추가 / 삭제
```

예시:
```
feat: 사용자 로그인 기능 추가
fix: 결제 모듈 버그 수정
docs: API 문서 업데이트
```
네이밍 컨벤션

- 기본 규칙 (camelCase)
```
변수명: userName, emailAddress
함수명: getUserInfo(), calculateTotalPrice()
클래스명: UserService, OrderController (PascalCase)
패키지명: com.example.userservice
```

- 데이터베이스 (snake_case)
```
테이블명: user_info, order_history
컬럼명: user_id, created_at, email_address
인덱스명: idx_user_email, idx_order_date
```


## 👥 팀원

| 이름  | 역할  | GitHub                                 | 담당 업무                   |
|-----|-----|----------------------------------------|-------------------------|
| 김도현 | 백엔드 | [@Doroddi](https://github.com/Doroddi) | 백엔드 개발, 프로젝트 설계, 인프라 세팅 |
| 문다훈 | 백엔드 | [@ekgns33](https://github.com/ekgns33) | 백엔드 개발, 프로젝트 설계         |


## 패키지 구조

```
src/main/java/io/github/Min4Cut/Min4Cut_server
├── common/
│   └── ...
├── config/
│   └── ...
├── api/
│   └── domain1
│       ├── controller/
│       └── dto/
├── service/
│   └── domain1
│       ├── service/
│       └── dto/
│   
└── domain/
    └── domain1/
        ├── entity/
        ├── repository/
        └── dto/
```

### 계층형 패키지 구조
- api, service, domain을 **수평적으로 계층을 나누어** 각 계층 
  내부에 도메인별 클래스들을 구현합니다.
- common: **전역에서** 사용되는 공통 기능 (예외 처리, 유틸리티, 상수 등)
- config: 애플리케이션 설정 관련 클래스
- api: 웹 계층 (컨트롤러, 요청/응답 DTO)
- service: 비즈니스 로직 계층 (서비스, 서비스 전용 DTO)
- domain: 도메인 계층 (엔티티, 레포지토리, 도메인 DTO)