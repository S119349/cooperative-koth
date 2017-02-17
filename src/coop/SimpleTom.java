package coop;

import java.util.ArrayList;

public class SimpleTom extends Player {

	public SimpleTom(CoopGame gameState) {
		super(gameState);
	}

	@Override
	public void turn() throws PlayerLost {
		for(int pile = 0; pile<2; ++pile){
			for(int card : new ArrayList<Integer>(hand)){
				if(card - 10 == gameState.piles[pile]){
					playCard(card,pile);
					return;
				} else if(card > gameState.piles[pile]){
					playCard(card,pile);
					return;
				}
			}
		}
		for(int pile = 2; pile<4; ++pile){
			for(int card : new ArrayList<Integer>(hand)){
				if(card + 10 == gameState.piles[pile]){
					playCard(card,pile);
					return;
				} else if(card < gameState.piles[pile]){
					playCard(card,pile);
					return;
				}
			}
		}
		// No cards could be played from my hand, just give up now.
		endTurn();
		
	}

	@Override
	public int[] react() {
		return new int[] {0,0,0,0};
	}

}
