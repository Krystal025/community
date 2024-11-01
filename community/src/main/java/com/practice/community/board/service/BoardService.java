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
        // ACTIVE 상태인 사용자 리스트
        List<User> activeUsers = userRepository.findByUserStatus(User.Status.ACTIVE);
        // 해당 사용자들의 게시글 리스트
        List<Board> boards = boardRepository.findByUserIn(activeUsers);
        // 게시글을 DTO로 변환하여 반환
        return boards
                .stream()
                .map(BoardDto::new)
                .toList();
    }

    // 게시글 상세내용 조회
    public BoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));
        User user = board.getUser();
        if(user.getUserStatus() == User.Status.INACTIVE){
            throw new RuntimeException("Deactivated User");
        }
        return new BoardDto(board);
    }
}
