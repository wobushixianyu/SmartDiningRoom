package com.david.smartdiningroom.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

/**
 * 操作数据库的工具类
 */
public class DBUtils {
    private static Context mContext;
    private static DBUtils instance;
    private static final String TABLE_NAME = "userInfo";
    private static final String CREATETABLE = "CREATE TABLE " + TABLE_NAME +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,password TEXT)";
    private SQLiteDatabase database;

    public static DBUtils getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            return instance = new DBUtils();
        } else {
            return instance;
        }
    }

    private DBUtils() {
        createTable();
    }

    /**
     * 创建数据库
     */
    private static void createTable() {
        boolean isExist = sqlTableIsExist(TABLE_NAME);
        System.out.println("=======>dbIsExist:"+isExist);
        if (!isExist) {
            SQLiteDatabase db = mContext.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
            db.execSQL(CREATETABLE);
            db.close();
        }
    }

    /**
     * 插入数据
     */
    public void insertData(String uName, String uPwd) {
        SQLiteDatabase db = mContext.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("username", uName);
        values.put("password", uPwd);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * 更新数据
     */
    public void updateData(String uName, String uPwd) {
        SQLiteDatabase db = mContext.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("username", uName);
        values.put("password", uPwd);
        db.update(TABLE_NAME, values, "_id=？", new String[]{"2"});
        db.close();
    }

    /**
     * 查询是否存在
     */
    public boolean queryIsExist(String userName) {
        System.out.println("======>queryIsExist");
        database = mContext.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        //查询部分数据
        String sql = "select * from " + TABLE_NAME + " where username=?";
        Cursor cursor = database.rawQuery(sql,new String[]{userName});
        Timber.i("======>count:"+cursor.getCount());
        return cursor.getCount() == 1;
    }

    /**
     * 查询账号用户名密码是否正确
     */
    public boolean queryAccountInfo(String uName,String uPwd) {
        SQLiteDatabase db = mContext.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        String sql = "select * from " + TABLE_NAME + " where username=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uName});
        cursor.moveToFirst();
        //获得对应的数据
        int pwdIndex = cursor.getColumnIndex("password");
        String password = cursor.getString(pwdIndex);
        int uNameIndex = cursor.getColumnIndex("username");
        String userName = cursor.getString(uNameIndex);
        Timber.i("======>uName:" +userName+ "======>password:"+password);
        db.close();
        return password.equals(uPwd);
    }

    /**
     * 判断该表是否存在
     */
    private static boolean sqlTableIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            //search.db数据库的名字
            db = mContext.openOrCreateDatabase("user.db", Context.MODE_PRIVATE, null);
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public void closeDatabase(){
        if (database != null){
            database.close();
        }
    }
}
