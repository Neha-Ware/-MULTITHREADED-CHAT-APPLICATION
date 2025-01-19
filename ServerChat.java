import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerChat {
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to  CLIENT-SERVER CHAT APPLICATION...");
        System.out.println("Chat server started...");
        ServerSocket ServerSocket = new ServerSocket(123456);

        while (true) {
            new ClientHandler(ServerSocket.accept()).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter outMessage;
        private BufferedReader inMessage;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void Run() {
            try {
                inMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outMessage = new PrintWriter(socket.getOutputStream(), true);
                
                synchronized (clientWriters) {
                    clientWriters.add(outMessage);
                }
                
                String Message;
                while ((Message = inMessage.readLine()) != null) {
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(Message);
                        }
                    }
                }
            } catch (IOException exc) {
                System.out.println(exc.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException exc) {
                    // Ignore
                } finally {
                    synchronized (clientWriters) {
                        clientWriters.remove(outMessage);
                    }
                }
            }
        }
    }
}
