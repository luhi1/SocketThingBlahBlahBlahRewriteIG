import java.net.Socket;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Client myClient = new Client("127.0.0.1");
        myClient.sendMessage("null"," to host a game!");
        Scanner myScanner = new Scanner(System.in);
        String resp;
        do {
            resp = myScanner.nextLine();
            if(resp.contains("Join:")){
                if (myClient.changeSocket(new Socket(resp.replace("Join:", ""), Server.serverPort+1))){
                    myClient.gameCommunicator();
                } else {
                    System.out.println("Invalid Game IP");
                }
                //Error Handling?
                break;
            }
            switch (resp) {
                case "list":
                    myClient.sendMessage(resp, "List of hosts you could join!");
                    System.out.println("Type \"Join:\" followed by a host IP to join a game!");
                    break;
                
                case "host":
                    myClient.sendMessage("host", "Type");
                    resp = myScanner.nextLine();
                    while (!resp.contentEquals("start") && !resp.contentEquals("quit")){
                        System.out.println("Type \"start\" to start the game.");
                        resp = myScanner.nextLine();
                    }
                    myClient.sendMessage(resp, "");
                    break;

                case "quit":
                    System.out.println("Bye");
                    break;

                default:
                    System.out.println("Unrecognized command.");
                    break;
            }
        } while (!resp.equals("quit"));

        myScanner.close();
    }
}
