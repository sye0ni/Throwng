package com.sieum.quiz.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "quiz_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuizHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_history_id")
    private Long id;

    @Column(length = 200)
    @NotNull
    private String submit;

    @NotNull private boolean result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    @NotNull
    private Quiz quiz;

    @NotNull private Long userId;
}
