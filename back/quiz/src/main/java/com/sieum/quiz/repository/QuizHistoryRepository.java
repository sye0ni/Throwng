package com.sieum.quiz.repository;

import com.sieum.quiz.domain.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {}
