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

    private final static int HPBASE = 30;
    private final static int MOVEBASE = 2;
    private final static int MANABASE = 3;
    private final static int MANAFOCUSMULT = 2;
    private final static int TALENTMANA = 3;
    private final static int SPELLCAPBASE = 2;
    private final static double ABILITYMULT = 0.5;
    private final static int SKILLMIN = 0;
    private final static int SKILLMAX = 6;
    private final static int BASEENCUMB = 50;
    private final static int ENCUMBFITMULT = 10;
    private final static int WOUNDBASE = 4;

    public CalculatedState calculateState(CharacterState state, CharacterObject character) {

        List<Effect> effectList = getActiveEffectsFromState(state);
        effectList = effectList.stream().filter(e->e != null).collect(Collectors.toList());
        CalculatedState result = new CalculatedState(character, state, MOVEBASE);
        List<Long> filters = new ArrayList<>();
        filters.add(-1L);
        filters.add(0L);

        
        //do mid-level calculations here
        Talent talent1 = talents.findByName(character.getTalent1()).orElse(new Talent(""));
        Talent talent2 = talents.findByName(character.getTalent2()).orElse(new Talent(""));
        result.setHitPointsMax(HPBASE + talent1.getHpBonus() + talent2.getHpBonus());

        int mpTalents = talent1.isCaster() ? TALENTMANA : 0;
        mpTalents += talent2.isCaster() ? TALENTMANA : 0;
        int level = getAttributeLevel(character.getAttribute1(), character.getAttribute2());
        result.setManaMax(MANABASE + (MANAFOCUSMULT * result.getFocus()) + mpTalents + level);
        
        result.setDexterity((int) Math.ceil(ABILITYMULT * (result.getPrecision() + result.getFitness())));
        result.setCelerity((int) Math.ceil(ABILITYMULT * (result.getFocus() + result.getPrecision())));
        result.setSubtlety((int) Math.ceil(ABILITYMULT * (result.getSense() + result.getPrecision())));
        result.setAwareness((int) Math.ceil(ABILITYMULT * (result.getSense() + result.getFocus())));
        result.setEvasion((int) Math.ceil(ABILITYMULT * (result.getSense() + result.getFitness())));
        result.setTenacity((int) Math.ceil(ABILITYMULT * (result.getFocus() + result.getFitness())));
        
        result.setEncumbrance(BASEENCUMB + (ENCUMBFITMULT * result.getFitness()));
        result.setSpellCapacity(SPELLCAPBASE + (mpTalents/TALENTMANA) + level + result.getFocus());

        String[] applying = concatenateEffects(effectList, filters);
        applyEffects(result, applying);

        result.setWoundsMax(WOUNDBASE + result.getTenacity());

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

    //recalculates state in case of equip
    public CharacterDAO calculateState(String effectName, CharacterState state) {
        if(effectName!= null){
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

    public int getAttributeLevel(int attribute1, int attribute2) {
        int total = 0;
        while (attribute1 > 0 || attribute2 > 0) {
            total += attribute1 & 1;
            total += attribute2 & 1;
            attribute1 >>= 1;
            attribute2 >>= 1;
        }
        return total;
    }
}