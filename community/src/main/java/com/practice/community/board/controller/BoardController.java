package com.practice.community.board.controller;

import com.practice.community.board.dto.BoardDto;
import com.practice.community.board.service.BoardService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> saveBoard(@Valid @RequestBody BoardDto boardDto){
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
    @PutMapping("/update/{boardId}/{userId}")
    public ResponseEntity<String> updatePost(@PathVariable("boardId") Long boardId,
                                             @PathVariable("userId") Long userId,
                                             @Valid @RequestBody BoardDto boardDto) {
        boardService.updateBoard(boardId, userId, boardDto);
        return ResponseEntity.ok("Post Updated");
    }

    @DeleteMapping("/delete/{boardId}/{userId}")
    public ResponseEntity<String> deletePost(@PathVariable("boardId") Long boardId,
                                             @PathVariable("userId") Long userId){
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok("Post Deleted");
    }
}
