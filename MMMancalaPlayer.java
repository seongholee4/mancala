import java.util.ArrayList;

/**
 * MancalaPlayer implemented with MiniMax algorithm 
 * @author Lee
 *
 */
public class MMMancalaPlayer extends MancalaPlayer implements MiniMax{
	

	int depthLimit;
	int generatedNodes, staticEvalCounts = 0;
	double nonLeafNodes = 0;
	long deadline = 0;
	boolean currentTurn;
	
	double sumElapsedTime = 0;
	double sumNodesGenerated = 0;
	double sumNonLeafNodes = 0;
	
	public MMMancalaPlayer() {
		this("MiniMaxMancalaPlayer", 5);
	}
	/**
	 * 
	 * @param name name of the player
	 */
	public MMMancalaPlayer (String name) {
		super(name);
		this.depthLimit = 5;
	}

	/**
	 * 
	 * @param name name of the player
	 * @param depthLimit depthLimit of the player to search
	 */
	public MMMancalaPlayer (String name, int depthLimit) {
		super(name);
		this.depthLimit = depthLimit;
	}
		
	Move getBestMove(GameState current){
		Move bestMove = null;
		double bestScore = -100000000;
		double curScore;
		generatedNodes++;
		nonLeafNodes++;		
		
		ArrayList<Move> moves = current.getLegalMoves();
		generatedNodes+= moves.size();
		for(int i=0; i<moves.size(); i++) {
			Move currentMove = moves.get(i);
			// Result of performing current move to the current state.
			GameState childState = current.makeMove(currentMove);
			
			// Store curScore to compare with different moves
			if (current.getTurn() == childState.getTurn()) {
				curScore = maxPlayer(childState, depthLimit - 1);
			} else {
				curScore = -minPlayer(childState, depthLimit - 1);
			}
			
			if (curScore > bestScore) {
				bestMove = currentMove;
				bestScore = curScore;
			}
		}
		return bestMove;
	}
	
	
	
	double maxPlayer(GameState current, long depthLeft) {
		if (current.isGameOver()) {
			if (currentTurn) return current.getFinalNetScore();
			else return -current.getFinalNetScore();
		} else if (depthLeft == 0) {
			staticEvalCounts++;
			return staticEvaluator(current);
		} else {
			nonLeafNodes++;
			double best = -100000000;
			
			ArrayList<Move> validMoves = current.getLegalMoves();
			generatedNodes += validMoves.size();
			ArrayList<GameState> childStates = new ArrayList<GameState>();
			// create childStates of currentState
			for (int i = 0; i < validMoves.size(); i++) {
				childStates.add(current.makeMove(validMoves.get(i)));
			}
			
			for (GameState childState : childStates) {
				if (current.getTurn() == childState.getTurn()) {
					best = Math.max(best, maxPlayer(childState, depthLeft-1));
				} else {
					best = Math.max(best, -minPlayer(childState, depthLeft-1));
				}
			}
			
			return best;

		}
	}
	
	double minPlayer(GameState current, long depthLeft) {
		if (current.isGameOver()) {
			if (currentTurn) return current.getFinalNetScore();
			else return -current.getFinalNetScore();
		} else if (depthLeft == 0) {
			staticEvalCounts++;
			return staticEvaluator(current);
		} else {
			nonLeafNodes++;
			double best = -100000000;
			
			ArrayList<Move> validMoves = current.getLegalMoves();
			generatedNodes += validMoves.size();

			ArrayList<GameState> childStates = new ArrayList<GameState>();
			// create childStates of currentState
			for (int i = 0; i < validMoves.size(); i++) {
				childStates.add(current.makeMove(validMoves.get(i)));
			}
			
			for (GameState childState : childStates) {
				if (current.getTurn() == childState.getTurn()) {
					best = Math.max(best, minPlayer(childState, depthLeft-1));
				} else {
					best = Math.max(best, -maxPlayer(childState, depthLeft-1));
				}
			}
			
			return best;

		}
	}
	
	@Override
	public Move getMove(GameState g, long deadline) {
			initializeValuesWithZero();
			this.deadline = deadline - System.currentTimeMillis();
			currentTurn = g.getTurn();
			double start = (double) System.currentTimeMillis();
			Move bestMove = getBestMove(g);
			double end = (double) System.currentTimeMillis();
			
			
			sumElapsedTime += (end - start);
			sumNodesGenerated += getNodesGenerated();
			sumNonLeafNodes += getNonLeafNodes();
			
			return bestMove;
	}
	
	void setTotalNodesAndTimeZero() {
		sumElapsedTime = 0;
		sumNodesGenerated = 0;
	}
	

	double getSumElapsedTime() {
		return sumElapsedTime;
	}
	
	double getSumNodesGenerated() {
		return sumNodesGenerated;
	}
	
	double getSumNonLeafNodes() {
		return sumNonLeafNodes;
	}
	
	void initializeValuesWithZero() { 
		generatedNodes = 0;
		nonLeafNodes = 0;
	}
	
	@Override
	public double staticEvaluator(GameState state) {		
		return state.getPlayerScore(state.getTurn()) - 
			   state.getPlayerScore(!state.getTurn());
	}
	
	@Override
	public int getNodesGenerated() {
		return generatedNodes;
	}
	
	public double getNonLeafNodes() {
		return nonLeafNodes;
	}

	@Override
	public int getStaticEvaluations() {
		return staticEvalCounts;
	}
	
    /**
     * Get the average branching factor of the nodes that
     * were expanded during the search.  This is to be computed
     * based upon the actual number of children for each node.
     * 
     * @return the average branching factor.
     */
	@Override
	public double getAveBranchingFactor() {
		return (generatedNodes - 1) / (nonLeafNodes);
	}

    /**
     * Get the effective branching factor of the nodes that were
     * expanded during the search.  This is to be computed based
     * upon the number of children that are explored in the search.
     * Without alpha/beta pruning this number will be equal to the 
     * average branching factor. With alpha/beta pruning it should be
     * significantly smaller.
     * 
     * @return the effective branching factor.
     */
	@Override
	public double getEffectiveBranchingFactor() {
		return getAveBranchingFactor();
	}



	public String toString() {
		return name;
	}
}
