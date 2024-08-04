package com.example.drobyshgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList<Result> resultArrayList;


    public CustomAdapter(Context context, ArrayList<Result> resultArrayList){
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView score;
        TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.resScore);
            duration = itemView.findViewById(R.id.resDuration);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.result_layout, parent, false);
        return new CustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.score.setText(String.valueOf(resultArrayList.get(position).score));
        holder.duration.setText(String.valueOf(resultArrayList.get(position).duration));
    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }
}
