package com.naranghiking.suntimes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SuntimeResponse(Response response) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(Body body) {};

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Body(Items items) {};

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Items(Item item) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(String sunrise, String sunset) {};


    public String sunrise() {
        return response.body().items().item().sunrise().trim();
    }

    public String sunset() {
        return response.body().items().item().sunset().trim();
    }
}
