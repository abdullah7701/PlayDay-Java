import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Player {
    private String name;
    private int cashBalance;
    private int currentPosition;
    private boolean paydaySpaceReached;
    private List<Card> mailCards;
    private List<Card> dealCards;

    public Player(String name, int startingCash) {
        this.name = name;
        this.cashBalance = startingCash;
        this.currentPosition = 0;
        this.paydaySpaceReached = false;
        this.mailCards = new ArrayList<>();
        this.dealCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCashBalance() {
        return cashBalance;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean hasReachedPaydaySpace() {
        return paydaySpaceReached;
    }

    public void move(int steps) {
        currentPosition += steps;
        if (currentPosition >= Board.NUMBER_OF_SPACES) {
            paydaySpaceReached = true;
        }
    }

    public void addCash(int amount) {
        cashBalance += amount;
    }

    public void deductCash(int amount) {
        cashBalance -= amount;
    }

    public void addMailCard(MailCard card) {
        mailCards.add(card);
    }

    public void addDealCard(DealCard card) {
        dealCards.add(card);
    }

    public List<Card> getMailCards() {
        return mailCards;
    }

    public List<Card> getDealCards() {
        return dealCards;
    }
}

class Board {
    public static final int NUMBER_OF_SPACES = 31; // Including start and end spaces
    private static final String[] SPACE_ACTIONS = {
        "Start", "Payday", "Deal", "Mail", "Bills", "Lottery", "Deal", "Mail", "Bills", "Charity", "Deal", "Mail",
        "Bills", "Deal", "Mail", "Bills", "Deal", "Mail", "Deal", "Mail", "Deal", "Mail", "Bills", "Deal", "Mail",
        "Bills", "Deal", "Mail", "Payday", "End"
    };

    public static String getSpaceAction(int position) {
        return SPACE_ACTIONS[position];
    }
}

abstract class Card {
    private String description;

    public Card(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void performAction(Player player);
}

class MailCard extends Card {
    public MailCard(String description) {
        super(description);
    }

    @Override
    public void performAction(Player player) {
        Random random = new Random();
        int amount = random.nextInt(500) + 100; // Generate a random amount between $100 and $600

        player.deductCash(amount);
        System.out.println("You lost $" + amount + " due to the mail card.");
    }
}

class DealCard extends Card {
    public DealCard(String description) {
        super(description);
    }

    @Override
    public void performAction(Player player) {
        Random random = new Random();
        int amount = random.nextInt(1000) + 500; // Generate a random amount between $500 and $1500

        player.addCash(amount);
        System.out.println("You gained $" + amount + " from the deal card.");
    }
}

class Dice {
    private static final Random random = new Random();

    public static int roll() {
        return random.nextInt(6) + 1; // Rolling a 6-sided die
    }
}

public class PaydayGame {
    private static List<Player> players; // Make players static
    private int currentTurn;

    public PaydayGame(int numberOfPlayers) {
        players = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player("Player " + i, 3500); // Starting cash $3500
            players.add(player);
        }
        currentTurn = 0;
    }

    public void startGame() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Payday Game!");
    System.out.println("Let's get started.\n");

    System.out.print("Enter the number of months to play: ");
    int months = scanner.nextInt();
    int daysInMonth = 30; // Assuming 30 days in each month for simplicity

    // Game loop for each month
    for (int month = 1; month <= months; month++) {
        System.out.println("\nMonth " + month + " begins!");

        // Loop for each day in the month
        for (int day = 1; day <= daysInMonth; day++) {
            System.out.println("Day " + day);

            // Player turns loop for each day
            for (Player player : players) {
                currentTurn++;
                System.out.println("\n" + player.getName() + "'s turn (Turn " + currentTurn + ")");
                System.out.println("Current Cash Balance: $" + player.getCashBalance());
                System.out.println("Current Position: " + player.getCurrentPosition());

                int diceRoll = Dice.roll();
                System.out.println("Rolling the dice... You rolled a " + diceRoll);

                player.move(diceRoll);

                String spaceAction = Board.getSpaceAction(player.getCurrentPosition() % Board.NUMBER_OF_SPACES);
                System.out.println("You landed on: " + spaceAction);

                switch (spaceAction) {
                    case "Payday":
                        player.addCash(2000); // Example payday action
                        System.out.println("You earned $2000 as salary.");
                        break;
                    case "Deal":
                        handleDealAction(player);
                        break;
                    case "Mail":
                        handleMailAction(player);
                        break;
                    case "Bills":
                        player.deductCash(500); // Example bills action
                        System.out.println("You paid $500 for bills.");
                        break;
                    case "End":
                        System.out.println("Reached the end of the board.");
                        player.move(-player.getCurrentPosition()); // Move back to the start of the board
                        break;
                    default:
                        // Other space actions
                        break;
                }

                // Prompt user to press Enter before proceeding to the next turn
                System.out.println("\nPress Enter to continue to the next turn.");
                scanner.nextLine(); // Consume the newline character from previous input
                scanner.nextLine(); // Wait for user input
            }
        }
    }

    // Game over, declare winner
    Player winner = determineWinner();
    System.out.println("\nGame Over!");
    System.out.println("Winner: " + winner.getName() + " with $" + winner.getCashBalance());

    // Return to main menu
    displayMainMenu(scanner);
}

    private Player determineWinner() {
        Player winner = players.get(0);
        for (Player player : players) {
            if (player.getCashBalance() > winner.getCashBalance()) {
                winner = player;
            }
        }
        return winner;
    }

private void handleDealAction(Player player) {
    Random random = new Random();
    int amount = random.nextInt(1000) + 500; // Generate a random amount between $500 and $1500
    player.addCash(amount);
    System.out.println("You gained $" + amount + " from the deal action.");
}

    private void handleMailAction(Player player) {
        Random random = new Random();
        int amount = random.nextInt(500) + 100; // Generate a random amount between $100 and $600

        player.deductCash(amount);
        System.out.println("You lost $" + amount + " due to the mail action.");
    }

    private static void displayMainMenu(Scanner scanner) {
    System.out.println("\nMain Menu:");
    System.out.println("1. Start Game");
    System.out.println("2. View mail cards");
    System.out.println("3. View deal cards");
    System.out.println("4. Exit");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();
    switch (choice) {
        case 1:
            System.out.print("Enter the number of players: ");
            int numberOfPlayers = scanner.nextInt();
            PaydayGame game = new PaydayGame(numberOfPlayers);
            System.out.println("Press Enter to start the game.");
            scanner.nextLine(); // Consume the newline character from previous input
            scanner.nextLine(); // Wait for user input
            game.startGame();
            break;
        case 2:
            // Code to view mail cards
            if (players != null) {
                System.out.println("Mail Cards:");
                System.out.println("------------");
                for (Player player : players) {
                    System.out.println(player.getName() + "'s Mail Cards:");
                    List<Card> mailCards = player.getMailCards();
                    for (Card card : mailCards) {
                        System.out.println(card.getDescription());
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No players available. Start a game first.");
            }
            displayMainMenu(scanner);
            break;
        case 3:
            // Code to view deal cards
            if (players != null) {
                System.out.println("Deal Cards:");
                System.out.println("------------");
                for (Player player : players) {
                    System.out.println(player.getName() + "'s Deal Cards:");
                    List<Card> dealCards = player.getDealCards();
                    for (Card card : dealCards) {
                        System.out.println(card.getDescription());
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No players available. Start a game first.");
            }
            displayMainMenu(scanner);
            break;
        case 4:
            System.out.println("Exiting Payday Game. Goodbye!");
            break;
        default:
            System.out.println("Invalid choice. Exiting Payday Game. Goodbye!");
            break;
    }
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        displayMainMenu(scanner);
    }
}