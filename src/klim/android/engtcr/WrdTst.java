package klim.android.engtcr;

import klim.android.engtcr.util.MnMesg;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class WrdTst extends Activity {

	private static final int RQST_WBTS = 0;

	Button btwrdstr;
	Button btdtstcn;
	EditText inwrdcnt;
	RadioButton rbwrdwrd;
	RadioButton rbwrdtrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdts);
		
		btwrdstr = (Button) findViewById(R.id.btwrdstr);
		btdtstcn = (Button) findViewById(R.id.btdtstcn);
		inwrdcnt = (EditText) findViewById(R.id.inwrdcnt);
		rbwrdwrd = (RadioButton) findViewById(R.id.rbwrdwrd);
		rbwrdtrl = (RadioButton) findViewById(R.id.rbwrdtrl);
		
		inwrdcnt.setText("1");
		rbwrdwrd.setChecked(true);
		
		btwrdstr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isVald()) {
					Intent mintent = new Intent(getBaseContext(), WdTest.class);					
					mintent.putExtra("CNT", Integer.parseInt(inwrdcnt.getText().toString()));
					
					String mtsttyp;
					if (rbwrdwrd.isChecked()) mtsttyp = "ENG";
					else mtsttyp = "RUS";
						
					mintent.putExtra("TYP", mtsttyp);
					startActivityForResult(mintent, RQST_WBTS);
				}
			}
		});
		
		btdtstcn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
	}
	
	private boolean isVald() {		
		if (inwrdcnt.getText().toString() == null) { 
			inwrdcnt.setError(MnMesg.WDT_WRDCNT_NULL);
			return false; 
		}
		
		try {
			int mtmpint = Integer.parseInt(inwrdcnt.getText().toString());
			if (mtmpint < 1) {
				inwrdcnt.setError(MnMesg.WDT_WRDCNT_CORR);
				return false;
			}
		} catch (Exception e) {
			inwrdcnt.setError(MnMesg.WDT_WRDCNT_TYPE);
			return false;
		}
		
		if (!rbwrdwrd.isChecked() && !rbwrdtrl.isChecked()) {
			Toast.makeText(getBaseContext(),
					MnMesg.WDT_RBTGRP_NOHI, Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (rbwrdwrd.isChecked() && rbwrdtrl.isChecked()) {
			Toast.makeText(getBaseContext(),
					MnMesg.WDT_RBTGRP_ALLH, Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
}
