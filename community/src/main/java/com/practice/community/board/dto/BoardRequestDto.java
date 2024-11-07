package com.practice.community.board.dto;

import com.practice.community.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private Long userId;

    @NotBlank(message = "Title must not be blank")
    private String boardTitle;

    @NotBlank(message = "Content must not be blank")
    private String boardContent;

}
