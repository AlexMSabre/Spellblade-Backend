package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Trait;
import com.spellblade.model.screens.BackgroundScreen;
import com.spellblade.repository.AncestryRepository;
import com.spellblade.repository.BackgroundRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TraitRepository;

public class TraitOperations{

    public final AncestryRepository ancestries;
    public final TraitRepository traits;
    public final BackgroundRepository backgrounds;
    public final EffectRepository effects;

    public TraitOperations( AncestryRepository ancestries, TraitRepository traits, BackgroundRepository backgrounds, EffectRepository effects){
        this.ancestries = ancestries;
        this.traits = traits;
        this.backgrounds = backgrounds;
        this.effects = effects;
    }

    public BackgroundScreen collectBackgroundScreenData(String source){
        BackgroundScreen result = new BackgroundScreen();
        boolean isSourced = source.equals("");
        result.setBackgrounds(isSourced ? backgrounds.findAll() :  backgrounds.findBySource(source));
        result.setAncestries(isSourced ? ancestries.findAll() :  ancestries.findBySource(source));
        List<Trait> traitResults = traits.findByTraitType("Parent Background");
        traitResults.addAll(traits.findByTraitType("Child Background"));
        traitResults.addAll(traits.findByTraitType("Ancestry"));
        result.setTraits(traitResults);
        List<String> effectNames = new ArrayList<>();
        result.getTraits().forEach(t->effectNames.add(t.getName()));
        result.setEffects(effects.findByNameIn(effectNames));
        return result;

    }
}