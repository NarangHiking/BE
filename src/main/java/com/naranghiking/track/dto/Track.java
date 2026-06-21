package com.naranghiking.track.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track {
    private Long id;
    private Long mountainId;
    private String name;
    private String gpxFilePath;
    private Integer recommendCnt;
    private String gpxUrl; // GPX 다운로드용 R2 공개 URL (gpxFilePath로부터 생성)
}
