import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Client myClient = new Client("10.129.210.117");
        myClient.sendMessage("null"," to host a game!");
        Scanner myScanner = new Scanner(System.in);
        myClient.sendMessage(myScanner.nextLine(), "Waiting for player connections.");
        /*String resp;
        do {
            System.out.println("Type \"Join: \" followed by an ip address to join a game!");
            resp = myScanner.nextLine();
        } while (!resp.contains("Join: "));
        myClient.sendMessage(resp, "");*/
        myScanner.close();
    }
}
