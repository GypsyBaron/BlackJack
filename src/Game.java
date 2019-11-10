import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Game {

    public static void main(String[] args) throws IOException {
        ArrayList<String> cards;
        String[][] cardDeck;
        String[][] playerHand = new String[10][2];
        String[][] dealerHand = new String[10][2];
        Scanner sc = new Scanner(System.in);
        int playerCardCounter, dealerCardCounter;
        int playerCardSum, dealerCardSum;
        int cardDeckIndex;
        boolean isGameOver, isPlaying = true;

        cards = readCards();

        while (isPlaying) {

            Collections.shuffle(cards);
            cardDeck = prepareCards(cards);
            isGameOver = false;
            playerCardCounter = 0;
            dealerCardCounter = 0;
            cardDeckIndex = 0;

            System.out.println("----------------------------");
            System.out.println("Dealer card");
            for (int i = 0; i < 2; i++) {
                dealCard(cardDeck, cardDeckIndex, dealerHand, dealerCardCounter);
                if (i != 1) {
                    showCard(dealerHand, dealerCardCounter);
                }
                cardDeckIndex++;
                dealerCardCounter++;
            }

            System.out.println("----------------------------");
            System.out.println("My cards");
            for (int i = 0; i < 2; i++) {
                dealCard(cardDeck, cardDeckIndex, playerHand, playerCardCounter);
                showCard(playerHand, playerCardCounter);
                cardDeckIndex++;
                playerCardCounter++;
            }
            System.out.println("----------------------------");

            if (Integer.parseInt(playerHand[0][1]) + Integer.parseInt(playerHand[1][1]) == 21) {
                if (Integer.parseInt(dealerHand[0][1]) + Integer.parseInt(dealerHand[1][1]) != 21) {
                    System.out.println("You won the game");
                } else {
                    System.out.println("Draw. Nice game");
                }
                break;
            }

            while (!isGameOver) {

                boolean isStand = false;
                boolean isInputCorrect = false;
                String input;


                System.out.println("To hit - H, to stand - S");
                while (!isInputCorrect) {
                    input = sc.nextLine().trim().toUpperCase();
                    if (input.equals("H")) {
                        dealCard(cardDeck, cardDeckIndex, playerHand, playerCardCounter);
                        //showCard(playerHand, playerCardCounter);
                        cardDeckIndex++;
                        playerCardCounter++;
                        isInputCorrect = true;
                    } else if (input.equals("S")) {
                        isStand = true;
                        isInputCorrect = true;
                    } else {
                        System.out.println("Bad input");
                    }
                }

                System.out.println("------ My cards --------");
                for (int i = 0; i < playerCardCounter; i++) {
                    showCard(playerHand, i);
                }

                if (minHandValue(playerHand, playerCardCounter) > 21) {
                    System.out.println("Your hand is over 21, you loose.");
                    break;
                }

                if (isStand) {

                    System.out.println("You select stand, let's see dealer cards.");
                    playerCardSum = maxHandValue(playerHand, playerCardCounter);
                    if (maxHandValue(playerHand, playerCardCounter) > 21) {
                        playerCardSum = minHandValue(playerHand, playerCardCounter);
                    } else {
                        playerCardSum = maxHandValue(playerHand, playerCardCounter);
                    }
                    System.out.println("My hand value - " + playerCardSum);

                    dealerCardSum = maxHandValue(dealerHand, dealerCardCounter);
                    int dealerMaxHandValue = 0;
                    int dealerMinHandValue = 0;

                    while (dealerCardSum < 17) {
                        dealerMaxHandValue = maxHandValue(dealerHand, dealerCardCounter);
                        dealerMinHandValue = minHandValue(dealerHand, dealerCardCounter);

                        if (dealerMaxHandValue <= 21) {
                            dealerCardSum = dealerMaxHandValue;
                        } else {
                            dealerCardSum = dealerMinHandValue;
                        }

                        dealCard(cardDeck, cardDeckIndex, dealerHand, dealerCardCounter);
                        showCard(dealerHand, dealerCardCounter);
                        cardDeckIndex++;
                        dealerCardCounter++;
                    }

                    System.out.println("------ Dealer cards --------");
                    for (int i = 0; i < playerCardCounter; i++) {
                        showCard(dealerHand, i);
                    }
                    System.out.println("Dealer hand value: " + dealerCardSum);

                    if (dealerCardSum > 21) {
                        System.out.println("You won the game");
                    } else if (playerCardSum > dealerCardSum) {
                        System.out.println("You won the game");
                    } else if (playerCardSum == dealerCardSum) {
                        System.out.println("Draw. Nice game");
                    } else {
                        System.out.println("You lost the game. Try next time");
                    }

                    isGameOver = true;
                }
            }

            System.out.println("If you want play again, press - Y or any other key to close the game.");
            if (!sc.nextLine().trim().toUpperCase().equals("Y")) {
                isPlaying = false;
                sc.close();
            }
        }
    }

    private static void showCard(String[][] hand, int cardIndex) {
        if (Integer.parseInt(hand[cardIndex][1]) < 10) {
            System.out.println(hand[cardIndex][0]);
        } else if (Integer.parseInt(hand[cardIndex][1]) == 10) {
            System.out.println(hand[cardIndex][0] + " - 10");
        } else {
            System.out.println(hand[cardIndex][0] + " - 1 or 11");
        }
    }

    private static int minHandValue(String[][] hand, int handCounter) {
        boolean isHandContainsAce = false;
        int handSum = 0;
        for (int i = 0; i < handCounter; i++) {
            if (Integer.parseInt(hand[i][1]) == 11 && !isHandContainsAce) {
                isHandContainsAce = true;
                handSum += 1;
            } else {
                handSum += Integer.parseInt(hand[i][1]);
            }
        }
        return handSum;
    }

    private static int maxHandValue(String[][] hand, int handCounter) {
        int handSum = 0;
        for (int i = 0; i < handCounter; i++) {
            handSum += Integer.parseInt(hand[i][1]);
        }
        return handSum;
    }

    private static String[][] dealCard(String[][] cardDeck, int cardDeckIndex, String[][] hand, int userCardCounter) {
        hand[userCardCounter][0] = cardDeck[cardDeckIndex][0];
        hand[userCardCounter][1] = cardDeck[cardDeckIndex][1];
        return hand;
    }

    private static void showCards(String[][] hand, int cardCounter) {
        for (int i = 0; i < cardCounter; i++) {
            if (Integer.parseInt(hand[i][1]) < 10) {
                System.out.println(hand[i][0]);
            } else if (Integer.parseInt(hand[i][1]) == 10) {
                System.out.println(hand[i][0] + " - 10");
            } else {
                System.out.println(hand[i][0] + " - 1 or 11");
            }
        }
        System.out.println("---------------------------------------");
    }

    private static String[][] prepareCards(ArrayList<String> cards) {
        String[][] cardDeck = new String[52][2];
        for (int i = 0; i < cards.size(); i++) {
            String[] cardInfo = cards.get(i).split(" ");
            cardDeck[i][0] = cardInfo[0] + " " + cardInfo[1] + " " + cardInfo[2];
            cardDeck[i][1] = cardInfo[3];
        }
        return cardDeck;
    }

    private static ArrayList<String> readCards() {
        ArrayList<String> cards = new ArrayList<>();
        Scanner file = null;
        try {
            file = new Scanner(new File("src\\cards.txt"));
            while (file.hasNextLine()) {
                String card = file.nextLine();
                cards.add(card);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.close();
            }
        }
        return cards;
    }
}