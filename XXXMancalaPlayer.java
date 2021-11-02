import java.util.ArrayList;

public class XXXMancalaPlayer extends MancalaPlayer implements MiniMax{
	

	int depthLimit;
	int generatedNodes = 0;
	int staticEvalCounts = 0;
	double avgBranchingFactor = 0;
	private final double INF = Double.POSITIVE_INFINITY;
	boolean currentTurn;
	double nonLeafNodes = 0;
	long deadline = 0;
	


	public XXXMancalaPlayer() {
		this("AlphaBetaPlayer", (int) (Math.random()*12 +1));
	}
	/**
	 * 
	 * @param name
	 */
	public XXXMancalaPlayer (String name) {
		super(name);
		this.depthLimit = 3;
	}

	/**
	 * 
	 * @param name
	 * @param depthLimit
	 */
	public XXXMancalaPlayer (String name, int depthLimit) {
		super(name);
		this.depthLimit = depthLimit;
	}
	
	/**
	 * 
	 * @param current current state of the root node (max)
	 * @return the best Move for the current state.
	 */
	public Move getABBestMove(GameState current) {
		ArrayList<Move> validMoves = current.getLegalMoves();
		Move bestMove = null;

		double alpha = -INF;
		double beta = INF;
		double score = -INF;
		double curVal;
		generatedNodes++;
		nonLeafNodes++;
//		while (deadline - System.currentTimeMillis() > 500) {

		for (Move currentMove : validMoves) {
			GameState nextState = current.makeMove(currentMove);
			boolean turnRepeats = current.getTurn() == nextState.getTurn();
			if (turnRepeats) {
				 curVal = maxABPlayer(nextState, depthLimit - 1, alpha, beta);

			} else {
				curVal = minABPlayer(nextState, depthLimit - 1, alpha, beta);
			}
//			9/22
			if (curVal > alpha) {
				alpha = curVal;
				bestMove = currentMove;
			} 			
		}

		return bestMove; 
	}
	
	/**
	 * 
	 * @param current
	 * @param depthLimit
	 * @param alpha
	 * @param beta
	 * @return
	 */
	double maxABPlayer(GameState current, long depthLeft, double alpha, double beta) {
		generatedNodes++;
//		System.out.println("Generated Nodes: " + generatedNodes);
		if (current.isGameOver()) {
			if (currentTurn) return current.getFinalNetScore();
			else return -current.getFinalNetScore();

		} else if (depthLeft == 0) {
			staticEvalCounts++;
			return staticEvaluator(current);
		} else {
			nonLeafNodes++;
			double curVal;
			ArrayList<Move> validMoves = current.getLegalMoves();
			for (Move childMove : validMoves) {
				GameState nextState = current.makeMove(childMove);
				
				// if turn repeats.
				boolean turnRepeats = current.getTurn() == nextState.getTurn();
				if (turnRepeats) {
					curVal = maxABPlayer(nextState, depthLeft - 1, alpha, beta);
				} else {
					curVal = minABPlayer(nextState, depthLeft - 1, alpha, beta);
				}
				
				if (!turnRepeats && curVal >= beta) {
					return curVal;
				}
				if (curVal > alpha) {
					alpha = curVal;
				}
				
//				System.out.println("alpha at " + (depthLeft - 1) + "(" + childMove.toString() + ")" + ": " + alpha);
			}
			return alpha;
		}
	
	}

	
	/**
	 * 
	 * @param current
	 * @param depthLimit
	 * @param alpha
	 * @param beta
	 * @return
	 */
	double minABPlayer(GameState current, long depthLeft, double alpha, double beta) {
		generatedNodes++;
		if (current.isGameOver()) {
			if (currentTurn) return current.getFinalNetScore();
			else return -current.getFinalNetScore();

		} else if (depthLeft == 0) {
			if (h1_scoreDiff_losingByMT3(current))
				depthLeft++;
			staticEvalCounts++;
			return staticEvaluator(current);
		} else {
			double curVal;

			ArrayList<Move> validMoves = current.getLegalMoves();
			for (Move childMove : validMoves) {
				GameState nextState = current.makeMove(childMove);
				boolean turnRepeats = current.getTurn() == nextState.getTurn();
				if (turnRepeats) {
					curVal = minABPlayer(nextState, depthLeft - 1, alpha, beta);
				} else {
					curVal = maxABPlayer(nextState, depthLeft - 1, alpha, beta);
				}
				
				if (!turnRepeats && curVal <= alpha) {
					return curVal;
				}
				// if returned value (return alpha) from maxABPlayer is less than beta -> beta = curVal
				// beta = min(beta, curVal)
				if (curVal < beta) {
					beta = curVal;
				} 

		
			}
			
			return beta;
		}
	}
	
	/**
	 * Compute the score difference. 
	 * If the score diff. is greater than 3 return true, false otherwise.
	 * @param state
	 * @return
	 */
	boolean h1_scoreDiff_losingByMT3(GameState state) {
		long scorediff;
		if (currentTurn) {
			scorediff = state.getFinalNetScore();
		} else {
			scorediff = -state.getFinalNetScore();
		}
		if (scorediff < -3) return true;
		else return false;
	}
	
	@Override
	public Move getMove(GameState g, long deadline) {
		this.deadline = deadline;
		currentTurn = g.getTurn();
		return getABBestMove(g);
	}
	
	
	@Override
	public double staticEvaluator(GameState state) {	
		return state.getPlayerScore(currentTurn) - state.getPlayerScore(!currentTurn);
//		return state.getPlayerScore(state.getTurn()) - 
//			   state.getPlayerScore(!state.getTurn());
	}
	

	@Override
	public int getNodesGenerated() {
		return generatedNodes;
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
		return "AlphaBetaMancalaPlayer";
	}
}

