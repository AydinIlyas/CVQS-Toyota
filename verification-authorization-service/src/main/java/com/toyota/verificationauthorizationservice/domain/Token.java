package com.toyota.verificationauthorizationservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    private String tokenId;
    private boolean revoked;
    private boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
