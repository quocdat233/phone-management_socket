package network;

import shared.request.FileMessage;
import shared.response.GroupUserListResponse;
import shared.response.HistoryResponse;
import shared.response.LoginResponse;
import shared.models.SanPham;
import shared.request.ChatMessage;
import shared.request.GroupListUpdate;
import shared.request.UserListUpdate;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.function.Consumer;

public class SocketManager {
    private static SocketManager instance;
    private Consumer<GroupListUpdate> groupListListener;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final String host = "localhost";
    private final int port = 8888;
    private Consumer<List<SanPham>> productListListener;
    private volatile boolean isProcessingRequest = false;
    private Consumer<LoginResponse> loginListener;
    private Consumer<UserListUpdate> userListListener;
    private Consumer<ChatMessage> chatListener;
    private final List<Consumer<ChatMessage>> chatListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
    public void setUserListListener(Consumer<UserListUpdate> listener) {
        this.userListListener = listener;
    }
    public void setChatListener(Consumer<ChatMessage> listener) {
        this.chatListener = listener;
    }
    private final List<Consumer<FileMessage>> fileListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
    private Consumer<GroupUserListResponse> groupUserListListener;
    private Consumer<HistoryResponse> historyListener;
    public void setProcessingRequest(boolean processing) {
        this.isProcessingRequest = processing;
    }
    public void setProductListListener(Consumer<List<SanPham>> listener) {
        this.productListListener = listener;
    }
    private SocketManager() throws Exception {
        this.connect();
    }
    private void connect() throws Exception {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutputStream() {
        return oos;
    }
    public static SocketManager getInstance() throws Exception {
        if (instance == null) {
            instance = new SocketManager();
        }

        return instance;
    }
    public ObjectInputStream getInputStream() {
        return ois;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }
    public void close() {
        try {
            if (ois != null) {
                ois.close();
            }

            if (oos != null) {
                oos.close();
            }

            if (socket != null && !this.socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void send(Object request) throws IOException {
        if (oos == null) throw new IOException("Output stream is null.");
        synchronized (oos) {
            oos.reset();
            oos.writeObject(request);
            oos.flush();
        }
    }
    public Object receive() throws IOException, ClassNotFoundException {
        synchronized (ois) {
            return ois.readObject();
        }
    }
    public void startListening() {
        System.out.println("[SocketManager] Start listening thread...");
        new Thread(() -> {
            while (isConnected()) {
                try {
                    Object obj;
                    synchronized (ois) {
                        obj = ois.readObject();
                    }
                    if (obj instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof SanPham) {
                        if (productListListener != null) {
                            SwingUtilities.invokeLater(() -> productListListener.accept((List<SanPham>) list));
                        }

                    } else if (obj instanceof LoginResponse loginRes) {
                        if (loginListener != null) {
                            SwingUtilities.invokeLater(() -> loginListener.accept(loginRes));
                        }

                    } else if (obj instanceof ChatMessage chatMsg) {
                        System.out.println("[SocketManager] Tin nhắn chat đến: " + chatMsg.getMessage());

                        if (chatListener != null) {
                            SwingUtilities.invokeLater(() -> {
                                System.out.println("[DEBUG] Gọi chatListener.accept(chatMessage)");
                                chatListener.accept(chatMsg);
                            });
                        }

                    }      else if (obj instanceof UserListUpdate userListUpdate) {
                        System.out.println("[SocketManager] UserListUpdate received: " + userListUpdate.getOnlineUsers());
                        if (userListListener != null) {
                            SwingUtilities.invokeLater(() -> userListListener.accept(userListUpdate));
                        } else {
                            System.out.println("[SocketManager] userListListener is null!");
                        }
                    }
                    else if (obj instanceof GroupListUpdate groupListUpdate) {
                        System.out.println("[SocketManager] Nhận GroupListUpdate: " + groupListUpdate.getGroupNames());
                        if (groupListListener != null) {
                            SwingUtilities.invokeLater(() -> groupListListener.accept(groupListUpdate));
                        }
                    }
                    else if (obj instanceof GroupUserListResponse res) {
                        if (groupUserListListener != null) {
                            SwingUtilities.invokeLater(() -> groupUserListListener.accept(res));
                        }
                    }
                    else if (obj instanceof FileMessage fileMsg) {
                        SwingUtilities.invokeLater(() -> {
                            for (Consumer<FileMessage> listener : fileListeners) {
                                listener.accept(fileMsg);
                            }
                        });
                    }
                    else if (obj instanceof shared.response.HistoryResponse historyResponse) {
                        SwingUtilities.invokeLater(() -> {
                            for (ChatMessage msg : historyResponse.getMessages()) {
                                for (Consumer<ChatMessage> listener : chatListeners) {
                                    listener.accept(msg);
                                }
                            }
                        });
                    }
                } catch (EOFException | StreamCorruptedException eof) {
                    System.out.println("[SocketManager] Luồng đầu vào bị đóng (EOF). Ngắt listener.");
                    break;
                } catch (SocketException e) {
                    System.out.println("[SocketManager] Kết nối bị mất, cần reconnect...");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }
    public void setGroupListListener(Consumer<GroupListUpdate> listener) {
        this.groupListListener = listener;
    }
    public static void resetInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
    public void sendFile(String sender, String recipient, String filename, byte[] data, boolean isGroup) throws IOException {
        FileMessage fileMessage = new FileMessage(sender, recipient, filename, data, isGroup);
        send(fileMessage);
    }
    public void setGroupUserListListener(Consumer<GroupUserListResponse> listener) {
        this.groupUserListListener = listener;
    }
    public void addFileListener(Consumer<FileMessage> listener) {
        fileListeners.add(listener);
    }
    public void addChatListener(Consumer<ChatMessage> listener) {
        chatListeners.add(listener);
    }



}