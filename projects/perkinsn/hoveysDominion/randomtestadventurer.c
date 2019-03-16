/**********************************************
 * Assignment 4
 * Sara Hovey
 * CS 362 
 * Winter 2019
 * gcc -o randomtestadventurer randomtestadventurer.c -g dominion.o rngs.o -Wall -fpic -coverage -lm -std=c99
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
    struct gameState Game, copyOfGame;

    int numberOfTests = 100;
    int handPosition = 0;
    int firstChoice = 0, secondChoice = 0, thirdChoice = 0;
    int bonus = 0;
    int currentPlayer;
    int addedCards = 2;
    
    int treasure, copytreasure;
    int i, handPos, r, returnValue;
    int totalCards, copytotalCards;
    
    char bee[] = "\U0001F41D";
    char angery[] = "\U0001F608";
    
    // Seed random with time
    srand(time(NULL));


    printf("\n**********************Testing Adventurer******************\n");

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
        handPosition = (rand() % Game.handCount[currentPlayer]);
        
        // Randomly assign some treasure and estates to the player's hand
        for (handPos = 0; handPos < Game.handCount[currentPlayer]; handPos++) {
            r = (rand() % 2);
            if (r == 0) {
                Game.hand[currentPlayer][handPos] = copper;
            } else {
                Game.hand[currentPlayer][handPos] = estate;
            }
        }
        
        // Same to their deck
        for (handPos = 0; handPos < Game.deckCount[currentPlayer]; handPos++) {
            r = (rand() % 2);
            if (r == 0) {
                Game.deck[currentPlayer][handPos] = copper;
            } 
            else {
                Game.deck[currentPlayer][handPos] = estate;
            }
        }

        // Make a copy of the game state struct such that we can
        // compare the original state to changes made via tested functions
        memcpy(&copyOfGame, &Game, sizeof(struct gameState));

        // Since my function is int and not void, we can test return value here
        returnValue = cardEffect(adventurer, firstChoice, secondChoice, thirdChoice, &copyOfGame, handPosition, &bonus);
        
        if (returnValue != 0) {
            //Expecting a 0 to indicate nothing went wrong
            isPassed = 0;
            printf("%s TEST FAILED on return value %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on return value %s\n", bee, bee);
        }

        // Test the count of cards in the hand
        if (copyOfGame.handCount[currentPlayer] != Game.handCount[currentPlayer]+addedCards){
            isPassed = 0;
            printf("Actual hand count: %d, Expected hand count: %d \n\n", copyOfGame.handCount[currentPlayer], Game.handCount[currentPlayer]+addedCards);
        }

        // Test the count of the deck and discard pile
        totalCards = Game.deckCount[currentPlayer] + Game.discardCount[currentPlayer];
        copytotalCards = copyOfGame.deckCount[currentPlayer] + copyOfGame.discardCount[currentPlayer];
        if (copytotalCards != totalCards - addedCards) {
            isPassed = 0;
            printf("%s TEST FAILED on total card count %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on total card count %s\n", bee, bee);
        }
        printf("Actual cards: %d, Expected cards: %d \n\n", copytotalCards, totalCards-addedCards);

        // Test amount of treasure
        treasure = 0;
        handPos = 0;
        while (handPos < numHandCards(&Game)) {
            if (handCard(handPos, &Game) == copper) {
                treasure++;
            } 
            else if (handCard(handPos, &Game) == silver) {
                treasure += 2;
            } 
            else if (handCard(handPos, &Game) == gold) {
                treasure += 3;
            }
            handPos++;
        }

        copytreasure = 0;
        handPos = 0;
        while (handPos < numHandCards(&copyOfGame)) {
            if (handCard(handPos, &copyOfGame) == copper) {
                copytreasure++;
            } 
            else if (handCard(handPos, &copyOfGame) == silver) {
                copytreasure += 2;
            } 
            else if (handCard(handPos, &copyOfGame) == gold) {
                copytreasure += 3;
            }
            handPos++;
        };
        if (copytreasure < treasure+2) {
            isPassed = 0;
            printf("%s TEST FAILED treasure count %s\n", angery, angery);
        }
        else{
            printf("%s TEST PASSED on treasure count %s\n", bee, bee);
        }
        printf("Actual treasure: %d, Expected treasure: >= %d \n\n", copytreasure, treasure+2);
    }
    
    //If the tests have not already failed, return true!
    if(isPassed){
        printf("%s ALL TESTS PASSED %s\n", bee, bee);
    }

    return 0;
};