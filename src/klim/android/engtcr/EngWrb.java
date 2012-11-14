package klim.android.engtcr;

import klim.android.engtcr.util.dbut.DBUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EngWrb extends Activity {

	private static final int RQST_WBLS = 0;
	private static final int RQST_WBTS = 1;
	
	Button btwrblst;
	Button btwrbbeg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ewrb);		
		
        btwrblst = (Button)findViewById(R.id.btwrblst);
        btwrbbeg = (Button)findViewById(R.id.btwrbbeg);
        
        btwrblst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent(getBaseContext(), WrbLst.class);					
				startActivityForResult(mintent, RQST_WBLS);				
			}						
		});
        
        btwrbbeg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent(getBaseContext(), WrbTst.class);					
				startActivityForResult(mintent, RQST_WBTS);
			}
		});               
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RQST_WBLS) {
			if (DBUtil.DB_CURSOR != null && !DBUtil.DB_CURSOR.isClosed()) 
				DBUtil.DB_CURSOR.close();							
		}
	}
}
