package com.spellblade.operations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.CharacterState;
import com.spellblade.model.Effect;
import com.spellblade.model.Talent;
import com.spellblade.model.dao.CharacterDAO;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.EffectRepository;
import com.spellblade.repository.TalentLkpRepository; 

public class StateOperations {

    private final EffectRepository effects;
    private final TalentLkpRepository talents;
    private final CharacterObjectRepository characters;

    public StateOperations(EffectRepository effects, TalentLkpRepository talents, CharacterObjectRepository characters) {
        this.effects = effects;
        this.talents = talents;
        this.characters = characters;
    }

    public CalculatedState calculateState(CharacterState state, CharacterObject character) {

        List<Effect> effectList = getActiveEffectsFromState(state);
        effectList = effectList.stream().filter(e->e != null).collect(Collectors.toList());
        CalculatedState result = new CalculatedState(character, state);
        List<Long> filters = new ArrayList<>();
        filters.add(-1L);
        filters.add(0L);

        
        //do mid-level calculations here
        Talent talent1 = talents.findByName(character.getTalent1()).orElse(new Talent(""));
        Talent talent2 = talents.findByName(character.getTalent2()).orElse(new Talent(""));
        result.setHitPointsMax(40 + talent1.getHpBonus() + talent2.getHpBonus());

        int mpTalents = talent1.getRole().contains("Spellcaster") ? 4 : 0;
        mpTalents += talent2.getRole().contains("Spellcaster") ? 4 : 0;
        result.setManaMax(2 + (2 * result.getFocus()) + mpTalents + getAspectLevel(character.getAspects1(), character.getAspects2()));
        
        result.setEvasion(10 + (int) Math.ceil(0.5 * result.getFitness()) + (int) Math.ceil(0.5 * result.getSense()));
        result.setHexResist(10 + (int) Math.ceil(0.5 * result.getFocus()) + (int) Math.ceil(0.5 * result.getFitness()));
        
        result.setEncumbrance(50 + (10 * result.getFitness()));

        String[] applying = concatenateEffects(effectList, filters);
        applyEffects(result, applying);

        //TODO: Wound counting
        int wounds = 6;
        switch(character.getSize()){
            case "Tiny" -> wounds = 4;
            case "Small" -> wounds = 5;
            case "Large" -> wounds = 8;
            case "Huge" -> wounds = 14;
        }
        result.setWoundsMax(wounds);

        result.setWounds((int)effectList.stream().filter(e-> e.getDescription().equals("Wound")).count());

        filters.clear();
        filters.add(1L);
        filters.add(2L);
        applying = concatenateEffects(effectList, filters);
        applyEffects(result, applying);

        return result;
    }

    private List<Effect> getActiveEffectsFromState(CharacterState state){
        List<Effect> effectList = new ArrayList<>();
        List<String> effectNames = Arrays.asList(state.getActiveEffects().split(","));
        for (String name : effectNames) {
            effectList.add(effects.findByName(name));
        }
        return effectList;
    }

    private void applyEffects(CalculatedState result, String[] applying) {
        Class[] params = new Class[1];
        params[0] = int.class;
        for (int i = 0; i < applying.length - 1; i += 3) {
            try {
                //gets the setters and getters for the value that is changing
                Method setter = result.getClass().getMethod("set" + StringUtils.capitalize(applying[i]), params);
                Method destGetter = result.getClass().getMethod("get" + StringUtils.capitalize(applying[i]));
                //source sourcing
                int source = applying[i + 1].equals("1") ? 1 : (int) result.getClass().getMethod("get" + StringUtils.capitalize(applying[i + 1])).invoke(result);
                int prev = (int) destGetter.invoke(result);
                int value = prev + (int) Math.ceil(source * Double.parseDouble(applying[i + 2]));
                setter.invoke(result, value >= 0 ? value: 0);
            } catch (Exception nme) {
                nme.printStackTrace();
            }
        }
    }

    //recalculates state incase of equip
    public CharacterDAO calculateState(String effectName, CharacterState state) {
        Effect newEffect = effects.findByName(effectName);
        String conditionalCheck = newEffect != null ? newEffect.getConditionalCheck() : null;
        String effectString = state.getActiveEffects();
        if(effectString.contains(effectName)){
            state.setActiveEffects(String.join("", effectString.split(effectName)));
        } else if(conditionalCheck != null && !conditionalCheck.equals("")){
            List<Effect> activeEffects = getActiveEffectsFromState(state);
            boolean isMet = activeEffects.stream().filter((e)->e.getCharProperty().equalsIgnoreCase(conditionalCheck)).count() > 0;
            if (isMet) {
                effectString += effectString.length() >0 ? "," : "";
                state.setActiveEffects(effectString + effectName);
            }
        } else {
            effectString += effectString.length() >0 ? "," : "";
            state.setActiveEffects(effectString + effectName);
        }
        CharacterObject character = characters.findById(state.getCharacterId()).orElseThrow();
        CharacterDAO result = new CharacterDAO();
        result.setCalculatedState(calculateState(state, character));
        result.setCharacterState(state);
        return result;
    }

//gets all effects from a list that contain the given filters, 
//then concatenates all of the effect text into one long string for processing
    private String[] concatenateEffects(List<Effect> effectList, List<Long> filters) {
        Collections.sort(effectList, Comparator.comparingLong(f -> f.getEffectType()));
        List<Effect> filteredList = effectList.stream()
                .filter(e -> filters.contains((long) e.getEffectType()))
                .collect(Collectors.toList());
        String result = "";
        for (Effect effect : filteredList) {
            result += effect.getEffect() + ",";
        }
        return result.split(",");
    }

    public int getAspectLevel(int aspect1, int aspect2) {
        int total = 0;
        while (aspect1 > 0 || aspect2 > 0) {
            total += aspect1 & 1;
            total += aspect2 & 1;
            aspect1 >>= 1;
            aspect2 >>= 1;
        }
        return total;
    }
}