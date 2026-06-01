package com.naranghiking.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest {
    private String userId;
    private String pass;
    private String name;
}
