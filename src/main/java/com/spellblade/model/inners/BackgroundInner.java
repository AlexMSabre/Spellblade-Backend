package com.spellblade.model.inners;

import com.spellblade.model.Background;
import com.spellblade.model.Trait;

import lombok.Data;

@Data
public class BackgroundInner {
    private String name;
    private String source;
    private Trait parentTrait;
    private Trait childTrait;
    private String deity;
    private String description;

    public BackgroundInner (){}

    public BackgroundInner (Background background){
        this.name = background.getName();
        this.source = background.getSource();
        this.deity = background.getDeity();
        this.description = background.getDescription();
    }

    public BackgroundInner (Background background, Trait parent, Trait child){
        this.name = background.getName();
        this.source = background.getSource();
        this.deity = background.getDeity();
        this.description = background.getDescription();
        this.parentTrait = parent;
        this.childTrait = child;
    }
}