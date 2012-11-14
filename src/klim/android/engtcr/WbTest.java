package klim.android.engtcr;

import klim.android.engtcr.ents.IrrWrb;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.dbut.DBUtil;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WbTest extends Activity {

	private int CNT = 0;	
	private String TYP = "";

	private int m_id; 
	private String minfntv, msmppst, mpstprt, mwrbtrl;	
	private int mcorcnt = 0;
	private int merrcnt = 0;
	private Cursor mtmpcur;
	
	Button btwrbnxt;
	Button btwrbcnc;
	EditText ininftst;
	EditText insmptst;
	EditText inpsttst;
	EditText inwrbtst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.btst);
		
		btwrbnxt = (Button) findViewById(R.id.btwrbnxt);
		btwrbcnc = (Button) findViewById(R.id.btwrbcnc);						
		ininftst = (EditText) findViewById(R.id.ininftst);
		insmptst = (EditText) findViewById(R.id.insmptst);
		inpsttst = (EditText) findViewById(R.id.inpsttst);
		inwrbtst = (EditText) findViewById(R.id.inwrbtst);
		
		btwrbnxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nxtWrb();
			}
		});
		
		btwrbcnc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
		
		Bundle mextras = getIntent().getExtras();
		if (mextras != null) {
			CNT = mextras.getInt("CNT");
			TYP = mextras.getString("TYP");
			getTst(CNT, TYP);
		}
	}	
	
	private void getTst(int pwrbcnt, String ptsttyp) {
		if (CNT > 0 && (TYP.equals("ENG") || TYP.equals("RUS"))) {
			
			mtmpcur = ((IrrWrb)DBUtil.getDbTbls().get(MnVars.TBL_IRRWRB_KEY)).getRnd(CNT); 
			
			if (mtmpcur == null) {
				btwrbnxt.setEnabled(false);
				return;
			}
			
			m_id = mtmpcur.getInt(mtmpcur.getColumnIndexOrThrow("_id")); 
			minfntv = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("infntv"));
			msmppst = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("smppst")); 
			mpstprt = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("pstprt")); 
			mwrbtrl = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("wrbtrl"));
			
			if (TYP.equals("ENG")) {
				ininftst.setText(minfntv);
				insmptst.setText("");
				inpsttst.setText("");
				inwrbtst.setText("");
			} else {
				ininftst.setText("");
				insmptst.setText("");
				inpsttst.setText("");
				inwrbtst.setText(mwrbtrl);
			}
		}
	}
	
	private void nxtWrb() {
		if (minfntv.equalsIgnoreCase(ininftst.getText().toString()) &&
			msmppst.equalsIgnoreCase(insmptst.getText().toString()) &&
			mpstprt.equalsIgnoreCase(inpsttst.getText().toString()) &&
			mwrbtrl.equalsIgnoreCase(inwrbtst.getText().toString())) mcorcnt++;			
		else {
			merrcnt++;
			if (!minfntv.equalsIgnoreCase(ininftst.getText().toString())) 
				ininftst.setError("Error");
			if (!msmppst.equalsIgnoreCase(insmptst.getText().toString())) 
				insmptst.setError("Error");
			if (!mpstprt.equalsIgnoreCase(inpsttst.getText().toString())) 
				inpsttst.setError("Error");
			if (!mwrbtrl.equalsIgnoreCase(inwrbtst.getText().toString())) 
				inwrbtst.setError("Error");
		}
			
		CNT--;
		
		if (CNT > 0) {
			mtmpcur.moveToNext();
			m_id = mtmpcur.getInt(mtmpcur.getColumnIndexOrThrow("_id")); 
			minfntv = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("infntv"));
			msmppst = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("smppst")); 
			mpstprt = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("pstprt")); 
			mwrbtrl = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("wrbtrl"));
			
			if (TYP.equals("ENG")) {
				ininftst.setText(minfntv);
				insmptst.setText("");
				inpsttst.setText("");
				inwrbtst.setText("");
			} else {
				ininftst.setText("");
				insmptst.setText("");
				inpsttst.setText("");
				inwrbtst.setText(mwrbtrl);
			}
		} else {
			String mmesage = "Тест окончен\nПравильных ответов: " + mcorcnt + "\n" + 
				"Ошибочных ответов: " + merrcnt;
			Toast.makeText(getBaseContext(),
					mmesage, Toast.LENGTH_LONG).show();
			btwrbnxt.setEnabled(false);
			Intent mintent = new Intent();
			setResult(RESULT_OK, mintent);
			finish();
		}
	}
}
