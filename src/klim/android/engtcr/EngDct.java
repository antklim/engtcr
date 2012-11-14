package klim.android.engtcr;

import klim.android.engtcr.util.dbut.DBUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EngDct extends Activity {

	private static final int RQST_WDLS = 0;
	private static final int RQST_WDTS = 1;

	Button btwrdlst;
	Button btdctbeg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edct);
		
		btwrdlst = (Button)findViewById(R.id.btwrdlst);
		btdctbeg = (Button)findViewById(R.id.btdctbeg);
		
		btwrdlst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent(getBaseContext(), WrdLst.class);					
				startActivityForResult(mintent, RQST_WDLS);
			}
		});
        
		btdctbeg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mintent = new Intent(getBaseContext(), WrdTst.class);					
				startActivityForResult(mintent, RQST_WDTS);
			}
		});		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RQST_WDLS) {
			if (DBUtil.DB_CURSOR != null && !DBUtil.DB_CURSOR.isClosed()) 
				DBUtil.DB_CURSOR.close();							
		}
	}
}
