import java.io.*;
import java.util.*; 

public class  Overtimes
{
	public static void main(String[] args) throws Exception
	{	
		FileInputStream fis = new FileInputStream("sample.txt");
		Overtimes o = new Overtimes(fis);
		o.findOvertimes();
		o.findZones();
		fis.close();
	}

	ArrayList<String> overtimes = new ArrayList<String>();
	ArrayList<String> zones = new ArrayList<String>();
	DataInputStream dis = null;
	DataOutputStream dos = null;

	public Overtimes(FileInputStream fin) throws IOException {
		 dis = new DataInputStream(fin);
		 dos = new DataOutputStream(new FileOutputStream("/temp/output.csv"));
	}

	public void closeFile() throws IOException{
		dis.close();
		dos.close();
	}
	public ArrayList<String> findOvertimes()throws IOException
		{
		 String s = "OVERTIMES\n";
		 dos.write(s.getBytes());
		 s = "Name,Type,Hours\n";
		 dos.write(s.getBytes());

		String curr = "";
		curr = dis.readLine();
		while(!curr.trim().equals("Pay Level")){
				//System.out.println(">"+curr);
				curr = dis.readLine();
			}
		String prev = curr;
		while(!curr.trim().equals("Hour Types")){
			String saveLine = "";
				prev = curr;
				curr = dis.readLine();
				if(curr.trim().equals("Definition")){
					curr = dis.readLine();
					String OTtype =curr.substring(curr.indexOf(":")+1,curr.length()).trim(); 
					curr = dis.readLine();
					String hours =curr.substring(curr.indexOf(":")+1,curr.length()).trim(); 
					saveLine=prev.trim()+","+OTtype+","+hours+" \n";
					if(OTtype.toUpperCase().indexOf("OVERTIME") != (-1)){
						overtimes.add(prev.trim());
						dos.write(saveLine.getBytes());
						System.out.println(prev.trim());
					}

				}
			}
	return overtimes;
	}

	public ArrayList<String> findZones()throws IOException
		{
		String curr = "";
		String prev = curr;
		while(!curr.trim().equals("Distribution Grid")){
				prev = curr;
				curr = dis.readLine();
				if(curr.trim().equals("Definition")){
					zones.add(prev.trim());
					curr = dis.readLine();
				}
			}
	return zones;
	}
}
