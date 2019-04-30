package com.example.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.addressbook.data.DatabaseDescription.Contact;

public class ContactsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Метод обратного вызова, реализуемый MainActivity
    public interface ContactsFragmentListener {
        // Вызывается при выборе контакт
        void onContactSelected(Uri contactUri);

        // Вызывается при нажатии кнопки добавления
        void onAddContact();
    }

    private static final int CONTACTS_LOADER = 0; // identifies Loader

    // used to inform the MainActivity when a contact is selected
    private ContactsFragmentListener listener;

    private ContactsAdapter contactsAdapter; // adapter for recyclerView

    // configures this fragment's GUI
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // fragment has menu items to display

        // inflate GUI and get reference to the RecyclerView
        View view = inflater.inflate(
                R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.recyclerView);

        // recyclerView should display items in a vertical list
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getBaseContext()));

        // создание адаптера recyclerView и слушателя щелчков на элементаx реализован в активности
        contactsAdapter = new ContactsAdapter(
                new ContactsAdapter.ContactClickListener() {
                    @Override
                    public void onClick(Uri contactUri) {
                        listener.onContactSelected(contactUri);
                    }
                }
        );
        recyclerView.setAdapter(contactsAdapter); // set the adapter

        // attach a custom ItemDecorator to draw dividers between list items
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        // improves performance if RecyclerView's layout size never changes
        recyclerView.setHasFixedSize(true);

        // get the FloatingActionButton and configure its listener
        FloatingActionButton addButton =
                (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    // displays the AddEditFragment when FAB is touched
                    @Override
                    public void onClick(View view) {
                        listener.onAddContact();
                    }
                }
        );

        return view;
    }

    // Присваивание ContactsFragment при присоединении фрагмент
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ContactsFragmentListener) context;
    }

    // Удаление ContactsFragment при отсоединении фрагмента
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // initialize a Loader when this fragment's activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
    }

    // called from MainActivity when other Fragment's update database
    public void updateContactList() {
        contactsAdapter.notifyDataSetChanged();
    }

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Создание CursorLoader на основании аргумента id; в этом
        // фрагменте только один объект Loader, и команда switch не нужна
        switch (id) {
            case CONTACTS_LOADER:
                return new CursorLoader(getActivity(),
                        Contact.CONTENT_URI, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        Contact.COLUMN_NAME + " COLLATE NOCASE ASC"); // sort по имени order
            default:
                return null;
        }
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactsAdapter.swapCursor(data);
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactsAdapter.swapCursor(null);
    }
}