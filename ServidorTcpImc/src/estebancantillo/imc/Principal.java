package estebancantillo.imc;

import estebancantillo.imc.vistas.VentanaPrincipal;

/**
 *
 * @author USUARIO
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VentanaPrincipal v = new VentanaPrincipal(); //Creacion del objeto de la clase VentanaPrincipal y lo almacena en la variable V.
        v.setLocationRelativeTo(null); //Centra la ventana en la pantalla.
        v.setVisible(true); //Hace que la ventana sea visible para el usuario.
    }

}
