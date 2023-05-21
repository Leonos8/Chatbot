package NLP;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.dataStorage;

public class Lemmatization //Lemmatization and POS tagging
{
	public static final File currDir=new File(".");
	public static final String absolutePath=currDir.getAbsolutePath();
	public static final String path=absolutePath.substring(0, absolutePath.length()-2);	
	public static final String posPath=path+File.separator+"POSDatasets"+File.separator;
	
	ArrayList<String> sentenceList=new ArrayList<>();
	
	public Lemmatization()
	{
		
	}
	
	public String extractFeatures(String sentence, int index)
	{
		String output=""; //Will use ### to split
		
		String[] words=sentence.split(" ");
		
		output+=words[index]+"###"; //the word
		
		if(index==0)
			output+="first###"; //is first word
		else
			output+="not first###";
		
		if(index==words.length-1)
			output+="last###"; //is last word
		else
			output+="not last###";
		
		if(Character.isUpperCase(words[index].charAt(0)))
			output+="capitalized###"; //is capitalized
		else
			output+="not capitalized###";
		
		if(words[index].toUpperCase().equals(words[index]))
			output+="uppercase###"; //is all upper cased
		else
			output+="not upper###";
		
		if(words[index].toLowerCase().equals(words[index]))
			output+="lowercase###"; //is all lower cased
		else
			output+="not lower###";
		
		// Regex to check string is alphanumeric or not.
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(words[index]);
		if(m.matches())
			output+="alphanumeric###"; //is alphanumeric
		else
			output+="not alphanumeric###";
		
		
		try
		{
			output+=words[index].substring(0, 1)+"###"; //prefix-1
			output+=words[index].substring(0, 2)+"###"; //prefix-2
			output+=words[index].substring(0, 3)+"###"; //prefix-3
			output+=words[index].substring(0, 4)+"###"; //prefix-4
			output+=words[index].substring(words[index].length()-1)+"###"; //suffix-1
			output+=words[index].substring(words[index].length()-2)+"###"; //suffix-2
			output+=words[index].substring(words[index].length()-3)+"###"; //suffix-3
			output+=words[index].substring(words[index].length()-4)+"###"; //suffix-4
		}catch(StringIndexOutOfBoundsException ex)
		{
			output+="too short###";
		}
		
		if(index==0)
			output+="no previous word###";
		else
			output+=words[index-1]; //previous word
		
		if(index==words.length-1)
			output+="no next word###";
		else
			output+=words[index+1]; //next word
		
		if(words[index].contains("-"))
			output+="hyphenated###"; //contains a hyphen (-)
		else
			output+="not hyphenated###";
		
		if(isNumeric(words[index]))
			output+="numeric###"; //is a number
		else
			output+="not numeric###";
		
		boolean capIn=false;
		
		for(int i=1; i<words[index].length()-1; i++)
		{
			if(Character.isUpperCase(words[index].charAt(i)))
				capIn=true;
		}
		
		if(capIn)
			output+="caps inside"; //contains capitals on the inside
		else
			output+="no caps inside";
		
		return output;
	}
	
