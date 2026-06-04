package com.naranghiking.track.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track {
    private Integer id;
    private Integer mountainId;
    private String name;
    private String gpxFilePath;
    private Integer recommendCnt;
}
