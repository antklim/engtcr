package klim.android.engtcr;

import klim.android.engtcr.ents.DctWrd;
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

public class WdTest extends Activity {

	private int CNT = 0;	
	private String TYP = "";

	private int m_id; 
	private String mdctwrd, mwrdtrl;	
	private int mcorcnt = 0;
	private int merrcnt = 0;
	private Cursor mtmpcur;
	
	Button btwrdnxt;
	Button btwrdcnc;
	EditText indctwrd;
	EditText inwrdtrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dtst);
		
		btwrdnxt = (Button) findViewById(R.id.btwrdnxt);
		btwrdcnc = (Button) findViewById(R.id.btwrdcnc);						
		indctwrd = (EditText) findViewById(R.id.indctwrd);
		inwrdtrl = (EditText) findViewById(R.id.inwrdtrl);
		
		btwrdnxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nxtWrb();
			}
		});
		
		btwrdcnc.setOnClickListener(new OnClickListener() {
			
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
			
			mtmpcur = ((DctWrd)DBUtil.getDbTbls().get(MnVars.TBL_DCTWRD_KEY)).getRnd(CNT); 
			
			if (mtmpcur == null) {
				btwrdnxt.setEnabled(false);
				return;
			}
			
			m_id = mtmpcur.getInt(mtmpcur.getColumnIndexOrThrow("_id")); 
			mdctwrd = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("dctwrd"));
			mwrdtrl = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("wrdtrl")); 
			
			if (TYP.equals("ENG")) {
				indctwrd.setText(mdctwrd);
				inwrdtrl.setText("");
			} else {
				indctwrd.setText("");
				inwrdtrl.setText(mwrdtrl);
			}
		}
	}
	
	private void nxtWrb() {
		if (mdctwrd.equalsIgnoreCase(indctwrd.getText().toString()) &&
			mwrdtrl.equalsIgnoreCase(inwrdtrl.getText().toString())) mcorcnt++;			
		else {
			merrcnt++;
			if (!mdctwrd.equalsIgnoreCase(indctwrd.getText().toString())) 
				indctwrd.setError("Error");
			if (!mwrdtrl.equalsIgnoreCase(inwrdtrl.getText().toString())) 
				inwrdtrl.setError("Error");
		}
			
		CNT--;
		
		if (CNT > 0) {
			mtmpcur.moveToNext();
			m_id = mtmpcur.getInt(mtmpcur.getColumnIndexOrThrow("_id")); 
			mdctwrd = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("dctwrd"));
			mwrdtrl = mtmpcur.getString(mtmpcur.getColumnIndexOrThrow("wrdtrl")); 
			
			if (TYP.equals("ENG")) {
				indctwrd.setText(mdctwrd);
				inwrdtrl.setText("");
			} else {
				indctwrd.setText("");
				inwrdtrl.setText(mwrdtrl);
			}
		} else {
			String mmesage = "Тест окончен\nПравильных ответов: " + mcorcnt + "\n" + 
				"Ошибочных ответов: " + merrcnt;
			Toast.makeText(getBaseContext(),
					mmesage, Toast.LENGTH_LONG).show();
			btwrdnxt.setEnabled(false);
			Intent mintent = new Intent();
			setResult(RESULT_OK, mintent);
			finish();
		}
	}
}
