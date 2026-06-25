package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Attribute;
import com.spellblade.model.CharacterObject;
import com.spellblade.repository.AttributeLkpRepository;


public class AttributeOperations{
    
    private final AttributeLkpRepository attribute;

    public AttributeOperations(AttributeLkpRepository attribute){
        this.attribute = attribute;
    }

    public List<Attribute> getAttributeFromTalentAndFlags(CharacterObject character){
        List<Attribute> results = new ArrayList<>();
        results.addAll(getAttributeFromTalentAndFlags(character.getTalent1(), character.getAttribute1()));
        results.addAll(getAttributeFromTalentAndFlags(character.getTalent2(), character.getAttribute2()));
        return results;
    }

    public List<Attribute> getAttributeFromTalentAndFlags(String talent, int flag){
        List<Attribute> results = new ArrayList<>();
        for(int i=1;i<16;i=i*2){
            //takes 2 to the power of i, converts it to an integer, compares it against the flag in a bitwise "And" operation and then check to make sure it isn't 0
            if((i & flag) != 0){
                //then
                results.add(attribute.findByTalentNameAndFlag(talent, i));
            }
        }
        return results;
    }
}