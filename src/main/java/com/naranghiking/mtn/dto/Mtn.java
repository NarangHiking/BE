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
}
