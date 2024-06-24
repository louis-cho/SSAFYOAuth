# SSAFYAuth

## ✨서비스 설명

![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/f14c53b6-1ed3-48f1-a83c-b736e6d03e3b)

## 팀원

| **김다나** | **김시은** | **서재화** | **이동재** | **이주연** |     **조현우**      |
| :--------: | :--------: | :--------: | :--------: | :--------: | :-----------------: |
| Fullstack  | Fullstack  | Fullstack  | Fullstack  |   Infra    | Fullstack <br> 팀장 |

### 개요 🧾

- **SSAFY 교육생들을 위해 OAuth2.0 인증, 인가, 모니터링 시스템**을 제공하여 회원 기능 개발에 대한 시간과 비용을 절감하고 비즈니스 로직에 집중하도록 기획한 프로젝트입니다.

### 주요 기능 💡

- `인증, 인가` 별도의 회원 가입 없이 OAuth 2.0 기반 SSO 인증을 제공합니다.
- `모니터링` Elasticsearch를 통해 별도 데이터를 유지하여 유저 로그를 빠르게 그래프 및 파일로 확인할 수 있는 기능입니다.
- `스케줄링` 공평의 관점에서 트래픽이 몰려 인증 서버 자원을 상대적으로 많이 점유하는 도메인은 후순위에 배치됩니다. 이는 트래픽이 몰리지 않는 유저들의 불편을 방지하기 위함입니다.

## ⚒️ 기술 소개

### 개발환경

| 분류     | 도구 및 버전                                             |
| :------- | :------------------------------------------------------- |
| OS       | - Local : Windows 10 <br> - AWS : Ubuntu 20.04.4 LTS     |
| IDE      | - IntelliJ IDE 2023.3.2 <br> - Visual Studio Code 1.70.0 |
| UI/UX    | - Figma                                                  |
| Database | - PosgreSQL <br> - ElasticSearch <br> - Redis            |
| CI/CD    | - Jenkins                                                |

### 사용 언어 및 라이브러리

| 분류         | 이름 및 버전                                                                                                                                                                                                                                                                      |
| :----------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Frontend** | - thymeleaf <br> - chart.js: 2.9.4 <br> - bootstrap: 4.6.0 <br> - jQuery: 3.6.0 <br> - fontawesome-free: 5.15.3 <br> - jQuery Easing: 1.4.1 <br> - DataTables: 1.10.24                                                                                                            |
| **Backend**  | - JAVA (Zulu 21) <br> - Gradle 8.5 <br> - SpringBoot 3.2.4 <br> - JPA <br> - Lombok 1.18.20 <br> - security6, oauth-client2, jjwt 0.11.5 <br> - JUnit5 <br> - oauth2-authorization-server <br> - oauth2-resource-server <br> - websocket <br> - batch <br> - actuator <br> - mail |

## 🔎 인가 과정

![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/d729e6f0-4733-4ef5-ba8b-b771d61ab096)

## 🔎 인증, 인가 API

![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/a4e06c12-4c70-43e3-aa39-f2b94ff7e668)

## 스케줄링

![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/3b62f563-22ae-4030-8dc5-e55eb580683a)

운영체제에서 사용하는 멀티레벨 피드백 큐에 착안하여 내부적으로 도메인 별 단위 시간 당 로그인 처리 횟수에 반비례하여 우선순위를 부여하였습니다.

<details>
<summary>우선 순위 재계산 코드</summary>

```java
@Scheduled(fixedRate = 5000)
private void schedulePriorityRestoration() {
    LocalDateTime current = LocalDateTime.now();
    AtomicInteger qSize = new AtomicInteger();

    for (ConcurrentLinkedQueue<PriorityQueueNode> queue : procs) {
        Iterator<PriorityQueueNode> iterator = queue.iterator();
        while (iterator.hasNext()) {
            PriorityQueueNode node = iterator.next();
            qSize.addAndGet(node.getRequests().size());
            if (node.getLastAccessTime() != null && node.getLastAccessTime().plusSeconds(10).isBefore(current)) {
                restorePriority(node, iterator); // restorePriority에 iterator를 전달하여 제거 수행
            }
        }
    }
    queueSize = qSize;
}

private void restorePriority(PriorityQueueNode node, Iterator<PriorityQueueNode> iterator) {
    int currentPriority = getCurrentPriority(node.getTeamId());
    int newPriority = Math.max(0, currentPriority / 10);
    teamTpsMap.get(node.getTeamId()).set(newPriority);

    iterator.remove(); // 안전하게 현재 요소를 제거
    procs[newPriority].add(node); // 새 우선순위에 노드를 추가
}
```

