# Spring Boot JWT 프로젝트

Spring Boot와 JWT를 이용한 인증/인가 구현 프로젝트입니다.

## 기술 스택

* Java 17
* Spring Boot 3.4.3
* Spring Security 6.x.x
* Spring Data JPA
* MySQL
* Lombok
* Gradle (Groovy)

## 프로젝트 구성

* JWT 기반 인증
* Spring Security를 이용한 보안 구현
* JPA를 이용한 데이터 영속성 관리
* MySQL 데이터베이스 연동

## 개발 환경 설정

### 요구사항

* JDK 17+
* MySQL 8.0+
* Gradle 8.x+

### 빌드 및 실행

1. 프로젝트 다운로드

```bash
git clone https://github.com/icksss/security-jwt.git
```

2. 의존성 추가

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'mysql:mysql-connector-java'
    implementation 'io.jsonwebtoken:jjwt:0.9.1'
}
```

```
VSCODE 핫 디플로이 설정
1. setting 검색에서 -> Hot Code Replace 검색
2. 핫 디플로이 활성화 , auto 로 설정
![image](https://velog.velcdn.com/images%2Fnonz%2Fpost%2F0d534a27-d59e-4143-a448-bddb88228f06%2F2021-05-06_09-51-25.png)
3. 스프링대시보드에서 debug 모드로 실행
4. 코드 수정 후 저장 시 자동으로 반영됨











```

## 라이센스

This project is licensed under the MIT License - see the LICENSE file for details
