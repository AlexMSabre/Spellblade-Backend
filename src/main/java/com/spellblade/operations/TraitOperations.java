package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.CharacterObject;
import com.spellblade.model.Trait;
import com.spellblade.model.dao.BackgroundDAO;
import com.spellblade.model.dao.TraitsDAO;
import com.spellblade.repository.AncestryRepository;
import com.spellblade.repository.BackgroundRepository;
import com.spellblade.repository.TraitRepository;

public class TraitOperations{

    public final AncestryRepository ancestries;
    public final TraitRepository traits;
    public final BackgroundRepository backgrounds;

    public TraitOperations( AncestryRepository ancestries, TraitRepository traits, BackgroundRepository backgrounds){
        this.ancestries = ancestries;
        this.traits = traits;
        this.backgrounds = backgrounds;
    }

    public TraitsDAO collectTraitDetails(CharacterObject character){
        TraitsDAO result = new TraitsDAO();

        result.setAncestry(ancestries.findByName(character.getAncestry()).orElseThrow());
        result.setBackground(backgrounds.findByName(character.getBackground()).orElseThrow());

        List<Trait> characterTraits = new ArrayList<>();
        
        characterTraits.add(traits.findByName(result.getAncestry().getTrait1()).orElseThrow());
        characterTraits.add(traits.findByName(result.getAncestry().getTrait2()).orElseThrow());
        characterTraits.add(traits.findByName(result.getBackground().getParentTrait()).orElseThrow());
        characterTraits.add(traits.findByName(result.getBackground().getChildTrait()).orElseThrow());

        result.setTraits(characterTraits);

        return result;
    }

    public BackgroundDAO collectBackgroundScreenData(String source){
        BackgroundDAO result = new BackgroundDAO();
        boolean isSourced = source.equals("");
        result.setBackgrounds(isSourced ? backgrounds.findAll() :  backgrounds.findBySource(source));
        result.setAncestries(isSourced ? ancestries.findAll() :  ancestries.findBySource(source));
        List<Trait> traitResults = traits.findByTraitType("Parent Background");
        traitResults.addAll(traits.findByTraitType("Child Background"));
        result.setTraits(traitResults);

        return result;

    }
}