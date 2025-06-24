package com.kiat.briCardGame.items;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    public List<Card> stack = new ArrayList<>();
    public boolean isUser;
    public Player(String name, boolean isUser) {
        this.name = name;
        this.isUser = isUser;
    }
}