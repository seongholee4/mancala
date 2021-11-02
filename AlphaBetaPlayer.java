import java.util.ArrayList;

/**
 * MancalaPlayer implemented with MiniMax algorithm and Alpha-Beta pruning
 * @author Lee
 *
 */
public class AlphaBetaPlayer extends MancalaPlayer implements MiniMax{
	

	int depthLimit;
	int generatedNodes, staticEvalCounts = 0;
	double nonLeafNodes, cutoff = 0;
	long deadline = 0;
	boolean currentTurn;

	double sumElapsedTime = 0;
	double sumNodesGenerated = 0;
	double sumNonLeafNodes = 0;
	double sumCutoff = 0;
	
	private final double INF = Double.POSITIVE_INFINITY;


	public AlphaBetaPlayer() {
		this("AlphaBetaPlayer", 12);
	}
	/**
	 * 
	 * @param name
	 */
	public AlphaBetaPlayer (String name) {
		super(name);
		this.depthLimit = 3;
	}

	/**
	 * 
	 * @param name
	 * @param depthLimit
	 */
	public AlphaBetaPlayer (String name, int depthLimit) {
		super(name);
		this.depthLimit = depthLimit;
	}
	
	/**
	 * 
	 * @param current current state of the root node (max)
	 * @return the best Move for the current state.
	 */
	public Move getABBestMove(GameState current) {
		Move bestMove = null;

		double alpha = -INF;
		double beta = INF;
		double curVal;
		generatedNodes++;
		nonLeafNodes++;
		
		ArrayList<Move> validMoves = current.getLegalMoves();
		generatedNodes += validMoves.size();
		for (int i = 0; i < validMoves.size(); i++) {
			Move currentMove = validMoves.get(i);
			GameState nextState = current.makeMove(currentMove);
			boolean turnRepeats = current.getTurn() == nextState.getTurn();
			if (turnRepeats) {
				 curVal = maxABPlayer(nextState, depthLimit - 1, alpha, beta);

			} else {
				curVal = minABPlayer(nextState, depthLimit - 1, alpha, beta);
			}

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
	 * @return the best value for maxABPlayer
	 */
	double maxABPlayer(GameState current, long depthLeft, double alpha, double beta) {
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
			generatedNodes += validMoves.size(); 
			for (int i = 0; i< validMoves.size(); i++) {
				Move childMove = validMoves.get(i);
				GameState nextState = current.makeMove(childMove);
				
				// if turn repeats.
				boolean turnRepeats = current.getTurn() == nextState.getTurn();
				if (turnRepeats) {
					curVal = maxABPlayer(nextState, depthLeft - 1, alpha, beta);
				} else {
					curVal = minABPlayer(nextState, depthLeft - 1, alpha, beta);
				}
				
				if (!turnRepeats && curVal >= beta) {
					cutoff += (validMoves.size() - 1) - i;
					return curVal;
				}
				if (curVal > alpha) {
					alpha = curVal;
				}
				
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
	 * @return the best value for minABPlayer
	 */
	double minABPlayer(GameState current, long depthLeft, double alpha, double beta) {
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
			generatedNodes += validMoves.size();
			for (int i = 0; i < validMoves.size(); i++) {
				Move childMove = validMoves.get(i);
				GameState nextState = current.makeMove(childMove);
				boolean turnRepeats = current.getTurn() == nextState.getTurn();
				if (turnRepeats) {
					curVal = minABPlayer(nextState, depthLeft - 1, alpha, beta);
				} else {
					curVal = maxABPlayer(nextState, depthLeft - 1, alpha, beta);
				}
				
				if (!turnRepeats && curVal <= alpha) {
					cutoff += (validMoves.size() - 1) - i;
					return curVal;
				}
				if (curVal < beta) {
					beta = curVal;
				} 

		
			}
			
			return beta;
		}
	}
	

	@Override
	public Move getMove(GameState g, long deadline) {
		initializeValuesWithZero();
		this.deadline = deadline - System.currentTimeMillis();
		currentTurn = g.getTurn();
		
		double start = (double) System.currentTimeMillis();
		Move bestMove = getABBestMove(g);
		double end = (double) System.currentTimeMillis();
		
		sumElapsedTime += (end - start);
		sumNodesGenerated += getNodesGenerated();
		sumNonLeafNodes += getNonLeafNodes();
		sumCutoff += getCutoff();
		
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
	
	double getSumCutoff() {
		return sumCutoff;
	}

	/*
	 * Initialize the number of generated nodes, nonLeafNodes, and cut-off nodes as zero.
	 */
	void initializeValuesWithZero() { 
		generatedNodes = 0;
		nonLeafNodes = 0;
		cutoff = 0;
	}	
	
	@Override
	public double staticEvaluator(GameState state) {	
		return state.getPlayerScore(currentTurn) - state.getPlayerScore(!currentTurn);
	}

	@Override
	public int getNodesGenerated() {
		return generatedNodes;
	}

	@Override
	public int getStaticEvaluations() {
		return staticEvalCounts;
	}
	
	double getNonLeafNodes() {
		return nonLeafNodes;
	}
	
	double getCutoff() {
		return cutoff;
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
		return (generatedNodes - 1 - cutoff) / (nonLeafNodes);
	}



	public String toString() {
		return name;
	}
}
