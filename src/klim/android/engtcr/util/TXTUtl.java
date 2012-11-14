package klim.android.engtcr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TXTUtl {

	public static boolean isExst(String pfilpth, int pwrkmod) {
		boolean mresult = false;

		if (pfilpth == null || pfilpth.equalsIgnoreCase(""))
			return mresult;

		File mtmpfil = new File(pfilpth);

		switch (pwrkmod) {
		case 1:
			return (mtmpfil.exists() && mtmpfil.isDirectory());
		case 2:
			return (mtmpfil.exists() && mtmpfil.isFile());
		default:
			return mresult;
		}
	}

	public static ArrayList<String[]> gtFILE(String pinfile) throws IOException {
		ArrayList<String[]> mresult = new ArrayList<String[]>();

		BufferedReader mbinput = new BufferedReader(new FileReader(pinfile));

		String mcurlin = "";
		while ((mcurlin = mbinput.readLine()) != null) {
			if (mcurlin.length() == 0)
				continue;
			mresult.add(mcurlin.split(MnVars.FILE_SPLT));
		}
		mbinput.close();

		return mresult;
	}
}