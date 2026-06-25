package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Attribute;
import com.spellblade.model.Talent;

import lombok.Data;

@Data
public class TalentDAO {

    private Talent talent;
    private List<Attribute> attributes;

    public TalentDAO() {}

    public TalentDAO(Talent talent, List<Attribute> attribute){
        this.talent = talent;
        this.attributes = attribute;
    }

}