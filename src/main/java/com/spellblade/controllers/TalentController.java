package com.spellblade.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.Aspect;
import com.spellblade.model.Talent;
import com.spellblade.model.dao.TalentDAO;
import com.spellblade.operations.AspectOperations;
import com.spellblade.repository.AspectLkpRepository;
import com.spellblade.repository.TalentLkpRepository;

//the endpoints for everything related to characters
@Controller
public class TalentController {

    private final AspectLkpRepository aspects;
    private final TalentLkpRepository talents;
    private final AspectOperations aspectOperations;

    public TalentController(AspectLkpRepository aspects, TalentLkpRepository talents){
        this.talents = talents;
        this.aspects = aspects;
        this.aspectOperations = new AspectOperations(aspects);
    }

    @QueryMapping
    public List<TalentDAO> getTalentAndAspectsData(@Argument String talent1Name, @Argument String talent2Name){
        List<TalentDAO> results = new ArrayList<>();
        results.add(getTalentData(talent1Name));
        results.add(getTalentData(talent2Name));
        return results;
    }

    private TalentDAO getTalentData(String talentName) {
        TalentDAO result = new TalentDAO();
        result.setTalent(talents.findByName(talentName).orElse(new Talent()));
        List<Aspect> aspectList = aspects.findByTalentName(talentName);
        Collections.sort(aspectList, (Aspect i1, Aspect i2) -> i1.getFlag() - i2.getFlag());
        result.setAspects(aspectList);
        return result;
    }

    @QueryMapping
    public List<String> getTalentsList(){
        List<Talent> talentList = talents.findAll();
        return talentList.stream().map((talent)->talent.getName()).collect(Collectors.toList());
    }
    
}
