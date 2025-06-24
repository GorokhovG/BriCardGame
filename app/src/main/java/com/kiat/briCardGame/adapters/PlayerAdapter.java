//package com.kiat.briCardGame.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.kiat.briCardGame.R;
//import com.kiat.briCardGame.items.Player;
//
//import java.util.List;
//
//public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
//    private final List<Player> players;
//    private final Context context;
//
//    public PlayerAdapter(Context ctx, List<Player> players) {
//        this.players = players;
//        this.context = ctx;
//    }
//
//    @NonNull
//    @Override
//    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
//        return new PlayerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
//        Player player = players.get(position);
//
//        // Closed cards (always 2)
//        holder.ivClosed1.setVisibility(player.closedCards.size() > 0 ? View.VISIBLE : View.INVISIBLE);
//        holder.ivClosed2.setVisibility(player.closedCards.size() > 1 ? View.VISIBLE : View.INVISIBLE);
//
//        // Open card
//        if (player.openCard != null) {
//            int resId = context.getResources().getIdentifier(
//                    player.openCard.imageResName, "drawable", context.getPackageName());
//            holder.ivOpen.setImageResource(resId);
//            holder.ivOpen.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivOpen.setVisibility(View.INVISIBLE);
//        }
//
//        holder.tvInfo.setText(player.name + "\n" + (player.closedCards.size() + (player.openCard != null ? 1 : 0)) + " cards");
//    }
//
//    @Override
//    public int getItemCount() {
//        return players.size();
//    }
//
//    static class PlayerViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivClosed1, ivClosed2, ivOpen;
//        TextView tvInfo;
//
//        public PlayerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ivClosed1 = itemView.findViewById(R.id.ivClosedCard1);
//            ivClosed2 = itemView.findViewById(R.id.ivClosedCard2);
//            ivOpen = itemView.findViewById(R.id.ivOpenCard);
//            tvInfo = itemView.findViewById(R.id.tvPlayerInfo);
//        }
//    }
//}
