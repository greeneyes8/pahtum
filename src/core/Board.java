package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Tuple;

/**
 * Board used in the game. Denotes current stage of the game with distribution 
 * of the pieces in respect to their color, as well as black holes.
 * @author kg687
 *
 */
public class Board implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[][] board;

	/**
	 * Initialize a new Board object.
	 * @param state State, select (1) for standard settings. (2) and (3) used 
	 * only for testing purpose.
	 * @param random Select (True) if you want to allow program to select black 
	 * holes at random, otherwise (False). The latter causes questions which aim 
	 * to specify exact locations.
	 * @param numberBlackHoles Number of black holes.
	 */
	public Board(int state, boolean random, int numberBlackHoles) {
		Random generator = new Random();
		switch(state) {
		case 1:
			board = new String[7][7];
			for(int x = 0; x < 7; ++x) {
				for(int y = 0; y < 7; ++y) {
					board[x][y] = "e";
				}
			}
			halfCase1 ( random,  numberBlackHoles,
					 generator );
			break;	
		case 2:
			String[][] asd = {
					{"b", "w", "w", "b", "b", "b", "b"},
					{"b", "e", "e", "e", "e", "e", "e"},
					{"b", "e", "e", "e", "e", "e", "e"},
					{"w", "e", "e", "e", "e", "e", "e"},
					{"w", "e", "e", "e", "e", "e", "e"},
					{"w", "e", "e", "e", "e", "e", "e"},
					{"w", "e", "e", "e", "e", "e", "e"},
			};
			board = asd;
			break;
		case 3:
			String[][] qwe = {
					{"x", "b", "w", "b", "w", "b", "w"},
					{"b", "w", "b", "w", "b", "w", "b"},
					{"w", "b", "w", "b", "w", "b", "e"},
					{"b", "w", "b", "w", "b", "w", "e"},
					{"w", "b", "w", "b", "w", "w", "e"},
					{"b", "w", "b", "w", "b", "b", "w"},
					{"w", "b", "w", "b", "b", "w", "b"},
			};
			board = qwe;
			break;
		 default:
		}

	}

	/**
	 * Make move on given field with given color if the field is not occupied.
	 * @param filed Destination field.
	 * @param color Color of a piece to move.
	 * @throws Exception Throws exception if the field is already occupied 
	 * (or is a black hole).
	 */
	public void makeMove(Tuple<Integer, Integer> filed, String color) 
	throws Exception {
		if(board[filed.getFirstElement()][filed.getSecondElement()].equals("e")) 
		{
			board[filed.getFirstElement()][filed.getSecondElement()] = color;
		} else {
			throw new Exception("Filed is occupied.");
		}
	}

	/**
	 * Duplicate of the board (the same as Python's "deep copy").
	 * @return Board's replica as a new object.
	 */
	public Board duplicate() {
		Board replica = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			
			byte [] byteData = bos.toByteArray();
			bos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			replica = (Board) new ObjectInputStream(bais).readObject();
			bais.close();
		} catch(Exception e) { 
			
			System.out.println("Something was wrong");  
		
		}
		return replica;
	}

	/**
	 * Generate list of all valid moves for current position.
	 * @return List of valid moves.
	 */
	public List<Tuple<Integer, Integer>> getListValidMoves() {
		ArrayList<Tuple<Integer, Integer>> list = 
			new ArrayList<Tuple<Integer, Integer>>();
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if(board[x][y].equals("e")) {
					list.add(new Tuple<Integer, Integer>(x, y));
				}
			}
		}
		return list;
	}
	
	/**
	 * Get potential evaluation of given field provided by Charles_2 heuristic.
	 * @param x Row.
	 * @param y Column.
	 * @param color Color to play
	 * @return Potential of the selected point if empty, -1 otherwise
	 */
	public int getHeuristicValue(int x, int y, String color) {
		int index_x = x, index_y = y;
		int potential = 0;
		int distance = 7;
		boolean case_1 = true, case_2 = true, case_3 = true, case_4 = true;
		int horizontal_adjustment = 1, vertical_adjustment = 1;
		// (-,0) direction. 
		
			 try_1 (board,  index_x,  index_y,
					color,  case_1,  potential,  distance,
					 vertical_adjustment);
			
		

		// (0,-) direction.
		index_x = x;
		index_y = y;
		distance = 7;
		
			try_2 ( board,  index_x,  index_y,
					color,  case_2,  potential,  distance,
					 horizontal_adjustment);
			
		

		// (0,+) direction.
		index_x = x;
		index_y = y;
		distance = 7;
		
			try_3 (board,  index_x,  index_y,
					 color,  case_3,  potential,  distance,
					 horizontal_adjustment);
			
		

		// (+,0) direction.
		index_x = x;
		index_y = y;
		distance = 7;
		
			try_4 ( board,  index_x,  index_y,
					 color,  case_4,  potential,  distance,
					 vertical_adjustment);
		

		// Update of adjustments.
		UpdateOfAdjustments (horizontal_adjustment,  potential  );
		UpdateOfAdjustments (vertical_adjustment,  potential  );

		

		// Adjustment for the opponent.
		String enemyColor = null;
		String w = "w";
		
		AdjustmentForTheOpponent ( color ,  enemyColor,	 w);

		index_x = x;
		index_y = y;

		int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
		
			try_5 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_x  );
		

		index_x = x;
		index_y = y;
	
			try_6 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_x  );

	

		index_x = x;
		index_y = y;
		
			try_7 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_y );
		

		index_x = x;
		index_y = y;
	
			try_8 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_x );
		
		
		UpdateOfAdjustments (enemy_adjustment_x,  potential  );
		UpdateOfAdjustments (enemy_adjustment_y,  potential  );
		

		// End of enemy's adjustment.
		return potential;

	}

	/**
	 * Generate list of X best move in respect to Charles_2's heuristic . X is 
	 * specified as a parameter (MAGIC_NUMBER).
	 * @param color Color of which next move will be made.
	 * @param MAGIC_NUMBER Number of moves to select.
	 * @return
	 */
	public List<Tuple<Integer, Integer>> heuristic_bestX_moves(String color, 
			int MAGIC_NUMBER) {
		List<Tuple<Integer, Integer>> list = 
			new ArrayList<Tuple<Integer,Integer>>();

		List<Tuple<Integer, Tuple<Integer, Integer>>> ratingList = 
			new ArrayList<Tuple<Integer,Tuple<Integer,Integer>>>();

		// Check if board is almost full.
		int countE = 0;
		CheceIfBoardIsAlmostFull (countE) ;
		
		if(countE < MAGIC_NUMBER) {
			return getListValidMoves();
		}


		//  There are quite few empty fields left.
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if(board[x][y].equals("e")) {
					// Empty field => calculate potential.
					int index_x = x, index_y = y;
					int potential = 0;
					int distance = 7;
					boolean case_1 = true, case_2 = true, case_3 = true, 
					case_4 = true;
					int horizontal_adjustment = 1, vertical_adjustment = 1;
					// (-,0) direction. 
					
					tryEmptyField1 ( index_x,  index_y,  color
							, case_1,  potential,  distance
							, vertical_adjustment );

					// (0,-) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					tryEmptyField2 ( index_x,  index_y,  color
							, case_2,  potential,  distance
							, horizontal_adjustment ); 

					// (0,+) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					tryEmptyField3 ( index_x,  index_y,  color
							, case_3,  potential,  distance
							, horizontal_adjustment );

					// (+,0) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					tryEmptyField4 ( index_x,  index_y,  color
							, case_4,  potential,  distance
							,vertical_adjustment );

					// Update of adjustments.
					 UpdateOfAdjustments (horizontal_adjustment,  potential  );
					
					UpdateOfAdjustments (vertical_adjustment,  potential  );
					

					// Adjustment for the opponent.
					String enemyColor=null;
					String w = "w";
									
					AdjustmentForTheOpponent ( color ,  enemyColor, w);
					index_x = x;
					index_y = y;

					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					
					 adjustmentForTheOpponentT1 ( board,  index_x,  index_y
								, enemyColor,  enemy_adjustment_x);

					index_x = x;
					index_y = y;
					
					adjustmentForTheOpponentT2 (board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x);

					index_x = x;
					index_y = y;
					
					adjustmentForTheOpponentT3 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y);

					index_x = x;
					index_y = y;
					
					adjustmentForTheOpponentT4 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y);
					
					UpdateOfAdjustments ( enemy_adjustment_x,  potential  );			
					UpdateOfAdjustments ( enemy_adjustment_y,  potential  );
					

					// End of enemy's adjustment.

					// Add to rating list.
					ratingList.add(new Tuple<Integer, Tuple<Integer, Integer>>(
							potential, new Tuple<Integer, Integer>(x, y)));


				} // end of if. check if field is empty.
			} // end of for (y)
		} // end of for (x)

		//Compose final selection based on calculated potentials for each field.
		ArrayList<Tuple<Integer, Integer>> finalList = 
			new ArrayList<Tuple<Integer, Integer>>();
		for(int i = 0; i < MAGIC_NUMBER; ++i) {
			
			composeFinal ( ratingList,finalList );
		}

		return finalList;

	}


	/**
	 * Calculate potential for all fields and return list of all of them. Used 
	 * by Charles agent.
	 * @param color Your color.
	 * @return
	 */
	public ArrayList<Tuple<Integer, Integer>> heuristic_adjustment_listMoves(String color) {
		ArrayList<Tuple<Integer, Integer>> list = new ArrayList<Tuple<Integer,Integer>>();

		int tmpPotential = -1;
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if(board[x][y].equals("e")) {
					// Empty field => calculate potential.
					int index_x = x, index_y = y;
					int potential = 0;
					int distance = 7;
					boolean case_1 = true, case_2 = true, case_3 = true, case_4 = true;
					int horizontal_adjustment = 1, vertical_adjustment = 1;
					// (-,0) direction. 
					
					 direction1 ( board,  index_x,  index_y
								,  case_1,  potential,  distance,  vertical_adjustment,  color);

					// (0,-) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					direction2 ( board,  index_x,  index_y
							,  case_2,  potential,  distance,  horizontal_adjustment,  color);

					// (0,+) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					direction3 (board,  index_x,  index_y
							,  case_3,  potential,  distance,  horizontal_adjustment,  color);

					// (+,0) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					direction4 ( board,  index_x,  index_y
							,  case_4,  potential,  distance,  vertical_adjustment,  color);

					// Update of adjustments.
					UpdateOfAdjustments ( horizontal_adjustment,  potential  );			
					UpdateOfAdjustments ( vertical_adjustment,  potential  );
				

					// Adjustment for the opponent.
					String enemyColor = null;
					String w = "w";
					
					AdjustmentForTheOpponent ( color ,  enemyColor, w);

					//					boolean extra_x_minus = false, extra_x_plus = false,
					//					extra_y_minus = false, extra_y_plus = false;

					index_x = x;
					index_y = y;

					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					enemyAdjustment1 ( board,  enemy_adjustment_x
							, index_x,  index_y,  enemyColor);

					index_x = x;
					index_y = y;
					
					enemyAdjustment2 (board,  enemy_adjustment_x
							, index_x,  index_y,  enemyColor);

					index_x = x;
					index_y = y;
					
					enemyAdjustment3 ( board,  enemy_adjustment_y
							, index_x,  index_y,  enemyColor);

					index_x = x;
					index_y = y;
					
					enemyAdjustment4 ( board,  enemy_adjustment_y
							, index_x,  index_y,  enemyColor);

					//					if(extra_x_minus && extra_x_plus) {
					//						++enemy_adjustment_x;
					//					}
					
					UpdateOfAdjustments ( enemy_adjustment_x,  potential  );			
					
					

					//					if(extra_y_minus && extra_y_plus) {
					//						++enemy_adjustment_y;
					//					}
					UpdateOfAdjustments ( enemy_adjustment_y,  potential  );
					

					// End of enemy's adjustment.

					if(potential > tmpPotential) {
						// New higher potential => dismiss all previous findings and add current.
						tmpPotential = potential;
						list.clear();
						list.add(new Tuple<Integer, Integer>(x, y));
					} else if(potential == tmpPotential) {
						// Another field with the same (the highest) score => add.
						list.add(new Tuple<Integer, Integer>(x, y));
					}
				} // end of if.
			} // end of for (y)
		} // end of for (x)

		return list;
	}

	/**
	 * Another list...
	 * @deprecated
	 * @param color
	 * @return
	 */
	public ArrayList<Tuple<Integer, Integer>> heuristic_listMoves(String color) {
		ArrayList<Tuple<Integer, Integer>> list = new ArrayList<Tuple<Integer,Integer>>();

		int tmpPotential = -1;
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if(board[x][y].equals("e")) {
					// Empty field => calculate potential.
					int index_x = x, index_y = y;
					int potential = 0;
					int distance = 7;
					boolean case_1 = true, case_2 = true, case_3 = true, case_4 = true;
					int horizontal_adjustment = 1, vertical_adjustment = 1;
					// (-,0) direction. 
					directionMovieList1 ( board,  index_x,  index_y ,  color
							,  case_1,  potential,  distance,  vertical_adjustment) ;

					// (0,-) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					 directionMovieList2 ( board,  index_x,  index_y ,  color
								,  case_2,  potential,  distance,  horizontal_adjustment);
					// (0,+) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					directionMovieList3 ( board,  index_x,  index_y ,  color
							,  case_3,  potential,  distance,  horizontal_adjustment);

					// (+,0) direction.
					index_x = x;
					index_y = y;
					distance = 7;
					
					directionMovieList4 ( board,  index_x,  index_y ,  color
							,  case_4,  potential,  distance,  vertical_adjustment);

					// Update of adjustments.
					UpdateOfAdjustments (horizontal_adjustment,  potential  );
					UpdateOfAdjustments (vertical_adjustment,  potential  );
					

					// Adjustment for the opponent.
					String enemyColor=null;
					String w = "w";
					
					AdjustmentForTheOpponent ( color ,  enemyColor, w);
					 
					boolean extra_x_minus = false, extra_x_plus = false,
					extra_y_minus = false, extra_y_plus = false;

					index_x = x;
					index_y = y;

					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					
					enemyAdjustmentExtra1 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x,  extra_x_minus);

					index_x = x;
					index_y = y;
					
					enemyAdjustmentExtra2 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x,  extra_x_plus);

					index_x = x;
					index_y = y;
					
					enemyAdjustmentExtra3 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y,  extra_y_minus);

					index_x = x;
					index_y = y;
					
					enemyAdjustmentExtra4 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y,  extra_y_plus);

					enemyAdjustmentDirectionXY ( extra_x_minus,  extra_x_plus 
							, enemy_adjustment_x,  potential, extra_y_minus,  extra_y_plus
							, enemy_adjustment_y);
					

					// End of enemy's adjustment.

					if(potential > tmpPotential) {
						// New higher potential => dismiss all previous findings and add current.
						tmpPotential = potential;
						list.clear();
						list.add(new Tuple<Integer, Integer>(x, y));
					} else if(potential == tmpPotential) {
						// Another field with the same (the highest) score => add.
						list.add(new Tuple<Integer, Integer>(x, y));
					}
				} // end of if.
			} // end of for (y)
		} // end of for (x)
		//		for(Tuple item : list) {
		//			System.out.println(item.toString());
		////			System.console().readLine();
		//		}
		return list;
	}



	public String[][] getState() {
		return board;
	}
	
	public void halfCase1 (boolean random, int numberBlackHoles,
			Random generator ) {
		int sevenvalue = 7;
		if(random) {
			while(numberBlackHoles > 0) {
				int coordianteX = generator.nextInt(sevenvalue);
				int coordinateY = generator.nextInt(sevenvalue);
				if(board[coordianteX][coordinateY].equals("e")) {
					board[coordianteX][coordinateY] = "x";
					--numberBlackHoles;
				}
			}
		}
	}
	
	public void UpdateOfAdjustments (int adjustment, int potential  ) {
		
		switch(adjustment) {
		case 6:
			potential += 119; break;
		case 5:
			potential += 56; break;
		case 4:
			potential += 25; break;
		case 3:
			potential += 10; break;
		case 2:
			potential += 3; break;
		case 1:
			potential += 2; break;
		default:
			break;	
		}
	}
	
	public void try_2 (String[][] board, int index_x, int index_y,
			String color, boolean case_2, int potential, int distance,
			int horizontal_adjustment) {
		
		try {
		boolean equalsE12, equalsColor12;
		equalsE12 = board[index_x][index_y - 1].equals("e");
		equalsColor12 = board[index_x][index_y - 1].equals(color);
		while(equalsE12|| 
				equalsColor12) {
			if(board[index_x][index_y - 1].equals("e")) {
				case_2 = false;
				potential += distance;
				--distance;
			} else {
				potential += distance + 2;
				--distance;
				if(case_2) {
					++horizontal_adjustment;
				}
			}
			--index_y;
			equalsE12 = board[index_x][index_y - 1].equals("e");
			equalsColor12 = board[index_x][index_y - 1].equals(color);
		}
		}	catch(Exception e) { }
	
	
		}
	
	public void try_1 (String[][] board, int index_x, int index_y,
			String color, boolean case_1, int potential, int distance,
			int vertical_adjustment) { 
		try {
			boolean 	equalsE14, equalsColor14;
		equalsE14 = board[index_x - 1][index_y].equals("e");
		equalsColor14 = board[index_x - 1][index_y].equals(color);
		while(equalsE14 || 
				equalsColor14) {
			if(board[index_x - 1][index_y].equals("e")) {
				case_1 = false;
				potential += distance;
				--distance;
			} else {
				potential += distance + 2;
				--distance;
				if(case_1) {
					++vertical_adjustment;
				}
			}
			--index_x;
			equalsE14 = board[index_x - 1][index_y].equals("e");
			equalsColor14 = board[index_x - 1][index_y].equals(color);
		}
		} catch(Exception e) { }
		
	}
	public void try_3 (String[][] board, int index_x, int index_y,
			String color, boolean case_3, int potential, int distance,
			int horizontal_adjustment) { 
		try {
		boolean equalsE13, equalsColor13;
		equalsE13 = board[index_x][index_y + 1].equals("e");
		equalsColor13 = board[index_x][index_y + 1].equals(color);
		while(equalsE13 || 
				equalsColor13) {
			if(board[index_x][index_y + 1].equals("e")) {
				case_3 = false;
				potential += distance;
				--distance;
			} else {
				potential += distance + 2;
				--distance;
				if(case_3) {
					++horizontal_adjustment;
				}
			}
			++index_y;
			equalsE13 = board[index_x][index_y + 1].equals("e");
			equalsColor13 = board[index_x][index_y + 1].equals(color);
			}
		}catch(Exception e) { }
		
	}
	public void try_4 (String[][] board, int index_x, int index_y,
			String color, boolean case_4, int potential, int distance,
			int vertical_adjustment) {
		try {
		boolean equalsE11, equalsColor11;
		equalsE11 = board[index_x + 1][index_y].equals("e");
		equalsColor11 = board[index_x + 1][index_y].equals(color);
		while(equalsE11 || 
				equalsColor11) {
			if(board[index_x + 1][index_y].equals("e")) {
				case_4 = false;
				potential += distance;
				--distance;
			} else {
				potential += distance + 2;
				--distance;
				if(case_4) {
					++vertical_adjustment;
				}
			}
			++index_x;
			equalsE11 = board[index_x + 1][index_y].equals("e");
			equalsColor11 = board[index_x + 1][index_y].equals(color);
			}
		}catch(Exception e) { }
		
	}
	
	public void try_5 (String[][] board,int index_x, int index_y , 
			String enemyColor, int enemy_adjustment_x  ) {
		try {
		boolean enemyColor12 = board[index_x - 1][index_y].equals(enemyColor);
		while(enemyColor12) {
			++enemy_adjustment_x;
			--index_x;
			enemyColor12 = board[index_x - 1][index_y].equals(enemyColor);
		}
		}
		catch(Exception e) { }
		
	}
	
	public void try_6 (String[][] board,int index_x, int index_y , 
			String enemyColor, int enemy_adjustment_x  ) {
		boolean enemyColor11 = board[index_x + 1][index_y].equals(enemyColor);
		while(enemyColor11) {
			++enemy_adjustment_x;
			++index_x;
			enemyColor11 = board[index_x + 1][index_y].equals(enemyColor);
		}
		
	}
	
	public void try_7 (String[][] board,int index_x, int index_y , 
			String enemyColor, int enemy_adjustment_y  ) {
		try {
		boolean enemyColor10 = board[index_x][index_y - 1].equals(enemyColor);
		while(enemyColor10) {
			++enemy_adjustment_y;
			--index_y;
			enemyColor10 = board[index_x][index_y - 1].equals(enemyColor);
			}
		}catch(Exception e) { }
	}
	
	public void try_8 (String[][] board,int index_x, int index_y , 
			String enemyColor, int enemy_adjustment_y  ) {
		try {
			boolean enemyColor9 = board[index_x][index_y + 1].equals(enemyColor);
		while(enemyColor9) {
			++enemy_adjustment_y;
			++index_y;
			enemyColor9 = board[index_x][index_y + 1].equals(enemyColor);
			}
		}
		catch(Exception e) { }
		
	}
	
	public void AdjustmentForTheOpponent (String color , String enemyColor,
			String w) {
		
		if(color.equals(w)) {
			enemyColor = "b";
		} else {
			enemyColor = "w";
		}

		
	}
	
	public void CheceIfBoardIsAlmostFull (int countE) {
		
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if(board[x][y].equals("e")) {
					++countE;
				}
			}
		}
		
		
	}
	
	public void tryEmptyField1 (int index_x, int index_y, String color
			,boolean case_1, int potential, int distance
			,int vertical_adjustment ) {
		
		try {
			boolean equalsE9, equalsColor9;
			equalsE9 = board[index_x - 1][index_y].equals("e") ;
			equalsColor9 = board[index_x - 1][index_y].equals(color);
			while(equalsE9 || 
					equalsColor9) {
				if(board[index_x - 1][index_y].equals("e")) {
					case_1 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_1) {
						++vertical_adjustment;
					}
				}
				--index_x;
				equalsE9 = board[index_x - 1][index_y].equals("e") ;
				equalsColor9 = board[index_x - 1][index_y].equals(color);
			}
		} catch(Exception e) { }
		
	}
	
	public void tryEmptyField2 (int index_x, int index_y, String color
			,boolean case_2, int potential, int distance
			,int horizontal_adjustment ) {
		
		try {
			boolean equalsE8, equalsColor8;
			equalsE8 = board[index_x][index_y - 1].equals("e");
			equalsColor8 = board[index_x][index_y - 1].equals(color);
			while(equalsE8 || 
					equalsColor8) {
				if(board[index_x][index_y - 1].equals("e")) {
					case_2 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_2) {
						++horizontal_adjustment;
					}
				}
				--index_y;
				equalsE8 = board[index_x][index_y - 1].equals("e");
				equalsColor8 = board[index_x][index_y - 1].equals(color);
			}
		} catch(Exception e) { }

		
	}

	public void tryEmptyField3 (int index_x, int index_y, String color
			,boolean case_3, int potential, int distance
			,int horizontal_adjustment ) {
		
		try {
			boolean equalsE7, equalsColor7;
			equalsE7 =board[index_x][index_y + 1].equals("e");
			equalsColor7 = board[index_x][index_y + 1].equals(color);
			while(equalsE7 || 
					equalsColor7) {
				if(board[index_x][index_y + 1].equals("e")) {
					case_3 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_3) {
						++horizontal_adjustment;
					}
				}
				++index_y;
				equalsE7 =board[index_x][index_y + 1].equals("e");
				equalsColor7 = board[index_x][index_y + 1].equals(color);
			}
		} catch(Exception e) { }

		
	}
	
	public void tryEmptyField4 (int index_x, int index_y, String color
			,boolean case_4, int potential, int distance
			,int vertical_adjustment ) {
		
		try {
			boolean equalsE6, equalsColor6;
			equalsE6 = board[index_x + 1][index_y].equals("e");
			equalsColor6 = board[index_x + 1][index_y].equals(color);
			while(equalsE6|| 
					equalsColor6) {
				if(board[index_x + 1][index_y].equals("e")) {
					case_4 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_4) {
						++vertical_adjustment;
					}
				}
				++index_x;
				equalsE6 = board[index_x + 1][index_y].equals("e");
				equalsColor6 = board[index_x + 1][index_y].equals(color);
			}
		} catch(Exception e) { }

		
	}
	public static void adjustmentForTheOpponentT1 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_x) {
		
		try {
			boolean enemyColor5 =board[index_x - 1][index_y].equals(enemyColor);
			while(enemyColor5) {
				++enemy_adjustment_x;
				--index_x;
				enemyColor5 =board[index_x - 1][index_y].equals(enemyColor);
			}
		} catch(Exception e) { }	
		
	}
	
	public static void adjustmentForTheOpponentT2 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_x) {
		
		try {
			boolean enemyColor4 = board[index_x][index_y - 1].equals(enemyColor);
			while(enemyColor4) {
				++enemy_adjustment_x;
				++index_x;
				enemyColor4 = board[index_x][index_y - 1].equals(enemyColor);
			}
		} catch(Exception e) { }
	
		
	}
	
	public static void adjustmentForTheOpponentT3 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_y) {
		
		try {
			boolean enemyColor3 = board[index_x][index_y - 1].equals(enemyColor);
			while(enemyColor3) {
				++enemy_adjustment_y;
				--index_y;
				enemyColor3 = board[index_x][index_y - 1].equals(enemyColor);
			}
		} catch(Exception e) { }
	
		
	}
	
	public static void adjustmentForTheOpponentT4 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_y) {
		
		try {
			boolean enemyColor2 = board[index_x][index_y + 1].equals(enemyColor);
			while(enemyColor2) {
				++enemy_adjustment_y;
				++index_y;
				enemyColor2 = board[index_x][index_y + 1].equals(enemyColor);
			}
		} catch(Exception e) { }
	
		
	}
	
	public static void composeFinal (List<Tuple<Integer, Tuple<Integer, Integer>>> ratingList
			,ArrayList<Tuple<Integer, Integer>> finalList ) {

		double tempScore = -1;
		Tuple<Integer, Tuple<Integer, Integer>> best = null;
		for(Tuple<Integer, Tuple<Integer, Integer>> item : ratingList) {
			if(item.getFirstElement() > tempScore) { 
				best = item;
				tempScore = item.getFirstElement();
			}
		}
		finalList.add(best.getSecondElement());
		ratingList.remove(best);
	
		
	}
	
	public static void direction1 (String[][] board, int index_x, int index_y
			, boolean case_1, int potential, int distance, int vertical_adjustment, String color) {
		try {	
		boolean equalsEX_4, equalsColorsX_4;
		equalsEX_4 = board[index_x - 1][index_y].equals("e");
		equalsColorsX_4 = board[index_x - 1][index_y].equals(color);
		while(equalsEX_4|| equalsColorsX_4) {
			if(board[index_x - 1][index_y].equals("e")) {
				case_1 = false;
				potential += distance;
				--distance;
			} else {
				potential += distance + 2;
				--distance;
				if(case_1) {
					++vertical_adjustment;
				}
			}
			--index_x;
			equalsEX_4 = board[index_x - 1][index_y].equals("e");
			equalsColorsX_4 = board[index_x - 1][index_y].equals(color);
		}
	} catch(Exception e) { }
		
	}
	
	public static void direction2 (String[][] board, int index_x, int index_y
			, boolean case_2, int potential, int distance, int horizontal_adjustment, String color) {
		try {
			boolean equalsEX_3, equalsColorsX_3;
			equalsEX_3 = board[index_x][index_y - 1].equals("e") ;
			equalsColorsX_3 =  board[index_x][index_y - 1].equals(color);
			while(equalsEX_3 || equalsColorsX_3) {
				if(board[index_x][index_y - 1].equals("e")) {
					case_2 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_2) {
						++horizontal_adjustment;
					}
				}
				--index_y;
				equalsEX_3 = board[index_x][index_y - 1].equals("e") ;
				equalsColorsX_3 =  board[index_x][index_y - 1].equals(color);
			}
		} catch(Exception e) { }
		
	}
	
	public static void direction3 (String[][] board, int index_x, int index_y
			, boolean case_3, int potential, int distance, int horizontal_adjustment, String color) {
		try {
			boolean equalsEX_2, equalsColorsX_2;
			equalsEX_2 = board[index_x][index_y + 1].equals("e") ;
			equalsColorsX_2 = board[index_x][index_y + 1].equals(color);
			while(equalsEX_2 || equalsColorsX_2) {
				if(board[index_x][index_y + 1].equals("e")) {
					case_3 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_3) {
						++horizontal_adjustment;
					}
				}
				++index_y;
				equalsEX_2 = board[index_x][index_y + 1].equals("e") ;
				equalsColorsX_2 = board[index_x][index_y + 1].equals(color);
			}
		} catch(Exception e) { }

		
	}
	
	public static void direction4 (String[][] board, int index_x, int index_y
			, boolean case_4, int potential, int distance, int vertical_adjustment, String color) {
		try {
			boolean equalsEX_1, equalsColorsX_1;
			equalsEX_1 = board[index_x + 1][index_y].equals("e") ;
			equalsColorsX_1 =board[index_x + 1][index_y].equals(color);
			while(equalsEX_1|| equalsColorsX_1) {
				if(board[index_x + 1][index_y].equals("e")) {
					case_4 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_4) {
						++vertical_adjustment;
					}
				}
				++index_x;
				equalsEX_1 = board[index_x + 1][index_y].equals("e") ;
				equalsColorsX_1 =board[index_x + 1][index_y].equals(color);
			}
		} catch(Exception e) { }

		
	}
	
	public void enemyAdjustment1 (String[][] board, int enemy_adjustment_x
			,int index_x, int index_y, String enemyColor) {
		try {
			boolean enemyColor_3 = board[index_x - 1][index_y].equals(enemyColor);
			while(enemyColor_3) {
				++enemy_adjustment_x;
				--index_x;
				enemyColor_3 = board[index_x - 1][index_y].equals(enemyColor);
			}
			//						if(board[index_x - 1][index_y].equals("e")) {
			//							extra_x_minus = true;
			//						}
		} catch(Exception e) { }
		
	}
	
	public void enemyAdjustment2 (String[][] board, int enemy_adjustment_x
			,int index_x, int index_y, String enemyColor) {
		try {
			boolean enemyColors_2 = board[index_x + 1][index_y].equals(enemyColor);
			while(enemyColors_2) {
				++enemy_adjustment_x;
				++index_x;
				enemyColors_2 = board[index_x + 1][index_y].equals(enemyColor);
			}
			//						if(board[index_x + 1][index_y].equals("e")) {
			//							extra_x_plus = true;
			//						}
		} catch(Exception e) { }
		
	}
	
	public void enemyAdjustment3 (String[][] board, int enemy_adjustment_y
			,int index_x, int index_y, String enemyColor) {
		try {
			boolean enemyColors_1 = board[index_x][index_y - 1].equals(enemyColor);
			while(enemyColors_1) {
				++enemy_adjustment_y;
				--index_y;
				enemyColors_1 = board[index_x][index_y - 1].equals(enemyColor);
			}
			//						if(board[index_x][index_y - 1].equals("e")) {
			//							extra_y_minus = true;
			//						}
		} catch(Exception e) { }
	}
	
	public void enemyAdjustment4 (String[][] board, int enemy_adjustment_y
			,int index_x, int index_y, String enemyColor) {
		try {
			boolean enemyColors_X = board[index_x][index_y + 1].equals(enemyColor);
			while(enemyColors_X) {
				++enemy_adjustment_y;
				++index_y;
				enemyColors_X = board[index_x][index_y + 1].equals(enemyColor);
			}
			//						if(board[index_x][index_y + 1].equals("e")) {
			//							extra_y_plus = true;
			//						}
		} catch(Exception e) { }
	}
	
	public void directionMovieList1 (String[][] board, int index_x, int index_y , String color
			, boolean case_1, int potential, int distance, int vertical_adjustment) {
		try {
			boolean equalsMinus, equalsColorMinus;
			equalsMinus = board[index_x - 1][index_y].equals("e") ;
			equalsColorMinus =  board[index_x - 1][index_y].equals(color);
			while(equalsMinus || equalsColorMinus) {
				if(board[index_x - 1][index_y].equals("e")) {
					case_1 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_1) {
						++vertical_adjustment;
					}
				}
				--index_x;
				equalsMinus = board[index_x - 1][index_y].equals("e") ;
				equalsColorMinus =  board[index_x - 1][index_y].equals(color);
			}
		} catch(Exception e) { }
		
	}
	
	public void directionMovieList2 (String[][] board, int index_x, int index_y , String color
			, boolean case_2, int potential, int distance, int horizontal_adjustment) {
		try {
			boolean equalsEXminus, equalsColorXminus;
			equalsEXminus = board[index_x][index_y - 1].equals("e");
			equalsColorXminus = board[index_x][index_y - 1].equals(color);
			while(equalsEXminus ||equalsColorXminus) {
				if(board[index_x][index_y - 1].equals("e")) {
					case_2 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_2) {
						++horizontal_adjustment;
					}
				}
				--index_y;
				equalsEXminus = board[index_x][index_y - 1].equals("e");
				equalsColorXminus = board[index_x][index_y - 1].equals(color);
			}
		} catch(Exception e) { }
		
	}
	
	public void directionMovieList3 (String[][] board, int index_x, int index_y , String color
			, boolean case_3, int potential, int distance, int horizontal_adjustment) {
		try {
			boolean equalsEX, equalsColorX;
			equalsEX = board[index_x][index_y + 1].equals("e");
			equalsColorX = board[index_x][index_y + 1].equals(color);
			while(equalsEX || equalsColorX) {
				if(board[index_x][index_y + 1].equals("e")) {
					case_3 = false;
					potential += distance;
					--distance;
				} else {
					potential += distance + 2;
					--distance;
					if(case_3) {
						++horizontal_adjustment;
					}
				}
				++index_y;
				equalsEX = board[index_x][index_y + 1].equals("e");
				equalsColorX = board[index_x][index_y + 1].equals(color);
			}
		} catch(Exception e) { }
		
	}
	
	public void directionMovieList4 (String[][] board, int index_x, int index_y , String color
			, boolean case_4, int potential, int distance, int vertical_adjustment) {
		try {
			boolean equalsE, equalsColor;
			equalsE = board[index_x + 1][index_y].equals("e");
			equalsColor = board[index_x + 1][index_y].equals(color);
			while(equalsE || equalsColor) {
				if(board[index_x + 1][index_y].equals("e")) {
					case_4 = false;
					potential += distance;
					--distance;
					equalsE = board[index_x + 1][index_y].equals("e");
					equalsColor = board[index_x + 1][index_y].equals(color);
				} else {
					potential += distance + 2;
					--distance;
					if(case_4) {
						++vertical_adjustment;
					}
				}
				++index_x;
			}
		} catch(Exception e) { }
		
	}
	public void enemyAdjustmentExtra1 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_x, boolean extra_x_minus) {
		
		try {
			boolean ValueEnemyColor_1Minus = board[index_x - 1][index_y].equals(enemyColor);
			while(ValueEnemyColor_1Minus) {
				++enemy_adjustment_x;
				--index_x;
				ValueEnemyColor_1Minus = board[index_x - 1][index_y].equals(enemyColor);
			}
			if(board[index_x - 1][index_y].equals("e")) {
				extra_x_minus = true;
			}
		} catch(Exception e) { }
	}
	
	public void enemyAdjustmentExtra2 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_x, boolean extra_x_plus) {
		
		try {
			boolean ValueEnemyColor_1Plus = board[index_x + 1][index_y].equals(enemyColor);
			while(ValueEnemyColor_1Plus) {
				++enemy_adjustment_x;
				++index_x;
				ValueEnemyColor_1Plus = board[index_x + 1][index_y].equals(enemyColor);
			}
			if(board[index_x + 1][index_y].equals("e")) {
				extra_x_plus = true;
			}
		} catch(Exception e) { }
	}
	
	public void enemyAdjustmentExtra3 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_y, boolean extra_y_minus) {
		
		try {
			boolean ValueEnemyColor_1Minus = board[index_x][index_y - 1].equals(enemyColor);
			while(ValueEnemyColor_1Minus) {
				++enemy_adjustment_y;
				--index_y;
				ValueEnemyColor_1Minus = board[index_x][index_y - 1].equals(enemyColor);
			}
			if(board[index_x][index_y - 1].equals("e")) {
				extra_y_minus = true;
			}
		} catch(Exception e) { }
	}
	
	public void enemyAdjustmentExtra4 (String[][] board, int index_x, int index_y
			,String enemyColor, int enemy_adjustment_y, boolean extra_y_plus) {
		
		try {
			boolean ValueEnemyColor = board[index_x][index_y + 1].equals(enemyColor);
			while(ValueEnemyColor) {
				++enemy_adjustment_y;
				++index_y;
				ValueEnemyColor = board[index_x][index_y + 1].equals(enemyColor);
			}
			if(board[index_x][index_y + 1].equals("e")) {
				extra_y_plus = true;
			}
		} catch(Exception e) { }
	}
	
	public void enemyAdjustmentDirectionXY (boolean extra_x_minus, boolean extra_x_plus 
			,int enemy_adjustment_x, int potential,boolean extra_y_minus, boolean extra_y_plus
			,int enemy_adjustment_y) {
		
		if(extra_x_minus && extra_x_plus) {
			++enemy_adjustment_x;
		}
		UpdateOfAdjustments (enemy_adjustment_x,  potential  );
		

		if(extra_y_minus && extra_y_plus) {
			++enemy_adjustment_y;
		}
		UpdateOfAdjustments (enemy_adjustment_y,  potential  );
	}
}