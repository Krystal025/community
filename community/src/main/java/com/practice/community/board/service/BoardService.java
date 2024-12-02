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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 등록
    public void saveBoard(BoardRequestDto boardRequestDto) {
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 활성화된 사용자인지 이메일로 확인
        User user = userRepository.findByUserEmailAndUserStatus(loggedInEmail, Status.ACTIVE)
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
                .map(board -> BoardResponseDto.builder()
                        .boardId(board.getBoardId())
                        .boardTitle(board.getBoardTitle())
                        .boardContent(board.getBoardContent())
                        .boardCreatedAt(board.getBoardCreatedAt())
                        .boardUpdatedAt(board.getBoardUpdatedAt())
                        .userId(board.getUser().getUserId())
                        .build())
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
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardCreatedAt(board.getBoardCreatedAt())
                .boardUpdatedAt(board.getBoardUpdatedAt())
                .userId(user.getUserId())
                .build();
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
        // 게시글 작성자 이메일
        String boardUserEmail = board.getUser().getUserEmail().replaceAll("\\s+", "").trim();
        // 게시글 수정 권한 확인
        validateEmail(loggedInEmail, boardUserEmail);
        // 변경된 내용이 없는지 확인
        // 제목 또는 내용에 변경이 있는지 확인
        boolean isTitleChanged = boardRequestDto.getBoardTitle() != null &&
                !boardRequestDto.getBoardTitle().equals(board.getBoardTitle());
        boolean isContentChanged = boardRequestDto.getBoardContent() != null &&
                !boardRequestDto.getBoardContent().equals(board.getBoardContent());
        // 만약 제목이나 내용이 변경되지 않았으면 업데이트하지 않음
        if (!isTitleChanged && !isContentChanged) {
            return;  // 변경사항 없으므로 리턴
        }
        Board updatedBoard = board.toBuilder()
                .boardTitle(isTitleChanged ? boardRequestDto.getBoardTitle() : board.getBoardTitle())
                .boardContent(isContentChanged ? boardRequestDto.getBoardContent() : board.getBoardContent())
                .build();
        boardRepository.save(updatedBoard);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId){
        // 인증된 사용자의 권한
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        // 현재 인증된 사용자 이메일
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));
        // 게시글 작성자 이메일
        String boardUserEmail = board.getUser().getUserEmail().replaceAll("\\s+", "").trim();
        // 게시글 수정 권한 확인
        if (!boardUserEmail.equals(loggedInEmail) && !isAdmin) {
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        boardRepository.delete(board);
    }

    // 이메일 비교 메소드 (추후 AOP로 분리)
    private void validateEmail(String loggedInEmail, String requestedEmail) {
        if (!loggedInEmail.equals(requestedEmail)) {
            throw new UnauthorizedAccessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
