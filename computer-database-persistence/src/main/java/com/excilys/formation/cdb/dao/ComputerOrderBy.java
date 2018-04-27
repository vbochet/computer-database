package com.excilys.formation.cdb.dao;

public enum ComputerOrderBy {
    ID ("id"), 
    NAME ("name"),
    INTRODUCED ("introduced"),
    DISCONTINUED ("discontinued"),
    COMPANY ("company");

    private String value = "";

    ComputerOrderBy(String value){
      this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

    public static ComputerOrderBy parse(String s){
        if (s == null) {
            return ID;
        }
        
        switch (s) {
        case "id": return ID;
        case "name": return NAME;
        case "introduced": return INTRODUCED;
        case "discontinued": return DISCONTINUED;
        case "company": return COMPANY;
        default: return ID;
        }
    }
}
