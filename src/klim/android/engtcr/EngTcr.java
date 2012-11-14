package klim.android.engtcr;

import klim.android.engtcr.ents.DctWrd;
import klim.android.engtcr.ents.IrrWrb;
import klim.android.engtcr.ents.WBTran;
import klim.android.engtcr.ents.WDTran;
import klim.android.engtcr.ents.WrbTrt;
import klim.android.engtcr.ents.WrdTrt;
import klim.android.engtcr.util.MnVars;
import klim.android.engtcr.util.dbut.DBUtil;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class EngTcr extends TabActivity {
    /** Called when the activity is first created. */
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
                
        dbInit();
        
        TabHost mtabhst = (TabHost)findViewById(android.R.id.tabhost);
        TabSpec mengwrb = mtabhst.newTabSpec(MnVars.TAB_WRBS);
        TabSpec mengdct = mtabhst.newTabSpec(MnVars.TAB_DICT);
        
        mengwrb.setIndicator(MnVars.TAB_WRBS_TXT).setContent(new Intent(this, EngWrb.class));
        mengdct.setIndicator(MnVars.TAB_DICT_TXT).setContent(new Intent(this, EngDct.class));
        mtabhst.addTab(mengwrb);
        mtabhst.addTab(mengdct);
    }
    
    private void dbInit() {
    	DBUtil.setDbName(MnVars.DB_NAME);        
        IrrWrb mirrwrb = new IrrWrb();
        WrbTrt mwrbtrt = new WrbTrt();
        WBTran mwbtran = new WBTran();
        DctWrd mdctwrd = new DctWrd();
        WDTran mwdtran = new WDTran();
        WrdTrt mwrdtrt = new WrdTrt();
        
        if (!DBUtil.getDbTbls().containsKey(mirrwrb.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_IRRWRB_KEY, mirrwrb);
        if (!DBUtil.getDbTbls().containsKey(mwrbtrt.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_WRBTRT_KEY, mwrbtrt);
        if (!DBUtil.getDbTbls().containsKey(mwbtran.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_WBTRAN_KEY, mwbtran);
        if (!DBUtil.getDbTbls().containsKey(mdctwrd.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_DCTWRD_KEY, mdctwrd);
        if (!DBUtil.getDbTbls().containsKey(mwdtran.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_WDTRAN_KEY, mwdtran);
        if (!DBUtil.getDbTbls().containsKey(mwrdtrt.getNam()))
        	DBUtil.getDbTbls().put(MnVars.TBL_WRDTRT_KEY, mwrdtrt);

    	DBUtil.setContxt(this);
    	DBUtil.genDbOper();
    	DBUtil.open();    	
    }
}