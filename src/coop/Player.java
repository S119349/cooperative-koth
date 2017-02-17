package coop;

import java.util.ArrayList;

public abstract class Player {

	/**
	 * Variables accessible by players:
	 */
	protected ArrayList<Integer> hand;
	protected CoopGame gameState;
	
	/**
	 * Methods that should be implemented by players:
	 */
	public abstract void turn() throws PlayerLost;
	public abstract int[] react();
	
	/**
	 * Game logic. You may use playCard() and endTurn(). Others may see int nHandCards().
	 */
	protected void playCard(int card, int pile) throws PlayerLost {
		if(playedCard){
			throw new PlayerLost(this.getClass() + ": Played without waiting for reactions.");
		} else {
			playedCard=true;
		}
		if (hand.contains(card)) {
			hand.remove(Integer.valueOf(card));
			if (pile < 2) {
				// Counting UP.
				if (card <= gameState.piles[pile] && (card - 10) != gameState.piles[pile]) {
					System.err.println("Illegal move");
					throw new PlayerLost(this.getClass() + ": Illegal move on deck " + pile);
				}
			} else {
				// Counting DOWN.
				if (card >= gameState.piles[pile] && (card + 10) != gameState.piles[pile]) {
					throw new PlayerLost(this.getClass() + ": Illegal move on deck " + pile);
				}
			}
			gameState.piles[pile]= card;
			nCardsPlayed++;
		} else {
			throw new PlayerLost(this.getClass() + ": Played card not in hand");
		}

	}

	protected void endTurn() throws PlayerLost{
		playedCard=false;
		hand.addAll(gameState.requestCards(6 - hand.size()));
		if(nCardsPlayed>1 || hand.size()==0){
			nCardsPlayed = 0;
		} else {
			throw new PlayerLost(); //This is the only "acceptable" way of losing, so no err msg.
		}
	}
	
	public int nHandCards(){
		return hand.size();
	}
	/**
	 * Stuff for game logic.
	 */
	Player(CoopGame gameState){
		this.gameState = gameState;
		hand = new ArrayList<Integer>();
	}
	
	void deal(int card) {
		hand.add(card);
	}
	
	// Returns whether next player can take a turn.
	public boolean turnHandler() throws PlayerLost{
		turn();
		if(playedCard = false){ //Only if endTurn was called or nothing at all
			return true;
		} else {
			playedCard = false; // New card allowed
			return false;
		}
	}
	
	private int nCardsPlayed = 0;
	private boolean playedCard = false;
}
