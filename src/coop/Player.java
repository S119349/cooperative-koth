package coop;

import java.util.ArrayList;

public abstract class Player {
	
	private boolean playedCard = false;
	Player(CoopGame gameState){
		this.gameState = gameState;
		hand = new ArrayList<Integer>();
	}
	void deal(int card) {
		hand.add(card);
	}

	void playCard(int card, int deck) throws PlayerLost {
		if(playedCard){
			throw new PlayerLost(this.getClass() + ": Played without waiting for reactions.");
		} else {
			playedCard=true;
		}
		if (hand.contains(card)) {
			hand.remove(Integer.valueOf(card));
			if (deck < 2) {
				// Counting UP.
				if (card <= gameState.piles[deck] && (card - 10) != gameState.piles[deck]) {
					System.err.println("Illegal move");
					throw new PlayerLost(this.getClass() + ": Illegal move on deck " + deck);
				}
			} else {
				// Counting DOWN.
				if (card >= gameState.piles[deck] && (card + 10) != gameState.piles[deck]) {
					throw new PlayerLost(this.getClass() + ": Illegal move on deck " + deck);
				}
			}
			gameState.piles[deck]= card;
			nCardsPlayed++;
		} else {
			throw new PlayerLost(this.getClass() + ": Played card not in hand");
		}

	}

	void endTurn() throws PlayerLost{
		playedCard=false;
		hand.addAll(gameState.requestCards(6 - hand.size()));
		if(nCardsPlayed>0){
			nCardsPlayed = 0;
		} else {
			throw new PlayerLost(); //This is the only "acceptable" way of losing, so no err msg.
		}
	}
	
	private int nCardsPlayed = 0;
	
	// Returns whether next player can take a turn.
	public boolean turnHandler() throws PlayerLost{
		turn();
		if(playedCard = false){ //Only if endTurn was called 
			return true;
		} else {
			playedCard = false; // New card allowed
			return false;
		}
	}
	
	abstract void turn() throws PlayerLost;

	abstract int[] react();

	public int nHandCards(){
		return hand.size();
	}
	public String selfName;
	protected ArrayList<Integer> hand;
	protected CoopGame gameState;
}
