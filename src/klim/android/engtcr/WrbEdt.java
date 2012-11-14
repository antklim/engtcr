package klim.android.engtcr;

import java.util.HashMap;

import klim.android.engtcr.util.*;
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

public class WrbEdt extends Activity {

	Button btsavwrb;
	Button btcnswrb;
	EditText ininfntv;
	EditText insmppst;
	EditText inpstprt;
	EditText inwrbtrl;

	HashMap<String, String> mcurwrb = 
		new HashMap<String, String>();
	HashMap<String, String> mcurtrn = 
		new HashMap<String, String>();
	HashMap<String, String> mcurwbt = 
		new HashMap<String, String>();
	
	private int mwrkmode = 0;
	private long _ID = 0;
	private int POS = -1;
	private long mtrn_ID = 0;
	private boolean merrcnf = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wbed);
		
		btsavwrb = (Button) findViewById(R.id.btsavwrb);
		btcnswrb = (Button) findViewById(R.id.btcnswrb);
		ininfntv = (EditText) findViewById(R.id.ininfntv);
		insmppst = (EditText) findViewById(R.id.insmppst);
		inpstprt = (EditText) findViewById(R.id.inpstprt);
		inwrbtrl = (EditText) findViewById(R.id.inwrbtrl);

		final AlertDialog.Builder mconfrm = new AlertDialog.Builder(this);
		mconfrm.setMessage(MnMesg.CONFRM).setCancelable(false)
				.setPositiveButton(MnMesg.CONFRM_SAVE,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									long mwrb_id, mtrn_id;
									if (mwrkmode == 0) {
										mwrb_id = DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).addRow(mcurwrb);
										mtrn_id = DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).addRow(mcurtrn);
										
										if (mwrb_id > 0 && mtrn_id > 0) {
											mcurwbt.put("wrb_id", String.valueOf(mwrb_id));
											mcurwbt.put("trn_id", String.valueOf(mtrn_id));											
											DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).addRow(mcurwbt);
										}
									} else { 
										DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).updRow(mcurwrb, _ID);
										DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).updRow(mcurtrn, mtrn_ID);
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
			ininfntv.setText(DBUtil.DB_CURSOR
					.getString(DBUtil.DB_CURSOR.getColumnIndexOrThrow( (DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY)).getFlN(1) )));
			insmppst.setText(DBUtil.DB_CURSOR
					.getString(DBUtil.DB_CURSOR.getColumnIndexOrThrow( (DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY)).getFlN(2) )));
			inpstprt.setText(DBUtil.DB_CURSOR
					.getString(DBUtil.DB_CURSOR.getColumnIndexOrThrow( (DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY)).getFlN(3) )));

			HashMap<String, String> mtmpmap = new HashMap<String, String>();			
			mtmpmap.put("wrb_id", String.valueOf(_ID));
			Cursor mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).fthRow(mtmpmap);
			mtmpmap.clear();
			
			mtrn_ID = mtmpcur.getLong(mtmpcur.getColumnIndexOrThrow( 
					DBUtil.getDbTbls().get(MnVars.TBL_WRBTRT_KEY).getFlN(2) ));
			
			mtmpcur = DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).fthRow( mtrn_ID );
				
			inwrbtrl.setText(mtmpcur.getString(mtmpcur.getColumnIndexOrThrow(DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).getFlN(1))));
			
			mtmpcur.close();
		}
		
		btsavwrb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fillWb();
				fillTr();
				int misvalw = 
					DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY).isVald(mcurwrb);
				int misvalt = 
					DBUtil.getDbTbls().get(MnVars.TBL_WBTRAN_KEY).isVald(mcurtrn);
				if (misvalw < 0 || misvalt < 0) {
					switch(misvalw) {
						case -1:
							ininfntv.setError(MnMesg.WBE_INFNTV_NULL);
							break;
						case -2:
							insmppst.setError(MnMesg.WBE_SMPPST_NULL);
							break;
						case -3:
							inpstprt.setError(MnMesg.WBE_PSTPRT_NULL);
							break;
					}

					switch(misvalt) {
						case -1:
							inwrbtrl.setError(MnMesg.WBE_WRBTRL_NULL);
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
		
		btcnswrb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
	}
	
	private void fillWb() {
		this.mcurwrb.clear();
		this.mcurwrb.put("infntv", ininfntv.getText().toString().trim());
		this.mcurwrb.put("smppst", insmppst.getText().toString().trim());
		this.mcurwrb.put("pstprt", inpstprt.getText().toString().trim());
	}
	private void fillTr() {
		this.mcurtrn.clear();
		this.mcurtrn.put("wrbtrl", inwrbtrl.getText().toString().trim());
	}
}
