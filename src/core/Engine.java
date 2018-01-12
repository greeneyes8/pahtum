package core;

import util.Tuple;

/**
 * Interfaccia che genera Tuple
 * @author Mina
 *
 */

public interface Engine {
	Tuple<Integer, Integer> uct(int n) throws Exception;
}
