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
    private Double lat;          // 위도 (지도 중심/마커용)
    private Double lng;          // 경도
    private String imageSource;  // 사진 출처 (저작자/출처 표기)
    private String originalFilename;
    private String storedFilename;
    private String imageUrl; // 화면 표시용 R2 공개 URL (storedFilename으로부터 생성)
    private Integer recommendCnt;
}
