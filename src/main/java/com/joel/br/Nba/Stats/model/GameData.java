package com.joel.br.Nba.Stats.model;

import lombok.Data;

@Data
public class GameData {
    private String date;
    private String opponent;
    private String result;
    private String minutes;
    private String fieldGoals;
    private String fgPct;
    private String threePointers;
    private String threePtPct;
    private String freeThrows;
    private String ftPct;
    private String rebounds;
    private String assists;
    private String blocks;
    private String steals;
    private String fouls;
    private String turnovers;
    private String points;
}
