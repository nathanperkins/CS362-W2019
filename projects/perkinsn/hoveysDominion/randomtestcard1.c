/**********************************************
 * Assignment 4
 * Sara Hovey
 * CS 362 
 * Winter 2019
 * gcc -o randomtestcard1 randomtestcard1.c -g dominion.o rngs.o -Wall -fpic -coverage -lm -std=c99
***********************************************/

#include "dominion.h"
#include "dominion_helpers.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "rngs.h"
#include <stdlib.h>
#include <math.h>
#include <time.h>

int main() {

    int isPassed = 1;
    int numberOfTests = 50;
    int handPosition = 0;
    int firstChoice = 0, secondChoice = 0, thirdChoice = 0;
    int bonus = 0;
    int currentPlayer;
    int addedCards = 4;
    int discard = 1;
    int addedBuys = 1;
    int otherPlrAdded = 1;
    int playerHand[4];
    int copyPlayerHand[4];
    
    char bee[] = "\U0001F41D";
    char angery[] = "\U0001F608";
    
    struct gameState Game, copyOfGame;
    int i, handPos, returnValue;
	int totalCards, copytotalCards;

    // Seed random with time
    srand(time(NULL));


    printf("\n**********************Testing Çouncil Room******************\n");

    // Populate the struct holding the game state with random numbers 
    for (i = 0; i < numberOfTests; i++){
        for (handPos = 0; handPos < sizeof(struct gameState)/ sizeof(int); handPos++) {
            ((int*)&Game)[handPos] = rand() % 128;
        }

        // Here we make sure that the parts of the game state that we will be accessing
        // are populated with valid input
        Game.numPlayers = (rand() % 3)+2;
        Game.whoseTurn = rand() % Game.numPlayers;
        currentPlayer = whoseTurn(&Game);
        
        Game.handCount[currentPlayer] = (rand() % (MAX_HAND/2))+1;
        Game.deckCount[currentPlayer] = (rand() % (MAX_DECK/2))+1;
        Game.discardCount[currentPlayer] = (rand() % (MAX_DECK/2))+1;
        
        Game.numBuys = 0;
        handPosition = (rand() % Game.handCount[currentPlayer]);


        // Make a copy of the game state struct such that we can
        // compare the original state to changes made via tested functions
        memcpy(&copyOfGame, &Game, sizeof(struct gameState));

        //Since my function is int and not void, we can test return value here
        returnValue = cardEffect(council_room, firstChoice, secondChoice, thirdChoice, &copyOfGame, handPosition, &bonus);
        
        //Expecting a 0 to indicate nothing went wrong
        if (returnValue != 0) {
            isPassed = 0;
            printf("%s TEST FAILED on return value %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on return value %s\n", bee, bee);
        }

        // Test number of cards in the hand
        if (copyOfGame.handCount[currentPlayer] != Game.handCount[currentPlayer]+addedCards-discard){
            isPassed = 0;
            printf("%s TEST FAILED on count of cards in hand %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on count of cards in hand %s\n", bee, bee);
        }
        printf("Actual hand count: %d, Expected hand count: %d \n\n", copyOfGame.handCount[currentPlayer], Game.handCount[currentPlayer]+addedCards-discard);

		// Test number of cards in deck and discard pile
        totalCards = Game.deckCount[currentPlayer] + Game.discardCount[currentPlayer];
		copytotalCards = copyOfGame.deckCount[currentPlayer] + copyOfGame.discardCount[currentPlayer];
		if (copytotalCards != totalCards - addedCards) {
			isPassed = 0;
            printf("%s TEST FAILED on total card count %s\n", angery, angery);
		}
		else{
            printf("%s TEST PASSED on total card count %s\n", bee, bee);
        }
		printf("Actual total cards: %d, Expected total cards: %d \n\n", copytotalCards, totalCards-addedCards);

        // Test number of buy actions the player has 
        if (copyOfGame.numBuys != Game.numBuys + addedBuys) {
            isPassed = 0;
            printf("%s TEST FAILED on count of cards in hand %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on buy count %s\n", bee, bee);
        }
        printf("Actual buys: %d, Expected buys: %d \n\n", copyOfGame.numBuys, Game.numBuys+addedBuys);
        
        for (handPos = 0; handPos < Game.numPlayers; handPos++) {
            if (handPos != currentPlayer) {
                copyPlayerHand[handPos] = copyOfGame.handCount[handPos];
                playerHand[handPos] = Game.handCount[handPos];
            }
        }

        //test the other players hand count
        for (handPos = 0; handPos < Game.numPlayers; handPos++) {
            if (handPos != currentPlayer) {
                if (copyPlayerHand[handPos] != playerHand[handPos] + otherPlrAdded) {
                    isPassed = 0;
                    printf("%s TEST FAILED on count of other player's hand %s\n", angery, angery);
                }
                else{
                    printf("%s TEST PASSED on count of other player's hand %s\n", bee, bee);
                }
			    printf("Actual count of other hand:: %d, Expected count of other hand: %d \n\n", copyPlayerHand[handPos],  playerHand[handPos] + otherPlrAdded);
            }
        }
    
    }


    //If the tests have not already failed, return true!
    if(isPassed){
        printf("%s ALL TESTS PASSED %s\n", bee, bee);
    }

    return 0;
};