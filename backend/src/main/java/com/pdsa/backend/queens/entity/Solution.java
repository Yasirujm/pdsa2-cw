package com.pdsa.backend.queens.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "JSON", nullable = false)
    private String placement;          // JSON array as string, e.g. "[0,4,7,5,...]"

    @Column(name = "placement_hash", unique = true, nullable = false, length = 64)
    private String placementHash;

    @Column(name = "is_claimed")
    private Boolean isClaimed = false;

    @Column(name = "claimed_by")
    private String claimedBy;

    @Column(name = "claimed_at")
    private LocalDateTime claimedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}