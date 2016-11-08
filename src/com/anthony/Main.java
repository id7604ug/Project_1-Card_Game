package com.anthony;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// http://stackoverflow.com/questions/551578/how-to-break-multiple-foreach-loop
public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("How many players will be playing? (2, 3, 4)");
        String playerCountInput;
        int playerCount;
        while (true) {
            try { // Validation
                playerCountInput = scan.nextLine();
                playerCount = Integer.parseInt(playerCountInput);
                if (playerCount < 2 || playerCount > 4){
                    throw new NumberFormatException();
                }
                break; // Break from loop
            } catch (NumberFormatException nfe){
                System.out.println("Please enter a valid integer between 2 and 4");
            }
        }
        ArrayList<Integer> playerScoreList = new ArrayList<>(); // List of each players score
        for (int i = 0; i < playerCount; i++) { // Create values for the player score list
            playerScoreList.add(0);
        }

        int rounds = 0;
        while (rounds <= 9) { // Loop for the whole game
            rounds += 1; // Increment the amound of rounds
            System.out.println("------------------------- Round " + rounds + " -------------------------");
            ArrayList<Player> playerList = new ArrayList<>(); // List of players

            Deck mainDeck = new Deck(); // Create deck of cards
            Deck discardDeck = new Deck(); // Create deck with face up cards
            mainDeck.createMainDeck(); // Calls the createMainDeck method to populate the 52 card deck
            mainDeck.shuffleDeck(); // Shuffle the deck
            // Start of player choices for picking cards to flip over
            for (int i = 0; i < playerCount; i++) { // Creates each player
                playerList.add(new Player(i));
                if (i >= 1){
                    playerList.get(i).setAI(true);
                }
            }

//          // Set the dealer
            playerList.get(0).isDealer = true;
            // Loops 6 times to give each player 6 cards
            for (int i = 0; i < 6; i++) {
                for (Player p : playerList) {
                    p.giveCard(mainDeck.dealCard());
                }
            }
            // ^ Initializing game stuff
            // ---------------------------------------------------------------------------------------------------------



            discardDeck.addCard(mainDeck.dealCard()); // Flip a card face up on discardDeck
            //-------------
            try {
                flipTwoCards(scan, playerList, mainDeck, discardDeck); // Flips two cards for each player
            } catch (InterruptedException e) {
                System.out.println("Caught Interrupted Exception"); // Had to use to keep it from breaking
            }
            //-------------
            boolean singleRound = true; // Variable to track when the current round is done
            while (singleRound) {
                for (Player p : playerList) {
                    if (!p.isAI()) { // For single player
                        System.out.println(p.getName() + " here is your hand:");
                        p.hand.returnHand();    // Print players hand
                        int choice = playerChoice(scan, discardDeck); // Print and get player choices
                        if (choice == 1) { // Draw card
                            Card drawnCard = mainDeck.dealCard(); // Deal card from mainDeck
                            drawnCard.setFaceUp();  // Set the drawn card face up
                            System.out.println("Which card would you like to trade for: " + drawnCard.toString() + "?");
                            p.hand.returnHand();    // Display players hand
                            System.out.println("1. Top Left" + " 2. Top Middle" + " 3. Top Right");
                            System.out.println("4. Bottom Left" + " 5. Bottom Middle" + " 6. Bottom Right");
                            System.out.println("7. Discard the card");
                            int cardSwitchChoice = 0;
                            while (true) {
                                try {
                                    String cardSwitchChoiceInput = scan.nextLine(); // Get player choice
                                    cardSwitchChoice = Integer.parseInt(cardSwitchChoiceInput);
                                    if (cardSwitchChoice < 1 || cardSwitchChoice > 7){
                                        throw new NumberFormatException();
                                    }
                                    break; // Break from validation loop
                                } catch (NumberFormatException nfe) {
                                    System.out.println("Please enter a valid choice between 1 and 7");
                                }
                            }
                            if(cardSwitchChoice == 7){ // If player doesn't want the drawn card
                                discardDeck.addCard(drawnCard); // Discard card
                            } else {
                                discardDeck.addCard(p.hand.switchCards(drawnCard, cardSwitchChoice - 1));
                            }
                        } else if (choice == 2) { // Take face up card
                            Card drawnCard = discardDeck.dealCard(); // Deal top card of discardDeck
                            System.out.println("Which card would you like to trade for: " + drawnCard.toString() + "?");
                            p.hand.returnHand();    // Display players hand
                            System.out.println("1. Top Left   " + " 2. Top Middle   " + " 3. Top Right   ");
                            System.out.println("4. Bottom Left" + " 5. Bottom Middle" + " 6. Bottom Right");
                            System.out.println("7. Discard the card");

                            int cardSwitchChoice;
                            while (true) {
                                try {
                                    String cardSwitchChoiceInput = scan.nextLine(); // Get player choice
                                    cardSwitchChoice = Integer.parseInt(cardSwitchChoiceInput);
                                    if (cardSwitchChoice < 1 || cardSwitchChoice > 7){
                                        throw new NumberFormatException();
                                    }
                                    break;
                                } catch (NumberFormatException nfe){
                                    System.out.println("enter an integer value between 1 and 7");
                                }
                            }
                            if(cardSwitchChoice == 7){ // If player doesn't want the drawn card
                                discardDeck.addCard(drawnCard); // Discard card
                            } else {
                                discardDeck.addCard(p.hand.switchCards(drawnCard, cardSwitchChoice - 1));
                            }
                        } else {
                            System.out.println("Please select a valid option.");
                        }
                        System.out.println(p.getName() + " here is your hand:");
                        p.hand.returnHand();

                        System.out.println("-------------------------------------------------"); // Divide players turns

                    } else {
                        aiMove(p, playerList, mainDeck, discardDeck); // AI Move
                        System.out.println(p.getName() + " here is your new hand (AI):");
                    }
                    singleRound = !isRoundDone(p); // Test if round is over
                    if (!singleRound) { // End the itar loop
                        break;
                    }
                }
            }
            System.out.println("\n\n\n");

            System.out.println("Printing each players hand and value:");
            System.out.println("-------------------------------------------------");
            for (Player playerScore:playerList) {
                playerScore.hand.returnHand(); // Print hand
                playerScore.hand.scoreCards(); // Tally hand value
                System.out.println(playerScore.getName() + " had a score of: " + playerScore.hand.getScore());
                System.out.println("-------------------------------------------------");
            }

            System.out.println("Round Complete.");
            for (Player p: playerList) { // Update scores
                p.hand.scoreCards();
                int newPlayerScore = playerScoreList.get(playerList.lastIndexOf(p)) + p.getScore(); // Calculate new score
                playerScoreList.set(playerList.lastIndexOf(p), newPlayerScore); // Set new score
            }
            for (int i = 0; i < playerScoreList.size(); i++) { // Print current scores
                System.out.println("Player " + (i + 1) + " has " + playerScoreList.get(i) + " points.");
            }
        }

        // End
        scan.close();
    }

    //---------------------------------- Methods ----------------------------------

    // Method for an AI's play
    private static void aiMove(Player p, ArrayList<Player> playerList, Deck mainDeck, Deck discardDeck ) {
        for (String strategy:p.decideMove()) { // Iterate over each strategy the AI will use
            if (strategy.equalsIgnoreCase("Column")) { // Column
                if (!p.hand.get(0).isFaceUp() && !p.hand.get(3).isFaceUp()) {
                    discardDeck.addCard(p.hand.switchCards(mainDeck.dealCard(), 3));
                } else if (!p.hand.get(1).isFaceUp() && !p.hand.get(4).isFaceUp()) {
                    discardDeck.addCard(p.hand.switchCards(mainDeck.dealCard(), 4));
                } else if (!p.hand.get(2).isFaceUp() && !p.hand.get(5).isFaceUp()) {
                    discardDeck.addCard(p.hand.switchCards(mainDeck.dealCard(), 5));
                } else {
                    continue; // Move on to next strat
                }
            } else if (strategy.equalsIgnoreCase("Pairs")) { // Pairs
                if (pairsAiMove(p, discardDeck)) continue; // Move on to next strat
            } else if (strategy.equalsIgnoreCase("LowestCard")){ // LowestCard
                if (lowestCardAiMove(p, discardDeck)) continue; // Move on to next strat
            } else {
                Card cardDraw = mainDeck.dealCard();
                // Put card on discard deck. Works because if the AI doesn't keep it
                // The card is already moved to the discard pile
                discardDeck.addCard(cardDraw); // Put drawn card on discard deck
                if (pairsAiMove(p, discardDeck)) continue; // Move on to next strat
                if (lowestCardAiMove(p, discardDeck)) continue; // Move on to next strat
            }
            System.out.println(p.getName() + " here is your hand:");
            p.hand.returnHand(); // Display hand
            System.out.println("-------------------------------------------------"); // Divider

            break; // Break out of the strat loop
        }
    }

    // Method for Ai to look for the lowest card
    private static boolean lowestCardAiMove(Player p, Deck discardDeck) {
        if (discardDeck.getTopCard().getValue() < p.hand.get(0).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 0));
        } else if (discardDeck.getTopCard().getValue() < p.hand.get(1).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 1));
        } else if (discardDeck.getTopCard().getValue() < p.hand.get(2).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 2));
        } else if (discardDeck.getTopCard().getValue() < p.hand.get(3).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 3));
        }else if (discardDeck.getTopCard().getValue() < p.hand.get(4).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 4));
        }else if (discardDeck.getTopCard().getValue() < p.hand.get(5).getValue()){
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 5));
        } else {
            return true;
        }
        return false;
    }

    // Method for Ai to look for pairs
    private static boolean pairsAiMove(Player p, Deck discardDeck) {
        if (p.hand.get(0).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Top-Left
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 3));
        } else if (p.hand.get(1).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Top-Mid
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 4));
        } else if (p.hand.get(2).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Top-Right
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 5));
        }else if (p.hand.get(3).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Bottom-Left
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 0));
        } else if (p.hand.get(4).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Bottom-Mid
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 1));
        } else if (p.hand.get(5).getFace().equalsIgnoreCase(discardDeck.getTopCard().getFace())){ // Bottom-Right
            discardDeck.addCard(p.hand.switchCards(discardDeck.dealCard(), 2));
        } else{
            return true;
        }
        return false;
    }


    private static boolean isRoundDone(Player player) {
        return player.hand.allFaceUp();
    }

    private static int playerChoice(Scanner scan, Deck discardPile) {
        System.out.println("Choose what you would like to do: ");
        System.out.println("1. Draw card from the deck.");
        System.out.println("2. Take the face up card from discard pile: " + discardPile.getTopCard().toString());
        int playerChoice;
        while (true) {
            String playerChoiceInput = scan.nextLine();
            try{
                playerChoice = Integer.parseInt(playerChoiceInput);
                if (playerChoice < 1 || playerChoice > 2){
                    throw new NumberFormatException();
                }
                break; // Break from loop
            } catch (NumberFormatException nfe){
                System.out.println("Please enter and integer: 1 or 2");
            }
        }
        return playerChoice;

    }

    // Method to flip two cards for each player
    private static void flipTwoCards(Scanner scan, ArrayList<Player> playerList, Deck mainDeck, Deck discardDeck) throws InterruptedException {
        for (Player pChoose: playerList) { // Allow players to choose their starting two cards
            ArrayList<String> selection = new ArrayList<>();
            selection.add("1: Top Left");
            selection.add("2: Top Middle");
            selection.add("3: Top Right");
            selection.add("4: Bottom Left");
            selection.add("5: Bottom Middle");
            selection.add("6: Bottom Right");
            if (!pChoose.isAI()) {
                selectCard(scan, pChoose, selection, 0); // Select first card to flip face up
                selectCard(scan, pChoose, selection, 0); // Select second card to flip face up

                pChoose.hand.returnHand(); // Display players hand
                if (selection.size() > 4){
                    for (String s : selection) { // Output all user card choices
                        System.out.println(s);
                    }
                }

            } else {
                // http://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#sleep%28long%29
                Thread.sleep(154);
                long seed1 = System.currentTimeMillis();
                int randomNum1 = new Random(seed1).nextInt(3);
                selectCard(scan, pChoose, selection, randomNum1); // Select first card to flip face up
                Thread.sleep(115);
                long seed2 = System.currentTimeMillis();
                int randomNum2 = new Random(seed2).nextInt(3) + 3;
                selectCard(scan, pChoose, selection, randomNum2); // Select second card to flip face up

                System.out.println(pChoose.getName() + " here is your hand:");
                pChoose.hand.returnHand(); // Display the AI's hand
            }
            System.out.println("-----------------------------------------"); // Divide players turns
        }
    }

    private static void selectCard(Scanner scan, Player pChoose, ArrayList<String> selection, int chosen) {
        int card;
        if (!pChoose.isAI()) {
            System.out.println(pChoose.getName() + " here is your hand:");
            pChoose.hand.returnHand(); // Display players hand
            System.out.println("Which card would you like to flip over?");
            for (String s : selection) { // Output all user card Choices
                System.out.println(s);
            }
            while (true){
                try{
                    String cardInput = scan.nextLine();
                    card = Integer.parseInt(cardInput);
                    if (card < 1 || card > 6){
                        throw new NumberFormatException();
                    }
                    break; // Break from loop
                } catch(NumberFormatException nfe){
                    System.out.println("Please enter a value between 1 and 6");
                }
            }
        } else {
            card = chosen + 1;
        }

        pChoose.getHand().get(card - 1).setFaceUp(); // Set chosen card face up
        if (selection.size() != 5 || card == 1) {
            selection.remove(card - 1); // Remove the selected card
        } else {
            selection.remove(card - 2); // Handles the change in selection array
        }
    }

    // Method to update palyer hand values
    private static void updateScores(ArrayList<Player> playerList) {
        for (Player p : playerList) {
            p.hand.scoreCards();
        }
    }
}
