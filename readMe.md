# 🏴‍☠️ SSAFYAuth
## ✨서비스 설명

![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/11c711d9-d1c9-4e92-bdab-a71e9a9689bb)


### 개요 🧾

- **SSAFY 교육생들을 위한 OAuth2.0 인증, 인가, 모니터링 시스템**!
- 서비스 명 : **SSAFYAuth**

### 주요 기능 💡
- `인증, 인가` OAuth2.0 표준 스펙에 맞췄으니, 싸피 교육생들은 별도의 회원 가입 없이 우리 서비스로 쉽게 인증, 인가를 구현해보자!
- `모니터링` 우리 서비스엔 어떤 유저가 있는 확인해보자!

## 🔎 인가 과정
![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/19c8a232-7057-44a2-b8bf-514205ebb3f5)

## 🔎 인증, 인가 API
![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/b9de39ba-7cdf-404c-9405-25b093b24818)


## 🖥️ 화면 예시
|**사용자 로그**|**로그인 기록**|
|:--:|:--:|
|![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/6475c664-3537-4d1a-a892-6367a83c02f1)|![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/08c8a9b0-41e7-4dff-ba68-092c051871f4)|

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
|**Frontend**|- thymeleaf <br> - chart.js: 2.9.4 <br> - bootstrap: 4.6.0 <br> - jQuery: 3.6.0 <br> - fontawesome-free: 5.15.3 <br> - jQuery Easing: 1.4.1 <br> - DataTables: 1.10.24|
|**Backend**|- JAVA (Zulu 21) <br> - Gradle 8.5 <br> - SpringBoot 3.2.4 <br> - JPA <br> - Lombok 1.18.20 <br> - security6, oauth-client2, jjwt 0.11.5 <br> - JUnit5 <br> - oauth2-authorization-server <br> - oauth2-resource-server <br> - websocket <br> - batch <br> - actuator <br> - mail|



### 협업 툴
|분류|도구|
|:---|:---|
|이슈 관리|- Jira|
|형상 관리|- GitLab <br> - Git|
|커뮤니케이션|- Notion <br> - Mattermost <br> - Discord|

### 아키텍처 다이어그램
![image](https://github.com/Juyeori/SSAFYOAuth/assets/98978787/e77faef0-0653-44e3-a864-f2d0fa5bb802)


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
|Fullstack|Fullstack|Fullstack|Fullstack|Infra|Fullstack <br> 팀장|
