package com.practice.community.board.service;

import com.practice.community.board.dto.BoardDto;
import com.practice.community.board.entity.Board;
import com.practice.community.board.repository.BoardRepository;
import com.practice.community.user.entity.User;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public void saveBoard(BoardDto boardDto) {
        User user = userRepository.findById(boardDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Board board = new Board(
                user,
                boardDto.getBoardTitle(),
                boardDto.getBoardContent()
        );
        boardRepository.save(board);
    }
}
