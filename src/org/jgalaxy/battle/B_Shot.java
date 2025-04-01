package org.jgalaxy.battle;


public record B_Shot( TYPE type, int round,String targetID, String targetFaction, int hits) implements IB_Shot {
}
