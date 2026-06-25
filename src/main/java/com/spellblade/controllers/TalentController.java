package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Attribute;
import com.spellblade.model.Talent;
import com.spellblade.model.dao.TalentDAO;
import com.spellblade.operations.AttributeOperations;
import com.spellblade.repository.AttributeLkpRepository;
import com.spellblade.repository.TalentLkpRepository;

//the endpoints for everything related to characters
@Controller
public class TalentController {

    private final AttributeLkpRepository attribute;
    private final TalentLkpRepository talents;
    private final AttributeOperations attributeOperations;

    public TalentController(AttributeLkpRepository attribute, TalentLkpRepository talents){
        this.talents = talents;
        this.attribute = attribute;
        this.attributeOperations = new AttributeOperations(attribute);
    }

    @QueryMapping
    public List<TalentDAO> getTalentAndAttributeData(@Argument String talent1Name, @Argument String talent2Name){
        List<TalentDAO> results = new ArrayList<>();
        results.add(getTalentData(talent1Name));
        results.add(getTalentData(talent2Name));
        return results;
    }

    private TalentDAO getTalentData(String talentName) {
        TalentDAO result = new TalentDAO();
        result.setTalent(talents.findByName(talentName).orElse(new Talent()));
        List<Attribute> attributeList = attribute.findByTalentName(talentName);
        Collections.sort(attributeList, (Attribute i1, Attribute i2) -> i1.getFlag() - i2.getFlag());
        result.setAttributes(attributeList);
        return result;
    }

    @QueryMapping
    public List<String> getTalentsList(){
        List<Talent> talentList = talents.findAll();
        return talentList.stream().map((talent)->talent.getName()).collect(Collectors.toList());
    }
    
}
