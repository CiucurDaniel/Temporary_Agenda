package com.example.temoporaryagenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ListAdapter<Contact ,ContactAdapter.ContactHolder> {

    private OnItemClickListener listener; //used for tap to edit contact

    public ContactAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPhone_number().equals(newItem.getPhone_number()) &&
                    oldItem.getTag().equals(newItem.getTag());
            //TODO: Compare all fields of a Contact object
        }
    };

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item,parent,false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact currentContact = getItem(position);
        holder.textViewName.setText(currentContact.getName());
        holder.textViewNumber.setText(currentContact.getPhone_number());
        holder.textViewTag.setText(currentContact.getTag());

        //However some might not be need here because in CardView we display only the 3 from before
        //On Card view only display name, phone_number and tag
    }



    //method used for swipe to DELETE
    public Contact getContactAt(int position){
        return getItem(position);
    }

    class ContactHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private TextView textViewNumber;
        private TextView textViewTag;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            //this itemView is the CardView
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewNumber = itemView.findViewById(R.id.text_view_phone_number);
            textViewTag = itemView.findViewById(R.id.text_view_tag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(getItem(position));}
                }
            });
        }
    }

    //use this to edit contact on tap
    public interface OnItemClickListener{
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
