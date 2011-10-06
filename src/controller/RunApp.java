package controller;

import model.ReadPDF;
import view.View;

public class RunApp {
	
	public static void main(String[] args) throws Exception {
		ReadPDF pdf = new ReadPDF();
		View view = new View(pdf);
	}

}
