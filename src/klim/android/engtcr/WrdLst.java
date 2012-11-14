package klim.android.engtcr;

import java.io.IOException;
import java.util.HashMap;

import klim.android.engtcr.ents.DctWrd;
import klim.android.engtcr.util.MnMesg;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.TXTUtl;
import klim.android.engtcr.util.dbut.DBUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
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

public class WrdLst extends ListActivity {

	private static final int ADDWRD_ID = 1;
	private static final int LOADWD_ID = 2;
	private static final int EDTWRD_ID = 3;
	private static final int DELWRD_ID = 4;

	private static final int RQST_WADD = 0;
	private static final int RQST_WEDT = 1;
	
	private boolean merrcnf = false;
	private long mtrn_ID = 0;
	private long mwtr_ID = 0;

	ProgressDialog mdialog;
	
	Button btwdlsbk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdls);

		btwdlsbk = (Button) findViewById(R.id.btwdlsbk);
		
		fillDt();
		registerForContextMenu(getListView());
		
		btwdlsbk.setOnClickListener(new OnClickListener() {
			
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
		menu.add(0, ADDWRD_ID, 0, R.string.mnaddwrd);
		menu.add(0, LOADWD_ID, 0, R.string.mnloadwd);
		return mresult;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, EDTWRD_ID, 0, R.string.mnedtwrd);
		menu.add(0, DELWRD_ID, 0, R.string.mndelwrd);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case ADDWRD_ID:
				Intent mintent = new Intent(getBaseContext(), WrdEdt.class);					
				startActivityForResult(mintent, RQST_WADD);
				return true;
			case LOADWD_ID:
				showDialog(0);
				loadWd();
				fillDt();				
				mdialog.dismiss();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
			case EDTWRD_ID:
				Intent mintent = new Intent(getBaseContext(), WrdEdt.class);				
				mintent.putExtra("_ID", ((AdapterContextMenuInfo) item.getMenuInfo()).id);
				mintent.putExtra("POS", ((AdapterContextMenuInfo) item.getMenuInfo()).position);				
				startActivityForResult(mintent, RQST_WEDT);
												
				return true;
			case DELWRD_ID:
				final AlertDialog.Builder mconfrm = new AlertDialog.Builder(this);
				mconfrm.setMessage(MnMesg.CONFRM_DEL).setCancelable(false)
				.setPositiveButton(MnMesg.CONFRM_DELY,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									HashMap<String, String> mtmpmap = new HashMap<String, String>();			
									mtmpmap.put("wrd_id", String.valueOf(((AdapterContextMenuInfo) item.getMenuInfo()).id));
									Cursor mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).fthRow(mtmpmap);
									mtmpmap.clear();
									
									mwtr_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow(
											DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).getFlN(0) ));
									mtrn_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow( 
											DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).getFlN(2) ));
	
									DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY)
										.delRow(mwtr_ID);
									DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY)
										.delRow(((AdapterContextMenuInfo) item.getMenuInfo()).id);
									DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY)
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
		mdialog = new ProgressDialog(WrdLst.this);
		mdialog.setMessage(MnMesg.DIAG_LOAD);
		return mdialog;
	}
	
	private void fillDt() {
        DBUtil.DB_CURSOR = DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY).fthAll();
        startManagingCursor(DBUtil.DB_CURSOR);

        String[] from = new String[] { DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY).getFlN(1) };
        int[] to = new int[] { R.id.txwrdrow };
        
        try {
        	SimpleCursorAdapter mwrdlst =
	            new SimpleCursorAdapter(this, R.layout.wdrw, DBUtil.DB_CURSOR, from, to);
	        this.setListAdapter(mwrdlst);
        } catch (Exception e) {
        }
	}
	
	private void loadWd() {
		if (!TXTUtl.isExst(MnVars.DICT_FILE, 2))
			Toast.makeText(getBaseContext(),
					MnVars.DICT_FILE + ". " + MnMesg.DICT_FILE_NF, 
					Toast.LENGTH_LONG).show();		
		else {
			try {
				DctWrd.loadWd(TXTUtl.gtFILE(MnVars.DICT_FILE));
			} catch (IOException e) {
				Toast.makeText(getBaseContext(),
						e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
}
