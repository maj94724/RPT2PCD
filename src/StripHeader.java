import java.io.*;
class  StripHeader
{
	public static void main(String[] args) throws IOException
	{
		FileInputStream fis;
		FileOutputStream fos;

		fis=new FileInputStream("tkcconfig-Marianna.txt");
		fos=new FileOutputStream("sample.txt");
		DataInputStream dis=new DataInputStream(fis);
		DataOutputStream dos=new DataOutputStream(fos);
		String record="",prev="",curr="";
		String record1=dis.readLine();
		String record2=dis.readLine();
		String record3=dis.readLine();
		/*
		while(( (curr=dis.readLine()) != null ) ){
			//if(curr.startsWith("Comprehensive Report")){
			if(curr.indexOf("Comprehensive Report") != -1){

				prev = dis.readLine();
				prev = curr = dis.readLine();
//				curr = dis.readLine();
			}
			prev  = prev + "\n";
			dos.write(prev.getBytes());
			prev = curr;
		}
		*/
		while(( (curr=dis.readLine()) != null ) ){
			//if( curr.indexOf("Comprehensive Report") != -1){
			if( curr.indexOf("Mariana Airmotive") != -1){
				dis.readLine(); //skip line after company name
				dis.readLine(); //skip second line after company name
				dis.readLine(); //skip third line after company name
				curr = dis.readLine();
			}
			curr  = curr + "\n";
			dos.write(curr.getBytes());
		}

		fis.close();
		fos.close();
	}
}
