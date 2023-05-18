import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    private Socket clientSocket;
    private PrintWriter messagesOut;
    private BufferedReader messagesIn;

    public Client(String serverIP){
        try {
            clientSocket = new Socket(serverIP, Server.serverPort);
            messagesOut = new PrintWriter(clientSocket.getOutputStream(), true);
            messagesIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean changeSocket(Socket s){
        this.clientSocket = s;
        try {
            messagesOut = new PrintWriter(clientSocket.getOutputStream(), true);
            messagesIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void gameCommunicator() throws IOException{
        messagesOut.println("check");
        while (messagesIn.readLine().equals("Waiting for game to start")){
            messagesOut.println("check");
            messagesOut.flush();
            System.out.println("Waiting for game to start.");
        }

        
    }

    public void sendMessage(String message, String terminator){
        messagesOut.println(message);
        try {
            Game.clearScreen();
            StringBuilder resp = new StringBuilder();
            String line;
            while( (line = messagesIn.readLine()) != null) {
                resp.append(line);
                resp.append("\n");
                if (resp.toString().contains(terminator)){
                    break;
                }
            }
            System.out.println(resp.toString());    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
