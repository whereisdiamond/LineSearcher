package finalProject;

import java.io.*;
import java.net.*;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        LineSearcher lineSearcher;
        try {
            lineSearcher = new LineSearcher("D:/FinalFinal/src/finalProject/hamlet.txt"); //Change this line to find the text file. I was having issues with locating it without an absolute location.
        } catch (IOException e) {
    System.err.println("Error opening file: " + e.getMessage());
    return;
}

        
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server started on port " + serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Error starting server.");
            return;
        }
        
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                String line = in.readLine();
                int n = Integer.parseInt(line);
                List<String> lines = lineSearcher.search(n);
                
                for (String str : lines) {
                    out.println(str);
                }
                
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                System.err.println("Error communicating with client.");
            }
        }
    }
}

