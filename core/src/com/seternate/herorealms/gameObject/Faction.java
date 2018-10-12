package com.seternate.herorealms.gameObject;

public enum Faction {
    IMPERIAL("Imperial"),
    GUILD("Guild"),
    NECROS("Necros"),
    WILD("Wild"),
    BLUE("Blue"),
    GREEN("Green"),
    RED("Red"),
    YELLOW("Yellow"),
    DEFAULT("Default");

    private String faction;

    Faction(String faction) {
        this.faction = faction;
    }

    @Override
    public String toString() {
        return faction;
    }
}
