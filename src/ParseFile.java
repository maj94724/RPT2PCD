import java.io.*;
import java.util.*; 

public class ParseFile
{
static ArrayList<String> overtimes = null;
static ArrayList<String> zonenames = null;

	// Main method
	public static void main (String args[])
	{
		// Stream to read file
		FileInputStream fin;		



		try
		{
		   
			//Get list of overtimes
			Overtimes o= new Overtimes( new FileInputStream ("/temp/sample.txt"));
			overtimes = o.findOvertimes();
			o.closeFile();
			
			// Open an input stream8/6/2008
		    fin = new FileInputStream ("/temp/sample.txt");
			FileOutputStream fos = new FileOutputStream("/temp/zones.csv");
			ParseFile.dos = new DataOutputStream(fos);
			ParseFile.pcdOut = new DataOutputStream(new FileOutputStream("/temp/pcd.csv"));
			IdentifyDistributions(null, new DataInputStream(fin));
			fos.close();
		    // Close our input stream
		    fin.close();		
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("**** UNABLE TO READ OR WRITE FILE ****");
			System.exit(-1);
		}
	}	

	public static DataOutputStream dos = null;
	public static DataOutputStream pcdOut = null;
		   static ArrayList<String> activityCodes = null;
	private static void IdentifyDistributions(String pcdname,DataInputStream dis){
		String prev = null;
		String curr = null;
		
		try{
			//while(1==1){
if(pcdname == null){
			curr = dis.readLine();
			prev = curr;

			if(pcdname == null)
			while(!curr.trim().equals("Pay levels")){
				prev = curr;
				curr = dis.readLine();
			}
			while(!curr.trim().equals("Pay levels")){
				prev = curr;
				curr = dis.readLine();
			}
		}
			if(pcdname == null) 	pcdname = prev;
			StorePCD spcd = new StorePCD(pcdname);



			curr = prev = dis.readLine().trim();

			while(!curr.trim().equals("Activity Codes")){
				prev = curr;
				curr = dis.readLine();
			}
			activityCodes = new ArrayList<String>();
			while(!curr.trim().equals("Distribution Grid")){
				prev = curr;
				curr = dis.readLine();
				if(!curr.equals("") && !curr.equals("Distribution Grid")){
					activityCodes.add(curr.trim());
				}
			}
			//System.out.println(activityCodes);
			while(!curr.trim().equals("Normal")){
				prev = curr;
				curr = dis.readLine();
			}
			
			//Stop Conditions
			while(!curr.trim().equals("Pay levels") && !curr.trim().equals("Work Rules")
					&& !activityCodes.contains(curr.trim()) ){
				prev = curr;
				curr = dis.readLine();
				while(activityCodes.contains(curr.trim())){
						StorePCD spcdActivity = new StorePCD(pcdname+" --> "+curr.trim());
						spcdActivity.setName(pcdname+" --> "+curr.trim());
						boolean first = true;
					while(!curr.trim().equals("Pay levels") && !curr.trim().equals("Work Rules")
							&& (!activityCodes.contains(curr.trim()) || first == true) ){
						first = false;
						prev = curr;
						curr = dis.readLine();
						processPCDRowsActivity(curr, spcdActivity);
					}//while
						spcdActivity.mergeBasicDayBasicHoursRow();
						spcd.printzones(dos);
						spcdActivity.print2CSV(pcdOut);
				}
				if(!activityCodes.contains(curr.trim()))
					processPCDRows(curr, spcd);
			}
			/*
			while(curr != null && !curr.trim().equals("Pay levels")){
				curr = dis.readLine();
			}*/
			
			if(curr != null && 
				(curr.trim().equals("Pay levels") || curr.trim().equals("Work Rules") )
				){
				spcd.mergeBasicDayBasicHoursRow();
				spcd.print2CSV(pcdOut);
				spcd.printzones(dos);
				if(!curr.trim().equals("Work Rules"))
				IdentifyDistributions(prev, dis);
			}
		//}//while
		}catch(IOException e){
			e.printStackTrace();
		}
	}


static String zonename = null;
private static void processPCDRows(String s, StorePCD spcd){
	if (s == null || s.trim().equals(""))
	{
		return;
	}
	String s1 = null;
	String value = null;
	s = s.trim();
		if((s.indexOf(":") != -1) && (s.indexOf(";") != -1)){

	//if(s.endsWith(";")){
		// entry
		s1 = s.substring(0, s.indexOf(":"));
		value = s.substring(s.indexOf(":")+1,s.indexOf(";"));

		String z = zonename;
		if (!zonename.equals("Basic Hours"))
		{
			if(s1.trim().equals("Basic Day")){
				z = zonename;
			} else
			z = zonename+"+"+s1;
		} else {
			z = s1;
			s1 = "Basic Day";
		}
		String OTs = null;
		String zones = null;
		StringTokenizer st=new StringTokenizer(z,"+");
		while (st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			//if(t.indexOf("Daily") >=0 || t.indexOf("Weekly") >=0 || t.indexOf("Over ") >= 0)
			if(overtimes.indexOf(t)	!= (-1))
			{ //found
				if (OTs == null){ OTs = t;}
				else { OTs = OTs+"+"+t;}
			}else{
				if (zones == null){ zones = t;}
				else { zones = zones+"+"+t;}
			}
		}
			if (zones == null)
			{
				zones = "Basic Hours";
			}
			if(OTs == null) { OTs = "Basic Day";};
			spcd.add(zones.trim(), OTs.trim(), value.trim());


	} else {
		//column name
		zonename = s.trim();

	}
}
static String zonenameActivity = null;
private static void processPCDRowsActivity(String s, StorePCD spcd){
	if (s == null || s.trim().equals(""))
	{
		return;
	}
	String s1 = null;
	String value = null;
		s = s.trim();
		if((s.indexOf(":") != -1) && (s.indexOf(";") != -1)){
		s1 = s.substring(0, s.indexOf(":"));
		value = s.substring(s.indexOf(":")+1,s.indexOf(";"));

		String z = zonenameActivity;
		if (!zonenameActivity.equals("Basic Hours"))
		{
			if(s1.trim().equals("Basic Day")){
				z = zonenameActivity;
			} else
			z = zonenameActivity+"+"+s1;
		} else {
			z = s1;
			s1 = "Basic Day";
		}
		
		String OTs = null;
		String zones = null;
		StringTokenizer st=new StringTokenizer(z,"+");
		while (st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			//if(t.indexOf("Daily") >=0 || t.indexOf("Weekly") >=0 || t.indexOf("Over ") >= 0)
			if(overtimes.indexOf(t)	!= (-1))
			{ //found
				if (OTs == null){ OTs = t;}
				else { OTs = OTs+"+"+t;}
			}else{
				if (zones == null){ zones = t;}
				else { zones = zones+"+"+t;}
			}
		}
			if (zones == null)
			{
				zones = "Basic Hours";
			}
			if(OTs == null) { OTs = "Basic Day";};
			spcd.add(zones.trim(), OTs.trim(), value.trim());


	} else {
		//column name
		zonenameActivity = s.trim();

	}
}

}
