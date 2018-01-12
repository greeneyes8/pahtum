package ai.minimax;

/**
 * Individua il nodo root 
 * @author Mina
 *
 */

public class Root {
	private MMNode root;
	
	/**
	 * sostituisce a root il nodo
	 * @param node
	 */
	
	public Root(MMNode node) {
		this.root = node;
	}
	
	public MMNode getRoot() {
		return this.root;
	}
}
