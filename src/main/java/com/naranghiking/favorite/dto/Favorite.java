package com.naranghiking.favorite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Favorite {
    private Long userId;
    private Long trackId;
    private LocalDateTime createdAt;
}
