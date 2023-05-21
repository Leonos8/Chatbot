package NLP;

import java.util.Random;

public class BiLSTM 
{
	int inputSize;
	int hiddenSize;
	
	//Weights and biases for forward LSTM
	private double[][] forwardWf; //Weight matrix for forget gate
	private double[][] forwardWi; //Weight matrix for input gate
	private double[][] forwardWc; //Weight matrix for cell state
	private double[][] forwardWo; //Weight matrix for output gate
	private double[] forwardbf; //Bias vector for forget gate
	private double[] forwardbi; //Bias vector for input gate
	private double[] forwardbc; //Bias vector for cell state
	private double[] forwardbo; //Bias vector for output gate
	
	//Weights and biases for backward LSTM
	private double[][] backwardWf; //Weight matrix for forget gate
	private double[][] backwardWi; //Weight matrix for input gate
	private double[][] backwardWc; //Weight matrix for cell state
	private double[][] backwardWo; //Weight matrix for output gate
	private double[] backwardbf; //Bias vector for forget gate
	private double[] backwardbi; //Bias vector for input gate
	private double[] backwardbc; //Bias vector for cell state
	private double[] backwardbo; //Bias vector for output gate
	
	//This will have to add backward
	public BiLSTM(int inputSize, int hiddenSize)
	{
		this.inputSize=inputSize;
		this.hiddenSize=hiddenSize;
		
		forwardWf=new double[hiddenSize][inputSize+hiddenSize];
		forwardWi=new double[hiddenSize][inputSize+hiddenSize];
		forwardWc=new double[hiddenSize][inputSize+hiddenSize];
		forwardWo=new double[hiddenSize][inputSize+hiddenSize];
		forwardbf=new double[hiddenSize];
		forwardbi=new double[hiddenSize];
		forwardbc=new double[hiddenSize];
		forwardbo=new double[hiddenSize];
		
		backwardWf=new double[hiddenSize][inputSize+hiddenSize];
		backwardWi=new double[hiddenSize][inputSize+hiddenSize];
		backwardWc=new double[hiddenSize][inputSize+hiddenSize];
		backwardWo=new double[hiddenSize][inputSize+hiddenSize];
		backwardbf=new double[hiddenSize];
		backwardbi=new double[hiddenSize];
		backwardbc=new double[hiddenSize];
		backwardbo=new double[hiddenSize];
		
		/*
		 * Initialize weights and biases with appropriate values
		 * (Initialization depends on the specific implementation and initialization strategy)
		 */
		
		forwardWf=initializeWeightMatrix(hiddenSize, inputSize);
		forwardWi=initializeWeightMatrix(hiddenSize, inputSize);
		forwardWc=initializeWeightMatrix(hiddenSize, inputSize);
		forwardWo=initializeWeightMatrix(hiddenSize, inputSize);
		forwardbf=initializeBiasVector(hiddenSize);
		forwardbi=initializeBiasVector(hiddenSize);
		forwardbc=initializeBiasVector(hiddenSize);
		forwardbo=initializeBiasVector(hiddenSize);
		
		backwardWf=initializeWeightMatrix(hiddenSize, inputSize);
		backwardWi=initializeWeightMatrix(hiddenSize, inputSize);
		backwardWc=initializeWeightMatrix(hiddenSize, inputSize);
		backwardWo=initializeWeightMatrix(hiddenSize, inputSize);
		backwardbf=initializeBiasVector(hiddenSize);
		backwardbi=initializeBiasVector(hiddenSize);
		backwardbc=initializeBiasVector(hiddenSize);
		backwardbo=initializeBiasVector(hiddenSize);
	}
	
	public double[] add(double[] v1, double[] v2)
	{
		double[] result=new double[v1.length];
		
		for(int i=0; i<result.length; i++)
		{
			result[i]=v1[i]+v2[i];
		}
		
		return result;
	}
	
	private double[] calculateGate(double[] input, double[] hiddenState, 
			double[][] weightMatrix, double[] biasVector)
	{
		int inputSize=input.length;
		int hiddenSize=hiddenState.length;
		
		System.out.println(weightMatrix[1].length);
		
		double[] gate=new double[hiddenSize];
		
	    // Perform matrix multiplication: gate = sigmoid(W * [input, hiddenState] + bias)
		for(int i=0; i<hiddenSize; i++)
		{
			double weightedSum=0.0;
			
			//Perform dot product of weights and inputs
			for(int j=0; j<inputSize; j++)
			{
				weightedSum+=weightMatrix[i][j]*input[j];
			}
			
			//Add the contribution from the hidden state
			weightedSum+=weightMatrix[i][inputSize]*hiddenState[i];
			
			//Apply sigmoid activation function
			gate[i]=sigmoid(weightedSum+biasVector[i]);
		}
		
		return gate;
	}
	
