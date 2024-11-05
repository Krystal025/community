package com.practice.community.board.service;

import com.practice.community.board.dto.BoardDto;
import com.practice.community.board.dto.BoardRequestDto;
import com.practice.community.board.dto.BoardResponseDto;
import com.practice.community.board.entity.Board;
import com.practice.community.board.repository.BoardRepository;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Status;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public void saveBoard(Long userId, BoardRequestDto boardRequestDto) {
        User user = userRepository.findByUserIdAndUserStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        Board board = Board.builder()
                        .user(user)
                        .boardTitle(boardRequestDto.getBoardTitle())
                        .boardContent(boardRequestDto.getBoardContent())
                        .build();
        boardRepository.save(board);
    }

    // 게시글 조회
    public List<BoardResponseDto> getList() {
        // ACTIVE 상태인 사용자 리스트
        List<User> activeUsers = userRepository.findByUserStatus(Status.ACTIVE);
        // 해당 사용자들의 게시글 리스트
        List<Board> boards = boardRepository.findByUserIn(activeUsers);
        // 게시글을 DTO로 변환하여 반환
        return boards
                .stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    // 게시글 상세내용 조회
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));
        User user = board.getUser();
        if(user.getUserStatus() == Status.INACTIVE){
            throw new RuntimeException("Deactivated User");
        }
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, Long userId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));
        if(!board.getUser().getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized User");
        }
        Board updatedBoard = board.toBuilder()
                .boardTitle(boardRequestDto.getBoardTitle())
                .boardContent(boardRequestDto.getBoardContent())
                .build();
        boardRepository.save(updatedBoard);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId, Long userId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));
        if(!board.getUser().getUserId().equals(userId)){
            throw new RuntimeException("Unauthorized User");
        }
        boardRepository.delete(board);
    }

}
