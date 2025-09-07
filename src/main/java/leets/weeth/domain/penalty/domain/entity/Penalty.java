package leets.weeth.domain.penalty.domain.entity;

import jakarta.persistence.*;
import leets.weeth.domain.penalty.domain.entity.enums.PenaltyType;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Penalty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cardinal_id")
    private Cardinal cardinal;

    @Enumerated(EnumType.STRING)
    private PenaltyType penaltyType;

    private String penaltyDescription;

    public void update(String penaltyDescription) {
        this.penaltyDescription = penaltyDescription;
    }

}
