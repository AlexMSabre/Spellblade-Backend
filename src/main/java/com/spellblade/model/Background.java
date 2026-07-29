package com.spellblade.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("BACKGROUND")
@Data
public class Background {

    @Id private String id;
    private String name;
    private String source;
    private String parentTrait;
    private String childTrait;
    private String deity;
    private String description;

    public Background(){}
}