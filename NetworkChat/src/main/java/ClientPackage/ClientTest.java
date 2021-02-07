package ClientPackage;

import javax.swing.*;

public class ClientTest {
    public static void main(String[] args) {
        Client clinet = new Client("localhost");
        clinet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clinet.startRunning();
    }
}
