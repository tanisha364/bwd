package com.bwd.bwd.response;

import java.beans.JavaBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JavaBean
public class PdfReport {

	private String capability;
    private String text;
    private String weight;
    private String left;
    private String right;
    private int score;
    private int sequence;
    private String color1;
    private String color2;
    private String color3;
    private String color4;
    private String color5;
    private String color6;
    
	public PdfReport() {

	}

	
}
