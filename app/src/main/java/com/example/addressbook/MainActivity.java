package com.example.addressbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements ContactsFragment.ContactsFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    // Ключ для сохранения Uri контакта в переданном объекте Bundle
    public static final String CONTACT_URI = "contact_uri";

    private ContactsFragment contactsFragment;  // Вывод списка контактов

    // Отображает ContactsFragment при первой загрузке MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Если макет содержит fragmentContainer, используется макет для
        // телефона; отобразить ContactsFragment
        if (savedInstanceState == null &&
                findViewById(R.id.fragmentContainer) != null) {
            // Создание  ContactsFragment
            contactsFragment = new ContactsFragment();

            // Добавление фрагмента в FrameLayout
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment);
            transaction.commit(); // Вывод  ContactsFragment
        }
        else {
            contactsFragment =
                    (ContactsFragment) getSupportFragmentManager().
                            findFragmentById(R.id.contactsFragment);
        }
    }

    // Отображение DetailFragment для выбранного контакта
    @Override
    public void onContactSelected(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null) // Телефон
            displayContact(contactUri, R.id.fragmentContainer);
        else { // планшет
            // Извлечение с вершины стека возврата
            getSupportFragmentManager().popBackStack();

            displayContact(contactUri, R.id.rightPaneContainer);
        }
    }

    // Отображение AddEditFragment для добавления нового контакта
    @Override
    public void onAddContact() {
        if (findViewById(R.id.fragmentContainer) != null) // телефон
            displayAddEditFragment(R.id.fragmentContainer, null);//создание, иначе второй параметр uri
        else // планшет
            displayAddEditFragment(R.id.rightPaneContainer, null);
    }

    // display a contact
    private void displayContact(Uri contactUri, int viewID) {
        DetailFragment detailFragment = new DetailFragment();

        // specify contact's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }

    // display fragment for adding a new or editing an existing contact
    private void displayAddEditFragment(int viewID, Uri contactUri) {
        AddEditFragment addEditFragment = new AddEditFragment();

        // if editing existing contact, provide contactUri as an argument
        if (contactUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);
        }

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes AddEditFragment to display
    }

    // return to contact list when displayed contact deleted
    @Override
    public void onContactDeleted() {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList(); // refresh contacts
    }

    // display the AddEditFragment to edit an existing contact
    @Override
    public void onEditContact(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayAddEditFragment(R.id.fragmentContainer, contactUri);
        else // tablet
            displayAddEditFragment(R.id.rightPaneContainer, contactUri);
    }

    // update GUI after new contact or updated contact saved
    @Override
    public void onAddEditCompleted(Uri contactUri) {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList(); // refresh contacts

        if (findViewById(R.id.fragmentContainer) == null) { // tablet
            // removes top of back stack
            getSupportFragmentManager().popBackStack();

            // on tablet, display contact that was just added or edited
            displayContact(contactUri, R.id.rightPaneContainer);
        }
    }
}
