package comgalaxyglotech.confirmexperts.generalmarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "myMarketDb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table UserDetails(id TEXT primary key, userID TEXT, fName TEXT, lName TEXT, email TEXT, phone TEXT, isMarketCleaning BIT, isAppForceUpdate BIT, storeCost DOUBLE, transactionFee FLOAT, lastUpdateDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {

    }
}