	public double[][] computeForwardHiddenStates(double[][] inputSequence)
	{
		int sequenceLength=inputSequence.length;
		double[][] forwardHiddenStates=new double[sequenceLength][hiddenSize];
		
		double[][] forwardCellStates=new double[sequenceLength][hiddenSize];
		double[] forwardHiddenState=new double[hiddenSize];
		double[] forwardCellState=new double[hiddenSize];
		
		for(int ts=0; ts<sequenceLength; ts++)
		{
			double[] input=inputSequence[ts];
			
			//Calculate forward LSTM gates
			double[] forgetGate=calculateGate(input, forwardHiddenState, forwardWf, forwardbf);
			double[] inputGate=calculateGate(input, forwardHiddenState, forwardWi, forwardbi);
			double[] cellGate=calculateGate(input, forwardHiddenState, forwardWc, forwardbc);
			double[] outputGate=calculateGate(input, forwardHiddenState, forwardWo, forwardbo);
			
			//Update forward cell state
			forwardCellState=multiply(forgetGate, forwardCellState);//Is multiply the dot product?
			forwardCellState=add(multiply(inputGate, cellGate), forwardCellState);
			
			//Update forward hidden state
			forwardHiddenState=multiply(outputGate, tanh(forwardCellState));
			
			//Store forward hidden state
			forwardHiddenStates[ts]=forwardHiddenState;
			forwardCellStates[ts]=forwardCellState;
		}
		
		return forwardHiddenStates;
	}
	
	public double[][] computeBackwardHiddenStates(double[][] inputSequence)
	{
		int sequenceLength=inputSequence.length;
		double[][] backwardHiddenStates=new double[sequenceLength][hiddenSize];
		
		double[][] backwardCellStates=new double[sequenceLength][hiddenSize];
		double[] backwardHiddenState=new double[hiddenSize];
		double[] backwardCellState=new double[hiddenSize];
		
		for(int ts=sequenceLength-1; ts>=0; ts++)
		{
			double[] input=inputSequence[ts];
			
			//Calculate backward LSTM gates
			double[] forgetGate=calculateGate(input, backwardHiddenState, backwardWf, backwardbf);
			double[] inputGate=calculateGate(input, backwardHiddenState, backwardWi, backwardbi);
			double[] cellGate=calculateGate(input, backwardHiddenState, backwardWc, backwardbc);
			double[] outputGate=calculateGate(input, backwardHiddenState, backwardWo, backwardbo);
			
			//Update backward cell state
			backwardCellState=multiply(forgetGate, backwardCellState);
			backwardCellState=add(multiply(inputGate, cellGate), backwardCellState);
			
			//Update backward hidden state
			backwardHiddenState=multiply(outputGate, tanh(backwardCellState));
			
			//Store backward hidden state
			backwardHiddenStates[ts]=backwardHiddenState;
			backwardCellStates[ts]=backwardCellState;
		}
		
		return backwardHiddenStates;
	}
	
	/*public double[][][] concatenateHiddenStates(double[][] forwardHiddenStates, double[][] backwardHiddenStates)
	{
		//double[][]][] hiddenStates=new double[forwardHiddenStates.length]
	}*/
	
	public double[][] initializeWeightMatrix(int rows, int cols)
	{
		double[][] weights=new double[rows][cols];
		
		Random random=new Random();		
		double stdDev=.01; //Standard deviation for weight initialization
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<cols; j++)
			{
				weights[i][j]=random.nextGaussian()*stdDev;
			}
		}
		
		return weights;
	}
	
	public double[] initializeBiasVector(int size)
	{
		double[] bias=new double[size];
		
		Random random=new Random();		
		double stdDev=.01; //Standard deviation for bias initialization
		
		for(int i=0; i<size; i++)
		{
			bias[i]=random.nextDouble()*stdDev;
		}
		
		return bias;
	}
	
	private double[] multiply(double[] v1, double[] v2)
	{
		double[] result=new double[v1.length];
		
		for(int i=0; i<result.length; i++)
		{
			result[i]=v1[i]*v2[i];
		}
		
		return result;
	}
	
	private double sigmoid(double x)
	{
		return 1.0/(1.0+Math.exp(-x));
	}
	
	public double[] tanh(double[] vector)
	{
		double[] result=new double[vector.length];
		
		for(int i=0; i<result.length; i++)
		{
			result[i]=Math.tanh(vector[i]);
		}
		
		return result;
	}
}