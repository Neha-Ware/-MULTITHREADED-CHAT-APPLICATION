import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 123456);
        PrintWriter outMessage = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader inMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner scan = new Scanner(System.in);

        // This Thread for receiving messages from Server
        new Thread(() -> {
            String IncomingMessage;
            try {
                while ((IncomingMessage = inMessage.readLine()) != null) {
                    System.out.println(IncomingMessage);
                }
            } catch (IOException exc) {
                System.out.println(exc.getMessage());
            }
        }).start();

        //This is a Main thread for sending messages
        String Message;
        while (scan.hasNextLine()) {
            Message = scan.nextLine();
            outMessage.println(Message);
        }
        
        socket.close();
        scan.close();
    }
}
