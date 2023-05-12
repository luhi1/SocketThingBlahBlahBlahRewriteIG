import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Client myClient = new Client("127.0.0.1");
        myClient.sendMessage("null"," to host a game!");
        Scanner myScanner = new Scanner(System.in);
        myClient.sendMessage(myScanner.nextLine(), "List of hosts you could join!");
        myScanner.close();
    }
}
