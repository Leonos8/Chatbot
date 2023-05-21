package Driver;

import NLP.CharacterEncodingTable;

public class UnitTests 
{
	public static void main(String[] args)
	{
		UnitTests test=new UnitTests();
		test.encodingTest();
	}
	
	public void encodingTest()
	{
		CharacterEncodingTable cet=new CharacterEncodingTable();
		
		double[] encoded=cet.encode("Happy");
		
		for(int i=0; i<encoded.length; i++)
		{
			System.out.println(encoded[i]);
		}
		
		String decoded=cet.decode(encoded);
		
		System.out.println(decoded);
	}
}
