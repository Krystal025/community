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

    // 게시글 등록 API
    @PostMapping
    public ResponseEntity<String> saveBoard(@RequestBody BoardDto boardDto){
        boardService.saveBoard(boardDto);
        return ResponseEntity.ok("Post saved");
    }

    // 게시글 목록 조회 API
    @GetMapping("/list")
    public List<BoardDto> getBoardList(){
        return boardService.getList();
    }

    // 게시글 상세내용 조회 API
    @GetMapping("{boardId}")
    public BoardDto getBoardInfo(@PathVariable("boardId") Long boardId){
        return boardService.getBoard(boardId);
    }

    // 게시글 수정 API
    @PutMapping("/update/{userId}/{boardId}")
    public ResponseEntity<String> updatePost(@PathVariable("userId") Long userId,
                                             @PathVariable("boardId") Long boardId,
                                             @RequestBody BoardDto boardDto) {
        boardService.updateBoard(userId, boardId, boardDto);
        return ResponseEntity.ok("Post Updated");
    }

}
