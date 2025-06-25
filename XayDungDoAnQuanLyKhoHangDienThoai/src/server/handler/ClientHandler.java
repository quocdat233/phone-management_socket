package server.handler;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    private final String username;

    public ClientHandler(Socket socket, String username, ObjectInputStream reader, ObjectOutputStream writer) {
        this.socket = socket;
        this.username = username;
        this.reader = reader;
        this.writer = writer;
    }

    public String getUsername() { return username; }
    public Socket getSocket() { return socket; }
    public ObjectOutputStream getWriter() { return writer; }
    public ObjectInputStream getReader() { return reader; }

    public void send(Object obj) {
        try {
            writer.writeObject(obj);
            writer.flush();
        } catch (IOException e) {
            System.err.println("[ClientHandler] Error sending to " + username + ": " + e.getMessage());
        }
    }


//    public void handleChatMessage(ChatMessage chatMessage) {
//        // Nếu là chat riêng
//        if (chatMessage.getType() == ChatMessage.Type.PRIVATE) {
//            Server.clientHandlers.stream()
//                    .filter(c -> c.getUsername().equals(chatMessage.getRecipient()))
//                    .findFirst()
//                    .ifPresent(c -> c.send(chatMessage));
//        }
//        // Nếu là chat nhóm
//        else if (chatMessage.getType() == ChatMessage.Type.GROUP) {
//            Server.clientHandlers.stream().forEach(c -> {
//                if (chatMessage.getRecipient().equals("ALL") || c.getUsername().equals(chatMessage.getRecipient())) {
//                    c.send(chatMessage);
//                }
//            });
//        }
//    }
}