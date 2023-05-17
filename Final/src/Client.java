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

    public void changeSocket(Socket s){
        this.clientSocket = s;
        try {
            messagesOut = new PrintWriter(clientSocket.getOutputStream(), true);
            messagesIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String sendMessage(String message, String terminator){
        messagesOut.println(message);
        try {
            Game.clearScreen();
            StringBuilder resp = new StringBuilder();
            String line;
            while( (line = messagesIn.readLine()) != null && line != "") {
                resp.append(line);
                resp.append("\n");
                if (line.contains(terminator)){
                    break;
                }
                System.out.println("die");
            }
            String sresp = resp.toString();
            if (!sresp.equals("") && sresp != null){
                System.out.println(sresp);    
            }
            return resp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
