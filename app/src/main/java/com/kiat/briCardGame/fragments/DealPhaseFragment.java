package com.kiat.briCardGame.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.kiat.briCardGame.R;
import com.kiat.briCardGame.items.Card;
import com.kiat.briCardGame.items.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DealPhaseFragment extends Fragment {
    private static final int CARD_USER_WIDTH_DP = 140;
    private static final int CARD_USER_HEIGHT_DP = 196;
    private static final int CARD_OPP_WIDTH_DP = 96;
    private static final int CARD_OPP_HEIGHT_DP = 130;
    // Оппоненты
    private List<Player> opponents = new ArrayList<>();
    private Player user;
    private List<Card> deck = new ArrayList<>();
    private Handler handler = new Handler();

    // UI
    private FrameLayout deckContainer, myStackContainer;
    private TextView myName;

    private View topAlert;
    private TextView tvTopAlert;

    // Opponent containers
    private FrameLayout[] opponentStacks = new FrameLayout[5];
    private TextView[] opponentNames = new TextView[5];
    private View[] opponentContainers = new View[5];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deal_phase, container, false);

        deckContainer = v.findViewById(R.id.deckContainer);
        myStackContainer = v.findViewById(R.id.myStackContainer);
        myName = v.findViewById(R.id.myName);
        topAlert = v.findViewById(R.id.topAlert);
        tvTopAlert = topAlert.findViewById(R.id.tvTopAlert);

        // Оппоненты — контейнеры и стеки
        opponentContainers[0] = v.findViewById(R.id.opponent1Container);
        opponentStacks[0] = v.findViewById(R.id.opponent1Stack);
        opponentNames[0] = v.findViewById(R.id.opponent1Name);

        opponentContainers[1] = v.findViewById(R.id.opponent2Container);
        opponentStacks[1] = v.findViewById(R.id.opponent2Stack);
        opponentNames[1] = v.findViewById(R.id.opponent2Name);

        opponentContainers[2] = v.findViewById(R.id.opponent3Container);
        opponentStacks[2] = v.findViewById(R.id.opponent3Stack);
        opponentNames[2] = v.findViewById(R.id.opponent3Name);

        opponentContainers[3] = v.findViewById(R.id.opponent4Container);
        opponentStacks[3] = v.findViewById(R.id.opponent4Stack);
        opponentNames[3] = v.findViewById(R.id.opponent4Name);

        opponentContainers[4] = v.findViewById(R.id.opponent5Container);
        opponentStacks[4] = v.findViewById(R.id.opponent5Stack);
        opponentNames[4] = v.findViewById(R.id.opponent5Name);

        // ---- Инициализация игроков и колоды ----
        int playerCount = 4; // demo, поменяй на нужное
        user = new Player("You", true);
        opponents.clear();
        for (int i = 1; i < playerCount; i++) {
            opponents.add(new Player("Player " + (i + 1), false));
        }
        generateFullDeck();
        Collections.shuffle(deck);

        // Раздаём по 2 карты в стопки (закрытые)
        user.stack.add(deck.remove(0));
        user.stack.add(deck.remove(0));
        for (Player op : opponents) {
            op.stack.add(deck.remove(0));
            op.stack.add(deck.remove(0));
        }
        // По одной открытой сверху
        user.stack.add(deck.remove(0));
        for (Player op : opponents) op.stack.add(deck.remove(0));

        // ---- UI отрисовка ----
        showUserStack();
        showDeckStack();
        showOpponents();

        // Drag&Drop из колоды: только ты!
        deckContainer.setOnLongClickListener(view -> {
            if (deck.isEmpty() || deckContainer.getChildCount() == 0) return false;
            Card top = deck.get(0);
            View cardView = deckContainer.getChildAt(deckContainer.getChildCount() - 1);
            cardView.startDragAndDrop(null, new View.DragShadowBuilder(cardView), top, 0);
            return true;
        });

        // Drop на свою стопку
        myStackContainer.setOnDragListener((v1, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                moveCardFromDeckToStack(user);
                return true;
            }
            return true;
        });

        // Drop на opponent-стопки
        for (int i = 0; i < opponents.size(); i++) {
            final int idx = i;
            opponentStacks[i].setOnDragListener((v2, event) -> {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    moveCardFromDeckToStack(opponents.get(idx));
                    return true;
                }
                return true;
            });
        }

        myName.setText(user.name);

        // Скрыть лишних оппонентов
        for (int i = 0; i < 5; i++) {
            if (i < opponents.size()) {
                opponentContainers[i].setVisibility(View.VISIBLE);
            } else {
                opponentContainers[i].setVisibility(View.GONE);
            }
        }

        return v;
    }

    private void showUserStack() {
        myStackContainer.removeAllViews();
        if (!user.stack.isEmpty()) {
            Card top = user.stack.get(user.stack.size() - 1);
            myStackContainer.addView(createCardView(top.imageResName, CARD_USER_WIDTH_DP, CARD_USER_HEIGHT_DP));
        } else {
            int resId = getResources().getIdentifier("sor2", "drawable", getContext().getPackageName());
            myStackContainer.addView(createCardViewByRes(resId, CARD_USER_WIDTH_DP, CARD_USER_HEIGHT_DP));
        }
    }

    private void showOpponents() {
        for (int i = 0; i < opponents.size(); i++) {
            Player op = opponents.get(i);
            opponentNames[i].setText(op.name);
            opponentStacks[i].removeAllViews();
            if (!op.stack.isEmpty()) {
                Card top = op.stack.get(op.stack.size() - 1);
                int resId = getResources().getIdentifier(top.imageResName, "drawable", getContext().getPackageName());
                opponentStacks[i].addView(createCardView(top.imageResName, CARD_OPP_WIDTH_DP, CARD_OPP_HEIGHT_DP));
            } else {
                int resId = getResources().getIdentifier("sor2", "drawable", getContext().getPackageName());
                opponentStacks[i].addView(createCardViewByRes(resId, CARD_USER_WIDTH_DP, CARD_USER_HEIGHT_DP));
            }
        }
    }

    private void moveCardFromDeckToStack(Player player) {
        if (deck.isEmpty()) return;
        Card toMove = deck.remove(0);
        FrameLayout targetStack = player.isUser ? myStackContainer : getOpponentStackContainer(player);
        animateCardFromDeckToStack(toMove, targetStack, () -> {
            player.stack.add(toMove);
            showUserStack();
            showOpponents();
            showDeckStack();
        });
    }

    private FrameLayout getOpponentStackContainer(Player player) {
        int idx = opponents.indexOf(player);
        if (idx < 0 || idx >= opponentStacks.length) return null;
        return opponentStacks[idx];
    }

    private void showDeckStack() {
        deckContainer.removeAllViews();
        int stackSize = Math.min(deck.size(), 12);
        for (int i = 0; i < stackSize; i++) {
            Card card = deck.get(i);
            ImageView iv = createCardView(card.imageResName, CARD_OPP_WIDTH_DP, CARD_OPP_HEIGHT_DP);
            iv.setTranslationX(i * 3f);
            iv.setTranslationY(i * 3f);
            iv.setScaleX(1f - i * 0.012f);
            iv.setScaleY(1f - i * 0.012f);
            iv.setAlpha(1f - i * 0.04f);
            deckContainer.addView(iv, 0);
        }
    }

    private void animateCardFromDeckToStack(Card card, FrameLayout targetStack, Runnable onEnd) {
        if (deckContainer.getChildCount() == 0 || targetStack == null) {
            if (onEnd != null) onEnd.run();
            return;
        }

        // Определяем размеры карты в зависимости от стека (твой/оппонента)
        int widthDp = (targetStack == myStackContainer) ? CARD_USER_WIDTH_DP : CARD_OPP_WIDTH_DP;
        int heightDp = (targetStack == myStackContainer) ? CARD_USER_HEIGHT_DP : CARD_OPP_HEIGHT_DP;

        // Получаем координаты колоды и целевой стопки на экране
        int[] deckLoc = new int[2];
        int[] stackLoc = new int[2];
        deckContainer.getLocationOnScreen(deckLoc);
        targetStack.getLocationOnScreen(stackLoc);

        // Создаём "летающую" карту нужного размера
        ImageView flying = createCardView(card.imageResName, widthDp, heightDp);

        // Получаем корневой layout для анимации
        FrameLayout root = requireActivity().findViewById(android.R.id.content);
        float density = getResources().getDisplayMetrics().density;
        // Устанавливаем начальные координаты (центр колоды)
        int startX = deckLoc[0];
        int startY = deckLoc[1];

        // Устанавливаем финальные координаты (центр целевой стопки)
        int endX = stackLoc[0];
        int endY = stackLoc[1];

        // Добавляем карту в root layout
        root.addView(flying, new FrameLayout.LayoutParams(
                (int) (widthDp * density), (int) (heightDp * density)));

        flying.setX(startX);
        flying.setY(startY);

        // Анимируем перемещение карты
        flying.animate()
                .x(endX)
                .y(endY)
                .setDuration(400)
                .withEndAction(() -> {
                    root.removeView(flying);
                    if (onEnd != null) onEnd.run();
                })
                .start();
    }

    private void generateFullDeck() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2","3","4","5","6","7","8","9","10","jack","queen","king","ace"};
        int[] values = {2,3,4,5,6,7,8,9,10,11,12,13,14};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                String name = (ranks[i].equals("ace") || ranks[i].equals("jack") || ranks[i].equals("queen") || ranks[i].equals("king")) ?
                        ranks[i] + "_of_" + suit : suit + "_of_" + ranks[i];
                deck.add(new Card(name, values[i], suit, ranks[i]));
            }
        }
        deck.add(new Card("black_joker", 15, "joker", "Joker"));
        deck.add(new Card("red_joker", 15, "joker", "Joker"));
    }

    private ImageView createCardView(String resName, int widthDp, int heightDp) {
        int resId = getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
        ImageView iv = new ImageView(getContext());
        float density = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                (int) (widthDp * density), (int) (heightDp * density));
        iv.setLayoutParams(lp);
        iv.setImageResource(resId);
        iv.setElevation(8f);
        return iv;
    }

    private ImageView createCardViewByRes(int resId, int widthDp, int heightDp) {
        ImageView iv = new ImageView(getContext());
        float density = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                (int) (widthDp * density), (int) (heightDp * density));
        iv.setLayoutParams(lp);
        iv.setImageResource(resId);
        iv.setElevation(8f);
        return iv;
    }
}