package com.kiat.briCardGame;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.kiat.briCardGame.adapters.CardAdapter;
import com.kiat.briCardGame.items.Card;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private RecyclerView handRecycler;
    private CardAdapter handAdapter;
    private List<Card> playerHand = new ArrayList<>();
    private View topAlert;
    private TextView tvTopAlert;
    private Handler alertHandler = new Handler();

    // For drag&drop restore
    private Card draggedCard;
    private int draggedCardPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        topAlert = findViewById(R.id.topAlert);
        tvTopAlert = topAlert.findViewById(R.id.tvTopAlert);

        handRecycler = findViewById(R.id.handRecycler);
        handRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Example hand, use your real data
        playerHand.add(new Card("ace_of_clubs"));
        playerHand.add(new Card("clubs_of_9"));
        playerHand.add(new Card("black_joker"));
        playerHand.add(new Card("diamonds_of_5"));
        playerHand.add(new Card("jack_of_hearts"));
        playerHand.add(new Card("queen_of_spades"));

        handAdapter = new CardAdapter(this, playerHand, (view, card) -> {
            // Remember card for restoring
            draggedCard = card;
            draggedCardPosition = playerHand.indexOf(card);

            // Remove from hand (UI)
            handAdapter.removeCard(card);

            // Start drag
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(null, shadowBuilder, card, 0);
        });
        handRecycler.setAdapter(handAdapter);

        // Example drop area: deckContainer (replace with your field)
        findViewById(R.id.deckContainer).setOnDragListener((v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                Card card = (Card) event.getLocalState();
                // Here, check if drop is valid
                boolean success = isDropValid(card, v); // implement this logic!
                if (!success) {
                    // Restore card to hand
                    handAdapter.addCard(draggedCard, draggedCardPosition);
                }
                draggedCard = null;
                return true;
            }
            if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && !event.getResult()) {
                // Drop was outside allowed area, restore
                if (draggedCard != null) {
                    handAdapter.addCard(draggedCard, draggedCardPosition);
                    draggedCard = null;
                }
                return true;
            }
            return true;
        });

        showGameAlert("Player 1's turn", Color.parseColor("#8BC34A"), 2000);
    }

    // Dummy drop check, always false for demo
    private boolean isDropValid(Card card, View dropTarget) {
        // TODO: implement your real logic
        return false;
    }

    // Top info alert
    public void showGameAlert(String text, int bgColor, int durationMs) {
        tvTopAlert.setText(text);
        ((MaterialCardView) topAlert).setCardBackgroundColor(bgColor);
        topAlert.setVisibility(View.VISIBLE);
        topAlert.setTranslationY(-topAlert.getHeight());
        topAlert.animate().translationY(0).setDuration(400).start();

        alertHandler.removeCallbacksAndMessages(null);
        alertHandler.postDelayed(this::removeGameAlert, durationMs);
    }

    public void removeGameAlert() {
        topAlert.animate().translationY(-topAlert.getHeight()).setDuration(300).withEndAction(() ->
                topAlert.setVisibility(View.GONE)).start();
    }
}