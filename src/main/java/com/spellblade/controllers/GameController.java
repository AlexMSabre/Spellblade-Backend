package com.spellblade.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spellblade.model.CharacterObject;
import com.spellblade.model.Game;
import com.spellblade.model.PartyInventory;
import com.spellblade.model.dao.GameDAO;
import com.spellblade.operations.GameOperations;
import com.spellblade.repository.CharacterObjectRepository;
import com.spellblade.repository.GameRepository;
import com.spellblade.repository.PartyInventoryRepository;

//the endpoints for everything related to character states and effects
@Controller
public class GameController {

    @Autowired
    private GameRepository games;
    @Autowired
    private CharacterObjectRepository characters;
    @Autowired
    private PartyInventoryRepository partyInventories;
    private final GameOperations gameOps = new GameOperations();

    @QueryMapping
    public Game createGame(@Argument String ownerId) {
        Game result = games.save(new Game(ownerId));
        System.out.println(new Game().getId());

        result.setJoinCode(gameOps.generate4DigitCode(result.getId()));

        return games.save(result);
    }

    @QueryMapping
    public Game saveGame(@Argument Game game){
        if(game.getJoinCode() == null || game.getJoinCode().equals(""))
            game.setJoinCode(games.findById(game.getId()).orElseThrow().getJoinCode());
        return games.save(game);
    }

    @MutationMapping
    public Game addCharactersToGame(@Argument String gameId,  @Argument String characterId) {
        Game game = games.findById(gameId).orElseThrow();
        String gameCharacters = game.getCharacterIds();
        if(gameCharacters.length() > 1) {
            game.setCharacterIds(gameCharacters + "," + characterId);
        } else {
            game.setCharacterIds(characterId);
        }
        return games.save(game);
    }

    @MutationMapping
    public Game changeGameOwner(@Argument String gameId, @Argument String userId){
        Game game = games.findById(gameId).orElseThrow();
        game.setOwnerId(userId);
        return games.save(game);
    }

    @MutationMapping
    public GameDAO RemoveCharacterFromGame(@Argument String gameId, @Argument String characterId){
        Game game = games.findById(gameId).orElseThrow();
        String characterIds = game.getCharacterIds().replace(characterId, "");
        game.setCharacterIds(characterIds.replace(",,", ","));
        List<CharacterObject> gameCharacters = characters.findAllById(Arrays.asList(game.getCharacterIds().split(",")));
        List<PartyInventory> partyInventory = partyInventories.findByGameId(gameId);
        return new GameDAO(games.save(game), gameCharacters, partyInventory);
    }

    @QueryMapping
    public GameDAO getGameById(@Argument String gameId){
        Game game = games.findById(gameId).orElseThrow();
        List<CharacterObject> gameCharacters = characters.findAllById(Arrays.asList(game.getCharacterIds().split(",")));
        List<PartyInventory> partyInventory = partyInventories.findByGameId(gameId);
        return new GameDAO(game, gameCharacters, partyInventory);
    }

    @MutationMapping
    public PartyInventory modifyPartyInventoryItem(@Argument PartyInventory inventoryItem){
        return partyInventories.save(inventoryItem);
    }
}
