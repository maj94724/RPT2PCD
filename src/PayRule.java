import java.util.*;
import java.io.*;

class PayRule 
{
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
	}

	public String fixedRule;
	public 	String name;
	public 	String workRule;

	public PayRule(String n){
		this.name = n;
	}

	public void print(){
		System.out.println("PayRule:"+name);
		System.out.println("	fixedRule:"+fixedRule);
		System.out.println("	workRule:"+workRule);
	}

	public void print2FixedRule(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		String w = name;
		StringTokenizer st = new StringTokenizer(fixedRule,"+");
		String x = w;
		while (st.hasMoreTokens()) {
			x = x+","+st.nextToken().trim(); 
		}
		x = x + "\n";
		dos.write(x.getBytes());
		dos.flush();
	}

	public void print2FilePayRule(DataOutputStream dos) throws IOException{
		if(name != null) name = name.trim();
		String x = name+","+workRule+"\n";
		dos.write(x.getBytes());
		dos.flush();
	}
}
