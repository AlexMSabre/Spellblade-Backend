package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Aspect;
import com.spellblade.model.CharacterObject;
import com.spellblade.repository.AspectLkpRepository;


public class AspectOperations{
    
    private final AspectLkpRepository aspects;

    public AspectOperations(AspectLkpRepository aspects){
        this.aspects = aspects;
    }

    public List<Aspect> getAspectsFromTalentAndFlags(CharacterObject character){
        List<Aspect> results = new ArrayList<>();
        results.addAll(getAspectsFromTalentAndFlags(character.getTalent1(), character.getAspects1()));
        results.addAll(getAspectsFromTalentAndFlags(character.getTalent2(), character.getAspects2()));
        return results;
    }

    public List<Aspect> getAspectsFromTalentAndFlags(String talent, int flag){
        List<Aspect> results = new ArrayList<>();
        for(int i=1;i<16;i=i*2){
            //takes 2 to the power of i, converts it to an integer, compares it against the flag in a bitwise "And" operation and then check to make sure it isn't 0
            if((i & flag) != 0){
                //then
                results.add(aspects.findByTalentNameAndFlag(talent, i));
            }
        }
        return results;
    }
}