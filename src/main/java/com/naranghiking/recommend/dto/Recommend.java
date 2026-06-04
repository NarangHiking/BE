package com.naranghiking.recommend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Recommend {
    private Long userId;
    private Long trackId;
    private LocalDateTime createdAt;
}
