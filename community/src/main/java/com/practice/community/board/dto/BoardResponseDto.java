package com.practice.community.board.dto;

import com.practice.community.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponseDto {

    private Long boardId;
    private Long userId;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;

    public BoardResponseDto(Board board){
        this.boardId = board.getBoardId();
        this.userId = board.getUser().getUserId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardCreatedAt = board.getBoardCreatedAt();
        this.boardUpdatedAt = board.getBoardUpdatedAt();
    }

}
