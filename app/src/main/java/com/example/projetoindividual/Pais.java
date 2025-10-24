package com.example.projetoindividual;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Pais implements Serializable {

    @SerializedName("name")
    private Name name;

    @SerializedName("flags")
    private Flags flags;

    @SerializedName("capital")
    private List<String> capital;

    @SerializedName("languages")
    private Map<String, String> languages;

    @SerializedName("region")
    private String region;

    @SerializedName("area")
    private double area;

    @SerializedName("population")
    private long population;

    public Name getName() {
        return name;
    }

    public Flags getFlags() {
        return flags;
    }

    public List<String> getCapital() {
        return capital;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public String getRegion() {
        return region;
    }

    public double getArea() {
        return area;
    }

    public long getPopulation() {
        return population;
    }

    public static class Name implements Serializable {
        @SerializedName("common")
        private String common;

        public String getCommon() {
            return common;
        }
    }

    public static class Flags implements Serializable {
        @SerializedName("png")
        private String png;

        public String getPng() {
            return png;
        }
    }
}