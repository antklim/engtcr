package klim.android.engtcr.ents;

import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import klim.android.engtcr.util.dbut.DBEnty;
import klim.android.engtcr.util.dbut.DBUtil;

public class WrdTrt implements DBEnty {
	private static final String TB_NAME = "wrdtrt";	
	private static final String[] TB_FLDS = 
		{"_id", "wrd_id", "trn_id"};

	@Override
	public long addRow(HashMap<String, String> pinargs) {
		String mtmpval = null;
        ContentValues minitvl = new ContentValues();
        for (int i = 1; i < TB_FLDS.length; i++) {
        	mtmpval = pinargs.get(TB_FLDS[i]);
        	if (mtmpval == null) return -1;
        	minitvl.put(TB_FLDS[i], mtmpval);
        	mtmpval = null;
        }
        return DBUtil.getCurrDb().insert(TB_NAME, null, minitvl);
	}

	@Override
	public boolean delRow(long prowuid) {
		return DBUtil.getCurrDb().delete(TB_NAME, 
				TB_FLDS[0] + "=" + prowuid, null) > 0;
	}

	@Override
	public Cursor fthAll() {
        return DBUtil.getCurrDb().query(TB_NAME, TB_FLDS, null, null, 
        		null, null, TB_FLDS[1]);
	}

	@Override
	public Cursor fthRow(long prowuid) throws SQLException {
        Cursor mcursor = 
        	DBUtil.getCurrDb().query(true, TB_NAME, TB_FLDS, TB_FLDS[0] + "=" + prowuid, 
        			null, null, null, TB_FLDS[1], null);
        
        if (mcursor != null) {
        	mcursor.moveToFirst();
        }
        return mcursor;
	}

	@Override
	public Cursor fthRow(HashMap<String, String> pinargs) throws SQLException {
		String mswhere = "";
		
		Iterator<?> mitratr = pinargs.entrySet().iterator();
		while(mitratr.hasNext()){			
			mswhere += mitratr.next();
			if (mitratr.hasNext()) mswhere += ", ";
		}
		Cursor mcursor = 
        	DBUtil.getCurrDb().query(true, TB_NAME, TB_FLDS, mswhere, 
        			null, null, null, TB_FLDS[1], null);
        
        if (mcursor != null) {
        	mcursor.moveToFirst();
        }
        return mcursor;
	}

	@Override
	public String getCrt() {
		return "create table " + TB_NAME + " (" +
		"_id integer primary key autoincrement, " +
		"wrd_id integer not null, " +
		"trn_id integer not null);";
	}

	@Override
	public String getDrp() {
		return "drop table if exists " + TB_NAME + ";";
	}

	@Override
	public String getFlN(int pfldind) {
		return TB_FLDS[pfldind];
	}

	@Override
	public String getNam() {
		return TB_NAME;
	}

	@Override
	public int isVald(HashMap<String, String> pinargs) {
		String mtmpval = null;		
        for (int i = 1; i < TB_FLDS.length; i++) {
        	mtmpval = pinargs.get(TB_FLDS[i]);
        	if (mtmpval == null || mtmpval.equalsIgnoreCase("")) return -i;
        	mtmpval = null;
        }
		return 0;
	}

	@Override
	public boolean updRow(HashMap<String, String> pinargs, long prowuid) {
		String mtmpval = null;
        ContentValues minitvl = new ContentValues();
        for (int i = 1; i < TB_FLDS.length; i++) {
        	mtmpval = pinargs.get(TB_FLDS[i]);
        	if (mtmpval == null) return false;
        	minitvl.put(TB_FLDS[i], mtmpval);
        	mtmpval = null;
        }
        return DBUtil.getCurrDb().update(TB_NAME, minitvl, 
        		TB_FLDS[0] + "=" + prowuid, null) > 0;
	}

}
