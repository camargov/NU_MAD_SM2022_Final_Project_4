package com.example.nu_mad_sm2022_final_project_4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ColorPalette implements Serializable {
    private String name;
    private List<Integer> colors;

    public ColorPalette() {}

    public ColorPalette(String name, List<Integer> colors) {
        this.name = name;
        this.colors = new ArrayList<>(colors);
    }

    public ColorPalette(String name) {
        this(name, new ArrayList<>());
    }

    public String GetName() {
        return this.name;
    }

    public List<Integer> GetColors() {
        return new ArrayList<>(this.colors);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }
}
