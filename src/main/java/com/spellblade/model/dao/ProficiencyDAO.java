package com.spellblade.model.dao;

import com.spellblade.model.Item;

import lombok.Data;

@Data
public class ProficiencyDAO {

    private Item item;
    private boolean mastery;

    public ProficiencyDAO() {}

    public ProficiencyDAO(Item item){
        this.item = item;
    }

    public ProficiencyDAO(Item item, boolean mastery){
        this.item = item;
        this.mastery = mastery;
    }

}