package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
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

	public Board(Object readObject) {
		// TODO Auto-generated constructor stub
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
			bos.close();
			byte [] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			ObjectInputStream obduplicate = new ObjectInputStream(bais);			
			replica = (Board) new Board(obduplicate.readObject());
			obduplicate.close();
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
		
			try_3 (board,  index_x,  index_y,
					 color,  case_3,  potential,  distance,
					 horizontal_adjustment);
			
		

		// (+,0) direction.
		
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

		int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
		
			try_5 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_x  );
		
	
			try_6 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_x  );

	

		
			try_7 ( board, index_x,  index_y , 
					 enemyColor,  enemy_adjustment_y );
		

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
					tryEmptyField3 ( index_x,  index_y,  color
							, case_3,  potential,  distance
							, horizontal_adjustment );

					// (+,0) direction.
					
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

					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					
					 adjustmentForTheOpponentT1 ( board,  index_x,  index_y
								, enemyColor,  enemy_adjustment_x);
					
					adjustmentForTheOpponentT2 (board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x);

					
					adjustmentForTheOpponentT3 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y);

					
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
					direction3 (board,  index_x,  index_y
							,  case_3,  potential,  distance,  horizontal_adjustment,  color);

					// (+,0) direction.
					
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


					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					enemyAdjustment1 ( board,  enemy_adjustment_x
							, index_x,  index_y,  enemyColor);

					
					enemyAdjustment2 (board,  enemy_adjustment_x
							, index_x,  index_y,  enemyColor);
					
					enemyAdjustment3 ( board,  enemy_adjustment_y
							, index_x,  index_y,  enemyColor);
					
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

					
					 directionMovieList2 ( board,  index_x,  index_y ,  color
								,  case_2,  potential,  distance,  horizontal_adjustment);
					// (0,+) direction.
					
					directionMovieList3 ( board,  index_x,  index_y ,  color
							,  case_3,  potential,  distance,  horizontal_adjustment);

					// (+,0) direction.
					
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

					int enemy_adjustment_x = 1, enemy_adjustment_y = 1;
					
					enemyAdjustmentExtra1 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x,  extra_x_minus);

					enemyAdjustmentExtra2 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_x,  extra_x_plus);

					
					enemyAdjustmentExtra3 ( board,  index_x,  index_y
							, enemyColor,  enemy_adjustment_y,  extra_y_minus);

					
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
	
	public void try_2 (String[][] board_try, int index_x_try, int index_y_try,
			String color_try, boolean case_2_try, int potential_try, int distance_try,
			int horizontal_adjustment_try) {
		
		try {
		boolean equalsE12, equalsColor12;
		equalsE12 = board_try[index_x_try][index_y_try - 1].equals("e");
		equalsColor12 = board_try[index_x_try][index_y_try - 1].equals(color_try);
		while(equalsE12|| 
				equalsColor12) {
			if(board_try[index_x_try][index_y_try - 1].equals("e")) {
				case_2_try = false;
				potential_try += distance_try;
				--distance_try;
			} else {
				potential_try += distance_try + 2;
				--distance_try;
				if(case_2_try) {
					++horizontal_adjustment_try;
				}
			}
			--index_y_try;
			equalsE12 = board_try[index_x_try][index_y_try - 1].equals("e");
			equalsColor12 = board_try[index_x_try][index_y_try - 1].equals(color_try);
		}
		}	catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	
	
		}
	
	public void try_1 (String[][] board_t1, int index_x_t1, int index_y_t1,
			String color_t1, boolean case_1_t1, int potential_t1, int distance_t1,
			int vertical_adjustment_t1) { 
		try {
			boolean 	equalsE14, equalsColor14;
		equalsE14 = board_t1[index_x_t1 - 1][index_y_t1].equals("e");
		equalsColor14 = board_t1[index_x_t1 - 1][index_y_t1].equals(color_t1);
		while(equalsE14 || 
				equalsColor14) {
			if(board_t1[index_x_t1 - 1][index_y_t1].equals("e")) {
				case_1_t1 = false;
				potential_t1 += distance_t1;
				--distance_t1;
			} else {
				potential_t1 += distance_t1 + 2;
				--distance_t1;
				if(case_1_t1) {
					++vertical_adjustment_t1;
				}
			}
			--index_x_t1;
			equalsE14 = board_t1[index_x_t1 - 1][index_y_t1].equals("e");
			equalsColor14 = board_t1[index_x_t1 - 1][index_y_t1].equals(color_t1);
		}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void try_3 (String[][] board_t3, int index_x_t3, int index_y_t3,
			String color_t3, boolean case_3_t3, int potential_t3, int distance_t3,
			int horizontal_adjustment_t3) { 
		try {
		boolean equalsE13, equalsColor13;
		equalsE13 = board_t3[index_x_t3][index_y_t3 + 1].equals("e");
		equalsColor13 = board_t3[index_x_t3][index_y_t3 + 1].equals(color_t3);
		while(equalsE13 || 
				equalsColor13) {
			if(board_t3[index_x_t3][index_y_t3 + 1].equals("e")) {
				case_3_t3 = false;
				potential_t3 += distance_t3;
				--distance_t3;
			} else {
				potential_t3 += distance_t3 + 2;
				--distance_t3;
				if(case_3_t3) {
					++horizontal_adjustment_t3;
				}
			}
			++index_y_t3;
			equalsE13 = board_t3[index_x_t3][index_y_t3 + 1].equals("e");
			equalsColor13 = board_t3[index_x_t3][index_y_t3 + 1].equals(color_t3);
			}
		}catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void try_4 (String[][] board_t4, int index_x_t4, int index_y_t4,
			String color_t4, boolean case_4_t4, int potential_t4, int distance_t4,
			int vertical_adjustment_t4) {
		try {
		boolean equalsE11, equalsColor11;
		equalsE11 = board_t4[index_x_t4 + 1][index_y_t4].equals("e");
		equalsColor11 = board_t4[index_x_t4 + 1][index_y_t4].equals(color_t4);
		while(equalsE11 || 
				equalsColor11) {
			if(board_t4[index_x_t4 + 1][index_y_t4].equals("e")) {
				case_4_t4 = false;
				potential_t4 += distance_t4;
				--distance_t4;
			} else {
				potential_t4 += distance_t4 + 2;
				--distance_t4;
				if(case_4_t4) {
					++vertical_adjustment_t4;
				}
			}
			++index_x_t4;
			equalsE11 = board_t4[index_x_t4 + 1][index_y_t4].equals("e");
			equalsColor11 = board_t4[index_x_t4 + 1][index_y_t4].equals(color_t4);
			}
		}catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void try_5 (String[][] board_t5,int index_x_t5, int index_y_t5 , 
			String enemyColor_t5, int enemy_adjustment_x_t5  ) {
		try {
		boolean enemyColor12 = board_t5[index_x_t5 - 1][index_y_t5].equals(enemyColor_t5);
		while(enemyColor12) {
			++enemy_adjustment_x_t5;
			--index_x_t5;
			enemyColor12 = board_t5[index_x_t5 - 1][index_y_t5].equals(enemyColor_t5);
		}
		}
		catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void try_6 (String[][] board_t6,int index_x_t6, int index_y_t6 , 
			String enemyColor_t6, int enemy_adjustment_x_t6  ) {
		boolean enemyColor11 = board_t6[index_x_t6 + 1][index_y_t6].equals(enemyColor_t6);
		while(enemyColor11) {
			++enemy_adjustment_x_t6;
			++index_x_t6;
			enemyColor11 = board[index_x_t6 + 1][index_y_t6].equals(enemyColor_t6);
		}
		
	}
	
	public void try_7 (String[][] board_t7,int index_x_t7, int index_y_t7 , 
			String enemyColor_t7, int enemy_adjustment_y_t7  ) {
		try {
		boolean enemyColor10 = board_t7[index_x_t7][index_y_t7 - 1].equals(enemyColor_t7);
		while(enemyColor10) {
			++enemy_adjustment_y_t7;
			--index_y_t7;
			enemyColor10 = board_t7[index_x_t7][index_y_t7 - 1].equals(enemyColor_t7);
			}
		}catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void try_8 (String[][] board_t8,int index_x_t8, int index_y_t8 , 
			String enemyColor_t8, int enemy_adjustment_y_t8  ) {
		try {
			boolean enemyColor9 = board_t8[index_x_t8][index_y_t8 + 1].equals(enemyColor_t8);
		while(enemyColor9) {
			++enemy_adjustment_y_t8;
			++index_y_t8;
			enemyColor9 = board_t8[index_x_t8][index_y_t8 + 1].equals(enemyColor_t8);
			}
		}
		catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
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
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
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
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}

		
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
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}

		
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
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}

		
	}
	public static void adjustmentForTheOpponentT1 (String[][] board_o1, int index_x_o1, int index_y_o1
			,String enemyColor_o1, int enemy_adjustment_x_o1) {
		
		try {
			boolean enemyColor5 =board_o1[index_x_o1 - 1][index_y_o1].equals(enemyColor_o1);
			while(enemyColor5) {
				++enemy_adjustment_x_o1;
				--index_x_o1;
				enemyColor5 = board_o1[index_x_o1 - 1][index_y_o1].equals(enemyColor_o1);
			}
		} catch(Exception e) {
			System.out.println("There's an error"); 
		}	
		
	}
	
	public static void adjustmentForTheOpponentT2 (String[][] board_o2, int index_x_o2, int index_y_o2
			,String enemyColor_o2, int enemy_adjustment_x_o2) {
		
		try {
			boolean enemyColor4 = board_o2[index_x_o2][index_y_o2 - 1].equals(enemyColor_o2);
			while(enemyColor4) {
				++enemy_adjustment_x_o2;
				++index_x_o2;
				enemyColor4 = board_o2[index_x_o2][index_y_o2 - 1].equals(enemyColor_o2);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	
		
	}
	
	public static void adjustmentForTheOpponentT3 (String[][] board_o3, int index_x_o3, int index_y_o3
			,String enemyColor_o3, int enemy_adjustment_y_o3) {
		
		try {
			boolean enemyColor3 = board_o3[index_x_o3][index_y_o3 - 1].equals(enemyColor_o3);
			while(enemyColor3) {
				++enemy_adjustment_y_o3;
				--index_y_o3;
				enemyColor3 = board_o3[index_x_o3][index_y_o3 - 1].equals(enemyColor_o3);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	
		
	}
	
	public static void adjustmentForTheOpponentT4 (String[][] board_o4, int index_x_o4, int index_y_o4
			,String enemyColor_o4, int enemy_adjustment_y_o4) {
		
		try {
			boolean enemyColor2 = board_o4[index_x_o4][index_y_o4 + 1].equals(enemyColor_o4);
			while(enemyColor2) {
				++enemy_adjustment_y_o4;
				++index_y_o4;
				enemyColor2 = board_o4[index_x_o4][index_y_o4 + 1].equals(enemyColor_o4);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	
		
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
	
	public static void direction1 (String[][] board_d1, int index_x_d1, int index_y_d1
			, boolean case_1_d1, int potential_d1, int distance_d1, int vertical_adjustment_d1, String color_d1) {
		try {	
		boolean equalsEX_4, equalsColorsX_4;
		equalsEX_4 = board_d1[index_x_d1 - 1][index_y_d1].equals("e");
		equalsColorsX_4 = board_d1[index_x_d1 - 1][index_y_d1].equals(color_d1);
		while(equalsEX_4|| equalsColorsX_4) {
			if(board_d1[index_x_d1 - 1][index_y_d1].equals("e")) {
				case_1_d1 = false;
				potential_d1 += distance_d1;
				--distance_d1;
			} else {
				potential_d1 += distance_d1 + 2;
				--distance_d1;
				if(case_1_d1) {
					++vertical_adjustment_d1;
				}
			}
			--index_x_d1;
			equalsEX_4 = board_d1[index_x_d1 - 1][index_y_d1].equals("e");
			equalsColorsX_4 = board_d1[index_x_d1 - 1][index_y_d1].equals(color_d1);
		}
	} catch(Exception e) { 
		System.out.println("There's an error"); 
	}
		
	}
	
	public static void direction2 (String[][] board_d2, int index_x_d2, int index_y_d2
			, boolean case_2_d2, int potential_d2, int distance_d2, int horizontal_adjustment_d2, String color_d2) {
		try {
			boolean equalsEX_3, equalsColorsX_3;
			equalsEX_3 = board_d2[index_x_d2][index_y_d2 - 1].equals("e") ;
			equalsColorsX_3 =  board_d2[index_x_d2][index_y_d2 - 1].equals(color_d2);
			while(equalsEX_3 || equalsColorsX_3) {
				if(board_d2[index_x_d2][index_y_d2 - 1].equals("e")) {
					case_2_d2 = false;
					potential_d2 += distance_d2;
					--distance_d2;
				} else {
					potential_d2 += distance_d2 + 2;
					--distance_d2;
					if(case_2_d2) {
						++horizontal_adjustment_d2;
					}
				}
				--index_y_d2;
				equalsEX_3 = board_d2[index_x_d2][index_y_d2 - 1].equals("e") ;
				equalsColorsX_3 =  board_d2[index_x_d2][index_y_d2 - 1].equals(color_d2);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public static void direction3 (String[][] board_d3, int index_x_d3, int index_y_d3
			, boolean case_3_d3, int potential_d3, int distance_d3, int horizontal_adjustment_d3, String color_d3) {
		try {
			boolean equalsEX_2, equalsColorsX_2;
			equalsEX_2 = board_d3[index_x_d3][index_y_d3 + 1].equals("e") ;
			equalsColorsX_2 = board_d3[index_x_d3][index_y_d3 + 1].equals(color_d3);
			while(equalsEX_2 || equalsColorsX_2) {
				if(board_d3[index_x_d3][index_y_d3 + 1].equals("e")) {
					case_3_d3 = false;
					potential_d3 += distance_d3;
					--distance_d3;
				} else {
					potential_d3 += distance_d3 + 2;
					--distance_d3;
					if(case_3_d3) {
						++horizontal_adjustment_d3;
					}
				}
				++index_y_d3;
				equalsEX_2 = board_d3[index_x_d3][index_y_d3 + 1].equals("e") ;
				equalsColorsX_2 = board_d3[index_x_d3][index_y_d3 + 1].equals(color_d3);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}

		
	}
	
	public static void direction4 (String[][] board_d4, int index_x_d4, int index_y_d4
			, boolean case_4_d4, int potential_d4, int distance_d4, int vertical_adjustment_d4, String color_d4) {
		try {
			boolean equalsEX_1, equalsColorsX_1;
			equalsEX_1 = board_d4[index_x_d4 + 1][index_y_d4].equals("e") ;
			equalsColorsX_1 =board_d4[index_x_d4 + 1][index_y_d4].equals(color_d4);
			while(equalsEX_1|| equalsColorsX_1) {
				if(board_d4[index_x_d4 + 1][index_y_d4].equals("e")) {
					case_4_d4 = false;
					potential_d4 += distance_d4;
					--distance_d4;
				} else {
					potential_d4 += distance_d4 + 2;
					--distance_d4;
					if(case_4_d4) {
						++vertical_adjustment_d4;
					}
				}
				++index_x_d4;
				equalsEX_1 = board_d4[index_x_d4 + 1][index_y_d4].equals("e") ;
				equalsColorsX_1 =board_d4[index_x_d4 + 1][index_y_d4].equals(color_d4);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}

		
	}
	
	public void enemyAdjustment1 (String[][] board_ea1, int enemy_adjustment_x_ea1
			,int index_x_ea1, int index_y_ea1, String enemyColor_ea1) {
		try {
			boolean enemyColor_3 = board[index_x_ea1 - 1][index_y_ea1].equals(enemyColor_ea1);
			while(enemyColor_3) {
				++enemy_adjustment_x_ea1;
				--index_x_ea1;
				enemyColor_3 = board[index_x_ea1 - 1][index_y_ea1].equals(enemyColor_ea1);
			}
			//						if(board[index_x - 1][index_y].equals("e")) {
			//							extra_x_minus = true;
			//						}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void enemyAdjustment2 (String[][] board_ea2, int enemy_adjustment_x_ea2
			,int index_x_ea2, int index_y_ea2, String enemyColor_ea2) {
		try {
			boolean enemyColors_2 = board[index_x_ea2 + 1][index_y_ea2].equals(enemyColor_ea2);
			while(enemyColors_2) {
				++enemy_adjustment_x_ea2;
				++index_x_ea2;
				enemyColors_2 = board[index_x_ea2 + 1][index_y_ea2].equals(enemyColor_ea2);
			}
			//						if(board[index_x + 1][index_y].equals("e")) {
			//							extra_x_plus = true;
			//						}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void enemyAdjustment3 (String[][] board_ea3, int enemy_adjustment_y_ea3
			,int index_x_ea3, int index_y_ea3, String enemyColor_ea3) {
		try {
			boolean enemyColors_1 = board[index_x_ea3][index_y_ea3 - 1].equals(enemyColor_ea3);
			while(enemyColors_1) {
				++enemy_adjustment_y_ea3;
				--index_y_ea3;
				enemyColors_1 = board[index_x_ea3][index_y_ea3 - 1].equals(enemyColor_ea3);
			}
			//						if(board[index_x][index_y - 1].equals("e")) {
			//							extra_y_minus = true;
			//						}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void enemyAdjustment4 (String[][] board_ea4, int enemy_adjustment_y_ea4
			,int index_x_ea4, int index_y_ea4, String enemyColor_ea4) {
		try {
			boolean enemyColors_X = board[index_x_ea4][index_y_ea4 + 1].equals(enemyColor_ea4);
			while(enemyColors_X) {
				++enemy_adjustment_y_ea4;
				++index_y_ea4;
				enemyColors_X = board[index_x_ea4][index_y_ea4 + 1].equals(enemyColor_ea4);
			}
			//						if(board[index_x][index_y + 1].equals("e")) {
			//							extra_y_plus = true;
			//						}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void directionMovieList1 (String[][] board_l1, int index_x_l1, int index_y_l1 , String color_l1
			, boolean case_1_l1, int potential_l1, int distance_l1, int vertical_adjustment_l1) {
		try {
			boolean equalsMinus, equalsColorMinus;
			equalsMinus = board[index_x_l1 - 1][index_y_l1].equals("e") ;
			equalsColorMinus =  board[index_x_l1 - 1][index_y_l1].equals(color_l1);
			while(equalsMinus || equalsColorMinus) {
				if(board[index_x_l1 - 1][index_y_l1].equals("e")) {
					case_1_l1 = false;
					potential_l1 += distance_l1;
					--distance_l1;
				} else {
					potential_l1 += distance_l1 + 2;
					--distance_l1;
					if(case_1_l1) {
						++vertical_adjustment_l1;
					}
				}
				--index_x_l1;
				equalsMinus = board[index_x_l1 - 1][index_y_l1].equals("e") ;
				equalsColorMinus =  board[index_x_l1 - 1][index_y_l1].equals(color_l1);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void directionMovieList2 (String[][] board_l2, int index_x_l2, int index_y_l2 , String color_l2
			, boolean case_2_l2, int potential_l2, int distance_l2, int horizontal_adjustment_l2) {
		try {
			boolean equalsEXminus, equalsColorXminus;
			equalsEXminus = board[index_x_l2][index_y_l2 - 1].equals("e");
			equalsColorXminus = board[index_x_l2][index_y_l2 - 1].equals(color_l2);
			while(equalsEXminus ||equalsColorXminus) {
				if(board[index_x_l2][index_y_l2 - 1].equals("e")) {
					case_2_l2 = false;
					potential_l2 += distance_l2;
					--distance_l2;
				} else {
					potential_l2 += distance_l2 + 2;
					--distance_l2;
					if(case_2_l2) {
						++horizontal_adjustment_l2;
					}
				}
				--index_y_l2;
				equalsEXminus = board[index_x_l2][index_y_l2 - 1].equals("e");
				equalsColorXminus = board[index_x_l2][index_y_l2 - 1].equals(color_l2);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void directionMovieList3 (String[][] board_l3, int index_x_l3, int index_y_l3 , String color_l3
			, boolean case_3_l3, int potential_l3, int distance_l3, int horizontal_adjustment_l3) {
		try {
			boolean equalsEX, equalsColorX;
			equalsEX = board[index_x_l3][index_y_l3 + 1].equals("e");
			equalsColorX = board[index_x_l3][index_y_l3 + 1].equals(color_l3);
			while(equalsEX || equalsColorX) {
				if(board[index_x_l3][index_y_l3 + 1].equals("e")) {
					case_3_l3 = false;
					potential_l3 += distance_l3;
					--distance_l3;
				} else {
					potential_l3 += distance_l3 + 2;
					--distance_l3;
					if(case_3_l3) {
						++horizontal_adjustment_l3;
					}
				}
				++index_y_l3;
				equalsEX = board[index_x_l3][index_y_l3 + 1].equals("e");
				equalsColorX = board[index_x_l3][index_y_l3 + 1].equals(color_l3);
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	
	public void directionMovieList4 (String[][] board_l4, int index_xl4, int index_yl4 , String colorl4
			, boolean case_l4, int potential_l4, int distance_l4, int vertical_adjustment_l4) {
		try {
			boolean equalsE, equalsColor;
			equalsE = board[index_xl4 + 1][index_yl4].equals("e");
			equalsColor = board[index_xl4 + 1][index_yl4].equals(colorl4);
			while(equalsE || equalsColor) {
				if(board[index_xl4 + 1][index_yl4].equals("e")) {
					case_l4 = false;
					potential_l4 += distance_l4;
					--distance_l4;
					equalsE = board[index_xl4 + 1][index_yl4].equals("e");
					equalsColor = board[index_xl4 + 1][index_yl4].equals(colorl4);
				} else {
					potential_l4 += distance_l4 + 2;
					--distance_l4;
					if(case_l4) {
						++vertical_adjustment_l4;
					}
				}
				++index_xl4;
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
		
	}
	public void enemyAdjustmentExtra1 (String[][] board1, int index_x1, int index_y1
			,String enemyColor1, int enemy_adjustment_x1, boolean extra_x_minus1) {
		
		try {
			boolean ValueEnemyColor_1Minus = board[index_x1 - 1][index_y1].equals(enemyColor1);
			while(ValueEnemyColor_1Minus) {
				++enemy_adjustment_x1;
				--index_x1;
				ValueEnemyColor_1Minus = board[index_x1 - 1][index_y1].equals(enemyColor1);
			}
			if(board[index_x1 - 1][index_y1].equals("e")) {
				extra_x_minus1 = true;
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void enemyAdjustmentExtra2 (String[][] board2, int index_x2, int index_y2
			,String enemyColor2, int enemy_adjustment_x2, boolean extra_x_plus2) {
		
		try {
			boolean ValueEnemyColor_1Plus = board[index_x2 + 1][index_y2].equals(enemyColor2);
			while(ValueEnemyColor_1Plus) {
				++enemy_adjustment_x2;
				++index_x2;
				ValueEnemyColor_1Plus = board[index_x2 + 1][index_y2].equals(enemyColor2);
			}
			if(board[index_x2 + 1][index_y2].equals("e")) {
				extra_x_plus2 = true;
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void enemyAdjustmentExtra3 (String[][] board_3, int index_x3, int index_y3
			,String enemyColor3, int enemy_adjustment_y3, boolean extra_y_minus3) {
		
		try {
			boolean ValueEnemyColor_1Minus = board[index_x3][index_y3 - 1].equals(enemyColor3);
			while(ValueEnemyColor_1Minus) {
				++enemy_adjustment_y3;
				--index_y3;
				ValueEnemyColor_1Minus = board[index_x3][index_y3 - 1].equals(enemyColor3);
			}
			if(board[index_x3][index_y3 - 1].equals("e")) {
				extra_y_minus3 = true;
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
	}
	
	public void enemyAdjustmentExtra4 (String[][] board_ea4, int index_xea4, int index_yea4
			,String enemyColor_ea4, int enemy_adjustment_yea4, boolean extra_y_plusea4) {
		
		try {
			boolean ValueEnemyColor = board[index_xea4][index_yea4 + 1].equals(enemyColor_ea4);
			while(ValueEnemyColor) {
				++enemy_adjustment_yea4;
				++index_yea4;
				ValueEnemyColor = board[index_xea4][index_yea4 + 1].equals(enemyColor_ea4);
			}
			if(board[index_xea4][index_yea4 + 1].equals("e")) {
				extra_y_plusea4 = true;
			}
		} catch(Exception e) { 
			System.out.println("There's an error"); 
		}
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