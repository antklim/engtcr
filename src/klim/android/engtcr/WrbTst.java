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

public class WrbTst extends Activity {

	private static final int RQST_WBTS = 0;

	Button btwrbstr;
	Button btbtstcn;
	EditText inwrbcnt;
	RadioButton rbwrbwrb;
	RadioButton rbwrbtrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wbts);
		
		btwrbstr = (Button) findViewById(R.id.btwrbstr);
		btbtstcn = (Button) findViewById(R.id.btbtstcn);
		inwrbcnt = (EditText) findViewById(R.id.inwrbcnt);
		rbwrbwrb = (RadioButton) findViewById(R.id.rbwrbwrb);
		rbwrbtrl = (RadioButton) findViewById(R.id.rbwrbtrl);
		
		inwrbcnt.setText("1");
		rbwrbwrb.setChecked(true);
		
		btwrbstr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isVald()) {
					Intent mintent = new Intent(getBaseContext(), WbTest.class);					
					mintent.putExtra("CNT", Integer.parseInt(inwrbcnt.getText().toString()));
					
					String mtsttyp;
					if (rbwrbwrb.isChecked()) mtsttyp = "ENG";
					else mtsttyp = "RUS";
						
					mintent.putExtra("TYP", mtsttyp);
					startActivityForResult(mintent, RQST_WBTS);
				}
			}
		});
		
		btbtstcn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent();
				setResult(RESULT_OK, mintent);
				finish();
			}
		});
	}
	
	private boolean isVald() {		
		if (inwrbcnt.getText().toString() == null) { 
			inwrbcnt.setError(MnMesg.WBT_WRBCNT_NULL);
			return false; 
		}
		
		try {
			int mtmpint = Integer.parseInt(inwrbcnt.getText().toString());
			if (mtmpint < 1) {
				inwrbcnt.setError(MnMesg.WBT_WRBCNT_CORR);
				return false;
			}
		} catch (Exception e) {
			inwrbcnt.setError(MnMesg.WBT_WRBCNT_TYPE);
			return false;
		}
		
		if (!rbwrbwrb.isChecked() && !rbwrbtrl.isChecked()) {
			Toast.makeText(getBaseContext(),
					MnMesg.WBT_RBTGRP_NOHI, Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (rbwrbwrb.isChecked() && rbwrbtrl.isChecked()) {
			Toast.makeText(getBaseContext(),
					MnMesg.WBT_RBTGRP_ALLH, Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
}
