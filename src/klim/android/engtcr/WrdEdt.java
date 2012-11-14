package klim.android.engtcr;

import java.util.HashMap;

import klim.android.engtcr.util.MnMesg;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.dbut.DBUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WrdEdt extends Activity {

	Button btsavwrd;
	Button btcnswrd;
	EditText indctwrd;
	EditText inwrdtrl;

	HashMap<String, String> mcurwrd = 
		new HashMap<String, String>();
	HashMap<String, String> mcurtrn = 
		new HashMap<String, String>();
	HashMap<String, String> mcurwdt = 
		new HashMap<String, String>();
	
	private int mwrkmode = 0;
	private long _ID = 0;
	private int POS = -1;
	private long mtrn_ID = 0;
	private boolean merrcnf = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wded);
		
		btsavwrd = (Button) findViewById(R.id.btsavwrd);
		btcnswrd = (Button) findViewById(R.id.btcnswrd);
		indctwrd = (EditText) findViewById(R.id.indictwd);
		inwrdtrl = (EditText) findViewById(R.id.inwrdtrl);

		final AlertDialog.Builder mconfrm = new AlertDialog.Builder(this);
		mconfrm.setMessage(MnMesg.CONFRM).setCancelable(false)
				.setPositiveButton(MnMesg.CONFRM_SAVE,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									long mwrb_id, mtrn_id;
									if (mwrkmode == 0) {
										mwrb_id = DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY).addRow(mcurwrd);
										mtrn_id = DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY).addRow(mcurtrn);
										
										if (mwrb_id > 0 && mtrn_id > 0) {
											mcurwdt.put("wrd_id", String.valueOf(mwrb_id));
											mcurwdt.put("trn_id", String.valueOf(mtrn_id));											
											DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).addRow(mcurwdt);
										}
									} else { 
										DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY).updRow(mcurwrd, _ID);
										DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY).updRow(mcurtrn, mtrn_ID);
									}
									merrcnf = true;
								} catch (Exception e) {
									merrcnf = false;
									Toast.makeText(getBaseContext(),
											e.getMessage(), Toast.LENGTH_LONG).show();
								}
								finish();
							}
						})
				.setNegativeButton(MnMesg.CONFRM_CNCL,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		
		Bundle mextras = getIntent().getExtras();
		if (mextras != null) {
			mwrkmode = 1;
			_ID = mextras.getLong("_ID");
			POS = mextras.getInt("POS");
			
			DBUtil.DB_CURSOR.move(POS);
			indctwrd.setText(DBUtil.DB_CURSOR
					.getString(DBUtil.DB_CURSOR.getColumnIndexOrThrow( (DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY)).getFlN(1) )));
			
			HashMap<String, String> mtmpmap = new HashMap<String, String>();			
			mtmpmap.put("wrd_id", String.valueOf(_ID));
			Cursor mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).fthRow(mtmpmap);
			mtmpmap.clear();
			
			mtrn_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow( 
					DBUtil.getDbTbls().get(MnVars.TBL_WRDTRT_KEY).getFlN(2) ));

			mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY).fthRow( mtrn_ID );
				
			inwrdtrl.setText(mtmpcur.getString(mtmpcur.getColumnIndexOrThrow(DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY).getFlN(1))));
			
			mtmpcur.close();
		}
		
		btsavwrd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fillWd();
				fillTr();
				int misvalw = 
					DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY).isVald(mcurwrd);
				int misvalt = 
					DBUtil.getDbTbls().get(MnVars.TBL_WDTRAN_KEY).isVald(mcurtrn);
				if (misvalw < 0 || misvalt < 0) {
					switch(misvalw) {
						case -1:
							indctwrd.setError(MnMesg.WDE_DICTWD_NULL);
							break;
					}

					switch(misvalt) {
						case -1:
							inwrdtrl.setError(MnMesg.WDE_WRDTRL_NULL);
							break;
					}
				} else {
					AlertDialog m_alert = mconfrm.create();
					DBUtil.begTrn();					
					m_alert.show();
					if (merrcnf) DBUtil.cmtTrn();
					else DBUtil.rlbTrn();
				}
			}
		});
		
		btcnswrd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
	}
	
	private void fillWd() {
		this.mcurwrd.clear();
		this.mcurwrd.put("dctwrd", indctwrd.getText().toString().trim());
	}
	private void fillTr() {
		this.mcurtrn.clear();
		this.mcurtrn.put("wrdtrl", inwrdtrl.getText().toString().trim());
	}
}
