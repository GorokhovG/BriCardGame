package com.kiat.briCardGame.items;

public class Card {
    private final String imageResName; // e.g. "ace_of_clubs"
    // You can add more fields for suit/rank if needed

    public Card(String imageResName) {
        this.imageResName = imageResName;
    }

    public String getImageResName() {
        return imageResName;
    }
}
