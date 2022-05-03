package com.example.Entity;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class words {
	
	public words() {
		
		 nouns = new ArrayList<String>();
		 verbs = new ArrayList<String>();
		 adverbs = new ArrayList<String>();
		 adjectives = new ArrayList<String>();
		
		setNouns();
		setAdjectives();
		setAdverb();
		setVerb();
	}
	
	private List<String> nouns;
	private List<String> verbs;
	private List<String> adverbs;
	private List<String> adjectives;
	
	public String getAdjective(int index){
		return adjectives.get(index);
	}
	
	public String getVerb(int index){
		return verbs.get(index);
	}
	
	public String getAdverb(int index){
		return adverbs.get(index);
	}
	
	public String getNoun(int index){
		return nouns.get(index);
	}
	
	private void setAdjectives() {
		adjectives.add("pretty");
		adjectives.add("new");
		adjectives.add("first");
		adjectives.add("last");
		adjectives.add("long");
		adjectives.add("little");
		adjectives.add("own");
		adjectives.add("other");
		adjectives.add("old");
		adjectives.add("smart");
		adjectives.add("big");
		adjectives.add("delicious");
		adjectives.add("different");
		adjectives.add("small");
		adjectives.add("cute");
		adjectives.add("next");
		adjectives.add("young");
		adjectives.add("few");
		adjectives.add("peace");
		adjectives.add("same");
		adjectives.add("colourful");
	}
	
	private void setAdverb() {
		adverbs.add("up");
		adverbs.add("so");
		adverbs.add("out");
		adverbs.add("just");
		adverbs.add("now");
		adverbs.add("how");
		adverbs.add("then");
		adverbs.add("more");
		adverbs.add("also");
		adverbs.add("here");
		adverbs.add("well");
		adverbs.add("only");
		adverbs.add("very");
		adverbs.add("even");
		adverbs.add("back");
		adverbs.add("there");
		adverbs.add("down");
		adverbs.add("still");
		adverbs.add("in");
		adverbs.add("as");
		adverbs.add("too");
		adverbs.add("when");
		adverbs.add("never");
		adverbs.add("really");
		adverbs.add("most");
	}
	
	private void setVerb() {
		verbs.add("be");
		verbs.add("have");
		verbs.add("do");
		verbs.add("say");
		verbs.add("get");
		verbs.add("make");
		verbs.add("go");
		verbs.add("know");
		verbs.add("drink");
		verbs.add("see");
		verbs.add("happy");
		verbs.add("think");
		verbs.add("look");
		verbs.add("want");
		verbs.add("eat");
		verbs.add("use");
		verbs.add("study");
		verbs.add("enjoy");
		verbs.add("read");
		verbs.add("run");
		verbs.add("laugh");
		verbs.add("feel");
		verbs.add("try");
		verbs.add("fly");
		verbs.add("play");
		verbs.add("is");
		verbs.add("are");
	}
	
	private void setNouns() {
		nouns.add("Evening");
		nouns.add("Morning");
		nouns.add("Pen");
		nouns.add("Hair");
		nouns.add("Books");
		nouns.add("Nature");
		nouns.add("Pizza");
		nouns.add("Planet");
		nouns.add("Apple");
		nouns.add("Party");
		nouns.add("Hand");
		nouns.add("Holiday");
		nouns.add("Butterfly");
		nouns.add("Park");
		nouns.add("Potato");
		nouns.add("Trees");
		nouns.add("Festival");
		nouns.add("Grass");
		nouns.add("Colours");
		nouns.add("House");
		nouns.add("Rain");
		nouns.add("Food");
		nouns.add("Rainbow");
		nouns.add("Kids");
		nouns.add("Friends");
		nouns.add("Holi");
		nouns.add("Bed");
		nouns.add("Insect");
		nouns.add("Students");
		nouns.add("University");
		nouns.add("Restaurant");

	}

}
