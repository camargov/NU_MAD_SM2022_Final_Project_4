package com.example.nu_mad_sm2022_final_project_4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorPalette implements Serializable, Cloneable {
    private String name;
    private List<Integer> colors;
    private String userId;
    private boolean cloudPalette;

    public ColorPalette() {}

    public ColorPalette(String name, String userId, boolean cloudPalette, List<Integer> colors) {
        this.name = name;
        this.userId = userId;
        this.colors = new ArrayList<>(colors);
        this.cloudPalette = cloudPalette;
    }

    public ColorPalette(String name, List<Integer> colors) {
        this(name, "", false, colors);
    }

    public ColorPalette(String name, String userId, List<Integer> colors) {
        this(name, userId, false, colors);
    }

    public ColorPalette(String name) {
        this(name, new ArrayList<>());
    }

    public String getName() {
        return this.name;
    }

    public List<Integer> getColors() {
        return new ArrayList<>(this.colors);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getCloudPalette() {
        return cloudPalette;
    }

    public void setCloudPalette(boolean cloudPalette) {
        this.cloudPalette = cloudPalette;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode()
                + this.colors.hashCode()
                + this.userId.hashCode()
                + (Boolean.valueOf(this.cloudPalette)).hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ColorPalette) {
            ColorPalette other = (ColorPalette) obj;
            return Objects.equals(other.getName(), this.name)
                    && other.getUserId().equals(this.userId)
                    && other.getCloudPalette() == this.cloudPalette
                    && colorListsEqual(other.getColors());
        } else {
            return false;
        }
    }

    private boolean colorListsEqual(List<Integer> other) {
        if (other == null) {
            return false;
        }
        if (other.size() != this.colors.size()) {
            return false;
        }
        for(int i = 0; i < this.colors.size(); i++) {
            if (this.colors.get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }
}
