package com.anthony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayList<Card> deckOfCards = new ArrayList<Card>();
    public Deck(){ // Create deck of cards

    }
    void createMainDeck(){
        for (int i = 1; i < 5; i++) { // Foreach suit
            for (int j = 1; j < 14; j++) { // Foreach position
                deckOfCards.add(new Card(j, i));
            }
        }
    }
    void shuffleDeck(){
        // Shuffle
        // http://stackoverflow.com/questions/4228975/how-to-randomize-arraylist
        // https://www.tutorialspoint.com/java/util/collections_shuffle_random.htm
        long seed = System.nanoTime();
        Collections.shuffle(this.deckOfCards, new Random(seed));
    }

    // Method to return and remove a card from the deck
    Card dealCard(){
        Card card = this.deckOfCards.get(this.deckOfCards.size() - 1);
        this.deckOfCards.remove(this.deckOfCards.size() - 1);
        return card;
    }

    void postDeck(){ // Print out the entire deck
        for (Card card: this.deckOfCards) {
            System.out.println(card.toString());
        }
    }

    void addCard(Card faceUpCard){
        faceUpCard.setFaceUp(); // Set card face up
        this.deckOfCards.add(faceUpCard); // Add card to discard pile
    }

    // Method to return the top card of the deck
    Card getTopCard(){
        return this.deckOfCards.get(this.deckOfCards.size() - 1);
    }

    // Method to print the top card
    void showTopCard(){
        System.out.println(this.deckOfCards.get(this.deckOfCards.size() - 1).toString());
    }
}
