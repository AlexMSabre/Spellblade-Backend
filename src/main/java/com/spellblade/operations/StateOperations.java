package com.spellblade.operations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.spellblade.model.CalculatedState;
import com.spellblade.model.CharacterObject;
import com.spellblade.model.Effect;
import com.spellblade.model.Talent;
import com.spellblade.repository.EffectRepository;

public class StateOperations {

    private final EffectRepository effects;

    public StateOperations(EffectRepository effects) {
        this.effects = effects;
    }

    private final static int HPBASE = 30;
    private final static int MOVEBASE = 2;
    private final static int MANABASE = 3;
    private final static int MANAFOCUSMULT = 2;
    private final static int TALENTMANA = 3;
    private final static int SPELLCAPBASE = 2;
    private final static double ABILITYMULT = 0.5;
    private final static int BASEENCUMB = 50;
    private final static int ENCUMBFITMULT = 10;
    private final static int WOUNDBASE = 4;

    public CalculatedState calculateState(CharacterObject character) {

        List<Effect> effectList = new ArrayList<>(); //getActiveEffectsFromState(character.getState());
        effectList = effectList.stream().filter(e->e != null).collect(Collectors.toList());
        CalculatedState result = new CalculatedState(character, MOVEBASE);
        List<Long> filters = new ArrayList<>();
        filters.add(-1L);
        filters.add(0L);

        
        //do mid-level calculations here
        Talent talent1 = character.getTalent1();
        Talent talent2 = character.getTalent2();
        result.setHitPointsMax(HPBASE + talent1.getHpBonus() + talent2.getHpBonus());

        int mpTalents = talent1.isCaster() ? TALENTMANA : 0;
        mpTalents += talent2.isCaster() ? TALENTMANA : 0;
        int level = character.getAttributeLevel();
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

    // private List<Effect> getActiveEffectsFromState(CharacterState state){
    //     List<Effect> effectList = new ArrayList<>();
    //     List<String> effectNames = Arrays.asList(state.getActiveEffects().split(","));
    //     for (String name : effectNames) {
    //         effectList.add(effects.findByName(name));
    //     }
    //     return effectList;
    // }

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

    // recalculates state in case of equip
    // public CharacterDAO calculateState(String effectName, CharacterObject character) {
    //     CharacterState state = character.getState();
    //     if(effectName!= null){
    //         Effect newEffect = effects.findByName(effectName);
    //         String conditionalCheck = newEffect != null ? newEffect.getConditionalCheck() : null;
    //         String effectString = state.getActiveEffects();

    //         //if the effect is already in the active effect list, remove it
    //         if(effectString.contains(effectName)){
    //             state.setActiveEffects(String.join("", effectString.split(effectName)));
    //         } else if(conditionalCheck != null && !conditionalCheck.equals("")){
    //             //else, if there is a condition, check if any other active effect meets the condition
    //             List<Effect> activeEffects = getActiveEffectsFromState(state);
    //             boolean isMet = activeEffects.stream().filter((e)->e.getCharProperty().equalsIgnoreCase(conditionalCheck)).count() > 0;
    //             //if the conditional is met, add it to the active effects list
    //             if (isMet) {
    //                 effectString += effectString.length() >0 ? "," : "";
    //                 state.setActiveEffects(effectString + effectName);
    //             }
    //         } else {
    //             //if no condition and not already in active effects list, add it to the list
    //             effectString += effectString.length() >0 ? "," : "";
    //             state.setActiveEffects(effectString + effectName);
    //         }
    //     }
    //     //then get re-calculate the state
    //     CharacterDAO result = new CharacterDAO();
    //     result.setCalculatedState(calculateState(character));
    //     result.setCharacter(character);
    //     return result;
    // }

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
}