package com.practice.community.board.service;

import com.practice.community.board.dto.BoardRequestDto;
import com.practice.community.board.dto.BoardResponseDto;
import com.practice.community.board.entity.Board;
import com.practice.community.board.repository.BoardRepository;
import com.practice.community.exception.ErrorCode;
import com.practice.community.exception.custom.RequiredFieldMissingException;
import com.practice.community.exception.custom.PostNotFoundException;
import com.practice.community.exception.custom.UnauthorizedAccessException;
import com.practice.community.exception.custom.UserNotFoundException;
import com.practice.community.user.entity.User;
import com.practice.community.user.enums.Status;
import com.practice.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public void saveBoard(Long userId, BoardRequestDto boardRequestDto) {
        User user = userRepository.findByUserIdAndUserStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
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
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
        User user = board.getUser();
        // 상태가 ACTIVE인 사용자의 게시글만 조회 가능
        if(user.getUserStatus() == Status.INACTIVE){
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, Long userId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
        // 게시글 수정 권한 확인
        if(!board.getUser().getUserId().equals(userId)){
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        // 제목 또는 내용에 빈 값이 없는지 확인
        if (boardRequestDto.getBoardTitle() == null || boardRequestDto.getBoardTitle().isEmpty() ||
                boardRequestDto.getBoardContent() == null || boardRequestDto.getBoardContent().isEmpty()) {
            throw new RequiredFieldMissingException(ErrorCode.REQUIRED_FIELD_MISSING);
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
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
        if(!board.getUser().getUserId().equals(userId)){
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        boardRepository.delete(board);
    }

}
