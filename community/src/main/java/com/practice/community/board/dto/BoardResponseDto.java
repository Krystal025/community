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

}
