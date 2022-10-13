package com.sber.cache.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "info")
@Access(value = AccessType.PROPERTY)
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

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "val")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "change_time")
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
}
