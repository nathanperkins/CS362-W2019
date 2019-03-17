//Smithy, with bug introduced 
int smithy(int currentPlayer, struct gameState *state, int handPos){
    //Draw 3 cards
    times = 3
    //introduced an off by 1 error here:
    for(int=1, i<times, i++){
        drawCard(currentPlayer, state);
    }
    
    //discard card from hand
    discardCard(handPos, currentPlayer, state, 0);
    return 0;
}

//Adventurer
int adventurer(struct gameState *state, int currentPlayer){
    while(drawntreasure<2){
    	if (state->deckCount[currentPlayer] <1){//if the deck is empty we need to shuffle discard and add to deck
    	  shuffle(currentPlayer, state);
    	}
    	drawCard(currentPlayer, state);
    	cardDrawn = state->hand[currentPlayer][state->handCount[currentPlayer]-1];//top card of hand is most recently drawn card.
    	if (cardDrawn == copper || cardDrawn == silver || cardDrawn == gold)
    	  drawntreasure++;
    	else{
    	  temphand[z]=cardDrawn;
    	  state->handCount[currentPlayer]--; //this should just remove the top card (the most recently drawn one).
    	  z++;
    	}
      }
    while(z-1>=0){
    	state->discard[currentPlayer][state->discardCount[currentPlayer]++]=temphand[z-1]; // discard all cards in play that have been drawn
    	z=z-1;
      }
    return 0;
}

int cutpurse(int currentPlayer, struct gameState *state ){
    updateCoins(currentPlayer, state, 2);
    for (i = 0; i < state->numPlayers; i++){
        if (i != currentPlayer){
          for (j = 0; j < state->handCount[i]; j++){
              if (state->hand[i][j] == copper){
                  discardCard(j, i, state, 0);
                  break;
                }
                if (j == state->handCount[i]){
                  for (k = 0; k < state->handCount[i]; k++){
            	    if (DEBUG)
            	        printf("Player %d reveals card number %d\n", i, state->hand[i][k]);
        	        }	
                  break;
                }		
            }
        			
        }
    		
    }   				
    
    //discard played card from hand
    discardCard(handPos, currentPlayer, state, 0);			
    return 0;
}

int council_room(struct gameState *state, int currentPlayer){
    //+4 Cards
      for (i = 0; i < 4; i++){
	  drawCard(currentPlayer, state);
	}
			
    //+1 Buy
    state->numBuys++;
			
    //Each other player draws a card
    for (i = 0; i < state->numPlayers; i++){
        if ( i != currentPlayer ){
	      drawCard(i, state);
	    }
	}
			
    //put played card in played card pile
    discardCard(handPos, currentPlayer, state, 0);
    	
    return 0;
}

int gardens(){
    return -1;
}


