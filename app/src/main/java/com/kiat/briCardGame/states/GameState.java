package com.kiat.briCardGame.states;

import com.kiat.briCardGame.items.Card;
import com.kiat.briCardGame.items.Player;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public List<Player> players = new ArrayList<>();
    public List<Card> deck = new ArrayList<>();
    public int currentPlayerIdx = 0;
    public Card openCard = null;

    public GameState(int playerCount) {
        // инициализация игроков и колоды
    }
}