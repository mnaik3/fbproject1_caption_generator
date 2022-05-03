package com.example.Entity;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class AnnotationEntity {
	
	private String label;
	private String score;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

}
