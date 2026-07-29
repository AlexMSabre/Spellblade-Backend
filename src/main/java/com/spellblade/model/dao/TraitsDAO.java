package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Ancestry;
import com.spellblade.model.Background;
import com.spellblade.model.Trait;

import lombok.Data;

@Data
public class TraitsDAO {

    private Ancestry ancestry;
    private Background background;
    private List<Trait> traits;

    public TraitsDAO(){}

    public TraitsDAO(Ancestry ancestry, Background background, List<Trait> traits){
        this.ancestry = ancestry;
        this.background = background;
        this.traits = traits;
    }

}