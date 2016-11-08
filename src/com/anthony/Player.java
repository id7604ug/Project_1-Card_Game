package com.anthony;

import java.util.Collection;
import java.util.Random;
import java.util.ArrayList;

class Player {
    HandArray<Card> hand = new HandArray<>();
    private String name;
//    private static int nextTablePos = 0;
    boolean isDealer = false;

    private int aiMode;
    private ArrayList<String> moveOrder = new ArrayList<>();



    private boolean isAI = false;

//    private String handR1 = ""; // Create row one for the players hand
//    private String handR2 = ""; // Create row two for the players hand

    Player(int tablePosition){
//        tablePosition = tablePosition; // Give player next position
        name = "Player " + (tablePosition + 1); // Give player name
    }

    String getName(){ // Get the players name
        return name;
    }

    void giveCard(Card card){ // Adds a card to the players hand
        hand.add(card);
    }


    ArrayList<String> decideMove(){ // Method to decide the order of moves for each ai
        aiMode = new Random().nextInt(2);
//        aiMode = 0; // Test
        if (aiMode == 0){ // Safe play
            // Will go after getting at least one of each column before working on getting pairs
            moveOrder.add("Column");
            moveOrder.add("Pairs");
            moveOrder.add("LowestCard");
        } else if (aiMode == 1){ // Quicker
            // Will go after getting pairs before working on getting each column
            moveOrder.add("Pairs");
            moveOrder.add("LowestCard");
            moveOrder.add("Pairs");
        } else if (aiMode == 2){ // Slow play
            // Will go after the lowest known card
            moveOrder.add("LowestCard");
            moveOrder.add("Column");
            moveOrder.add("Pairs");
        }
        moveOrder.add("DrawCard");
        return moveOrder;
    }

    boolean isAI() {
        return isAI;
    }

    void setAI(boolean AI) {
        isAI = AI;
    }

    ArrayList<Card> getHand() { // Return the players hand
        return hand;
    }

    public Integer getScore() {
        return this.hand.getScore();
    }
}
