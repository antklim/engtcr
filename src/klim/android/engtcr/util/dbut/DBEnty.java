package klim.android.engtcr.util.dbut;

import java.util.HashMap;

import android.database.Cursor;
import android.database.SQLException;

public interface DBEnty {
	public String getNam();
	public String getCrt();
	public String getDrp();
	
	public long addRow(HashMap<String, String> pinargs);
	public boolean updRow(HashMap<String, String> pinargs, long prowuid);
	public boolean delRow(long prowuid);
	
	public Cursor fthRow(long prowuid) throws SQLException;	
	public Cursor fthRow(HashMap<String, String> pinargs) throws SQLException;	
	public Cursor fthAll();	
	
	public String getFlN(int pfldind);
	public int isVald(HashMap<String, String> pinargs);
}
