package com.spellblade.model.dao;

import java.util.List;

import com.spellblade.model.Ancestry;
import com.spellblade.model.Background;
import com.spellblade.model.Effect;
import com.spellblade.model.Trait;

import lombok.Data;

//store all the data necessary for populating the background screen
@Data
public class BackgroundDAO {

    private List<Ancestry> ancestries;
    private List<Background> backgrounds;
    private List<Trait> traits;
    private List<Effect> effects;

    public BackgroundDAO(){}

}