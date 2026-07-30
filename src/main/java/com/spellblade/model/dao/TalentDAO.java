package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Effect;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class TalentDAO {

    private List<Talent> talents;
    private List<Effect> effects;

    public TalentDAO() {}

    public TalentDAO(List<Talent> talents, List<Effect> effects){
        this.talents = talents;
        this.effects = effects;
    }

}