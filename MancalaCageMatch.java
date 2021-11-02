/**
 * Plays a series of matches among several players
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.*;

public class MancalaCageMatch {

	Random myRandom = new Random();
	
	int totalMoveinAGame = 0;
	int totalNodes = 0;
	double totalElapsedTime = 0;
	double totalNodesGenerated = 0;
	double totalElapsedTimeAB = 0;
	double totalNodesGeneratedAB = 0;
	double totalElapsedTimeMM = 0;
	double totalNodesGeneratedMM = 0;
	
	
	public static void main(String[] args) {
		RandomPlayer rp = new RandomPlayer();
		HumanMancalaPlayer hu = new HumanMancalaPlayer();
		MMMancalaPlayer mm = new MMMancalaPlayer("MM", 10);
		MMMancalaPlayer mm2 = new MMMancalaPlayer("MM", 10);

		AlphaBetaPlayer ab13 = new AlphaBetaPlayer("AB13", 13);
		AlphaBetaPlayer ab12 = new AlphaBetaPlayer("AB12", 12);
		NearRandomPlayer nrp = new NearRandomPlayer();
		OneMovePlayer omp = new OneMovePlayer();
		XXXMancalaPlayer xx = new XXXMancalaPlayer("XX", 10);
		HeuristicDeepeningMancalaPlayer phmm = new HeuristicDeepeningMancalaPlayer("phmm12", 12);
		MancalaCageMatch mcm = new MancalaCageMatch();
//		mcm.addPlayer(ab13);
		mcm.addPlayer(ab13);
		
//		mcm.addPlayer(mm);
//		mcm.addPlayer(mm2);
		mcm.addPlayer(phmm);
		
//		mcm.addPlayer(omp);
//		mcm.addPlayer(rp);
//		mcm.addPlayer(nrp);

//		mcm.addPlayer(phmm);
//		mcm.addPlayer(phmm);
		mcm.runGames();

	}

	ArrayList<SortablePlayer> thePlayers;

	public MancalaCageMatch() {
		thePlayers = new ArrayList<>();
	}

	public void runGames() {
//		int cnt = 50;
		
		double totalGameMove = 0;
		int cnt = 100;
		while (cnt > 0) {
			for (int i = 0; i < thePlayers.size(); ++i) {
				for (int j = i + 1; j < thePlayers.size(); ++j) {

					SortablePlayer sp1 = thePlayers.get(i);
					SortablePlayer sp2 = thePlayers.get(j);
					
					MMMancalaPlayer MMPlayer = null;
					AlphaBetaPlayer ABPlayer = null;
					
//					if (sp1.myPlayer instanceof MMMancalaPlayer) {
//						((AlphaBetaPlayer) sp1.myPlayer).setTotalNodesAndTimeZero();
//						((AlphaBetaPlayer) sp2.myPlayer).setTotalNodesAndTimeZero();
//					} else {
//						((AlphaBetaPlayer) sp2.myPlayer).setTotalNodesAndTimeZero();
//						((AlphaBetaPlayer) sp1.myPlayer).setTotalNodesAndTimeZero();
//					}
					
//					if (sp1.myPlayer instanceof HeuristicDeepeningMancalaPlayer) {
//						((HeuristicDeepeningMancalaPlayer) sp1.myPlayer).depthLimit = 12;
//					} else if (sp2.myPlayer instanceof HeuristicDeepeningMancalaPlayer){
//						((HeuristicDeepeningMancalaPlayer) sp2.myPlayer).depthLimit = 12;
//					}
					
					System.out.println(sp1.myPlayer + " versus " + sp2.myPlayer);
					Mancala m = new Mancala(sp1.myPlayer, sp2.myPlayer, 7, 4, 2000);
					long p1FirstScore = m.playGame();

//					if (sp1.myPlayer instanceof MMMancalaPlayer) {
//						totalElapsedTimeMM += ((MMMancalaPlayer) sp1.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedMM += ((MMMancalaPlayer) sp1.myPlayer).getSumNodesGenerated();
//						totalElapsedTimeAB += ((MMMancalaPlayer) sp2.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedAB += ((MMMancalaPlayer) sp2.myPlayer).getSumNodesGenerated();
//					} else {
//						totalElapsedTimeMM += ((AlphaBetaPlayer) sp2.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedMM += ((AlphaBetaPlayer) sp2.myPlayer).getSumNodesGenerated();
//						totalElapsedTimeAB += ((AlphaBetaPlayer) sp1.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedAB += ((AlphaBetaPlayer) sp1.myPlayer).getSumNodesGenerated();
//					}
//					System.out.println("********************************************************TotalNodes: " + totalNodesGeneratedMM);
					
//					
//					if (sp1.myPlayer instanceof MMMancalaPlayer) {
//						((MMMancalaPlayer) sp1.myPlayer).setTotalNodesAndTimeZero();
//						((MMMancalaPlayer) sp2.myPlayer).setTotalNodesAndTimeZero();
//					} else {
//						((AlphaBetaPlayer) sp2.myPlayer).setTotalNodesAndTimeZero();
//						((AlphaBetaPlayer) sp1.myPlayer).setTotalNodesAndTimeZero();
//					}
					
//					if (sp1.myPlayer instanceof HeuristicDeepeningMancalaPlayer) {
//						((HeuristicDeepeningMancalaPlayer) sp1.myPlayer).depthLimit = 12;
//					} else if (sp2.myPlayer instanceof HeuristicDeepeningMancalaPlayer){
//						((HeuristicDeepeningMancalaPlayer) sp2.myPlayer).depthLimit = 12;
//					}
					
					m = new Mancala(sp2.myPlayer, sp1.myPlayer, 7, 4, 2000);
					long p2FirstScore = m.playGame();
					totalGameMove += m.totalMoveCount;
					
//					if (sp1.myPlayer instanceof MMMancalaPlayer) {
//						totalElapsedTimeMM += ((MMMancalaPlayer) sp1.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedMM += ((MMMancalaPlayer) sp1.myPlayer).getSumNodesGenerated();
//						totalElapsedTimeAB += ((MMMancalaPlayer) sp2.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedAB += ((MMMancalaPlayer) sp2.myPlayer).getSumNodesGenerated();
//					} else {
//						totalElapsedTimeMM += ((AlphaBetaPlayer) sp2.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedMM += ((AlphaBetaPlayer) sp2.myPlayer).getSumNodesGenerated();
//						totalElapsedTimeAB += ((AlphaBetaPlayer) sp1.myPlayer).getSumElapsedTime();
//						totalNodesGeneratedAB += ((AlphaBetaPlayer) sp1.myPlayer).getSumNodesGenerated();
//					}
					
					long totalScore = p1FirstScore - p2FirstScore;
					sp1.gamesPlayed++;
					sp2.gamesPlayed++; 
					if (totalScore > 0) {
						System.out.println(
								sp1.myPlayer + " beats " + sp2.myPlayer + " " + p1FirstScore + " to " + p2FirstScore);
						sp1.wins++;
					} else if (totalScore == 0) {
						System.out.println(sp1.myPlayer + " and " + sp2.myPlayer + " tie");

						sp1.wins += .5;
						sp2.wins += .5;
					} else {
						System.out.println(
								sp2.myPlayer + " beats " + sp1.myPlayer + " " + p2FirstScore + " to " + p1FirstScore);

						sp2.wins++;
					}
					Collections.sort(thePlayers);
					System.out.println("w%\twins\tgames\tname");
					for (int k = 0; k < thePlayers.size(); ++k) {
						System.out.println(thePlayers.get(k));
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			cnt--;
		}
		
//		System.out.println("========================================================");
//		System.out.println("Number of Nodes Generated: " + totalNodesGeneratedMM);
//		System.out.println("Total Elapsed Time: " + totalElapsedTimeMM);
//		System.out.println("Nodes per second: " + (totalNodesGeneratedMM / (totalElapsedTimeMM / 1000.0)));
//
//		System.out.println("========================================================");
//		System.out.println("Number of Nodes Generated: " + totalNodesGeneratedAB);
//		System.out.println("Total Elapsed Time: " + totalElapsedTimeAB);
//		System.out.println("Nodes per second: " + (totalNodesGeneratedAB / (totalElapsedTimeAB / 1000.0)));
	}

	public void addPlayer(MancalaPlayer p) {
		thePlayers.add(new SortablePlayer(p));
	}

	private class SortablePlayer implements Comparable<SortablePlayer> {
		double gamesPlayed;
		double wins;
		MancalaPlayer myPlayer;

		public SortablePlayer(MancalaPlayer p) {
			myPlayer = p;
			gamesPlayed = 0;
			wins = 0;
		}

		double getWinningPct() {
			if (gamesPlayed == 0) {
				return 0;

			} else {
				return wins / gamesPlayed;
			}
		}

		public String toString() {
			return String.format("%.2f", getWinningPct()) + "\t" + wins + "\t" + gamesPlayed + "\t" + myPlayer;
		}

		@Override
		public int compareTo(SortablePlayer o) {
			double winPct = getWinningPct();
			double oWinPct = o.getWinningPct();
			if (winPct > oWinPct) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