</details>
<br><br>

해당 연산은 저희 인증 서비스를 이용하는 다양한 도메인의 공정성을 만족시키기 위해 도입하였으며, JAVA 21부터 적용된 Virtual Thread를 활용한 멀티 스레드 프로그래밍을 통해 개선된 성능을 보입니다.

<details>
<summary>Virtual Thread 적용</summary>

```java
public class AuthorizationApplication implements ApplicationRunner {

  private final VirtualThreadExecutor virtualThreadExecutor;
  private final LoginQueueManager loginQueueManager;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    virtualThreadExecutor.execute(loginQueueManager);
  }
}
```

</details>
<br><br>

또한 동시성 문제를 해결하기 위해 Non-Blocking CAS 연산을 사용하는 Concurrent 컬렉션과 Atomic 자료형을 사용해 임계 영역 동시 접근을 방지하였습니다.

<details>
<summary>Concurrent Collection & Atomic Data Type</summary>

```java
@Component
public class LoginQueueManager implements Runnable {
    private final ConcurrentLinkedQueue<PriorityQueueNode>[] procs;
    private final ConcurrentHashMap<Integer, PriorityQueueNode> teamNodes;
    private ConcurrentHashMap<Integer, AtomicInteger> teamTpsMap;

    private final LoginController loginController;
    @Getter
    private AtomicInteger queueSize;
    private static final int NUM_PRIORITIES = 100;
    ...
}
```

</details>
<br><br>

## 🖥️ 화면 예시

|                                            **사용자 로그**                                             |                                            **로그인 기록**                                             |
| :----------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------: |
| ![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/b73a0a68-2514-4e86-9d76-3ab22115c7d9) | ![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/4b1d241f-4d32-46cb-94aa-d2a539215731) |

## Spring Security .yml 예시

```
spring:
  security:
    oauth2:
      client:
        registration:
          ssafyOAuth:
            provider: ssafyOAuth
            client-id: 55853ea6-cd86-4e36-bbea-e2036954e9c4
            client-secret: 1234
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/ssafyOAuth
            scope: email,image
            client-name: ssafyOAuth


        provider:
          ssafyOAuth:
            authorization-uri: https://ssafyauth-authorization.duckdns.org/oauth2/authorize
            token-uri: https://ssafyauth-authorization.duckdns.org/oauth2/token
            user-info-uri: https://ssafyauth-resource.duckdns.org/user/info
            user-name-attribute: email
```

### 사용 방법

1. Spring Boot OAuth2 Client, Spring Security, Spring Web 의존성 추가

2. application.yml 내 OAuth 2.0 인증 설정 정보 저장

3. SecurityConfig 설정 클래스 내에 로그인 endpoint 지정

4. 인증 후 리디렉션 페이지와 사용자 정보를 처리하는 컨트롤러 작성

5. 로그인 및 홈페이지에 대한 뷰 파일 작성

### 협업 툴

| 분류         | 도구                                      |
| :----------- | :---------------------------------------- |
| 이슈 관리    | - Jira                                    |
| 형상 관리    | - GitLab <br> - Git                       |
| 커뮤니케이션 | - Notion <br> - Mattermost <br> - Discord |

### 아키텍처 다이어그램

![image](https://github.com/louis-cho/SSAFYOAuth/assets/38391852/d1c1f44e-bc09-470f-8a9f-5633e82929dc)

### Git Commit 컨벤션

- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `docs` : 문서 내용 변경
- `style` : 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 등
- `refactor` : 코드 리팩토링
- `test` : 테스트 코드 작성
- `chore` : 빌드 수정, 패키지 매니저 설정, 운영 코드 변경이 없는 경우 등

```
type: subject

ex) 회원가입 기능

- feat: Join in #Jira Issue Number
```

### Git Branch 전략

- `master`
- `develop`
- `feat/function1`
- `feat/function2`

### EC2 포트 정리

_Manager(main) Server_
| Port | |
| ---- | -------------------- |
| 8888 | Jenkins |
| 8080 | Spring boot |
| 80 | nginx HTTP 기본 포트 |
| 443 | nginx HTTPS |
| 5432 | PostgreSQL |
| 6379 | Redis |
| 3000 | ElasticSearch |

_Authorization Server_
| Port | |
| ---- | -------------------- |
| 9000 | Spring boot |
| 80 | nginx HTTP 기본 포트 |
| 443 | nginx HTTPS |

_Resource Server_
| Port | |
| ---- | -------------------- |
| 8090 | Spring boot |
| 80 | nginx HTTP 기본 포트 |
| 443 | nginx HTTPS |
