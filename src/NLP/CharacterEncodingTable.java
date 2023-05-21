package NLP;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterEncodingTable 
{
	private HashMap<Character, Integer> encodingTable;
	
	public CharacterEncodingTable()
	{
		encodingTable=new HashMap<>();
		initializeEncodingTable();
	}
	
	private void initializeEncodingTable()
	{
		for(char c='a'; c<='z'; c++)
		{
			encodingTable.put(c,  c-'a'+1);
		}
		
		for(char c='A'; c<='Z'; c++)
		{
			encodingTable.put(c,  c-'A'+27);
		}
		
		for(char c='0'; c<='9'; c++)
		{
			encodingTable.put(c,  c-'0'+53);
		}
		
		encodingTable.put('.', 63);
		encodingTable.put(',', 64);
		encodingTable.put('?', 65);
		encodingTable.put('!', 66);
		encodingTable.put('\'', 67);
	}
	
	public String decode(double[] encodedWord)
	{
		//char[] charArray=new char[encodedWord.length];
		ArrayList<Character> charList=new ArrayList<>();
		for(int i=0; i<encodedWord.length; i++)
		{
			int encodedChar=(int)encodedWord[i];
			encodingTable.forEach((key, value)->
			{
				if(value.equals(encodedChar))
				{
					charList.add(key);
					//charArray[i]=key;
				}
			});	
		}
		
		String word="";
		for(int i=0; i<charList.size(); i++)
		{
			word+=charList.get(i);
		}
		
		return word;
	}
	
	public double[] encode(String word)
	{
		double[] numericRepresentation=new double[word.length()];
		
		for(int i=0; i<numericRepresentation.length; i++)
		{
			if(encodingTable.get(word.charAt(i))!=null)
			{
				numericRepresentation[i]=encodingTable.get(word.charAt(i));
			}
			else
				numericRepresentation[i]=0;
		}
		
		return numericRepresentation;
	}
	
	
}
