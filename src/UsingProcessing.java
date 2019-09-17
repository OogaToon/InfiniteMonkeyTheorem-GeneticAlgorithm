import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;

public class UsingProcessing extends PApplet{

	PFont f;
	String goal = "To be or not to be that is the question";
	int maximumFitness = goal.length()*goal.length();
	int populationSize = 1500;
	int generation = 0;
	ArrayList<Sentence> population = new ArrayList<>();
	String bestFit = "";
	float maxFitness = (float) 0;
	ArrayList<Sentence> matingPool = new ArrayList<>();

	public static void main(String[] args) {
		PApplet.main("UsingProcessing");

	}

	public void settings(){
		size(800,800);
	}

	public void setup(){
		f = createFont("Arial", 16, true);
		setInitialPopulation();
	}

	public void draw(){
		background(255);
		textFont(f, 16);
		fill(0);
		for(int i=0; i<populationSize; i++) {
			textAlign(RIGHT);
			text(population.get(i).sentence, 750, 25*i+25);
		}
		textAlign(LEFT);
		generation++;
		setFitness();
		makeMatingPool();
		selection();
		evaluate();
		text("Target sequence:", 10, 70);
		text(goal, 10, 100);
		text("Best fit:", 10, 200);
		text(bestFit, 10, 230);
		text("Max fitness score: "+maxFitness, 10, 270);
		text("Generations: " + generation, 10, 300);
		text("Population: " + populationSize, 10, 330);
		matingPool.clear();
	}

	void evaluate() {
		if(maxFitness >= 1.0) {
			noLoop();
		}
	}
	
	void makeMatingPool() {
		for(int i=0; i<populationSize; i++) {
			for(int j=0; j<population.get(i).fitnessScore*100; j++) {
				matingPool.add(new Sentence(population.get(i).sentence));
			}
		}
	}
	
	void selection(){
		ArrayList<Sentence> newPopulation = new ArrayList();
		for(int i=0; i<populationSize; i++){
			Sentence parentA = selectParent();
			Sentence parentB = selectParent();
			Sentence newChild = crossOver(parentA, parentB);
			newChild.sentence = mutate(newChild.sentence, (float) 0.01);
			newPopulation.add(newChild);
		}
		population.clear();
		population = newPopulation;
	}

	String mutate(String sentence, float mutationRate) {
		StringBuilder s = new StringBuilder();  
		float r;
		for(int i=0; i<goal.length(); i++) {
			r = random(1);
			if(r<mutationRate) {
				s.append(randomChar());
			} else {
				s.append(sentence.charAt(i));
			}
		}
		return s.toString();
	}

	char randomChar(){
		Random rnd = new Random();
		char c = (char) floor(random(32, 124));
		return c;
	}


	Sentence crossOver(Sentence parentA, Sentence parentB) {
		int splitPoint = floor(random(goal.length()));
		StringBuilder crossOverSentence = new StringBuilder();
		for(int i=0; i<goal.length(); i++) {
			if(i<splitPoint) {
				crossOverSentence.append(parentA.sentence.charAt(i));
			} else {
				crossOverSentence.append(parentB.sentence.charAt(i));
			}
		}
		String s = crossOverSentence.toString();
		return new Sentence(s);
	}

	Sentence selectParent(){
		int r = floor(random(matingPool.size()));
		return matingPool.get(r);
		
	}

	void setFitness(){
		for(int i=0; i<populationSize; i++) {
			population.get(i).fitnessScore = fitness(population.get(i).sentence);
			if(maxFitness<population.get(i).fitnessScore) {
				maxFitness = population.get(i).fitnessScore;
				bestFit = population.get(i).sentence;
			}
		}
	}

	float fitness(String sentence){
		int fitness = 0;
		for(int i=0; i<sentence.length(); i++) {
			if(sentence.charAt(i) == goal.charAt(i)) {
				fitness ++; 
			}
		}
		return ((float)fitness*fitness/maximumFitness);
	}


	void setInitialPopulation(){

		for(int i=0; i<populationSize; i++) {
//			int r = floor(random(5));
//			population.add(new Sentence(sentences[r].sentence));
			population.add(newSentence());
		}
	}

	private Sentence newSentence() {
		StringBuilder s = new StringBuilder();
		for(int i=0; i<goal.length(); i++) {
			s.append((char) floor(random(32, 124)));
		}
		return new Sentence(s.toString());
	}

}
