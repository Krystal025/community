package com.practice.community.board.controller;

import com.practice.community.board.dto.BoardDto;
import com.practice.community.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<String> saveBoard(@RequestBody BoardDto boardDto){
        boardService.saveBoard(boardDto);
        return ResponseEntity.ok("Post saved");
    }

}
