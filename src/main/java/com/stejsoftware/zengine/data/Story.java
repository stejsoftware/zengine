package com.stejsoftware.zengine.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class Story {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Byte[] data;
}



