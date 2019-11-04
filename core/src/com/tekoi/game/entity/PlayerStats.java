package com.tekoi.game.entity;

public class PlayerStats {

    private int HP;
    //add inventory
    //add defence
    //attack


    public PlayerStats() {
        HP = 3;
    }

    public int getHP() {
        return HP;
    }

    public void takeDamage(int damage) {
        HP -= damage;
    }
}
