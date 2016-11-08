package com.anthony;

// This class is for each card of the deck
class Card {
    private String face; // Name of the card
    private char suit; // Stores the cards suit (Club, Spade, Diamond, Heart)
    private int value; // Stores the point value
    private boolean isFaceUp;


    Card(int pos, int su) {
        // Unicode characters for playing card symbols
        char spadeCh = 9824;
        char clubCh = 9827;
        char heartCh = 9829;
        char diamondCh = 9830;
        isFaceUp = false; // Makes the cards position face down
        if (pos == 1) {
            face = "A";
            value = 1;
        } else if (1 < pos && pos < 11){
            face = Integer.toString(pos);
            if (pos != 2){ // Makes two negative in value
                value = pos;
            } else {
                value = -2;
            }
        } else if (pos == 11){
            face = "J";
            value = 10;
        } else if (pos == 12){
            face = "Q";
            value = 10;
        } else {
            face = "K";
            value = 0;
        }
        if (su == 1) {
            suit = spadeCh;
        } else if (su == 2) {
            suit = clubCh;
        } else if (su == 3) {
            suit = diamondCh;
        } else {
            suit = heartCh;
        }
    }

    boolean getFaceUp(){ // Method to return if the card is face up
        return isFaceUp;
    }
    void setFaceUp(){
        this.isFaceUp = true;
    }


    @Override
    public String toString(){// Allow the card to be printed correctly
        if (this.isFaceUp) {
            return (face + " of " + suit);
        } else {
            return "This card is not face up";
        }
    }

    int getValue() { // Returns the value of the card
        return value;
    }

    String getFace() {
        return face;
    }

    boolean isFaceUp() {
        return isFaceUp;
    }
}
