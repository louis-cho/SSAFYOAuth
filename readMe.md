# 🏴‍☠️ SSAFYAuth
## ✨서비스 설명

![main](./README_IMAGE/main.png)

### 개요 🧾

- **SSAFY 교육생들을 위한 OAuth2.0 인증, 인가, 모니터링 시스템**!
- 서비스 명 : **SSAFYAuth**

### 주요 기능 💡
- `인증, 인가` OAuth2.0 표준 스펙에 맞췄으니, 우리 서비스로 쉽게 인증, 인가를 구현해보자!
- `모니터링` 우리 서비스엔 어느 시간대에 유저가 많이 들어오나 확인해보자!

## 🔎 서비스 소개

![rule](./README_IMAGE/rule.png)
![page](./README_IMAGE/page.png)
![store](./README_IMAGE/store.png)

## 🖥️ 화면 예시
|렌더링|상점|
|:--:|:--:|
|![rendering](./README_IMAGE/rendering.gif)|![storegif](./README_IMAGE/storegif.gif)|
|**마이페이지**|**사용자게임**|
|![mypage](./README_IMAGE/mypage.gif)|![room](./README_IMAGE/room.gif)|
|**인게임(시작 위치)**|**인게임(이동)**|
|![start](./README_IMAGE/start.gif)|![move](./README_IMAGE/move.gif)|
|**인게임(조사)**|**인게임(체포)**|
|![investigate](./README_IMAGE/investigate.gif)|![arrest](./README_IMAGE/arrestgif.gif)|



## ⚒️ 기술 소개
### 개발환경
|분류|도구 및 버전|
|:---|:---|
|OS|- Local : Windows 10 <br> - AWS : Ubuntu 20.04.4 LTS|
|IDE|- IntelliJ IDE 2023.3.2 <br> - Visual Studio Code 1.70.0|
|UI/UX|- Figma|
|Database|- PosgreSQL <br> - ElasticSearch <br> - Redis
|CI/CD|- Jenkins|

### 사용 언어 및 라이브러리
|분류|이름 및 버전|
|:---|:---|
|**Frontend**|- npm: >=9 <br> - node: >=18 <br> - TypeScript: >=5 <br> - React: >=18 <br> - Next.js: >=14 <br> - Three.js: 0.162.0 <br> - @stomp/stompjs: 7.0.0 <br> - Zustand: 4.5.2 <br> - sass: 1.71.1 <br> - @emotion/react: 11.11.4, @emotion/styled: 11.11.0 <br> - prettier: 3.2.5 <br> - eslint: >=8 <br> - jest: >=29 <br> - leva: 0.9.35|
|**Backend**|- JAVA (Zulu 21) <br> - Gradle 8.5 <br> - SpringBoot 3.2.1 <br> - JPA <br> - Lombok 1.18.20 <br> - security6, oauth-client2, jjwt 0.11.5 <br> - JUnit5 <br> - Stomp 2.3.4|



### 협업 툴
|분류|도구|
|:---|:---|
|이슈 관리|- Jira|
|형상 관리|- GitLab <br> - Git|
|커뮤니케이션|- Notion <br> - Mattermost <br> - Discord|

### 아키텍처 다이어그램
![image.png](./README_IMAGE/architecture.png)

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

### ERD
![erd.png](./README_IMAGE/erdiagram.png)

### EC2 포트 정리
*Manager(main) Server*
| Port |                      |
| ---- | -------------------- |
| 8888 | Jenkins      |
| 8080 | Spring boot      |
| 80   | nginx HTTP 기본 포트 |
| 443  | nginx HTTPS          |
| 5432 | PostgreSQL            |
| 6379 | Redis            |
| 3000 | ElasticSearch            |

*Authorization Server*
| Port |                      |
| ---- | -------------------- |
| 9000 | Spring boot      |
| 80   | nginx HTTP 기본 포트 |
| 443  | nginx HTTPS          |

*Resource Server*
| Port |                      |
| ---- | -------------------- |
| 8090 | Spring boot      |
| 80   | nginx HTTP 기본 포트 |
| 443  | nginx HTTPS          |




## 팀원
|**김다나**|**김시은**|**서재화**|**이동재**|**이주연**|**조현우**|
|:--:|:--:|:--:|:--:|:--:|:--:|
|Fullstack|Fullstack|Fullstack|Fullstack|인프라|Fullstack <br> 팀장|
