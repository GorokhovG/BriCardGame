package com.kiat.briCardGame.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiat.briCardGame.R;
import com.kiat.briCardGame.items.Card;
import com.kiat.briCardGame.items.Player;

import java.util.List;

public class OpponentAdapter extends RecyclerView.Adapter<OpponentAdapter.OpponentVH> {
    public interface OnDropTargetListener {
        void onCardDropped(Player opponent);
    }
    private final List<Player> opponents;
    private final Context context;

    public OpponentAdapter(Context ctx, List<Player> opponents) {
        this.opponents = opponents;
        this.context = ctx;
    }

    @NonNull
    @Override
    public OpponentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opponent, parent, false);
        return new OpponentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpponentVH holder, int position) {
        Player opponent = opponents.get(position);
        holder.tvName.setText(opponent.name);

        // Показываем только верхнюю карту стопки (или рубашку, если пусто)
        holder.stackContainer.removeAllViews();
        if (!opponent.stack.isEmpty()) {
            Card top = opponent.stack.get(opponent.stack.size() - 1);
            int resId = context.getResources().getIdentifier(top.imageResName, "drawable", context.getPackageName());
            holder.stackContainer.addView(createCardView(resId));
        } else {
            int resId = context.getResources().getIdentifier("sor2", "drawable", context.getPackageName());
            holder.stackContainer.addView(createCardView(resId));
        }
    }

    @Override
    public int getItemCount() {
        return opponents.size();
    }

    public class OpponentVH extends RecyclerView.ViewHolder {
        public FrameLayout stackContainer;
        TextView tvName;
        OpponentVH(@NonNull View itemView) {
            super(itemView);
            stackContainer = itemView.findViewById(R.id.opponentStackContainer);
            tvName = itemView.findViewById(R.id.tvOpponentName);
        }
    }

    private View createCardView(int resId) {
        android.widget.ImageView iv = new android.widget.ImageView(context);
        iv.setImageResource(resId);
        iv.setLayoutParams(new FrameLayout.LayoutParams(140, 196));
        iv.setElevation(8f);
        return iv;
    }
}
