package com.anthony;

import java.util.ArrayList;

// Class to create a players hand while implementing some methods

class HandArray<C> extends ArrayList<Card> { // ** Have to make sure it extends an ArrayList that stores Card
    // Keeps track of this hands score
    private int score = 0;

    HandArray() {

    }

    // Method to calculate the hands value
    void scoreCards(){
        score = 0;
        for (Card c : this) {
            score += c.getValue();
        }
        if (this.get(0).getFace().equalsIgnoreCase(this.get(3).getFace())){ // Test first column
            score -= (this.get(0).getValue() + this.get(3).getValue());
        }
        if (this.get(1).getFace().equalsIgnoreCase(this.get(4).getFace())){ // Test second column
            score -= (this.get(1).getValue() + this.get(4).getValue());
        }
        if (this.get(2).getFace().equalsIgnoreCase(this.get(5).getFace())){ // Test third column
            score -= (this.get(2).getValue() + this.get(5).getValue());
        }
    }
    // todo Do proper padding for displaying cards
    // http://stackoverflow.com/questions/388461/how-can-i-pad-a-string-in-java - padding
    void returnHand() { // Complicated method to display each card in the HandArray class correctly
        for (int i = 0; i < this.size(); i++) { // Loops over each card
            if (i < 2) {
                if (this.get(i).getFaceUp()){
                    System.out.print(this.get(i).toString() + "   |");
                } else {
                    System.out.print("Face Down|");
                }
            } else if (i == 2){
                if (this.get(2).getFaceUp()){
                    System.out.print(this.get(i).toString() + "   |\n");
                } else {
                    System.out.print("Face Down|\n");
                }
            } else if (i < 6){
                if (this.get(i).getFaceUp()){
                    System.out.print(this.get(i).toString() + "   |");
                } else {
                    System.out.print("Face Down|");
                }
            }
        }
        System.out.println(""); // Returns the terminal to the next line
    }

    boolean allFaceUp(){ // Method to test if all cards are flipped face up
        boolean allFlipped = true;
        if (!this.get(0).isFaceUp()){
            allFlipped = false;
        } else if (!this.get(1).isFaceUp()){
            allFlipped = false;
        } else if (!this.get(2).isFaceUp()){
            allFlipped = false;
        } else if (!this.get(3).isFaceUp()){
            allFlipped = false;
        } else if (!this.get(4).isFaceUp()){
            allFlipped = false;
        } else if (!this.get(5).isFaceUp()){
            allFlipped = false;
        }
        return allFlipped;
    }

    Card switchCards(Card switchCard, int position){
        Card returnCard = this.get(position);
        this.set(position, switchCard);
        this.get(position).setFaceUp();
        returnCard.setFaceUp(); // Set return card face up
        return returnCard; // Return the card that was replaced
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
