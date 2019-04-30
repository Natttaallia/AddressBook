package com.example.addressbook;


import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.addressbook.data.DatabaseDescription.Contact;

public class ContactsAdapter
        extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    // Интерфейс реализуется ContactsFragment для обработки
    // прикосновения к элементу в списке RecyclerView
    public interface ContactClickListener {
        void onClick(Uri contactUri);
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется
    // для реализации паттерна View–Holder в контексте RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        private long rowID;

        // configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            // attach listener to itemView
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        // executes when the contact in this ViewHolder is clicked
                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(Contact.buildContactUri(rowID));
                        }
                    }
            );
        }

        // set the database row ID for the contact in this ViewHolder
        public void setRowID(long rowID) {
            this.rowID = rowID;
        }
    }

    // ContactsAdapter Переменные экземпляроv
    private Cursor cursor = null;
    private final ContactClickListener clickListener;

    // constructor
    public ContactsAdapter(ContactClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // Подготовка нового элемента списка и его объекта ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макетa android.R.layout.simple_list_item_1 layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view); // return current item's ViewHolder
    }

    // Назначает текст элемента списка
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(Contact._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(
                Contact.COLUMN_NAME)));
    }

    // Возвращает количество элементов, предоставляемых адаптером
    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    // Текущий объект Cursor адаптера заменяется новым
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
