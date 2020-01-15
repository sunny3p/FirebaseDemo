package com.sp.firebasedemo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<Student> data = new ArrayList<>();
    private OnChangeListner mOnChangeListner;


    public RecyclerAdapter(ArrayList<Student> _data, OnChangeListner onChangeListner) {
        super();
        data = _data;
        Log.d("stock", "got data with size=" + _data.size());
        this.mOnChangeListner = onChangeListner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v, mOnChangeListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d("student", "Bind a view for pos" + position);
        viewHolder.fname.setText(data.get(position).getFirst_name());
        viewHolder.lname.setText(data.get(position).getLast_name());
        viewHolder.marks.setText(data.get(position).getMarks());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public View itemView;
        public TextView fname;
        public TextView lname;
        public TextView marks;
        public ImageButton delete;
        OnChangeListner onChangeListner;


        public ViewHolder(View itemView, final OnChangeListner onChangeListner) {
            super(itemView);
            this.itemView = itemView;
            fname = itemView.findViewById(R.id.item_fname);
            lname = itemView.findViewById(R.id.item_lname);
            marks = itemView.findViewById(R.id.item_marks);
            delete = itemView.findViewById(R.id.deleteBtn);
            this.onChangeListner = onChangeListner;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onChangeListner != null) {
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                            onChangeListner.onUpdateListner(position);
                    }
                }
            });

            delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onChangeListner != null) {
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                            onChangeListner.onDeleteListner(position);
                    }
                }
            });

        }

    }
    public interface OnChangeListner{
        void onUpdateListner(int position);
        void onDeleteListner(int position);
    }
}
