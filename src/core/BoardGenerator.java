package core;

import java.io.FileOutputStream;
import java.io.IOException;
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Board[] boardCollection = new Board[200];
		//Generate boards (9 dead fields each, no guarantee that in terms of 
		//duplicates).
		int valueboardGB1 = 1;
		boolean valueboardGB2 = true;
		int valueboardGB3 = 3;
		
		for(int i = 0; i < 200; ++i) {
			boardCollection[i] = new Board(valueboardGB1, valueboardGB2, valueboardGB3);
		}

		//Save to the file.
	
			FileOutputStream fos = new FileOutputStream("200_boards_3.sav");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(boardCollection);
			
			oos.close();
			oos.flush();
			fos.close();
			fos.flush();
		
	}

}
