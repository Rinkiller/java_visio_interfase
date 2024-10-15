import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServerInterfase extends JFrame{
    private static final int WIDTH = 250;
    private static final int HEIGHT = 350;
    private boolean isSerwerWorking = false;
    List<ClientGUI> clientList;
    private String fileLogName = "log.txt";

    ServerInterfase(){
        clientList = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Test Server Interfase");
        setResizable(false);
        
        JPanel panBottom = new JPanel(new GridLayout(3, 1));
        JPanel labelServerSec = new JPanel(new GridBagLayout());
        JLabel isSerwerWorkerLabel = new JLabel("Сервер отключен");

        isSerwerWorkerLabel.setForeground(Color.RED);
        JButton btnStart = new JButton("Запуск сервера");
        JButton btnExit = new JButton("Отключение сервера");
        labelServerSec.add(isSerwerWorkerLabel);
        panBottom.add(labelServerSec);
        
        panBottom.add(btnStart);
        panBottom.add(btnExit);
        add(panBottom);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isSerwerWorking) {return;}
                isSerwerWorkerLabel.setForeground(Color.RED);
                isSerwerWorkerLabel.setText("Сервер отключен");
                isSerwerWorking = false;
                while (!clientList.isEmpty()){
                    disconnectUser(clientList.get(clientList.size()-1));
                }
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isSerwerWorking){return;}
                isSerwerWorkerLabel.setForeground(Color.green);
                isSerwerWorkerLabel.setText("Сервер включен");
                isSerwerWorking = true;
            }
        });
        setVisible(true);
    }

    public boolean connectUser(ClientGUI clientGUI){
        if (!isSerwerWorking){
            return false;
        }
        for (ClientGUI elem : clientList) {
            if (clientGUI == elem){return true;}
        }
        clientList.add(clientGUI);
        return true;
    }

    public void disconnectUser(ClientGUI clientGUI){
        clientList.remove(clientGUI);
        if (clientGUI != null){
            clientGUI.disconnectFromServer();
        }
    }

    public void message(String mess){
        if (!isSerwerWorking){
            return;
        }
        mess += "";
        messToClientGUI(mess);
        saveInfile(mess);
    }

    private void saveInfile(String mess){
        try (FileWriter writer = new FileWriter(fileLogName, true)){
            writer.write(mess + "\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void messToClientGUI(String mess){
        for (ClientGUI clientGUI: clientList){
            clientGUI.answer(mess);
        }
    }

    public String getLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(fileLogName);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}