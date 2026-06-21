package com.naranghiking.mtn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mtn {
    private Long id;
    private String name;
    private String location;
    private Integer height;
    private String description;
    private String originalFilename;
    private String storedFilename;
    private String imageUrl; // 화면 표시용 R2 공개 URL (storedFilename으로부터 생성)
}
