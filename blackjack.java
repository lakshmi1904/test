import java.util.*;
//import javax.swing.JFrame;

public class blackjack{

    public static void main(String[] args) {
        
        //get player info and make a player to compare to the original player
        player playerOne = makePlayer();

        //Shows initial values
        System.out.println("------------------------------------------------------");
        System.out.println("Player: " + playerOne.getName() + "\nPlaying amount: " + playerOne.getAmount());
        System.out.println("------------------------------------------------------");

        //How much the player started with
        double initialAmount = playerOne.getAmount();

        //Play blackjack
        playBlackjack(playerOne);

        //End game, shows ending values 
        System.out.println("------------------------------------------------------");
        System.out.println(playerOne.getName() + ", you finished with " + playerOne.getAmount() + "!");
        System.out.println("Your profit: " + (playerOne.getAmount()-initialAmount));
        System.out.println("------------------------------------------------------");

    }

    //Make player function taking in the name and the ammount they wanna play with
    public static player makePlayer(){
        String name;
        double amount;

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter your name");
        name = myObj.nextLine();

        System.out.println("How much money do you plan on playing with?");
        amount = myObj.nextDouble();

        player playerOne = new player(name, amount);

        return(playerOne);
    }

    //Game of black jack, the function runs into the user has no money left or chooses to leave
    public static void playBlackjack(player user){
        String keepPlaying = "Yes";

        //While the User has more than 0 dollars to play with, the game keeps going
        while(user.getAmount() > 0){
            user = blackjackRound(user);

            Scanner scan = new Scanner(System.in);
            System.out.println("Do you wish to keep playing? (Yes/No)");
            keepPlaying = scan.nextLine();
            
            //Asks if the the user wants to keep playing or not, if not it breaks the while loop
            if(keepPlaying.compareTo("No")==0)
                break;
        }

    }

    //A single round of poker
    //This program is gonna work a bit differently than a normal casino
    //The deck of cards will be "shuffled" after every round and you can't double down nor split
    //Everything else still stands
    public static player blackjackRound(player user){
        //Amount user is going to bet
        int betAmount;

        //Choice of player if he/she would like to push or stand
        String playerChoice;

        //variable if player wants to keep standing or hitting
        boolean choice = true;

        //Hand totals int array
        int[] handTotals = new int[2];
        handTotals[0] = 0;
        handTotals[1] = 0;

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("How much would you like to bet for this round?:");
        betAmount = myObj.nextInt();

        //Function to make a new deck every round
        ArrayList<card> deck = makeDeck();

        //Create player cards and dealers cards
        ArrayList<card> playerCards = new ArrayList<card>(15);
        ArrayList<card> dealerCards = new ArrayList<card>(15);
        
        //Go every other like a casino
        playerCards.add(deck.get(0));
        dealerCards.add(deck.get(1));
        playerCards.add(deck.get(2));
        dealerCards.add(deck.get(3));

        //counts how many cards are used
        int cardCounter = 4;


        //User rounds of picking cards
        while(choice == true){
            handTotals = readHandBeforeFlop(playerCards, dealerCards);

            //Player busts
            if(handTotals[0]>21){
                user.setAmount(user.getAmount() - betAmount);
                System.out.println("You busted!");
                System.out.println("You lost " + betAmount);
                System.out.println("You now have a total of " + user.getAmount() + " left to play with");
                choice = false;
            }
            else if(handTotals[0]==21){
                System.out.println("Blackjack!");
                //User gets 1.5 times the bet amount for blackjack
                user.setAmount(user.getAmount() + (1.5 * betAmount));
                System.out.println("You now have a total of " + user.getAmount() + " left to play with"); 
                return(user);
            }
            else{ //If player chooses to push
                System.out.println("Would you like to Hit or Stand? (Type Hit/Stand)");
                Scanner scan = new Scanner(System.in);
                playerChoice = scan.nextLine();
                if(playerChoice.compareTo("Hit")==0 || playerChoice.compareTo("hit")==0){
                    playerCards.add(deck.get(cardCounter));
                    cardCounter++;
                }
                else{ //If player chooses to stand
                    choice = false;
                }
            }
        }

        //If player has more than 21 than end round
        if(handTotals[0] > 21){
            return(user);
        }
        else{//Player hand is valid and not over 21
            //Read the current board
            handTotals = readHandAfterFlop(playerCards, dealerCards);

            while(handTotals[1]<17){
                //Add a card to the dealer if he still has less than 17
                dealerCards.add(deck.get(cardCounter));
                cardCounter++;
                handTotals = readHandAfterFlop(playerCards, dealerCards);
                if(handTotals[1]>21){//Dealer busts
                    System.out.println("Dealer busted! You win!");
                    user.setAmount(user.getAmount() + betAmount);
                    System.out.println("You now have a total of " + user.getAmount() + " left to play with");
                    return(user);
                }
                else if(handTotals[1]==21 || handTotals[1]>handTotals[0]){//Dealer gets blackjack or wins outright
                    System.out.println("Dealer Wins!");
                    user.setAmount(user.getAmount() - betAmount);
                    System.out.println("You lost " + betAmount);
                    System.out.println("You now have a total of " + user.getAmount() + " left to play with");
                    return(user);
                }
            }

            //If dealer has more than 17 on the flop
            if(handTotals[0]>handTotals[1]){//You have more
                System.out.println("You win!");
                user.setAmount(user.getAmount() + betAmount);
                System.out.println("You now have a total of " + user.getAmount() + " left to play with"); 
            } else if(handTotals[0]<handTotals[1]){//Dealer has more
                System.out.println("Dealer Wins!");
                user.setAmount(user.getAmount() - betAmount);
                System.out.println("You lost " + betAmount);
                System.out.println("You now have a total of " + user.getAmount() + " left to play with");
            }
            else{
                System.out.println("It's a push! No one wins!");
            }
        }

        return(user);
    }

