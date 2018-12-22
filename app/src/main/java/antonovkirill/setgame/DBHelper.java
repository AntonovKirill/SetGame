package antonovkirill.setgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "tokens.db";
    public final static String TABLE_NAME = "tokens";
    public SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "nickname TEXT PRIMARY KEY," +
                "token INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addNicknameToken(String nickname, int token) {
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                "nickname, token" +
                ") " +
                "VALUES (" +
                String.format(Locale.US, "'%s', %d", nickname, token)+
                ");");
    }
}
