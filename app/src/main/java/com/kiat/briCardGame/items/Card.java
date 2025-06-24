package com.kiat.briCardGame.items;

public class Card {
    public String imageResName;
    public int rankValue;
    public String suit;
    public String rank;
    public Card(String imageResName, int rankValue, String suit, String rank) {
        this.imageResName = imageResName;
        this.rankValue = rankValue;
        this.suit = suit;
        this.rank = rank;
    }
}
