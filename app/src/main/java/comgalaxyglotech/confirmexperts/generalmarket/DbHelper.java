package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private ModelClass modelClass = new ModelClass();
    public DbHelper(Context context) {
        super(context, "MarketDb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table appSettings( settingName TEXT, valueText TEXT, valueInt INT, valueDouble Double, dataType TEXT)");
        DB.execSQL("create Table userDetails( settingName TEXT, valueText TEXT, valueInt INT, valueDouble Double, dataType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists appSettings");
        DB.execSQL("drop Table if exists userDetails");
    }
    public boolean insertUserDetails(ArrayList<SettingsModel> userDetails){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.delete("userDetails",null,  null);
        boolean isDone = true;
        for (SettingsModel x: userDetails) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("settingName",x.getSettingName());
            if(!x.getValueText().isEmpty())
                contentValues.put("valueText",x.getValueText());
            else if(x.getValueDouble() != 0)
                contentValues.put("valueInt",x.getValueDouble());
            else if(x.getValueInt() != 0)
                contentValues.put("valueDouble",x.getValueInt());
           long result = DB.insert("userDetails", null, contentValues);
           if(result == -1)
               isDone =false;
        }
        return isDone;
    }
    public boolean insertAppSettings(ArrayList<SettingsModel> userDetails){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.delete("appSettings",null,  null);
        boolean isDone = true;
        for (SettingsModel x: userDetails) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("settingName",x.getSettingName());
            if(!x.getValueText().isEmpty())
                contentValues.put("valueText",x.getValueText());
            else if(x.getValueDouble() != 0)
                contentValues.put("valueInt",x.getValueDouble());
            else if(x.getValueInt() != 0)
                contentValues.put("valueDouble",x.getValueInt());
            long result = DB.insert("appSettings", null, contentValues);
            if(result == -1)
                isDone =false;
        }
        return isDone;
    }
    public Cursor getUserData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userDetails", null);
        cursor.close();
        return cursor;
    }
    public Cursor getAppSettings(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from appSettings", null);
        cursor.close();
        return cursor;
    }
}
