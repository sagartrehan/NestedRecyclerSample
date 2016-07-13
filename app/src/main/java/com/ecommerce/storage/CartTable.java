package com.ecommerce.storage;

import android.database.sqlite.SQLiteDatabase;

public class CartTable {

    public static final String TABLE_NAME = "cart";

    public static final String COL_ID = "_id";
    public static final String COL_CAT_ID = "cat_id";
    public static final String COL_PROD_ID = "prod_id";

    private static final String DATABASE_CREATE = "CREATE TABLE if not exists " + TABLE_NAME + " " +
        "(" +
        "'" + COL_ID + "'" + " TEXT PRIMARY KEY, " +
        "'" + COL_CAT_ID + "'" + " TEXT," +
        "'" + COL_PROD_ID + "'" + " TEXT" +
        ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
