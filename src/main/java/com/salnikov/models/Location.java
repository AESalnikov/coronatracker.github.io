package com.salnikov.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {
    String state;
    String country;
    long latestTotalCases;
    int delta;

    @Override
    public String toString() {
        return "Location{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                ", delta=" + delta +
                '}';
    }
}
