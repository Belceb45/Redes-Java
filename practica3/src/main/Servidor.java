import java.io.*;   // Importa clases necesarias para la comunicación en red (Socket, ServerSocket, etc.)
import java.net.*;    // Importa clases necesarias para manejo de entrada/salida (streams, archivos, etc.)

public class Servidor {
    public static void main(String[] args) {
        try {
            // Se crea un ServerSocket que escucha en el puerto 5050
            ServerSocket s = new ServerSocket(5050);
            System.out.println("Servidor iniciado. Esperando conexiones...");

            // Bucle infinito para aceptar múltiples conexiones de clientes uno a uno
            for (;;) {
                // Espera y acepta una conexión entrante de un cliente
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde " + cl.getInetAddress() + ":" + cl.getPort());

                // Flujo de entrada para recibir datos del cliente
                DataInputStream dis = new DataInputStream(cl.getInputStream());

                // Se lee la cantidad de archivos que el cliente va a enviar
                int cantidadArchivos = dis.readInt();
                System.out.println("Cantidad de archivos a recibir: " + cantidadArchivos);

                // Buffer para almacenar los datos recibidos temporalmente
                byte[] b = new byte[1024];

                // Ciclo para recibir cada archivo
                for (int i = 0; i < cantidadArchivos; i++) {
                    // Se recibe el nombre del archivo
                    String nombre = dis.readUTF();
                    // Se recibe el tamaño del archivo
                    long tam = dis.readLong();
                    System.out.println("Recibiendo archivo: " + nombre + " (" + tam + " bytes)");

                    // Se crea un flujo de salida para guardar el archivo recibido en el disco
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));

                    // Variables para controlar el progreso de la transferencia
                    long recibidos = 0;
                    int n, porcentaje;

                    // Se recibe el contenido del archivo en bloques
                    while (recibidos < tam) {
                        // Lee del flujo de entrada y guarda en el buffer
                        n = dis.read(b);
                        // Escribe del buffer al archivo
                        dos.write(b, 0, n);
                        dos.flush();

                        // Actualiza cantidad de bytes recibidos
                        recibidos += n;

                        // Calcula porcentaje de avance
                        porcentaje = (int) (recibidos * 100 / tam);
                        System.out.print("Recibiendo '" + nombre + "': " + porcentaje + "%\r");
                    }

                    // Cierra el flujo de salida del archivo
                    dos.close();
                    System.out.println("\nArchivo '" + nombre + "' recibido correctamente.");
                }

                // Cierra el flujo de entrada y el socket cliente
                dis.close();
                cl.close();
                System.out.println("Conexión finalizada.\n");
            }
        } catch (Exception e) {
            // Captura y muestra cualquier error que ocurra durante la ejecución
            e.printStackTrace();
        }
    }
}
