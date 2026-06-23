package com.naranghiking.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest {
    private Long userId;
    private String email;
    private String currentPass; // 본인 확인용 현재 비밀번호 (수정 전 검증)
    private String pass;        // 새 비밀번호
    private String name;
}
