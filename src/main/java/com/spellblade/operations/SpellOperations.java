package com.spellblade.operations;

import java.util.ArrayList;
import java.util.List;

import com.spellblade.model.Spell;
import com.spellblade.model.SpellCharacter;
import com.spellblade.model.dao.SpellDAO;
import com.spellblade.repository.SpellCharacterRepository;
import com.spellblade.repository.SpellRepository;


public class SpellOperations{
    
    private final SpellRepository spells;
    private final SpellCharacterRepository spellCharacters;


    public SpellOperations(SpellRepository spells, SpellCharacterRepository spellCharacters){
        this.spells = spells;
        this.spellCharacters = spellCharacters;
    }

    public List<SpellDAO> saveUpdateSpellDAOs(List<SpellDAO> newSpellDAOs){
        List<SpellCharacter> newCharacterSpells = extractSpellList(newSpellDAOs);
        if(!newCharacterSpells.isEmpty()){
            saveUpdateSpells(newCharacterSpells);
            String characterId = newCharacterSpells.get(0).getCharacterId();
            return createSpellDAOList(characterId);
        }
        return new ArrayList<>();
    }

    public void saveUpdateSpells(List<SpellCharacter> characterSpells){
        String characterId = characterSpells.get(0).getCharacterId();
        List<SpellCharacter> removeList = getCharacterSpells(characterId);
        for(SpellCharacter characterSpell : characterSpells){
            removeList.removeIf((e)->e.getId().equals(characterSpell.getId()));
            characterSpell.setCharacterId(characterId);
            spellCharacters.save(characterSpell);
        }
        removeList.stream().forEach(spell->spellCharacters.delete(spell));
    }

    public List<SpellCharacter> extractSpellList(List<SpellDAO> SpellDAOs){
        List<SpellCharacter> newSpells = new ArrayList<>();
        for(SpellDAO inv: SpellDAOs){
            newSpells.add(inv.getSpellCharacter());
        }
        return newSpells;
    }

    public List<SpellCharacter> getCharacterSpells(String characterId){
        return spellCharacters.findByCharacterId(characterId);
    }

    public List<SpellDAO> createSpellDAOList(String characterId){
        List<SpellDAO> results = new ArrayList<>();
        //finds the inventory item, then gets the item data, then puts both into an output object.
        spellCharacters.findByCharacterId(characterId).forEach(a -> {
            Spell spellData = spells.findById(a.getSpellId()).orElseThrow();
            SpellDAO SpellDAO = new SpellDAO(a, spellData);
            results.add(SpellDAO);
        });
        return results;
    }
}