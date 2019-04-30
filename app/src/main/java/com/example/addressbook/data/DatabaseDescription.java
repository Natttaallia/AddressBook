package com.example.addressbook.data;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
    // ContentProvider's name: typically the package name
    public static final String AUTHORITY =
            "com.example.addressbook.data";

    // base URI used to interact with the ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // Вложенный класс, определяющий содержимое таблицы contact
    public static final class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contacts"; // table's name

        // Объект Uri для таблицы contacts
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // Имена столбцов таблицы
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";

        // Создание Uri для конкретного контакт
        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
