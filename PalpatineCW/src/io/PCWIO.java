package io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import manager.PCWManager;

public class PCWIO {

	public static PCWManager readFile(int fileNumber) {
		File file;
		if (fileNumber == 0) {
			file = new File("original/data.txt");
		} else {
			file = new File("save" + fileNumber + "/data.txt");
		}
		Scanner in;
		try {
			in = new Scanner(file);
		} catch (IOException e) {
			//TODO this shouldn't happen, but I will still make a handle for it
		}
		//TODO read in general condition data
		//TODO read in Side data
		//TODO read in Palpatine's personal data
		//TODO read in Interest Point data
		//TODO read in World data (and add Interest Points, and assign to Sides)
		//TODO read in Utility Set data
		//TODO read in General data (and assign Interest Points)
		//TODO read in Unit data (and assign to Generals)
		//TODO read in Politician data (and assign Interest Points)
		return null;
	}
	
	public static boolean saveGame(int fileNumber) {
		File file = new File("save" + fileNumber + "/data.txt");
		PrintStream in;
		try {
			in = new PrintStream(file);
		} catch (IOException e) {
			//TODO this shouldn't happen, but I will still make a handle for it
		}
		//TODO write everything that's read in the readFile method
		return false;
	}
}
