// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import com.lloseng.ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host, int port, ChatIF clientUI)
      throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(formatMessage(msg.toString()));

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
    if (message.startsWith("#")) {
      handleCommand(message);
      
    } else {

    try {
      sendToServer(message);
    } catch (IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }
}

private void handleCommand(String command){
  String [] parts = command.split(" ");
  switch (command) {
    case "#quit":
      quit();
      break;
    case "#logoff":
      clientUI.display("Loggin off...");
      try {
        closeConnection();
      } catch (IOException e) {
        clientUI.display("Error while logging off.");
      }
      break;
    case "#sethost":
      if (parts.length > 1) {
        String newHost = parts[1];
        clientUI.display("Host set to: " + newHost);
        
      } else {
        clientUI.display("Usage: #sethost <host>");
      }
      break;
    case "#setport":
      if (parts.length > 1) {
        try {
          int newPort = Integer.parseInt(parts[1]);
          clientUI.display("Port set to : " + newPort);
        } catch (NumberFormatException e) {
          clientUI.display("Invalid port number.");
        }
        
      } else {
        clientUI.display("Usage: #setport <port>");
      }
      break;
    case "#login":
       
      clientUI.display("Please enter your login ID.");
      break;
    case "#gethost":
            
      clientUI.display("Current host: " + getHost());
      break;
    case "#getport":
            
      clientUI.display("Current port: " + getPort());
      break;
    case "#help":
      clientUI.display("Available commands: #quit, #logoff, #sethost <host>, #setport <port>, #login, #gethost, #getport, #help");
      break;
    default:
      clientUI.display("Unknown command: " + command);
      break;
  }
}

private String formatMessage(String message){
  return "SERVER MSG> " + message;
}

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
      clientUI.display("Client is closing...");
    } catch (IOException e) {
      clientUI.display("Error while clsoing connection.");
    }
    System.exit(0);
  }
}
// End of ChatClient class
