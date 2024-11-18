package com.practice.community.board.dto;

import com.practice.community.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private Long userId;

    @NotBlank(message = "Title must not be blank", groups = ValidationGroups.Create.class)
    @Size(min = 1, message = "Title must not be blank", groups = ValidationGroups.Update.class) // 빈문자열을 허용하지 않음
    private String boardTitle;

    @NotBlank(message = "Content must not be blank", groups = ValidationGroups.Create.class)
    @Size(min = 1, message = "Content must not be blank", groups = ValidationGroups.Update.class)
    private String boardContent;

}
