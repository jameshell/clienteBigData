/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bigdataclient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A simple Swing-based client for the capitalization server.
 * It has a main frame window with a text field for entering
 * strings and a textarea to see the results of capitalizing
 * them.
 */
public class BigDataClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Fundamentos de Big Data");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);

    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Enter in the
     * listener sends the textfield contents to the server.
     */
    public BigDataClient() {

        // INTERFAZ QUE SE VA A USAR
        messageArea.setEditable(false);
        frame.getContentPane().add(dataField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");

        // SE AGREGAN LISTENERS
        dataField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield
             * by sending the contents of the text field to the
             * server and displaying the response from the server
             * in the text area.  If the response is "." we exit
             * the whole application, which closes all sockets,
             * streams and windows.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(dataField.getText());
                   String response;
                try {
                    response = in.readLine();
                    if (response == null || response.equals("")) {
                          System.exit(0);
                      }
                } catch (IOException ex) {
                       response = "Error: " + ex;
                }
                messageArea.append(response + "\n");
                dataField.selectAll();
            }
        });
    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The Capitalizer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void connectToServer() throws IOException {

        // SE CONSIGUE LA DIRECCION DEL SERVIDOR A TRAVES DEL CUADRO DE DIALOGO
        String serverAddress = JOptionPane.showInputDialog(
            frame,
            "Escribe la direccion IP del servidor:",
            "Bienvenido al curso de Fundamentos de BigData!",
            JOptionPane.QUESTION_MESSAGE);

        // SE REALIZA LA CONEXION Y LOS STREAMS
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(new InputStreamReader( socket.getInputStream() ) );
        out = new PrintWriter(socket.getOutputStream() , true);

        // MENSAJE DE BIENVENIDA DEL SERVIDOR AL CLIENTE
        for (int i = 0; i < 4; i++) {
           
            String localStr=in.readLine();
            if(localStr.equals("100")){
                String msj="Se recibio el numero 100\n";
                contadorNumPrimos(100);
                messageArea.append(msj);
            } else if(localStr.equals("200")){
                String msj="Se recibio el numero 200\n";
                contadorNumPrimos(200);
                messageArea.append(msj);
            }else if(localStr.equals("300")){
                String msj="Se recibio el numero 300\n";
                contadorNumPrimos(300);
                messageArea.append(msj);
            }else{
                messageArea.append(localStr + "\n");
            }
        }
        
    }
    
      public static boolean isPrime(int n) {
    //Se prueba si es multiplo de 2
    if (n%2==0) return false;
    //Si no checkear impares
    for(int i=3;i*i<=n;i+=2) {
        if(n%i==0)
            return false;
    }
    return true;
}
      
      public static void contadorNumPrimos(int i){
     
        int contador=0;
        int cantNumPrimos=0;
        while(contador<i){
            if(isPrime(contador)==true){
                cantNumPrimos++;
            }
            contador++;
        }
        System.out.println("Cantidad de numeros primos desde 0 hasta "+i+" es:"+" "+cantNumPrimos);
  }
    

    /**
     * SE CORRE LA APLICACION DE CLIENTE
     */
    public static void main(String[] args) throws Exception {
        BigDataClient client = new BigDataClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.pack();
        client.frame.setVisible(true);
        client.connectToServer();
    }
}