import java.io.*; // Permite al usuario seleccionar archivos mediante una interfaz gráfica
import java.net.*;                // Manejo de flujos de entrada/salida
import javax.swing.JFileChooser;               // Clases para la comunicación por red

public class Cliente implements Serializable{

    private String name;
    private String telefono;
    private String curp;
    private int edad;
    private float peso;
    private double altura;
    private boolean estadoEnfermo;

    public Cliente(String name, String telefono, String curp, int edad, float peso, double altura,boolean estadoEnfermo) {
        this.name = name;
        this.telefono = telefono;
        this.curp = curp;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.estadoEnfermo = estadoEnfermo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCURP() {
        return curp;
    }
    public void setCURP(String curp) {
        this.curp = curp;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public float getPeso() {
        return peso;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }
    public double getAltura() {
        return altura;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public boolean isEstadoEnfermo() {
        return estadoEnfermo;
    }
    public void setEstadoEnfermo(boolean estadoEnfermo) {
        this.estadoEnfermo = estadoEnfermo;
    }

    



    public static void main(String[] args) {
        try {
            // Dirección y puerto del servidor al que se conectará el cliente
            String host = "localhost";
            int pto = 5050;

            // Establecer conexión con el servidor
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

            // Se crea un selector de archivos con opción de múltiples selecciones
            JFileChooser jf = new JFileChooser();
            jf.setMultiSelectionEnabled(true);
            int r = jf.showOpenDialog(null); // Abre la ventana para seleccionar archivos

            // Si el usuario aprueba la selección de archivos
            if (r == JFileChooser.APPROVE_OPTION) {
                File[] archivos = jf.getSelectedFiles(); // Arreglo de archivos seleccionados

                // Enviar al servidor la cantidad de archivos que se enviarán
                dos.writeInt(archivos.length);
                dos.flush();

                // Iterar sobre cada archivo seleccionado para enviarlo
                for (File f : archivos) {
                    String archivo = f.getAbsolutePath(); // Ruta completa
                    String nombre = f.getName();          // Nombre del archivo
                    long tam = f.length();               // Tamaño del archivo

                    // Preparar flujos para leer el archivo
                    FileInputStream fis = new FileInputStream(f);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    // Enviar nombre del archivo
                    dos.writeUTF(nombre);
                    dos.flush();

                    // Enviar tamaño del archivo
                    dos.writeLong(tam);
                    dos.flush();

                    // Enviar el contenido del archivo en bloques de 1024 bytes
                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje, n;
                    while (enviados < tam) {
                        n = bis.read(b);                // Leer datos desde el archivo
                        dos.write(b, 0, n);             // Enviar datos al servidor
                        dos.flush();
                        enviados += n;                 // Acumular bytes enviados

                        // Calcular y mostrar el porcentaje enviado
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.print("Enviando '" + nombre + "': " + porcentaje + "%\r");
                    }

                    bis.close(); // Cierra el archivo leído
                    System.out.println("\nArchivo '" + nombre + "' enviado.");
                }

                // Cierre de conexión y flujo de salida
                dos.close();
                cl.close();
                System.out.println("\nTodos los archivos fueron enviados correctamente.");
            }
        } catch (Exception e) {
            // Imprime detalles del error si ocurre una excepción
            e.printStackTrace();
        }
    }
}
