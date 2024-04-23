package com.sieum.music.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThrowHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "throw_history_id")
    private Long id;

    @NotNull private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "throw_id")
    @NotNull
    private Throw throwItem;
}
