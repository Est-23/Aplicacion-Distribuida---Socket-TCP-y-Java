
package estebancantillo.imc.modelo;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */

//Procedemos a realizar el constructor por defecto.
public class CalculoImc implements Serializable {

    //Atributos del contructor.
    private float peso; 
    private float altura;

    
    //Metodos Set y get para obtener y establecer los valores de los atributos.
    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    
    public static class Imc{
        public float resultado;
        public String mensaje;
    }
    
    private Imc imc;
    
    
    public CalculoImc(){
        
    }
    
    public CalculoImc(float peso, float altura){
        this.peso = peso; 
        this.altura = altura;
    }
    
    //Crear un objeto llamado Imc para validar si el peso y la altura son valores positivos. Si alguno es negativo o cero.
    public Imc getImc(){
        imc = new Imc();
        
        if(peso <= 0 || altura <= 0){
            imc.mensaje = "ERROR: El peso y la altura deben ser mayores que 0";
            return imc;
        }else {
            imc.resultado = peso / (altura * altura);
            if(imc.resultado < 18.5){
                imc.mensaje = "Debes consultar un Medico, tu peso es muy bajo";
            }else if(imc.resultado >= 18.5 && imc.resultado <= 24.9){
                imc.mensaje = "Estas bien de peso";
            }else if(imc.resultado > 24.9 && imc.resultado <= 29.9){
                imc.mensaje = "Debes bajar un poco de peso"; 
            }else {
                imc.mensaje = "Debes consultar un medico, tu peso es muy alto";
            }return imc; //Retorna el objeto con el resultado del cálculo.
        }
    }
}
