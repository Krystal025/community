# Project

### Community Backend
- 프로젝트 일정 : 2024/10/31 ~ 
- 개발인원 : BackEnd 1명 
- GitHub Repository : https://github.com/Krystal025/community
</br>

## 프로젝트 개요
---
### 목적
- Spring Security를 활용한 회원 인증 및 권한 관리 학습
- OAuth 2.0 기반의 소셜 로그인 기능 구현
- Docker를 활용한 컨테이너화 및 배포 환경 구성 학습
- RESTful API 설계 및 구현 능력 심화

### 주요 기능
- 회원 인증 및 권한 관리
  - Spring Security와 JWT를 사용한 로그인 및 회원가입
  - 관리자와 일반 사용자의 권한 분리
  - Access Token 및 Refresh Token 발급
- OAuth 2.0 소셜 로그인
  - Google을 사용한 소셜 로그인 구현
- 게시글 CRUD
  - 게시글 작성, 수정, 삭제, 조회 기능 제공
- Docker 활용
  - Dockerfile과 Docker Compose를 사용한 컨테이너 기반 서버 환경 구축
</br>

## 기술 스택
---
### 1. Programming Languages
<div style="display : flex">
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"/>
</div>

### 2. Frameworks / Libraries
<div style="display : flex">
    <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
</div>

### 3. Database
<div style="display : flex">
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
</div>

### 4. Build & Deployment
<div style="display : flex">
    <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 
    <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>
</div>

### 5. Tools
<div style="display : flex">
    <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"/> 
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"/> 
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=gitHub&logoColor=white"/> 
</div>
</br>

### Java 17
- 주 프로그래밍 언어로 선택
- Spring Boot 3.0 이상과의 호환성을 위해 사용
- 안정성과 성능을 제공하며, 현재 많은 프로젝트에서 사용하는 버전

### Spring Boot
- 백엔드 프레임워크로 활용
- 다양한 스타터 패키지와 내장 서버로 인해 초기 설정이 간단하고, 작은 규모의 개인 프로젝트에 적합
- Dependency Injection 및 IoC 컨테이너를 통해 코드의 유지보수가 용이

### MySQL
- 데이터베이스로 사용
- 현업에서 널리 사용되고, Oracle 및 MariaDB를 사용한 경험을 바탕으로 학습용으로 선택
- 데이터 저장, CRUD API 설계 및 테스트에 활용

### JPA
- SQL문 없이 데이터 접근 계층을 구현하기 위해 선택
- 특정 DB에 종속되지 않아 유지보수에 유리하며, 간단한 CRUD 중심의 프로젝트에 적합

### Gradle
- 빌드 도구로 사용
- 빠른 빌드 속도와 유연한 설정이 가능하여 생산성 향상

### JWT (JSON Web Token)
- 회원 인증 및 권한 부여를 위한 Access Token과 Refresh Token 발급
- 토큰 기반 인증으로 API의 보안성을 강화

### OAuth 2.0 (Google OAuth)
- Google OAuth를 통한 소셜 로그인 기능 구현
- 브라우저에서 로그인 요청 후 토큰 발급 및 리디렉션 동작 확인

### Git, GitHub
- 소스코드 버전 관리에 Git 사용
- 프로젝트 기록 및 백업을 위해 GitHub 활용
</br>

## 테스트
---
### API 호출 테스트 (Postman)
- 회원가입 및 로그인 : Postman으로 JWT 및 Refresh Token 발급 확인
- CRUD 기능 : 게시글 생성, 수정, 삭제, 조회 테스트

### 소셜 로그인 테스트 (Google OAuth)
- 브라우저에서 http://localhost:8080/login 접속 후 Google 로그인 실행
- 로그인 성공 시 토큰 발급 및 리디렉션 확인
</br>




