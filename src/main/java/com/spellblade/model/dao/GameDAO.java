package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.CharacterObject;
import com.spellblade.model.Game;
import com.spellblade.model.PartyInventory;

import lombok.Data;

@Data
public class GameDAO{

    private Game game;
    private List<CharacterObject> characters;
    private List<PartyInventory> partyInventory;

    public GameDAO (){}

    public GameDAO (Game game, List<CharacterObject> characters, List<PartyInventory> partyInventory){
        this.game = game;
        this.characters = characters;
        this.partyInventory = partyInventory;
    }
}