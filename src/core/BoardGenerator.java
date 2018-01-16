package core;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import core.Board;

/**
 * Classe relativa al BoardGenerator
 * @author Mina
 *
 */

public class BoardGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board[] boardCollection = new Board[200];
		//Generate boards (9 dead fields each, no guarantee that in terms of 
		//duplicates).
		for(int i = 0; i < 200; ++i) {
			boardCollection[i] = new Board(1, true, 3);
		}

		//Save to the file.
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream("200_boards_3.sav");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(boardCollection);
			oos.close();
		} catch(Exception e) {
			System.err.println("Error" + e.getMessage());
		} finally {
			   if (fos != null) {
	               try {
	            	   fos.close (); 
	               } catch (java.io.IOException e3) {
	                 System.out.println("I/O Exception");
	               }	
	           	}	
		}
}
}

