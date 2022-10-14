package com.sber.proxy.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Info implements Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 6207766401415563566L;

    @NotNull
    @Min(value = 0)
    private Long id;

    private String value;

    private LocalDateTime changeTime;

    public Info() {}

    public Info(Long id, String value, LocalDateTime changeTime) {
        this.id = id;
        this.value = value;
        this.changeTime = changeTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info)) return false;
        Info info = (Info) o;
        return Objects.equals(id, info.id) && Objects.equals(value, info.value) && Objects.equals(changeTime, info.changeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, changeTime);
    }
}
