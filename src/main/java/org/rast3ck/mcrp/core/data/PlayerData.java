package org.rast3ck.mcrp.core.data;

import java.util.UUID;

public class PlayerData {

    private final UUID playerId;


    private double money;


    public PlayerData(UUID playerId) {

        this.playerId = playerId;
        this.money = 0;
    }


    public UUID getPlayerId() {
        return playerId;
    }


    public double getMoney() {
        return money;
    }


    public void setMoney(double money) {
        this.money = money;
    }

}