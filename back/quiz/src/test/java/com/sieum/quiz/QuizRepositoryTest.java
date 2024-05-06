package com.sieum.quiz;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sieum.quiz.domain.Quiz;
import com.sieum.quiz.domain.enums.QuizType;
import com.sieum.quiz.repository.QuizRepository;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("quiz repository test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuizRepositoryTest {

    @Autowired QuizRepository quizRepository;

    @Transactional
    @Test
    public void save_test() {
        // given
        Map<Integer, String> choice =
                Map.of(
                        1, "first choice",
                        2, "second choice",
                        3, "third choice",
                        4, "fourth choice");

        Quiz quiz =
                Quiz.builder()
                        .question("test_question")
                        .answer("test_answer")
                        .quizType("MULTIPLE")
                        .choice(choice)
                        .build();

        // when
        Quiz savedQuiz = quizRepository.save(quiz);

        // then
        assertThat(savedQuiz.getChoice().get(1)).isEqualTo("first choice");
        assertThat(QuizType.valueOf(savedQuiz.getQuizType()).getValue()).isEqualTo("객관식");

    }
}
