-- 회원정보 테이블 --
CREATE TABLE user(
	user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID',
	user_name VARCHAR(50) NOT NULL COMMENT '회원 이름',
	user_email VARCHAR(100) NOT NULL UNIQUE COMMENT '회원 이메일',
	user_pwd VARCHAR(128) COMMENT '회원 비밀번호',
	user_nickname VARCHAR(50) NOT NULL UNIQUE COMMENT '회원 닉네임',
	user_gender ENUM('MALE', 'FEMALE') DEFAULT 'MALE' COMMENT '회원 성별',
	user_birthday DATE COMMENT '회원 생년월일',
	user_status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '회원 상태',
	user_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '회원 가입일',
	user_role ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER' COMMENT '회원 역할(권한)',
	socialUserId VARCHAR(100) UNIQUE COMMENT '회원 소셜 ID'
)COMMENT '회원' ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 게시글 테이블 --
CREATE TABLE board (
    board_id BIGINT(20) AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    user_id BIGINT(20) COMMENT '작성자 ID',
    board_title VARCHAR(100) NOT NULL COMMENT '게시글 제목',
    board_content MEDIUMTEXT NOT NULL COMMENT '게시글 내용',
    board_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '게시글 작성일',
    board_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '게시글 수정일'
) COMMENT '게시판' ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 게시글 테이블 FK 설정 --
ALTER TABLE board
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;
