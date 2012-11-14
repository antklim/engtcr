package klim.android.engtcr;

import java.io.IOException;
import java.util.HashMap;

import klim.android.engtcr.ents.IrrWrb;
import klim.android.engtcr.util.MnMesg;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.TXTUtl;
import klim.android.engtcr.util.dbut.DBUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
//import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WrbLst extends ListActivity {

	private static final int ADDWRB_ID = 1;
	private static final int LOADWB_ID = 2;
	private static final int EDTWRB_ID = 3;
	private static final int DELWRB_ID = 4;
	
	private static final int RQST_WADD = 0;
	private static final int RQST_WEDT = 1;

	private boolean merrcnf = false;
	private long mtrn_ID = 0;
	private long mwtr_ID = 0;
	
	ProgressDialog mdialog;
	
	Button btwblsbk;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wbls);
		
		btwblsbk = (Button) findViewById(R.id.btwblsbk);
		
		fillDt();
		registerForContextMenu(getListView());
		
		btwblsbk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean mresult = super.onCreateOptionsMenu(menu);
		menu.add(0, ADDWRB_ID, 0, R.string.mnaddwrb);
		menu.add(0, LOADWB_ID, 0, R.string.mnloadwb);
		return mresult;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, EDTWRB_ID, 0, R.string.mnedtwrb);
		menu.add(0, DELWRB_ID, 0, R.string.mndelwrb);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case ADDWRB_ID:
				Intent mintent = new Intent(getBaseContext(), WrbEdt.class);					
				startActivityForResult(mintent, RQST_WADD);
				return true;
			case LOADWB_ID:
				showDialog(0);
				loadWb();
				fillDt();				
				mdialog.dismiss();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
			case EDTWRB_ID:
				Intent mintent = new Intent(getBaseContext(), WrbEdt.class);				
				mintent.putExtra("_ID", ((AdapterContextMenuInfo) item.getMenuInfo()).id);
				mintent.putExtra("POS", ((AdapterContextMenuInfo) item.getMenuInfo()).position);				
				startActivityForResult(mintent, RQST_WEDT);
												
				return true;
			case DELWRB_ID:
				final AlertDialog.Builder mconfrm = new AlertDialog.Builder(this);
				mconfrm.setMessage(MnMesg.CONFRM_DEL).setCancelable(false)
				.setPositiveButton(MnMesg.CONFRM_DELY,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									HashMap<String, String> mtmpmap = new HashMap<String, String>();			
									mtmpmap.put("wrb_id", String.valueOf(((AdapterContextMenuInfo) item.getMenuInfo()).id));
									Cursor mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).fthRow(mtmpmap);
									mtmpmap.clear();
									
									mwtr_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow(
											DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).getFlN(0) ));
									mtrn_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow( 
											DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).getFlN(2) ));

									DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY)
										.delRow(mwtr_ID);
									DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY)
										.delRow(((AdapterContextMenuInfo) item.getMenuInfo()).id);
									DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY)
										.delRow(mtrn_ID);
									
									merrcnf = true;
								} catch (Exception e) {
									merrcnf = false;
									Toast.makeText(getBaseContext(),
											e.getMessage(), Toast.LENGTH_LONG).show();
								}
								fillDt();
							}
						})
				.setNegativeButton(MnMesg.CONFRM_DELC,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog m_alert = mconfrm.create();
				DBUtil.begTrn();
				m_alert.show();
				if (merrcnf) DBUtil.cmtTrn();
				else DBUtil.rlbTrn();
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mdialog = new ProgressDialog(WrbLst.this);
		mdialog.setMessage(MnMesg.DIAG_LOAD);
		return mdialog;
	}

	private void fillDt() {
        DBUtil.DB_CURSOR = DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).fthAll();
        startManagingCursor(DBUtil.DB_CURSOR);

        String[] from = new String[] { DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).getFlN(1) };
        int[] to = new int[] { R.id.txwrbrow };
        
        try {
        	SimpleCursorAdapter mwrblst =
	            new SimpleCursorAdapter(this, R.layout.wbrw, DBUtil.DB_CURSOR, from, to);
	        this.setListAdapter(mwrblst);
        } catch (Exception e) {
        }
	}	

	private void loadWb() {
		if (!TXTUtl.isExst(MnVars.IRRW_FILE, 2))
			Toast.makeText(getBaseContext(),
					MnVars.IRRW_FILE + ". " + MnMesg.IRRW_FILE_NF, 
					Toast.LENGTH_LONG).show();		
		else {
			try {
				IrrWrb.loadWb(TXTUtl.gtFILE(MnVars.IRRW_FILE));
			} catch (IOException e) {
				Toast.makeText(getBaseContext(),
						e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
}
