package com.spellblade.model.screens;

import java.util.List;

import com.spellblade.model.Effect;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class TalentScreen {

    private List<Talent> talents;
    private List<Effect> effects;

    public TalentScreen() {}

    public TalentScreen(List<Talent> talents, List<Effect> effects){
        this.talents = talents;
        this.effects = effects;
    }

}