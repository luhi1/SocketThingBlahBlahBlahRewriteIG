import java.io.PrintWriter;
import java.io.Serializable;

public class Game implements Serializable{
    private int numGuesses;
    private int strikes;
    private String hangman;
    private final String[] hangmanParts = {"  O", "-----", "/|||\\", " / \\", "  X"};
    private String guessString;
    private String secretWord;
    private String currentPlayer;

    public Game(String word) throws Exception{
        this.secretWord = word;
        this.numGuesses = 0;
        this.strikes = 0;
        this.hangman = "";
        this.guessString = "";
        for (int i = 0; i < secretWord.length(); ++i){
            this.guessString += "_ ";
        }
        this.currentPlayer = "";
    }

    public void setCurrentPlayer(String player){
        this.currentPlayer = player;
    }

    public String getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void displayScreen(PrintWriter outputStream) throws Exception{
        clearScreen();
        hangman = "";
        if (strikes == 5){
            outputStream.println("You lost! The hangman got buried.");
            for (int i = this.strikes-1; i >= 0; i--){
                hangman += "\n              "+hangmanParts[i];
            }
            outputStream.println(hangman);
            outputStream.println("Type \"home\" to return to the menu.");
            return;

        }
        String spacedSecret = "";
        for (int i = 0; i < secretWord.length(); ++i){
            spacedSecret += secretWord.charAt(i) + " ";
        }

        if (spacedSecret.equals(guessString)){
            outputStream.println("GG You Won!");
            outputStream.println("Type \"home\" to return to the menu.");
            return;
        }

        try {
            outputStream.println("ìììììììììììììììììììììììììììììì");

            outputStream.printf("CurrentPlayer: %s; Guesses: %d\n", this.currentPlayer, this.numGuesses);
            outputStream.println("ìììììììììììììììììììììììììììììì");
            for (int i = 0; i < this.strikes; ++i){
                hangman += "\n              "+hangmanParts[i];
            }
            outputStream.println(hangman);
            outputStream.println(guessString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkGuess(String guess){
        String spacedSecret = "";
        for (int i = 0; i < secretWord.length(); ++i){
            spacedSecret += secretWord.charAt(i) + " ";
        }
        if (spacedSecret.equals(guessString)){
            return;
        }
        if (strikes == 5){
            return;
        }
        if (secretWord.contains(guess) && guess.length() == 1 && this.strikes != 5){
            String[] guessArray = this.guessString.split(" ");
            this.guessString = "";
            for (int i = 0; i < secretWord.length(); ++i){
                if (secretWord.charAt(i) == (guess.toCharArray()[0])){
                    guessArray[i] = guess;
                }
                this.guessString += guessArray[i] + " ";
            }
            return;
        }
        this.strikes++;
    }

    public static void clearScreen() throws Exception{
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
