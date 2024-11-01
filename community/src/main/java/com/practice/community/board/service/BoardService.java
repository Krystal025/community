package com.practice.community.board.service;

import com.practice.community.board.dto.BoardDto;
import com.practice.community.board.entity.Board;
import com.practice.community.board.repository.BoardRepository;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public void saveBoard(BoardDto boardDto) {
        User user = userRepository.findByUserIdAndUserStatus(boardDto.getUserId(), "ACTIVE")
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Board board = new Board(
                user,
                boardDto.getBoardTitle(),
                boardDto.getBoardContent()
        );
        boardRepository.save(board);
    }

    // 게시글 조회
    public List<BoardDto> getList() {
        return boardRepository.findAll()
                    .stream()
                    .map(BoardDto::new)
                    .collect(Collectors.toList());
    }
}
