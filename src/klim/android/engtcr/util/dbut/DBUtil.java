package klim.android.engtcr.util.dbut;

import java.util.ArrayList;
import java.util.HashMap;

import klim.android.engtcr.util.MnVars;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBUtil {
	public static Cursor DB_CURSOR;
	
	private static final int DB_VERS = 2;
	private static final String DBU_TAG = "DBUtil";
	private static Context DBU_CTX;
	
	private static String mdbname = "";
	private static ArrayList<String> mdbcrea = new ArrayList<String>();
	private static ArrayList<String> mdbdrop = new ArrayList<String>();		
	private static HashMap<Integer, DBEnty> mdbtbls = 
		new HashMap<Integer, DBEnty>();

	private static DbHlpr mdbhlpr;
	private static SQLiteDatabase mcurrdb; 
	
	private static class DbHlpr extends SQLiteOpenHelper {

		DbHlpr(Context context) {
			super(context, MnVars.DB_NAME, null, DB_VERS);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			for (int i = 0; i < mdbcrea.size(); i++)
				arg0.execSQL(mdbcrea.get(i));
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DBU_TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			for (int i = 0; i < mdbdrop.size(); i++)
				db.execSQL(mdbdrop.get(i));
            onCreate(db);
		}
	}
	
	public static void setContxt(Context pcontxt) {
		DBU_CTX = pcontxt;
	}	
	public static void open() throws SQLException {
		mdbhlpr = new DbHlpr(DBU_CTX);
		mcurrdb = mdbhlpr.getWritableDatabase();
	}
	public static void close() {
		mdbhlpr.close();
	}
	
	public static String getDbName() {
		return mdbname;
	}

	public static void setDbName(String pdbname) {
		DBUtil.mdbname = pdbname;
	}

	public static HashMap<Integer, DBEnty> getDbTbls() {
		return mdbtbls;
	}

	public static void setDbTbls(HashMap<Integer, DBEnty> pdbtbls) {
		mdbtbls = pdbtbls;
	}
	public static void genDbOper() {
		if (mdbtbls != null) {
			for (int i = 1; i <= mdbtbls.size(); i++) {
				mdbcrea.add(mdbtbls.get(i).getCrt().trim());
				mdbdrop.add(mdbtbls.get(i).getDrp().trim());				
			}
		}
	}
	public static SQLiteDatabase getCurrDb() {
		return mcurrdb;
	}	
	
	public static void begTrn() {
		mcurrdb.beginTransaction();
	}
	public static void cmtTrn() {
		mcurrdb.setTransactionSuccessful();
		mcurrdb.endTransaction();
	}
	public static void rlbTrn() {
		mcurrdb.endTransaction();
	}
	
	public static Cursor gQuery(String pquerst, String[] pquerar) {
		return mcurrdb.rawQuery(pquerst, pquerar);
	}
	
	public static void execRq(String pquerst, Object[] pquerar) {
		mcurrdb.execSQL(pquerst, pquerar);
	}
}
