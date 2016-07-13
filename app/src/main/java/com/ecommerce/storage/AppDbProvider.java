package com.ecommerce.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

public class AppDbProvider extends ContentProvider {

    private DbHelper database;

    private static final int CART = 1;

    public static final String AUTHORITY = "com.ecommerce.storage.AppDbProvider";

    private static final String CART_BASE_PATH = "cart_table_base_path";
    public static final Uri CONTENT_URI_CART = Uri.parse("content://" + AUTHORITY + "/" + CART_BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, CART_BASE_PATH, CART);
    }

    @Override
    public boolean onCreate() {
        database = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Context context = getContext();
        if (context == null) {
            return null;
        }

        Cursor cursor = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase sqlDB = database.getReadableDatabase();
        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case CART:
                queryBuilder.setTables(CartTable.TABLE_NAME);
                cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Context context = getContext();
        if (context == null) {
            return Uri.EMPTY;
        }

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id;
        Uri insertedUri = null;

        switch (uriType) {
            case CART:
                id = sqlDB.insert(CartTable.TABLE_NAME, null, values);
                insertedUri = Uri.parse(CART_BASE_PATH + "/" + id);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Context context = getContext();
        if (context == null) {
            return -1;
        }

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case CART:
                rowsDeleted = sqlDB.delete(CartTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                break;
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return -1;
    }

}