	public boolean isNumeric(String str)
	{
		try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }
	}
	
	public void lemmatize(String input)
	{
		dataStorage[] ds=posTagging(input);
		//System.out.println(ds[0].getSentence());
		
		HashMap[] data=transformToDataset(ds); //0 is X, 1 is Y
		
		HashMap<Integer, String[]> x=data[0];
		HashMap<Integer, String[]> y=data[1];
		String[] tmp=y.get(0);
		//System.out.println(y.get(0)[5]);
		/*for(int i=0; i<tmp.length; i++)
		{
			System.out.println(tmp[i]);
		}*/
		
		String[] pos= {"ADJ", "ADP", "ADV", "AUX", "CCONJ", "DET", "INTJ", "NOUN", "NUM", "PART", "PRON", "PROPN", 
				"PUNCT", "SCONJ", "SYM", "VERB", "X",};
		
		//CRF crf=new CRF(x, y, pos, sentenceList, .01, .1, 100, true);
		
		//crf.runCRF();
		
		//System.out.println(sentenceList.get(50));
		//ArrayList<String>[][] wordSequence;
		String[] wordSequence=new String[sentenceList.size()];
		
		for(int i=0; i<sentenceList.size(); i++)
		{
			wordSequence[i]=sentenceList.get(i);
		}
		
		//System.out.println(sentenceList.get(50));
		//System.out.println(wordSequence[50][0]);
		
		POS_Tagging tagger=new POS_Tagging(wordSequence);
		
		tagger.runTagging();
	}
	
	public String[][] fixData()//String[][] ws)
	{
		String[][] wordSequence=new String[sentenceList.size()][];
		
		for(int i=0; i<wordSequence.length; i++)
		{
			
		}
		
		return wordSequence;
	}
	
	
	/*https://universaldependencies.org/format.html for conllu format guide
	 * Outputs:
	 * ADJ: adjective
	 * ADP: adposition
	 * ADV: adverb
	 * AUX: auxiliary
	 * CCONJ: coordinating conjunction
	 * DET: determiner
	 * INTJ: interjection
	 * NOUN: noun
	 * NUM: numeral
	 * PART: particle
	 * PRON: pronoun
	 * PROPN: proper noun
	 * PUNCT: punctuation
	 * SCONJ: subordinating conjunction
	 * SYM: symbol
	 * VERB: verb
	 * X: other

	 */
	public dataStorage[] posTagging(String input) //Part of speech tagging
	{
		//ArrayList<String> posList=new ArrayList<String>();
		
		FileFilter directoryFileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.isDirectory();
		    }
		};
		
		File directory = new File(posPath);
		File[] directoryList=directory.listFiles(directoryFileFilter);
		
		ArrayList<String> sentenceList=new ArrayList<String>();
		ArrayList<ArrayList<String>> posList=new ArrayList<ArrayList<String>>();
		
		for(int i=0; i<directoryList.length; i++)
		{
			String tmp=directoryList[i].getName().split("-")[1].toLowerCase();
			File fileName=new File(posPath+directoryList[i].getName()+File.separator+"en_"+tmp+"-ud-train.conllu");
			
			if(!fileName.exists())
				fileName=new File(posPath+directoryList[i].getName()+File.separator+"en_"+tmp+"-ud-test.conllu");
			
			//System.out.println(fileName.getAbsolutePath());
			
			try
			{
				Scanner sc=new Scanner(fileName);
				
				while(sc.hasNext())
				{
					String line=sc.nextLine();
					if(line.contains("# text ="))
					{
						sentenceList.add(line.substring(9));
						
						int numOfWords=line.split(" ").length-3;
						
						ArrayList<String> posListTmp=new ArrayList<String>();
						
						for(int j=0; j<numOfWords; j++)
						{
							posListTmp.add(sc.nextLine());
						}
						
						posList.add(posListTmp);
					}
				}
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		this.sentenceList=sentenceList;
		
		/*
		 * 0: ID: Word index, integer starting at 1 for each new sentence; may be a range for multiword tokens; 
		 * may be a decimal number for empty nodes (decimal numbers can be lower than 1 but must be greater than 0).
		 * 1: FORM: Word form or punctuation symbol. *
		 * 2: LEMMA: Lemma or stem of word form. *
		 * 3: UPOS: Universal part-of-speech tag. *
		 * 4: XPOS: Language-specific part-of-speech tag; underscore if not available.
		 * 5: FEATS: List of morphological features from the universal feature inventory 
		 * or from a defined language-specific extension; underscore if not available.
		 * 6: HEAD: Head of the current word, which is either a value of ID or zero (0).
		 * 7: DEPREL: Universal dependency relation to the HEAD (root iff HEAD = 0) 
		 * or a defined language-specific subtype of one.
		 * 8: DEPS: Enhanced dependency graph in the form of a list of head-deprel pairs.
		 * 9: MISC: Any other annotation.
		 */
		int datasetSize=5;
		
		dataStorage[] ds=new dataStorage[datasetSize];
		
		for(int i=0; i<ds.length; i++)
		{
			ds[i]=new dataStorage();
			ds[i].setSentence(sentenceList.get(i));
			String[] posTmp=new String[sentenceList.get(i).split(" ").length];
			//System.out.println(sentenceList.get(i));
			for(int j=0; j<posTmp.length; j++)
			{
				posTmp[j]=posList.get(i).get(j).split("\t")[3];
				//System.out.println(posTmp[j]);
			}
			ds[i].setPos(posTmp);
			//System.out.println();
			//System.out.println(ds[i].getPos()[0]);
		}
		
		return ds;
	}
	
	public HashMap[] transformToDataset(dataStorage[] ds)
	{
		HashMap<Integer, String[]> x=new HashMap<Integer, String[]>();
		HashMap<Integer, String[]> y=new HashMap<Integer, String[]>();
		
		HashMap[] output=new HashMap[2]; //0 is x, 1 is y
		
		//System.out.println(ds[0].getPos()[0]);
		
		int length=0;
		for(int i=0; i<ds.length; i++)
		{
			length+=ds[i].getLength();
		}
		
		for(int i=0; i<ds.length; i++)
		{
			//System.out.println(ds[i].getSentence());
			String[] sentenceWordFeatures=new String[ds[i].getLength()];
			String[] tags=new String[ds[i].getLength()];
			
			for(int index=0; index<ds[i].getLength(); index++)
			{
				sentenceWordFeatures[index]=extractFeatures(ds[i].getSentence(), index);
				tags[index]=ds[i].getPos()[index];
			}
			//System.out.println(tags[i]);
			x.put(i, sentenceWordFeatures);
			y.put(i, tags);
		}
		
		output[0]=x;
		output[1]=y;
		//System.out.println(y.get(1)[0]);
		
		return output;
	}
}
