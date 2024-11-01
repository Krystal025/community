package com.practice.community.board.repository;

import com.practice.community.board.entity.Board;
import com.practice.community.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUserIn(List<User> users);

}
