package coop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CoopGame {

	public static void main(String[] args) {
		List<Class<? extends Player>> allPlayers = new ArrayList<Class<? extends Player>>();
		/**
		 * Add all players here:
		 * 
		 * v v v v v v v v v v v v v v v v 
		 */
		allPlayers.add(SimpleTom.class);
		
		/**
		 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
		 */

		final int nTrails = (int) 1e6;

		// Generate empty score table.
		Map<Class<? extends Player>, List<Integer>> scores = new HashMap<Class<? extends Player>, List<Integer>>();
		for (Class<? extends Player> aPlayer : allPlayers) {
			scores.put(aPlayer, new ArrayList<Integer>());
		}

		// Do the game with random players, many times!
		Random randomSource = new Random();
		for (int i = 0; i < nTrails; i++) {
			
			// Players for the following game
			List<Class<? extends Player>> thisGamePlayers = new ArrayList<Class<? extends Player>>(4);

			for (int n = 0; n < 4; ++n) {
				Class<? extends Player> playerToAdd = allPlayers.get(randomSource.nextInt(allPlayers.size()));
				thisGamePlayers.add(playerToAdd);
			}
			
			// Play the game! (try/catch to (not) deal with reflection)
			try {
				CoopGame game = new CoopGame(thisGamePlayers);
				for (Class<? extends Player> aPlayer : thisGamePlayers) {
					scores.get(aPlayer).add(game.nCardsLeft);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		//Calculate final scores.
		Map<Class<? extends Player>, Double> avgScores = new HashMap<Class<? extends Player>, Double>();
		for (Class<? extends Player> aPlayer : allPlayers) {
			int sum = 0;
			for (Integer roundScore : scores.get(aPlayer)) {
				sum += roundScore;
			}
			double playerAvg = ((double) sum) / scores.get(aPlayer).size();
			avgScores.put(aPlayer, playerAvg);
		}
		System.out.println(avgScores);
	}

	public CoopGame(List<Class<? extends Player>> playerClasses) throws Exception {

		List<Player> playersAsList = new ArrayList<Player>(4);
		// Invite players to the table
		for (Class<? extends Player> playerClass : playerClasses) {
			playersAsList.add(playerClass.getConstructor(CoopGame.class).newInstance(this));
		}
		players = playersAsList.toArray(new Player[4]);

		// Shuffle the stack, deal the cards.
		shuffleStack();
		dealCards();

		// Set up public elements
		piles = new int[] { 1, 1, 100, 100 };
		priorities = new HashMap<Player, int[]>();

		// Play game
		while (playRound()) {
			continue;
		}
		// Get results
		nCardsLeft = stack.size();
		for (Player player : players) {
			nCardsLeft += player.nHandCards();
		}

	}

	private void shuffleStack() {
		ArrayList<Integer> cards = new ArrayList<Integer>(98);
		for (int i = 1; i < 100; ++i) {
			cards.add(i);
		}
		Collections.shuffle(cards);
		stack = new ArrayDeque<Integer>(cards);
	}

	private void dealCards() {
		for (int i = 0; i < 6; ++i) {
			for (Player player : players) {
				player.deal(stack.pop());
			}
		}
	}

	public ArrayList<Integer> requestCards(int nCards) {
		nextPlayer = true;
		int nCardsOut = Integer.min(nCards, stack.size());
		ArrayList<Integer> cardsOut = new ArrayList<Integer>(nCardsOut);
		for (int i = 0; i < nCardsOut; ++i) {
			cardsOut.add(stack.pop());
		}
		return cardsOut;
	}

	private boolean playRound() {
		for (Player currentPlayer : players) {
			nextPlayer = false;
			do {
				try {
					nextPlayer = currentPlayer.turnHandler();
				} catch (PlayerLost e) {
					return false;
				}
				for (Player reactingPlayer : players) {
					priorities.put(reactingPlayer, reactingPlayer.react());
				}
			} while (!nextPlayer);
		}
		return true;
	}

	private int nCardsLeft;
	protected Player[] players;

	private boolean nextPlayer = false;

	private Deque<Integer> stack;
	public Map<Player, int[]> priorities;
	public List<String> playerNames;
	public int[] piles;

}
