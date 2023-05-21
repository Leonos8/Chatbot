package utils;

public class dataStorage 
{
	int length;
	
	String sentence;
	String[] pos;
	String[] words;
	
	public dataStorage()
	{
		
	}
	
	public void setPos(String[] pos)
	{
		this.pos=pos;
	}
	
	public void setSentence(String sentence)
	{
		this.sentence=sentence;
		String[] words=sentence.split(" ");
		this.length=words.length;
	}
	
	public int getLength()
	{
		return this.length;
	}
	
	public String getSentence()
	{
		return this.sentence;
	}
	
	public String[] getPos()
	{
		return pos;
	}
	
	public String[] getWords()
	{
		return words;
	}
}
