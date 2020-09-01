import java.io.*;
import java.util.*; 

public class ParseRules
{
	private static DataOutputStream fixedRule = null;
	private static DataOutputStream payRule   = null;
	private static DataOutputStream workrule = null;
	private static DataOutputStream deductions = null;
	private static DataOutputStream meals = null;
	// Main method
	public static void main (String args[])
	{
		// Stream to read file
		FileInputStream fin;		
		try
		{
		    // Open an input stream
		    fin = new FileInputStream ("/temp/sample.txt");
			ParseRules.fixedRule = new DataOutputStream(new FileOutputStream("/temp/fixedRule.csv"));
			ParseRules.payRule = new DataOutputStream(new FileOutputStream("/temp/payRule.csv"));
			ParseRules.workrule = new DataOutputStream(new FileOutputStream("/temp/workrule.csv"));
			ParseRules.deductions = new DataOutputStream(new FileOutputStream("/temp/deductions.csv"));
			ParseRules.meals = new DataOutputStream(new FileOutputStream("/temp/meals.csv"));

			IdentifyWorkRules( new DataInputStream(fin));
			IdentifyPayRules(new DataInputStream(fin));
			
			fixedRule.close();
			payRule.close();
			workrule.close();
			deductions.close();
			meals.close();
		    // Close our input stream
		    fin.close();		
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("Unable to read from file");
			System.exit(-1);
		}
	}	

static ArrayList<WorkRule> workRules = new ArrayList<WorkRule>();

private static void IdentifyWorkRules(DataInputStream dis) throws IOException{
	String prev = "";
	String curr = "";
while(true){
	while(!curr.trim().equals("Time") && !curr.trim().equals("Rounding Rules")){
		if(!curr.equals("")) prev = curr;
			curr = dis.readLine();
	}
//break Condition	
	if(curr.trim().equals("Rounding Rules")) return;
		
	WorkRule w = new WorkRule(prev);

	while(!curr.trim().startsWith("Rounding rule :")){
		curr = dis.readLine();
	}
	w.roundingRule = curr.substring(curr.indexOf(":")+1,curr.length()).trim();

	while(!curr.trim().startsWith("Default meal rule :") && !curr.trim().startsWith("Apply deduction to :")){
		curr = dis.readLine();
	}
	
	if(!curr.trim().startsWith("Apply deduction to :"))
	w.addMeal(curr.substring(curr.indexOf(":")+1,curr.length()).trim());

	while(!curr.trim().startsWith("Apply deduction to :")){
		curr = dis.readLine();
		if (!curr.trim().startsWith("Apply deduction to :"))
		{	w.addMeal(curr.trim());
		}
	}


		
	while(!curr.trim().equals("Deduction Rules") &&
		  !curr.trim().startsWith("Distribution grid to pay hours worked :") &&
		!curr.trim().equals("Bonus")){
		curr = dis.readLine();
	}

	if(curr.trim().startsWith("Deduction Rules")){
		while(!curr.trim().equals("Bonus")){
			curr = dis.readLine();
			if(!curr.trim().equals("Bonus")){
				w.addDeduction(curr.trim());
			}
		}
	}
	while(!curr.trim().startsWith("Distribution grid to pay hours worked :")){
		curr = dis.readLine();
	}
	w.pcdName = curr.substring(curr.indexOf(":")+1,curr.length()).trim();

	w.print();
	w.print2FileWorkrule(workrule);
	w.print2FileDeductions(deductions);
	w.print2FileMeals(meals);
	workRules.add(w);
	}
}//IdentifyWorkRules


static ArrayList<PayRule> payRules = new ArrayList<PayRule>();

private static void IdentifyPayRules(DataInputStream dis) throws IOException{
	String prev = "";
	String curr = "";
	String payPeriod = null;
	String startsEach = null;
	String StartDate = null;
	String DayStarts = null;
	PayRule p = null;
while(true){
	while(!curr.trim().equals("Pay period") && !curr.trim().equals("Terminals")){
			if(!curr.trim().equals("")) prev = curr;
			curr = dis.readLine();
	}
//break Condition	
	if(curr.trim().equals("Terminals")) return;
	
	p=new PayRule(prev);

	while(!curr.trim().startsWith("Pay period :") && !curr.trim().startsWith("Length :") ){
			curr = dis.readLine();
			System.out.println(curr);
	}
	payPeriod = curr.substring(curr.indexOf(":")+1,curr.length()).trim();
	while(!curr.trim().startsWith("Starts ")){
			curr = dis.readLine();
	}
	startsEach = curr.substring(curr.indexOf(":")+1,curr.length()).trim();
	while(!curr.trim().startsWith("Start date :")){
			curr = dis.readLine();
	}
	StartDate = curr.substring(curr.indexOf(":")+1,curr.length()).trim();
	while(!curr.trim().startsWith("Default work rule :")){
			curr = dis.readLine();
	}
	p.workRule = curr.substring(curr.indexOf(":")+1,curr.length()).trim();
	while(!curr.trim().startsWith("Day starts at :")){
			curr = dis.readLine();
	}
	DayStarts = curr.substring(curr.indexOf(":")+1,curr.length()).trim();
	p.fixedRule = payPeriod+"+"+startsEach+"+"+StartDate+"+"+DayStarts;
	p.print();
	p.print2FixedRule(fixedRule);
	p.print2FilePayRule(payRule);
	payRules.add(p);
}//while
}//IdentifyPayRules


}
