package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Aspect;
import com.spellblade.model.Item;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class TalentDAO {

    private Talent talent;
    private List<Aspect> aspects;

    public TalentDAO() {}

    public TalentDAO(Talent talent, List<Aspect> aspects){
        this.talent = talent;
        this.aspects = aspects;
    }

}