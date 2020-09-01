import java.util.*;
import java.io.*;

class WorkRule 
{
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
	}

	public String roundingRule;
	ArrayList<String> meals;
	ArrayList<String> deductions;
	public 	String name;
	public 	String pcdName;

	public WorkRule(String n	){
		this.name = n;
		roundingRule = null;
		meals = new ArrayList<String>();
		deductions = new ArrayList<String>();
		pcdName = null;
	}

	public void addMeal(String meal){
		if( meal == null || meal.equals("")) return;
		meals.add(meal);
	}

	public void addDeduction(String deduction){
		if( deduction == null || deduction.equals("")) return;
		deductions.add(deduction);
	}

	public void print(){
		System.out.println("WorkRule:"+name);
		System.out.println("	RoundingRule:"+roundingRule);
		System.out.println("	PcdName:"+pcdName);
		System.out.println("	Deductions:"+deductions);	
		System.out.println("	Meals:"+meals);	
	}

	public void print2FileWorkrule(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		String w = name+","+roundingRule+","+pcdName+"\n";
		dos.write(w.getBytes());
	}

	public void print2FileDeductions(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		String w = name;
		for(String s: deductions){
			if(s != null)
			{
				String x = w+","+s.trim()+"\n"; 
				dos.write(x.getBytes());
			}
		}
	}

	public void print2FileMeals(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		String w = name;
		for(String s: meals){
			if(s != null)
			{
				String x = w+","+s.trim()+"\n"; 
				dos.write(x.getBytes());
			}
		}
	}


}
