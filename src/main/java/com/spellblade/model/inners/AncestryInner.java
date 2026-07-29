package com.spellblade.model.inners;

import com.spellblade.model.Ancestry;
import com.spellblade.model.Trait;

import lombok.Data;

@Data
public class AncestryInner {
    private String name;
    private String parent;
    private String source;
    private Trait trait1;
    private Trait trait2;
    private String size;
    private String description;

    public AncestryInner(Ancestry ancestry){
        this.name = ancestry.getName();
        this.parent = ancestry.getParent();
        this.source = ancestry.getSource();
        this.size = ancestry.getSize();
        this.description = ancestry.getDescription();
    }

    public AncestryInner(Ancestry ancestry, Trait trait1, Trait trait2){
        this.name = ancestry.getName();
        this.parent = ancestry.getParent();
        this.source = ancestry.getSource();
        this.size = ancestry.getSize();
        this.description = ancestry.getDescription();
        this.trait1 = trait1;
        this.trait2 = trait2;
    }
}