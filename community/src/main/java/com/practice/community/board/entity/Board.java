package com.practice.community.board.entity;

import com.practice.community.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩으로 데이터가 필요할 때까지 로딩하지 않음
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String boardTitle;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String boardContent;

    @Column(updatable = false)
    private LocalDateTime boardCreatedAt;

    @Column
    private LocalDateTime boardUpdatedAt;

//    @Column(updatable = false)
//    private Long boardCreatedBy;

//    @Column(updatable = false)
//    private Long boardUpdatedBy;

    @PrePersist // 처음 생성되는 엔티티가 DB에 저장되기 전에 호출됨
    private void onCreate() {
        this.boardCreatedAt = LocalDateTime.now();
        this.boardUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate // 이미 존재하는 엔티티가 수정되어 DB에 반영되기 전에 호출됨
    private void onUpdate() {
        this.boardUpdatedAt = LocalDateTime.now();
    }

//    public Board(User user, String boardTitle, String boardContent){
//        this.user = user;
//        this.boardTitle = boardTitle;
//        this.boardContent = boardContent;
//    }
//
//    public void updateBoard(String boardTitle, String boardContent){
//        this.boardTitle = boardTitle;
//        this.boardContent = boardContent;
//    }

}
