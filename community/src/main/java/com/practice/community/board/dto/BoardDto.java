package com.practice.community.board.dto;

import com.practice.community.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long boardId;
    private Long userId;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;

    public BoardDto(Board board){
        this.boardId = board.getBoardId();
        this.userId = board.getUser().getUserId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardCreatedAt = board.getBoardCreatedAt();
        this.boardUpdatedAt = board.getBoardUpdatedAt();
    }
}
