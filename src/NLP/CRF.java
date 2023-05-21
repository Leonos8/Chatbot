package NLP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRF 
{
	ArrayList<String> x=new ArrayList<String>();
	ArrayList<String> y=new ArrayList<String>();
	String[] pos;
	ArrayList<String> sentenceList=new ArrayList<>();
	
	double c1;
	double c2;
	
	int maxIterations;
	
	boolean allPossibleTransitions;
	
	/*
	 * Param:
	 * x(HashMap<Integer, String[]>): The Feature vectors
	 * y(HashMap<Integer, String[]>): The words
	 * pos(String[]): The parts of speech
	 * c1(double): The coefficient for L1 regularization.
	 * c2(double): The coefficient for L2 regularization.
	 * maxIterations(int): The maximum number of iterations for optimization algorithms.
	 * allPossibleTransitions(boolean): Specify whether CRFsuite generates transition features that
        do not even occur in the training data (i.e., negative transition
        features). When True, CRFsuite generates transition features that
        associate all of possible label pairs. Suppose that the number
        of labels in the training data is L, this function will
        generate (L * L) transition features.
	 */
	public CRF(HashMap<Integer, String[]> x, HashMap<Integer, String[]> y, String[] pos, 
			ArrayList<String> sentenceList, double c1, double c2, int maxIterations, boolean allPossibleTransitions)
	{
		for(int i=0; i<x.size(); i++)
		{
			for(int j=0; j<x.get(i).length; j++)
			{
				this.x.add(x.get(i)[j]);
				this.y.add(y.get(i)[j]);
			}
		}
		this.pos=pos;
		this.sentenceList=sentenceList;
		this.c1=c1;
		this.c2=c2;
		this.maxIterations=maxIterations;
		this.allPossibleTransitions=allPossibleTransitions;
	}
	
	public void createGraph()
	{
		
	}
	
	public void runCRF() //Will probably eventually output whatever the output is supposed to be
	{
		unigramFeatureFunction(sentenceList.get(0));
	}
	
	public String[] unigramFeatureFunction(String sentence)//String yi, String xi)
	{	
		String[] unigramFeatureVector=sentence.split(" ");
		
		return unigramFeatureVector;
	}
	
	public void bigramFeatureFunction(String sentence)//String prev_yi, String yi, String prev_xi, String xi)
	{
		
	}
	
	public void prefixFeatureFunction()
	{
		
	}
	
	public void suffixFeatureFunction()
	{
		
	}
	
	public class Vertex
	{
		String label;
		
		public Vertex(String label)
		{
			this.label=label;
		}
	}
	
	public class Graph
	{
		private Map<Vertex, List<Vertex>> adjVertices;
		
		public void addVertex(String label)
		{
			adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
		}
		
		public void removeVertex(String label)
		{
			Vertex v=new Vertex(label);
			
			adjVertices.values().stream().forEach(e -> e.remove(v));
			adjVertices.remove(new Vertex(label));
		}
		
		public void addEdge(String label1, String label2)
		{
			Vertex v1=new Vertex(label1);
			Vertex v2=new Vertex(label2);
			
			adjVertices.get(v1).add(v2);
			adjVertices.get(v2).add(v1);
		}
		
		public void removeEdge(String label1, String label2)
		{
			Vertex v1=new Vertex(label1);
			Vertex v2=new Vertex(label2);
			
			List<Vertex> eV1=adjVertices.get(v1);
			List<Vertex> eV2=adjVertices.get(v2);
			
			if(eV1 != null)
				eV1.remove(v2);
			if(eV2 != null)
				eV2.remove(v1);
		}
	}
}