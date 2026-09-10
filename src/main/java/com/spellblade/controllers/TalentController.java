package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Attribute;
import com.spellblade.model.Effect;
import com.spellblade.model.screens.TalentScreen;
import com.spellblade.repository.AttributeLkpRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TalentLkpRepository;

//the endpoints for everything related to characters
@Controller
public class TalentController {

    @Autowired
    private AttributeLkpRepository attributes;
    @Autowired
    private TalentLkpRepository talents;
    @Autowired
    private EffectRepository effects;

    @QueryMapping
    public List<TalentScreen> getTalentAndAttributeData(@Argument String talent1Name, @Argument String talent2Name){
        List<TalentScreen> results = new ArrayList<>();
        results.add(getTalentData(talent1Name));
        results.add(getTalentData(talent2Name));
        return results;
    }

    @QueryMapping
    public TalentScreen getTalentScreen() {
        return getTalentData("");
    }

    private TalentScreen getTalentData(String talentName) {
        TalentScreen result = new TalentScreen();
        result.setTalents(talents.findAll());
        List<Effect> effectList = new ArrayList<>();
        result.getTalents().forEach(t-> effectList.addAll(effects.findByNameContainingIgnoreCase(t.getName())));
        result.setEffects(effectList);
        return result;
    }

    @QueryMapping
    public List<Attribute> getAttributeList(){
        return attributes.findAll();
    }
    
}