    //make the deck of card to play with
    public static ArrayList<card> makeDeck(){
        ArrayList<card> deck = new ArrayList<card>(50);

        //Create 50 cards
        //13 different cards
        //4 different suits
        for(int i=0; i<50; i++){
            int value = (int) (Math.random() * ((13 - 1) + 1)) + 1;
            int suit = (int) (Math.random() * ((4 - 1) + 1)) + 1;
            card deckCard = new card(value, suit);
            deck.add(deckCard);
        }

        return(deck);
    }

    //switch statment that takes in a card and returns the numerical value of the card
    public static int cardToNumber(card cardVal){
        int answer = 0;
        if(cardVal.value > 10){
            answer = 10;
        }
        else if(cardVal.value==1)
            answer = 11;
        else{
            answer = cardVal.value;
        }

        return(answer);
    }

    //Returns the string the name of the card
    public static String cardValReader(card cardVal){
        String cardPlayed = new String();
        
        //Textual value of card
        switch(cardVal.value){
            case 1 -> cardPlayed = "Ace of ";
            case 2 -> cardPlayed = "2 of ";
            case 3 -> cardPlayed = "3 of ";
            case 4 -> cardPlayed = "4 of ";
            case 5 -> cardPlayed = "5 of ";
            case 6 -> cardPlayed = "6 of ";
            case 7 -> cardPlayed = "7 of ";
            case 8 -> cardPlayed = "8 of ";
            case 9 -> cardPlayed = "9 of ";
            case 10 -> cardPlayed = "10 of ";
            case 11 -> cardPlayed = "J of ";
            case 12 -> cardPlayed = "Q of ";
            case 13 -> cardPlayed = "K of ";    
        }

        //Textual value of the suit of the card
        switch(cardVal.suit){
            case 1 -> cardPlayed = cardPlayed + "Spades";
            case 2 -> cardPlayed = cardPlayed + "Clubs";
            case 3 -> cardPlayed = cardPlayed + "Hearts";
            case 4 -> cardPlayed = cardPlayed + "Diamonds";
        }

        return(cardPlayed);
    }

    //After dealer flop; Reading out the hands and totals function
    public static int[] readHandAfterFlop(ArrayList<card> playerHand, ArrayList<card> dealerHand){
        int[] handTotals = new int[2];
        boolean aceFind = false;

        //Player hands and total
        System.out.println("------------------------------------------------------");
        System.out.print("You have:");
        try{
            for(int i = 0; i < playerHand.size(); i++){
                System.out.print(" " + cardValReader(playerHand.get(i)) + ",");
                handTotals[0] = handTotals[0] + cardToNumber(playerHand.get(i));
            }
        }
        catch(NullPointerException e){
            System.out.println("oops");
        }
        System.out.println(" Totaling: " + handTotals[0]);

        //Dealer hands and total
        System.out.print("Dealer has:");
        try{
            for(int i = 0; i < dealerHand.size(); i++){
                System.out.print(" " + cardValReader(dealerHand.get(i)) + ",");
                handTotals[1] = handTotals[1] + cardToNumber(dealerHand.get(i));
            }
        }
        catch(NullPointerException e){}
        //If dealer has more than 21 and has an ace, then subtract 10 from total
        aceFind = findAce(dealerHand);
        if(handTotals[1]>21 && aceFind==true)
            handTotals[1] = handTotals[1] - 10;
        System.out.println(" Totaling: " + handTotals[1]);
        System.out.println("------------------------------------------------------");

        return(handTotals);
    }


    //Pre dealer flop; reading out the hands and totals function
    public static int[] readHandBeforeFlop(ArrayList<card> playerHand, ArrayList<card> dealerHand){
        int[] handTotals = new int[2];
        handTotals[0] = 0;
        handTotals[1] = 0;

        //Tries to find an ace in the hand
        boolean aceFind = false;

        //Player hands and total
        System.out.println("------------------------------------------------------");
        System.out.print("You have:");
        try{
            for(int i = 0; i < playerHand.size(); i++){
                System.out.print(" " + cardValReader(playerHand.get(i)) + ",");
                handTotals[0] = handTotals[0] + cardToNumber(playerHand.get(i));
            }
        }
        catch(NullPointerException e){}
        //If player's hand is over 21 but has an ace, remove 10 from the total hand value
        aceFind = findAce(playerHand);
        if(handTotals[0]>21 && aceFind==true)
            handTotals[0] = handTotals[0] - 10;
        System.out.println(" Totaling: " + handTotals[0]);

        //Dealer hands and total but only showing the second card
        System.out.println("Dealer is showing:" + cardValReader(dealerHand.get(1)));
        System.out.println("------------------------------------------------------");

        return(handTotals);
    }

    //Function to find an ace in a hand
    public static boolean findAce(ArrayList<card> hand){
        boolean aceFind = false;
        
        try{
            for(int i = 0; i < hand.size(); i++){
                if(hand.get(i).value == 1)
                    aceFind = true;
            }
        }
        catch(NullPointerException e){}

        return aceFind;
        
    }


}
