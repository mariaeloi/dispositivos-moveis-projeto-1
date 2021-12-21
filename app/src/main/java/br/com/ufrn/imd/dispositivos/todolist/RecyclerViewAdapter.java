package br.com.ufrn.imd.dispositivos.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<TodoItem> mData;
    private List<TodoItem> mDataCopy;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    RecyclerViewAdapter(Context context, List<TodoItem> data, List<TodoItem> dataCopy) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataCopy = dataCopy;
    }


    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem item = mData.get(position);
        holder.myTextView.setText(item.getTitle());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // convenience method for getting data at click position
    TodoItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void filter(String text) {
        mData.clear();
        if(text.isEmpty()){
            mData.addAll(mDataCopy);
        } else{
            text = text.toLowerCase();
            for(TodoItem item: mDataCopy){
                if(item.getTitle().toLowerCase().contains(text)){
                    mData.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
