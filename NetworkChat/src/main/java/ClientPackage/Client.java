
package ClientPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private String serverIP;
    private String message = "";
    
    public Client(String port){
        super("Clinet Computer");
        serverIP = port;
        userText = new JTextField();
        chatWindow = new JTextArea();
        userText.setEditable(false);
        userText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(e.getActionCommand());
                userText.setText("");
            }
        });
        add(userText, BorderLayout.NORTH);
        add(new JScrollPane(chatWindow));
        setSize(300, 300);
        setVisible(true);
    }
    public void startRunning(){
        try{
            while (true){
                try {
                    connectToServer();
                    setUpStream();
                    whileChatting();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeObject() {
        showMessage("Close Connection\n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void whileChatting() {
        String message = "Now Connected";
        ableToType(true);
        do{
            try{
                message = (String)input.readObject();
                showMessage("\n" + message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }while (!message.equals("CLIENT-END"));
    }

    private void ableToType(boolean b) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userText.setEditable(b);
            }
        });
    }

    private void setUpStream() {
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void connectToServer() {
        try {
            showMessage("เชื่อมต่อกับ Server");
            connection = new Socket(InetAddress.getByName("localhost"), 6789);
            showMessage("Connect :"+connection.getInetAddress().getHostName());
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void sendMessage(String str){
        try {
            output.writeObject("CLIENT :" + str);
            output.flush();
            showMessage("\nCLIENT :" + str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showMessage(String txt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatWindow.append(txt);
            }
        });
    }
}
