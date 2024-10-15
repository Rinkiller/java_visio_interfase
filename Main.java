public class Main {
    public static void main(String[] args) {
        ServerInterfase serv =  new ServerInterfase();
        new ClientGUI(serv);
        new ClientGUI(serv);
    }
}