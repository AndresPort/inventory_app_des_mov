package com.andresport.app_inventory.DB;

import static androidx.compose.runtime.ComposeVersion.version;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConnection extends SQLiteOpenHelper {

    // nombre de la base de datos
    private static final String DB_NAME = "db_app_inventory";
    private static final  int DB_VERSION = 1;

    public DBConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    /*esta función solo se ejecuta una vez, que es en el caso de que la base de datos se
    crea y se almacena en el dispositivo*/
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL CODE
        sqLiteDatabase.execSQL(DBManager.CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //SQL CODE
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DBManager.TABLE_NAME);
    }
}
