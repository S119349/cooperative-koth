package coop;

import java.util.ArrayList;

public class SimpleTom extends Player {

	public SimpleTom(CoopGame gameState) {
		super(gameState);
		selfName = "SimpleTom";
	}

	@Override
	void turn() throws PlayerLost {
		for(int deck = 0; deck<2; ++deck){
			for(int card : new ArrayList<Integer>(hand)){
				if(card - 10 == gameState.piles[deck]){
					playCard(card,deck);
					return;
				} else if(card > gameState.piles[deck]){
					playCard(card,deck);
					return;
				}
			}
		}
		for(int deck = 2; deck<4; ++deck){
			for(int card : new ArrayList<Integer>(hand)){
				if(card + 10 == gameState.piles[deck]){
					playCard(card,deck);
					return;
				} else if(card < gameState.piles[deck]){
					playCard(card,deck);
					return;
				}
			}
		}
		// No cards could be played from my hand, just give up now.
		endTurn();
		
	}

	@Override
	int[] react() {
		return new int[] {0,0,0,0};
	}

}
