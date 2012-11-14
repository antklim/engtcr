package klim.android.engtcr.ents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.dbut.*;

public class IrrWrb implements DBEnty {
	private static final String TB_NAME = "irrwrb";	
	private static final String[] TB_FLDS = 
		{"_id", "infntv", "smppst", "pstprt"};
	
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
	public String getCrt() {
		return "create table " + TB_NAME + " (" +
			"_id integer primary key autoincrement, " +
			"infntv text not null, " +
			"smppst text not null, " +
			"pstprt text not null);";
	}

	@Override
	public String getDrp() {
		return "drop table if exists " + TB_NAME + ";";
	}

	@Override
	public String getNam() {
		return TB_NAME;
	}

	@Override
	public String getFlN(int pfldind) {
		return TB_FLDS[pfldind];
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

	public Cursor getRnd(int p_limit) {		
		
		if (p_limit < 1) return null;
		
		String mquercm = "SELECT a._id, a.infntv, a.smppst, a.pstprt, b.wrbtrl " +
						   "FROM irrwrb a, wbtran b, wrbtrt c " +
						  "WHERE c.wrb_id = a._id " +
						  	"AND b._id = c.trn_id " +
						  "ORDER BY RANDOM() " +
						  "LIMIT " + String.valueOf(p_limit);
		
		Cursor mcursor = DBUtil.gQuery(mquercm, null);

		if (mcursor != null) {
        	mcursor.moveToFirst();
        }
        return mcursor;
	}
	
	public static void loadWb(ArrayList<String[]> pinrows) {
		HashMap<String, String> mtmpmap = new HashMap<String, String>();
		String[] mtmparr;
		long mwrb_id, mtrn_id;
		
		for (int i = 0; i < pinrows.size(); i++) {
			mtmpmap.clear();
			mtmparr = pinrows.get(i);
			if (mtmparr.length == 4) {				
				try {
					DBUtil.begTrn();
					mtmpmap.put("infntv", mtmparr[0]);
					mtmpmap.put("smppst", mtmparr[1]);
					mtmpmap.put("pstprt", mtmparr[2]);
					mwrb_id = DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).addRow(mtmpmap);
					
					mtmpmap.clear();
					mtmpmap.put("wrbtrl", mtmparr[3]);
					mtrn_id = DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).addRow(mtmpmap);
					
					mtmpmap.clear();
					mtmpmap.put("wrb_id", String.valueOf(mwrb_id));
					mtmpmap.put("trn_id", String.valueOf(mtrn_id));
					DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).addRow(mtmpmap);
					DBUtil.cmtTrn();
				} catch (Exception e) {
					DBUtil.rlbTrn();
				}
			}
		}
	}
}
