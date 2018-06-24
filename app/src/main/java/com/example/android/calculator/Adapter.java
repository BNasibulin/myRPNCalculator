package com.example.android.calculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {
    private List<Item> list;

    public Adapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder= new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent, false));
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item item = list.get(position);
        holder.formulaTXV.setText(item.getFormula() + " = " + item.getSolution());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView formulaTXV;
        public ItemViewHolder(View itemView) {
            super(itemView);
            formulaTXV = itemView.findViewById(R.id.formula);
        }
    }
}
