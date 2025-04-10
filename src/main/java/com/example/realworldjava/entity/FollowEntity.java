package com.example.realworldjava.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "user_follows", uniqueConstraints = {
        @UniqueConstraint(name = "unique_follow_pair", columnNames = {
                "following", "followedBy"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "following")
    private UserEntity following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "followedBy")
    private UserEntity followedBy;

    public FollowEntity(UserEntity following, UserEntity followed) {
        this.following = following;
        this.followedBy = followed;
    }
}
