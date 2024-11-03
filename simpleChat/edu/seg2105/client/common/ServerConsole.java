package edu.seg2105.client.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

/**
 * This class provides a console for the server to interact with, allowing it to send commands
 * and messages through the console.
 */
public class ServerConsole implements ChatIF {
    private EchoServer server;
    
    /**
     * Constructs an instance of the ServerConsole.
     *
     * @param server The server instance to which this console is attached.
     */
    public ServerConsole(EchoServer server) {
        this.server = server;
    }
    
    /**
     * Displays a message on the console.
     *
     * @param message The message to be displayed.
     */
    public void display(String message) {
        System.out.println("> " + message);
    }

    /**
     * This method waits for user input from the console. Commands prefixed with '#' are interpreted
     * as server commands, while other input is broadcasted as a message to all clients.
     */
    public void accept() {
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;
            
            while (true) {
                message = fromConsole.readLine();
                
                if (message != null) {
                    server.handleMessageFromClient(message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * The main entry point for running the server with a console.
     * Starts the EchoServer and attaches the ServerConsole to it.
     *
     * @param args Command line arguments for port number (optional).
     */
    public static void main(String[] args) {
        int port = EchoServer.DEFAULT_PORT;  // Default port if no arguments are provided

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);  // Use the specified port
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Using default port: " + EchoServer.DEFAULT_PORT);
        }

        EchoServer server = new EchoServer(port);
        ServerConsole console = new ServerConsole(server);
        
        try {
            server.listen();  // Start listening for clients
        } catch (IOException e) {
            System.out.println("Error: Could not start server on port " + port);
        }

        console.accept();  // Start accepting console input
    }
}

