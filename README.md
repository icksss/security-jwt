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

## 추가적인 구정 IP 기반 제어
* 요청 IP 확인 : PC 기반
* PC의 경우 IP 주소가 변경될 일이 거의 없습니다. IP 주소가 변경되는 경우 요청이 거부되도록 진행할 수 있습니다.

## 로직 구상
* 로그인시 JWT 발급과 함께 JWT와 IP를 DB 테이블에 저장
* Access 토큰으로 요청시 요청 IP와 로그인시 저장한 IP 주소를 대조
* Access 토큰 재발급시 새로운 Access 토큰과 IP를 DB 테이블에 저장


## 네이버의 경우
* 네이버도 PC(노트북) 환경에서 로그인을 진행 후 다른 IP 주소로 변경되면 재 로그인을 진행하라는 알림이 발생합니다.

## 라이센스

This project is licensed under the MIT License - see the LICENSE file for details
