import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Collectors;

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
            stop();
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
            stop();
        }
        stop();
    }

    public void stop(){
        try {
                messagesIn.close();
                messagesOut.close();
                clientSocket.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
