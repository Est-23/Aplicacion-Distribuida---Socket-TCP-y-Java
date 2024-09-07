
package estebancantillo.imc.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import estebancantillo.imc.modelo.CalculoImc;
import estebancantillo.imc.vistas.VentanaPrincipal;

/**
 *
 * @author USUARIO
 */

//La clase representativa de un hilo secundario que se encarga de atender a un cliente individual conectado al servidor.
public class SubProcesoCliente extends Thread {

    // Creamos los atributos
    private Socket cliente; //Objeto Socket que representa la conexión con el cliente.
    private String ip; //Dirección IP del cliente.
    private VentanaPrincipal ventana; //Referencia a la ventana principal de la aplicación para actualizar la interfaz gráfica.

    //Creamos los metodos
    //Constructor que inicializa los atributos con la conexión del cliente y la referencia a la ventana principal.
    public SubProcesoCliente(Socket cliente, VentanaPrincipal v) {
        this.cliente = cliente;
        ip = cliente.getInetAddress().getHostAddress();
        ventana = v;
    }

    @Override
    //Creamos el metodo principal del hilo para calcular el IMC y enviar la respuesta al cliente, de lo contrario se muestra un mensaje de error y y se cierra la conexión.
    public void run() {
        try {
            CalculoImc.Imc imc = calcularImc();
            enviarRespuesta(imc);
        } catch (Exception ex) {
            System.out.println(log() + ex.getMessage());
            ventana.getCajaLog().append(log() + ex.getMessage() + "\n");
            try {
                cliente.close();
            } catch (IOException ex1) {
                ServidorTcp.listaDeClientes.remove(ip);
            } finally {
                ServidorTcp.listaDeClientes.remove(ip);
            }
        }
    }

    //Método que recibe el peso y la altura del cliente, calcula el IMC utilizando la clase "CalculoImc" y devuelve el resultado.
    public CalculoImc.Imc calcularImc() throws Exception {
        DataInputStream input = null;
        try {
            input = new DataInputStream(cliente.getInputStream());
            String msg = "Esperando el PESO: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n" + "\n");
            float peso = input.readFloat();
            msg = "PESO: " + peso;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            msg = "Esperando La Altura: ";
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            float altura = input.readFloat();
            msg = "ALTURA: " + altura;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            CalculoImc datosImc = new CalculoImc(peso, altura);
            System.out.println(log() + "IMC: " + datosImc.getImc().resultado);
            msg = "IMC: " + datosImc.getImc().resultado;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");

            System.out.println(log() + "MENSAJE: " + datosImc.getImc().mensaje);
            msg = "MENSAJE: " + datosImc.getImc().mensaje;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            return datosImc.getImc();
        } catch (IOException ex) {
            String msg = "Error al caputurar datos del cliente " + ip;
            System.out.println(log() + msg);
            ventana.getCajaLog().append(log() + msg + "\n");
            throw new Exception("Error al caputurar datos del cliente " + ip);
        }
    }

    //Método que envía el resultado del cálculo del IMC y el mensaje interpretativo al cliente (hilo separado).
    public void enviarRespuesta(CalculoImc.Imc imc) {
        Thread hiloResponde = new Thread() {
            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(cliente.getOutputStream());
                    output.writeFloat(imc.resultado);
                    output.writeUTF(imc.mensaje);
                    String msg = "IMC: " + imc.resultado;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    msg = "MENSAJE: " + imc.mensaje;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    output.flush();
                    enviarRespuesta(calcularImc());
                } catch (IOException ex) {
                    String msg = "Error al enviar datos al cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    ServidorTcp.listaDeClientes.remove(ip);
                } catch (Exception ex) {
                    String msg = "Error al leer datos del cliente " + ip;
                    System.out.println(log() + msg);
                    ventana.getCajaLog().append(log() + msg + "\n");
                    try {
                        cliente.close();
                    } catch (IOException ex1) {
                        ServidorTcp.listaDeClientes.remove(ip);
                    } finally {
                        ServidorTcp.listaDeClientes.remove(ip);
                    }
                }
            }
        };
        hiloResponde.start();
    }

    //Metodod Set Y get para obtener y establecer el objeto Socket del cliente.
    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
    }

    //Creamos un string con la fecha, hora y dirección IP del cliente en formato para los mensajes de log.
    public String log() {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        return ip + " -> " + f.format(new Date()) + " - ";
    }
}

