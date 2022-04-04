package com.ultimapieza.puzzledroid.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimapieza.puzzledroid.R;
import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListPlayersAdapter extends RecyclerView.Adapter<ListPlayersAdapter.PlayerViewHolder> {

    ArrayList<Players> listPlayers;

    public ListPlayersAdapter(ArrayList<Players> listPlayers){

        this.listPlayers=listPlayers;
    }


    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_player,null,false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        holder.viewName.setText(listPlayers.get(position).getName());
        holder.viewScore.setText(String.valueOf(listPlayers.get(position).getScore()));

        //Pruebas ordenar items en ReciclerView
        sortItems();
//        Collections.sort(listPlayers, new Comparator<Players>() {
//            @Override
//            public int compare(Players t1, Players t2) {
//                return Integer.compare(t1.getScore(), t2.getScore());
//            }
//        });

    }

    public void sortItems() {
        Collections.sort(listPlayers, new Comparator<Players>() {
            @Override
            public int compare(Players t1, Players t2) {
                return Integer.compare(t1.getScore(), t2.getScore());
            }
        });
    }

    @Override
    public int getItemCount() {

        return listPlayers.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView viewName,viewScore;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            viewName=itemView.findViewById(R.id.viewName);
            viewScore=itemView.findViewById(R.id.viewScore);


        }
    }
}
