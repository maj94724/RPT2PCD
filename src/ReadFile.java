import java.io.*;
class  ReadFile
{
	public static void main(String[] args) throws IOException
	{
		FileInputStream fis;
		FileOutputStream fos;

		fis=new FileInputStream("tkcconfig-Domtar.txt");
		fos=new FileOutputStream("sample.txt");
		DataInputStream dis=new DataInputStream(fis);
		DataOutputStream dos=new DataOutputStream(fos);
		String record="",prev="",curr="";
		String record1=dis.readLine();
		String record2=dis.readLine();
		String record3=dis.readLine();
		while(( (curr=dis.readLine()) != null ) ){
			if(curr.startsWith("Comprehensive Report")){
				prev = dis.readLine();
				prev = curr = dis.readLine();
//				curr = dis.readLine();
			}
			prev  = prev + "\n";
			dos.write(prev.getBytes());
			prev = curr;
		}
		fis.close();
		fos.close();
	}
}
