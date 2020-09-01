import java.io.*;
import java.util.*;

class StorePCD 
{
	public int MAX_ROWS = 500;
	public int MAX_COLS = 500;
	public static void main(String[] args) 
	{
		StorePCD spcd = new StorePCD("Test");
		spcd.add("Weekly OT-1","Weekend-1","OT1");
		spcd.add("Weekly OT-1","Basic Day","OT1");
		spcd.add("Basic Hours","Weekend-1","OT1");
		spcd.add("Basic Hours","Basic Day","REG1");
		spcd.add("Daily OT-2","Weekend-1","OT1");
		spcd.add("Daily OT-2","Basic Day","OT1");
		spcd.add("Daily OT-2 + Weekly OT-1","Weekend-1","OT1");		
		spcd.add("Daily OT-2 + Weekly OT-1","Basic Day","OT1");
		System.out.println("Daily".compareTo("Weekly"));
		spcd.sortRows();
		spcd.sortCols();
		spcd.print();
		try{
		
		//spcd.printzones(new DataOutputStream(new FileOutputStream("/temp/test.txt")));
		}catch(Exception e){e.printStackTrace();}

		System.out.println("****************************************");
System.out.println(spcd.normalize("a23+a12+a543"));
	
	}
	
	ArrayList<String> rownames;
	ArrayList<String> colnames;
	String[][] pcdvalues;
	String name = null;
	public StorePCD(String n){
		this.name = n;
		rownames = new ArrayList<String>();
		//rownames.add("Basic Hours");
		colnames = new ArrayList<String>();
		//colnames.add("Basic Day");
		pcdvalues = new String[MAX_ROWS][MAX_COLS];
	
		}
	
	public void setName(String s){
		this.name = s;
	}

	public void add(String rowname, String colname, String value){
		rowname = normalize(rowname.trim());
		colname = normalize(colname.trim());
		int colIndex = colnames.indexOf(colname);
		int rowIndex = rownames.indexOf(rowname);

		if (colIndex == (-1))	{	colnames.add(colname);	}
		if (rowIndex == (-1))	{	rownames.add(rowname);	}
		//repeat
		colIndex = colnames.indexOf(colname);
		rowIndex = rownames.indexOf(rowname);
		
		pcdvalues[rowIndex][colIndex] = value.trim();
		
	}

	public void print(){
		System.out.println(" Pay Code Distribution : "+name+"...............................................");
		for(String s: colnames)
			System.out.print(s+"   |    ");
		System.out.println();
		for(int i=0; i < rownames.size(); i++){
			
			if(!rownames.get(i).equals("Basic Day"))
			System.out.print(rownames.get(i)+".>.");
			for(int j=0; j < colnames.size(); j++){
				if(!rownames.get(i).equals("Basic Day"))
				System.out.print(pcdvalues[i][j]+"...");//+"["+i+"]["+j+"]     ");
			}
		if(!rownames.get(i).equals("Basic Day"))	System.out.println("");
		}
	}

	public void print2CSV(DataOutputStream dos) throws IOException{
		mergeBasicDayBasicHoursRow();
		sortRows();
		sortCols();
		String record;
		record="\n\nPay Code Distribution "+name+"\n,";
		dos.write(record.getBytes());

		for(String s: colnames){
			record = s+",";
			if(s.equals("Basic Day")) record = "Default,";
			dos.write(record.getBytes());
		}
		record="\n";
		dos.write(record.getBytes());

		for(int i=0; i < rownames.size(); i++){
			
			if(!rownames.get(i).equals("Basic Day")){
			record=rownames.get(i)+",";
			if(rownames.get(i).equals(" ")) record = "Default,";
			dos.write(record.getBytes());
			}

			for(int j=0; j < colnames.size(); j++){
				if(!rownames.get(i).equals("Basic Day")){
				record=pcdvalues[i][j]+",";
				dos.write(record.getBytes());
				}
			}
		if(!rownames.get(i).equals("Basic Day"))	{
			record="\n";
			dos.write(record.getBytes());
		}
		}
	}
	
private void swapRow(int r1, int r2){
		String[] temp = new String[MAX_COLS];
		for(int i=0; i < colnames.size(); i++)
			temp[i] = pcdvalues[r1][i];
		for(int i=0; i < colnames.size(); i++)
			pcdvalues[r1][i] = pcdvalues[r2][i];
		for(int i=0; i < colnames.size(); i++)
			pcdvalues[r2][i] = temp[i];
		//swap names
		String s= rownames.get(r1);
		rownames.set(r1, rownames.get(r2)) ;
		rownames.set(r2, s);
	}

private void swapCol(int c1, int c2){
		String[] temp = new String[MAX_ROWS];
		int n = rownames.size();
		for(int i=0; i < n; i++)
			temp[i] = pcdvalues[i][c1];

		for(int i=0; i < n; i++)
			pcdvalues[i][c1] = pcdvalues[i][c2];

		for(int i=0; i < n; i++)
			pcdvalues[i][c2] = temp[i];
		//swap names
		String s= colnames.get(c1);
		colnames.set(c1, colnames.get(c2)) ;
		colnames.set(c2, s);
	}

	private void sortRows(){
		int index = rownames.indexOf("Basic Hours");

	if (index > 0){
			swapRow(0,index);
			rownames.set(0, "Default");
		}

		int n = rownames.size();
		for(int i=0; i< n; i++){
			for(int j=n-1; j > i+1  ; j--){
				if(rownames.get(j-1).compareTo(rownames.get(j)) > 0){
					swapRow(j,j-1);
				}
			}
		}
		

	}

	private void sortCols(){
		int index = colnames.indexOf("Basic Hours");
		if (index > 0){
			swapCol(0,index);
			colnames.set(0,"Default");
		}

		int n = colnames.size();
		for(int i=0; i< n; i++){
			for(int j=n-1; j > i+1; j--){
				if(colnames.get(j-1).compareTo(colnames.get(j)) > 0){
					swapCol(j,j-1);
				}
			}
		}



	}

	public void mergeBasicDayBasicHoursRow()
		{
		//Convert all the row entires to Basic Hours
		int day = rownames.indexOf("Basic Day");
		int hours = rownames.indexOf("Basic Hours");
		if(day == -1) return;
		if( hours == -1 && day != -1) {
			rownames.set(day,"Basic Hours");
			return;
		}else{
		for(int i=0; i < MAX_COLS; i++)
			if( pcdvalues[day][i] != null)
				pcdvalues[hours][i] = pcdvalues[day][i];
		}

	}

	public String normalize(String s){

	 ArrayList<String> a = new ArrayList<String>();
     StringTokenizer st = new StringTokenizer(s,"+");
     while (st.hasMoreTokens()) {
         a.add(st.nextToken().trim());
     }
	 Collections.sort(a);
	 String name = a.get(0);
	 for(int i=1; i < a.size(); i++)
		name = name + "+" + a.get(i);
	return name;
	}


	public void printzones(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		for(String s: colnames){
			if(s != null && s.indexOf("+") == -1 && !s.equals("Basic Day") && !s.equals("Basic Hours"))
			{
				String w = name+","+s+"\n";
				dos.write(w.getBytes());
			}
		}
		for(String s: rownames)
			if(s.indexOf("+") == -1 && !s.equals("Basic Day") && !s.equals("Basic Hours"))
			{
				String w = name+","+s+"\n";
				dos.write(w.getBytes());
			}
	}

}
