import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame{
    ServerInterfase server;
    private static final int WIDTH = 450;
    private static final int HEIGHT = 360;
    private boolean serverConnect = false;
    private String name;
    JTextField ipServer,portServer,login,password,message;
    JButton sendButton,loginButton;
    JTextArea messagArea;
    /**
     Интерфейс тестового клиент мессенджера
    **/
    ClientGUI(ServerInterfase server){
        this.server = server;
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Test Client Interfase");
        setResizable(false);


        JPanel headerJpanel = new JPanel(new  GridLayout(2,3));
        JPanel downJpanel = new JPanel(new GridLayout(1,2));
        messagArea = new JTextArea();
        messagArea.setEditable(false);
        
        ipServer = new JTextField("127.0.0.1");
        portServer = new JTextField("3086");
        login = new JTextField("Ilkin Rinat");
        password = new JTextField("123456");
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverConnect){
                    disconnectFromServer();
                    return;
                }
                connectToServer();
            }
        });
        headerJpanel.add(ipServer);
        headerJpanel.add(portServer);
        headerJpanel.add(login);
        headerJpanel.add(password);
        headerJpanel.add(loginButton);

        message = new JTextField();
        sendButton = new JButton("Send");
        downJpanel.add(message);
        downJpanel.add(sendButton);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageUploadToServer();
            }
        });

        add(headerJpanel,BorderLayout.NORTH);
        add(downJpanel,BorderLayout.SOUTH);
        add(new JScrollPane(messagArea));
        setVisible(true);
    }
    public void answer(String text){
        appendMessage(text,Color.BLACK);
    }
    private void appendMessage(String mess,Color col){
        messagArea.setForeground(col);
        messagArea.append(mess + "\n");
    }
    private void connectToServer(){
        if (server.connectUser(this)){
            appendMessage("Вы успешно подключились!\n",Color.green);
            ipServer.setEditable(false);
            portServer.setEditable(false);
            login.setEditable(false);
            password.setEditable(false);
            serverConnect = true;
            name = login.getText();
            String mess = server.getLog();
            loginButton.setText("Logout");
            if (mess != null){
                appendMessage(mess,Color.BLACK);
            }
        } else {
            appendMessage("Подключение не удалось",Color.RED);
        }

    }
    public void disconnectFromServer() {
        if (serverConnect) {
            serverConnect = false;
            ipServer.setEditable(true);
            portServer.setEditable(true);
            login.setEditable(true);
            password.setEditable(true);
            loginButton.setText("Login");
            appendMessage("Вы были отключены от сервера!",Color.RED);
            server.disconnectUser(this);
        }
    }

    private void messageUploadToServer(){
        if (serverConnect){
            String text = message.getText();
            if (!text.equals("")){ 
                server.message(name + ": " + text);
                message.setText("");
            }
        } else {
            appendMessage("Нет подключения к серверу",Color.RED);
        }
    }
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer();
        }
        super.processWindowEvent(e);
    }

}
