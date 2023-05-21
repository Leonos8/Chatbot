package NLP;

import java.util.ArrayList;

public class POS_Tagging 
{
	String[] wordSequence;
	
	int hiddenSize=128;
	int inputSize=128;
	
	public POS_Tagging(String[] wordSequence)
	{
		this.wordSequence=wordSequence;
	}
	
	public void runTagging()
	{		
		CharacterEncodingTable cet=new CharacterEncodingTable();
		
		String tmpSentence="Happy birthday to me, friend";
		
		
		
		int charCount=0;
		
		for(int i=0; i<tmpSentence.length(); i++)
		{
			if(tmpSentence.charAt(i)!=' ')
			{
				charCount++;
			}
		}
		
		//Will have to readjust this to include the fact that its a large quantity of sentences, make it 3d eventually
		double[][][] encodedSentence=new double[wordSequence.length][][];
		
		//System.out.println(wordSequence[0]);
		
		for(int i=0; i<encodedSentence.length; i++)
		{
			//System.out.println(wordSequence[i]);
			//encodedSentence[i]=wordSequence[i].split(" ");
			String[] splitSentence=wordSequence[i].split(" ");
			//System.out.println(splitSentence[0]);
			
			
			double[][] embeddedWord=wordEmbedding(cet, splitSentence);
			//char[][] encodedChar=characterEncoding(splitSentence);
			
			encodedSentence[i]=embeddedWord;
			//encodedSentence[i]=concatenate(embeddedWord, encodedChar);
		}
		
		//ArrayList<String> encodedX=new ArrayList<>();
		for(int i=0; i<encodedSentence.length; i++)
		{
			for(int j=0; j<encodedSentence[i].length; j++)
			{
				for(int k=0; k<encodedSentence[i][j].length; k++)
				{
					//encodedX.add(encodedSentence[i][j][k]); //Not sure if one dimension makes it better or easier
					//System.out.println(encodedSentence[i][j][k]); 
				}
			}
		}
		
		double[][] encodedX=new double[encodedSentence.length][];		
		for(int i=0; i<encodedSentence.length; i++)
		{
			encodedX[i]=downsize(encodedSentence[i]);
		}
		
		/*for(int i=0; i<encodedX.length; i++)
		{
			for(int j=0; j<encodedX[i].length; j++)
			{
				System.out.println(encodedX[i][j]);
			}
		}*/
		
		BiLSTM bilstm=new BiLSTM(inputSize, hiddenSize);
		
		double[][] forwardHiddenStates=bilstm.computeForwardHiddenStates(encodedX);
		double[][] backwardHiddenStates=bilstm.computeBackwardHiddenStates(encodedX);
		
		System.out.println(forwardHiddenStates.length+"\t"+forwardHiddenStates[0].length);
		System.out.println(backwardHiddenStates.length+"\t"+backwardHiddenStates[0].length);
		
		//System.out.println(concatenate(a, b)[0]);
	}
	
	public double[] downsize(double[][] x)
	{
		ArrayList<Double> downsizedList=new ArrayList<>();
		
		for(int i=0; i<x.length; i++)
		{
			for(int j=0; j<x[i].length; j++)
			{
				downsizedList.add(x[i][j]);
			}
		}
		
		double[] downsized=new double[downsizedList.size()];
		
		for(int i=0; i<downsized.length; i++)
		{
			downsized[i]=downsizedList.get(i);
		}
		
		return downsized;
	}
	
	public double[][] wordEmbedding(CharacterEncodingTable cet, String[] word)
	{
		double[][] embeddedWord=new double[word.length][];
		
		for(int i=0; i<embeddedWord.length; i++)
		{
			embeddedWord[i]=cet.encode(word[i]);
		}
		
		return embeddedWord;
	}
	
	public char[][] characterEncoding(String[] word)
	{
		char[][] output=new char[word.length][];
		
		for(int i=0; i<output.length; i++)
		{
			output[i]=word[i].toCharArray();
		}
		
		return output;
	}
	
	public String[][] concatenate(int[][] embeddedWord, char[][] charEncoding)
	{
		String[][] output=new String[embeddedWord.length][];
		
		for(int i=0; i<embeddedWord.length; i++)
		{
			String[] tmp=new String[embeddedWord[i].length];
			for(int j=0; j<embeddedWord[i].length; j++)
			{
				tmp[j]=embeddedWord[i][j]+""+charEncoding[i][j];
			}
			output[i]=tmp;
		}
		
		return output;
	}
}
