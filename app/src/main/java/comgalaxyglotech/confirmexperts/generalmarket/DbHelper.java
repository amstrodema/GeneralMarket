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
            String type = x.getDataType();
            ContentValues contentValues = new ContentValues();
            contentValues.put("settingName",x.getSettingName());
            contentValues.put("dataType", type);
            switch (type) {
                case "text":
                    contentValues.put("valueText", x.getValueText());
                    break;
                case "double":
                    contentValues.put("valueInt", x.getValueDouble());
                    break;
                case "int":
                    contentValues.put("valueDouble", x.getValueInt());
                    break;
            }
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
            String type = x.getDataType();
            contentValues.put("settingName",x.getSettingName());
            contentValues.put("dataType", type);
            switch (type) {
                case "text":
                    contentValues.put("valueText", x.getValueText());
                    break;
                case "double":
                    contentValues.put("valueInt", x.getValueDouble());
                    break;
                case "int":
                    contentValues.put("valueDouble", x.getValueInt());
                    break;
            }
            long result = DB.insert("appSettings", null, contentValues);
            if(result == -1)
                isDone =false;
        }
        return isDone;
    }
    public ArrayList<String> getAppSettings(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from appSettings", null);

        ArrayList<String> appSetting = new ArrayList<>();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                StringBuffer buffer = new StringBuffer();
                buffer.append(cursor.getString(0)+"\n");
                String type = cursor.getString(4);
                switch (type) {
                    case "text":
                        buffer.append(cursor.getString(1)+"\n");
                        break;
                    case "double":
                        buffer.append(cursor.getString(3)+"\n");
                        break;
                    case "int":
                        buffer.append(cursor.getString(2)+"\n");
                        break;
                }
                appSetting.add(buffer.toString());
            }
        }
        cursor.close();
        return appSetting;
    }
    public ArrayList<SettingsModel> getUserDetails(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userDetails", null);

        ArrayList<SettingsModel> appSetting = new ArrayList<>();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                SettingsModel model = new SettingsModel();
                model.setDataType(cursor.getString(4));
                model.setSettingName(cursor.getString(0));
                model.setValueText(cursor.getString(1));
                model.setValueDouble(Double.parseDouble(cursor.getString(3)));
                model.setValueInt(Integer.parseInt(cursor.getString(2)));
            /*  if(cursor.getString(0).equals("dateUpdated")){
                   if( modelClass.isDbStale(cursor.getString(1)))
                   {

                       break;
                   }
                }*/
                appSetting.add(model);
            }
        }
        cursor.close();
        return appSetting;
    }
}
