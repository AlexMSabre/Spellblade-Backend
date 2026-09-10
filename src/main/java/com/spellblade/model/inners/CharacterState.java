package com.spellblade.model.inners;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Effect;

import lombok.Data;

//the model for accessing Characters in the DB
//it is called CharacterObject because Character is already a java type and it helps to be specific
//@ Document Defines this POJO as belonging to the Character repository
//@ Data causes the POJO methods to auto-generate

@Data
public class CharacterState {

    private int hitPoints;
    private int armor;
    private int manaPoints;
    private int wounds;
    private List<Effect> inactiveEffects;
    private List<Effect> activeEffects;

    public CharacterState(){
        activeEffects = new ArrayList<>();
        inactiveEffects = new ArrayList<>();
    }

}