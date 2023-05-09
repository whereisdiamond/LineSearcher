package finalProject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ClientGUI {

    private static final String SERVER_HOSTNAME = "localhost";
    private static final int PORT = 8080;

    private JFrame frame;
    private JLabel label;
    private JTextField textField;
    private JButton button;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;
    private JScrollPane scrollPane;
    private JTextField responseField;

    public ClientGUI() {
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame("Hamlet Line Searcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));

        label = new JLabel("Enter line number:");
        textField = new JTextField(10);
        button = new JButton("Search");
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(textField);
        panel.add(button);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(500, 200));


        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        button.addActionListener(e -> {
            search();
        });
    }

    private void search() {
        String lineNumStr = textField.getText();
        int lineNum = Integer.parseInt(lineNumStr);
    
        try (Socket socket = new Socket(SERVER_HOSTNAME, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(lineNumStr);
            String response = in.readLine();
            StringBuilder responseBuilder = new StringBuilder(response);
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append("\n").append(line);
            }
            response = responseBuilder.toString();
            List<String> contextLines = Arrays.asList(response.split("\n"));
            listModel.clear();
            for (String contextLine : contextLines) {
                listModel.addElement(contextLine);
                listModel.addElement("\n");
            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    

    public static void main(String[] args) {
        new ClientGUI();
    }
}