# CLAUDE.md

이 파일은 이 저장소에서 작업하는 Claude Code(claude.ai/code)에게 제공되는 가이드입니다.

## 프로젝트 개요

Spring Boot 기반 게시판 웹 애플리케이션입니다: Thymeleaf 서버 사이드 렌더링 뷰, Spring Data JPA + MySQL, 그리고 (Spring Security 없이) 직접 구현한 세션 기반 인증을 사용합니다. 코드 주석, 로그 메시지, 유효성 검증 메시지는 한글로 작성되어 있습니다.

## 명령어

모든 명령어는 `board/`(Gradle 프로젝트 루트, `gradlew`가 있는 위치)에서 실행합니다.

- 앱 실행: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)
- 빌드: `./gradlew build`
- 전체 테스트 실행: `./gradlew test`
- 단일 테스트 클래스 실행: `./gradlew test --tests "com.example.board.BoardApplicationTests"`

### 데이터베이스 사전 조건

앱을 실행하려면 `src/main/resources/application.yaml` 설정에 맞는 MySQL 인스턴스가 실행 중이어야 합니다:
- `jdbc:mysql://localhost:3306/boarddb`, 사용자 `root`, 비밀번호 `1234`.
- `spring.jpa.hibernate.ddl-auto`가 `create`로 설정되어 있어 **애플리케이션을 시작할 때마다 스키마가 삭제되고 재생성됩니다** — 재시작 후에도 데이터가 유지될 것으로 기대하면 안 됩니다.
- `InitData`(`src/main/java/com/example/board/InitData.java`)는 시작 시 회원 데이터를 시드(seed)합니다. `loginId`가 `"user1"`인 회원이 있는지 확인하지만, 실제로 생성하는 회원은 `loginId "1111"` / 비밀번호 `"1111"`입니다 — 이는 설정 옵션이 아니라 시드 로직에 존재하는 불일치입니다.

## 아키텍처

표준 계층형 MVC 구조입니다: `controller` → `service` → `repository`(Spring Data JPA) 순서이며, `src/main/resources/templates/{board,member}`의 Thymeleaf 템플릿을 컨트롤러가 뷰 이름 문자열을 반환하는 방식으로 렌더링합니다.

- **엔티티** (`domain/`): `Board`와 `Member`. `Board.member`는 `Member`에 대한 지연(lazy) `@ManyToOne` 관계입니다. `BoardRepository`에는 N+1/지연 로딩 초기화 문제를 피하기 위해 `join fetch`로 member를 함께 가져오는 `findWithMember()` / `findWithMember(id)` JPQL 쿼리가 있습니다 — 뷰에서 `board.member`가 필요할 때는 `findById`/`findAll`보다 이 메서드들을 우선 사용해야 합니다.
- **인증**: Spring Security를 사용하지 않습니다. 로그인 상태는 `Member` 객체를 `HttpSession`에 `"loginMember"`라는 속성 키로 직접 저장하는 방식으로 관리됩니다. 인증이 필요한 각 컨트롤러(`BoardController.loginMember(session)` 참고)는 이 속성을 직접 읽어서, 값이 없으면 `/login`으로 리다이렉트합니다(필요 시 `redirectUrl` 쿼리 파라미터 포함). 비밀번호는 평문으로 저장 및 비교됩니다(`MemberService.login`) — 해싱이 적용되어 있지 않습니다.
- **인가(권한)**: 수정/삭제 권한(작성자 본인 확인)은 컨트롤러별로 개별 구현되어 있습니다(예: `BoardService.isOwner`), 세션 회원의 `loginId`와 `board.getMember().getLoginId()`를 비교하는 방식입니다. `BoardController.edit`/`delete`는 현재 `isOwner`를 호출하지 **않습니다**(`editForm`만 호출) — 해당 엔드포인트를 확장할 때 이 점을 유의해야 합니다.
- **DTO vs 엔티티**: 폼 제출은 `dto/`에 있는 DTO(`BoardForm`, `LoginForm`, `MemberJoinForm`)에 바인딩되며 `jakarta.validation` 어노테이션으로 검증됩니다. 서비스 계층에서 이를 JPA 엔티티로 변환/역변환합니다. 게시글 수정 POST(`BoardController.edit`)는 DTO가 아니라 `@ModelAttribute`로 `Board` 엔티티에 직접 바인딩합니다 — write/join/login과 일관성이 없는 부분입니다.
- **필터**: `WebConfig`가 `LogFilter`(`web/filter/LogFilter.java`)를 `/*` 경로에 등록하여 요청/응답 URI를 로깅합니다. 요청 처리 파이프라인에 존재하는 유일한 필터/인터셉터입니다.
- **메시지**: `spring.messages.basename`은 `messages, errors`로 설정되어 있지만, 현재는 `errors.properties`만 존재합니다(Bean Validation 메시지 오버라이드에 사용).
