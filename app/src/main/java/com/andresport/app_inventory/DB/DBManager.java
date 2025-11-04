package com.andresport.app_inventory.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.andresport.app_inventory.Models.Product;

import java.util.ArrayList;

public class DBManager {
    //PRODUCT TABLE
    // los nombres de los atributos deben ser en snake_case, porque facilita las cosas para postgres

    /* se deben cambiar los valores
      por los datos que ingresa el usuario desde las vistas */

    // nombre de la tabla
    public static final String TABLE_NAME = "product";

    //columnas
    public static final String PRODUCT_REF = "product_ref";
    public static final String PRODUCT_NAME = "product_name";
    public static final String UNIT_PRICE = "unit_price";
    public static final String STOCK = "stock";

    //Query para crear la tabla product
    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + TABLE_NAME + "( " + PRODUCT_REF + " TEXT PRIMARY KEY ," +
                    PRODUCT_NAME + " TEXT NOT NULL,"+
                    UNIT_PRICE + " NUMERIC(10,2) NOT NULL,"+
                    STOCK + " INTEGER NOT NULL);";


    //atributo para la conexión a la base de datos
    private DBConnection _connection;
    private SQLiteDatabase _database;

    //constructor
    public DBManager(Context context) {
        _connection = new DBConnection(context);
    }

    public DBManager open() throws SQLException{
        _database = _connection.getWritableDatabase();
        return  this;

    }
    public void  close(){
        _connection.close();
    }


    // ======================================
    // CRUD METHODS
    // ======================================

    //Create
    public long CreateProduct (Product product){
        ContentValues values= new ContentValues();
        values.put(PRODUCT_REF, product.getProductRef());
        values.put(PRODUCT_NAME, product.getProductName());
        values.put(UNIT_PRICE, product.getUnitPrice());
        values.put(STOCK, product.getStock());

        return _database.insert(TABLE_NAME, null, values);
    }

    //Get All Products
    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> productList = new ArrayList<>();
        Cursor cursor = _database.query(TABLE_NAME, null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                Product product = new Product();
                product.setProductRef(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_REF)));
                product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
                product.setUnitPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(UNIT_PRICE)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(STOCK)));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    //Get Product By Ref
    public Product getProductByReg(String ref){
        Product product = null;
        String selection = PRODUCT_REF + "=?";
        String[] selectionArgs = {ref};
        Cursor cursor = _database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()){
            product = new Product();
            product.setProductRef(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_REF)));
            product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)));
            product.setUnitPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(UNIT_PRICE)));
            product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(STOCK)));
        }
        cursor.close();
        return product;
    }

    //UpdateProduct

    public int UpdateProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getProductName());
        values.put(UNIT_PRICE, product.getUnitPrice());
        values.put(STOCK, product.getStock());

        String whereClause = PRODUCT_REF + "=?";
        String[] whereArgs = {product.getProductRef()};

        return  _database.update(TABLE_NAME, values,whereClause,whereArgs);
    }

    //DeleteProductByRef

    public int DeleteProductByRef(String productRef){
        String whereClause = PRODUCT_REF+ "=?";
        String[] whereArgs = {productRef};

        return _database.delete(TABLE_NAME,whereClause,whereArgs);
    }

}
