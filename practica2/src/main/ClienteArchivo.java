import javax.swing.JFileChooser;
import java.io.File.*;
import java.net.*;
import java.io.*;

public class ClienteArchivo {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            // System.out.printf("Escriba la dirección del servidor:");
            String host = "localhost";
            // System.out.printf("Escriba el puerto:");
            // int pto = Integer.parseInt(br.readLine());
            int pto=5050;

            Socket cl = new Socket(host, pto);

            
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
        
            int r = jf.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile(); // Manejador
                String archivo = f.getAbsolutePath(); // Dirección
                String nombre = f.getName(); // Nombre
                long tam = f.length(); // Tamaño

                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(new FileInputStream(archivo));

                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();

                byte[] b = new byte[1024];
                long enviados = 0;
                int porcentaje, n;
                while (enviados < tam) {
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    enviados = enviados + n;
                    porcentaje = (int) (enviados * 100 / tam);
                    System.out.print("Enviados: " + porcentaje + "%\r");
                } // While
                System.out.print("\n\nArchivos enviados");
                dos.close();
                dis.close();
                cl.close();
                // if
            }
        } catch (

        Exception e) {
            e.printStackTrace();

        }
    }
}
