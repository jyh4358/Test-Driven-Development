package tdd.membership.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String membershipName;

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;

    @Builder
    public Membership(
            String membershipName,
            String userId,
            Integer point,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.membershipName = membershipName;
        this.userId = userId;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
