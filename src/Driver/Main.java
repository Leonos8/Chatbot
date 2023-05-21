package Driver;

import NLP.Lemmatization;

public class Main 
{
	public static void main(String[] args)
	{
		String s="What is the current placement scenario?";
		
		//System.out.println(s.substring(s.length()-4));
		Lemmatization lemma=new Lemmatization();
		
		lemma.lemmatize(s);
	}
}
