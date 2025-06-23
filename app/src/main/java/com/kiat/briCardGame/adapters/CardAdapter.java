package com.kiat.briCardGame.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.kiat.briCardGame.R;
import com.kiat.briCardGame.items.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final List<Card> cards;
    private final OnCardDragListener dragListener;
    private final Context context;

    public interface OnCardDragListener {
        void onStartDrag(View cardView, Card card);
    }

    public CardAdapter(Context ctx, List<Card> cards, OnCardDragListener listener) {
        this.cards = cards;
        this.context = ctx;
        this.dragListener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);

        // Set real card image from resource name
        int resId = context.getResources().getIdentifier(
                card.getImageResName(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivCard.setImageResource(resId);
        } else {
            // fallback
            holder.ivCard.setImageResource(R.drawable.sor2);
        }

        // Drag & Drop: long press
        holder.itemView.setOnLongClickListener(v -> {
            // Haptic feedback
            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vib != null) vib.vibrate(30);

            dragListener.onStartDrag(holder.itemView, card);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    // Remove card from hand and update
    public void removeCard(Card card) {
        int idx = cards.indexOf(card);
        if (idx != -1) {
            cards.remove(idx);
            notifyItemRemoved(idx);
        }
    }

    // Add card to hand at exact position and update
    public void addCard(Card card, int position) {
        if (position < 0 || position > cards.size()) {
            position = cards.size();
        }
        cards.add(position, card);
        notifyItemInserted(position);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCard;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivCard);
        }
    }
}
