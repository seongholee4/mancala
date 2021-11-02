
/**
 * Plays a match between two players
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */
public class Mancala {

	MancalaPlayer p1, p2, randomPlayer;

	int size, startingStones,time;
	int totalMoveCount = 0;
	double sumAveBranchingFactor = 0;
	double sumGeneratedNodes = 0;
	double sumNonLeafNodes = 0;
	
	double totalElapsedTimeMM = 0;
	double totalNodesGeneratedMM = 0;
	double totalElapsedTimeAB = 0;
	double totalNodesGeneratedAB = 0;
	double totalNonLeafNodesMM = 0;
	double totalNonLeafNodesAB = 0;
	
	double totalCutoffMM = 0;
	double totalCutoffAB = 0;

	
	public static void main(String[] args) {

		RandomPlayer rp = new RandomPlayer();
		HumanMancalaPlayer hu = new HumanMancalaPlayer();
		MMMancalaPlayer mm = new MMMancalaPlayer("MM", 9);
		MMMancalaPlayer mm2 = new MMMancalaPlayer("MM2", 9);

		AlphaBetaPlayer ab = new AlphaBetaPlayer("AB", 17);
		AlphaBetaPlayer ab2 = new AlphaBetaPlayer("AB2", 17);
		NearRandomPlayer nrp = new NearRandomPlayer();
		OneMovePlayer omp = new OneMovePlayer();
//		Mancala m = new Mancala(mm, omp, 7,4,10000);
		Mancala m = new Mancala(ab, ab2, 7,4,1500);
		m.playGame();
		
	}

	public Mancala(MancalaPlayer p1, MancalaPlayer p2, int size, int startingStones, int time) {
		this.p1 = p1;
		this.p2 = p2;
		this.size = size;
		this.startingStones = startingStones;
		this.time = time;
		randomPlayer = new RandomPlayer();
	}

	public long playGame() {
		return playGame(true);
	}
	
	public long playGame(boolean p1IsBottom) {
		int moveCount = 0;
		MancalaPlayer bottom,top;
		if(p1IsBottom) {
			bottom=p1;
			top = p2;
		}else {
			bottom = p2;
			top = p1;
		}
		GameState g = new GameState(size, startingStones);
		while (!g.isGameOver()) {

			moveCount++;
			totalMoveCount++;
			GameThread t;
			if (g.isBottomTurn) {
				t = new GameThread(bottom,g,time);
				
			} else {

				t = new GameThread(top,g,time);
				
			}
//			if (bottom instanceof ProbHeuristicMinimaxMancalaPlayer) {
//				((ProbHeuristicMinimaxMancalaPlayer) bottom).setTotalNodesAndTimeZero();
//				((MMMancalaPlayer) top).setTotalNodesAndTimeZero();
//			} else {
//				((AlphaBetaPlayer) bottom).setTotalNodesAndTimeZero();
//				((AlphaBetaPlayer) top).setTotalNodesAndTimeZero();
//			}
		//	System.out.println("trying ");
			t.run();
			
//			if (bottom instanceof MMMancalaPlayer) {
//				totalNodesGeneratedMM += ((MMMancalaPlayer) bottom).getSumNodesGenerated();
//				totalNonLeafNodesMM += ((MMMancalaPlayer) bottom).getSumNonLeafNodes();
//				totalElapsedTimeMM += ((MMMancalaPlayer) bottom).getSumElapsedTime();
//				
//				totalNodesGeneratedAB += ((MMMancalaPlayer) top).getSumNodesGenerated();
//				totalNonLeafNodesAB += ((MMMancalaPlayer) top).getSumNonLeafNodes();
//				totalElapsedTimeAB += ((MMMancalaPlayer) top).getSumElapsedTime();
//			} else {
//				totalNodesGeneratedMM += ((AlphaBetaPlayer) bottom).getSumNodesGenerated();
//				totalNonLeafNodesMM += ((AlphaBetaPlayer) bottom).getSumNonLeafNodes();
//				totalCutoffMM += ((AlphaBetaPlayer) bottom).getSumCutoff();
//				totalElapsedTimeMM += ((AlphaBetaPlayer) bottom).getSumElapsedTime();
//				
//				totalNodesGeneratedAB += ((AlphaBetaPlayer) top).getSumNodesGenerated();
//				totalNonLeafNodesAB += ((AlphaBetaPlayer) top).getSumNonLeafNodes();
//				totalCutoffAB += ((AlphaBetaPlayer) top).getSumCutoff();
//				totalElapsedTimeAB += ((AlphaBetaPlayer) top).getSumElapsedTime();
//			}
			
			int segments =0;
			while(t.m==null && segments <8) {
				try {
					Thread.sleep(time/8);
					segments++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(t.m == null){
				System.out.println("Player took too long! " + t.p);
				System.out.println("Making random move...");
				t.m=randomPlayer.getMove(g,time);
			}
			g = g.makeMove(t.m);
//			g.printBoard();
//			System.out.println("Top is " + top);
//			System.out.println("Bottom is " + bottom);

		}
		long finalScore = g.getFinalNetScore();
		System.out.println( "Game over!");
		System.out.println("Final net score is " + finalScore );
		
		if(finalScore > 0) {
			System.out.println(bottom + " wins!");
		}else if(finalScore <0) {
			System.out.println(top + " wins!");
		}else {
			System.out.println("It's a tie!");
		}
		
//		System.out.println("=====================================================");
//		System.out.println("AveBranchingFactor: " + (totalNodesGeneratedMM - 1) / totalNonLeafNodesMM);
//		System.out.println("=====================================================");
//		System.out.println("EffectiveBranchingFactor: " + (totalNodesGeneratedMM - 1 - totalCutoffMM) / totalNonLeafNodesMM);
//		System.out.println("Total number of nodes generated (bot): " + totalNodesGeneratedMM);
//		System.out.println("Total number of nodes generated (top): " + totalNodesGeneratedAB);
//		System.out.println("Total elapsed time (bot): " + totalElapsedTimeMM);
//		System.out.println("Total elapsed time (top): " + totalElapsedTimeAB);
		
		
		
		/*
		System.out.println("=====================================================");
//		System.out.println("AverageBranching Factor: " + sumAveBranchingFactor);
		System.out.println("sumGeneratedNodes: " + sumGeneratedNodes);
		System.out.println("sumNonLeafNodes " + sumNonLeafNodes);
		System.out.println("AveBranchingFactor: " + (sumGeneratedNodes - 1) / sumNonLeafNodes);
		System.out.println(moveCount);
		System.out.println(sumAveBranchingFactor / totalMoveCount);
		*/
		return g.getFinalNetScore();
	}

	private class GameThread implements Runnable {

		MancalaPlayer p;
		GameState g;
		int time;
		Move m=null;

		public GameThread(MancalaPlayer p, GameState g, int time) {
			this.p = p;
			this.g = g;
			this.time = time;
		}

		@Override
		public void run() {
//			System.out.println("getting move");
//			long start = System.currentTimeMillis();
			m = p.getMove(g, System.currentTimeMillis() + time);
//			if (p instanceof MMMancalaPlayer) {
//				sumAveBranchingFactor += ((MMMancalaPlayer) p).getAveBranchingFactor();
//				sumGeneratedNodes += ((MMMancalaPlayer) p).getNodesGenerated();
//				sumNonLeafNodes += ((MMMancalaPlayer) p).getNonLeafNodes();
//				totalMoveCount ++;
//				
//			}
//			long end = System.currentTimeMillis();
//			end - start 누적 단위
//			System.out.println((end - start) + "mili seconds ..-------------------");
		}

	}
}
