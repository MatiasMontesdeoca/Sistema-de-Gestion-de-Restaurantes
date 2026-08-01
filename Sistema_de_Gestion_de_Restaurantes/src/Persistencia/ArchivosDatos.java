package Persistencia;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import GestionDeClientes.Cliente;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ArchivosDatos {
    
    //Guardar Clientes
    public static void guardarClientes(ArrayList<Cliente> clientes){
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(new FileOutputStream("clientes.dat"));
            out.writeObject(clientes);
        } catch(IOException e){
            MensajesDeExcepciones.mostrarError("No se pudo guardar la informacion de los clientes");
        } finally{
            if(out != null){
                try{
                    out.close();
                } catch(IOException e){
                    MensajesDeExcepciones.mostrarError("Error al cerrar el archivo de clientes");
                }
            }
        }
    }
    
    //Cargar Clientes
    @SuppressWarnings("unchecked") public static ArrayList<Cliente> cargarClientes(){
        ObjectInputStream in = null;
        try{
            in = new ObjectInputStream(new FileInputStream("clientes.dat"));
            return (ArrayList<Cliente>)in.readObject();
        } catch(FileNotFoundException e){
            return new ArrayList<>();
        } catch(IOException e){
            MensajesDeExcepciones.mostrarError("No se pudo leer el archivo de clientes");
            return new ArrayList<>();
        } catch(ClassNotFoundException e){
            MensajesDeExcepciones.mostrarError("Los datos del archivo no son compatibles");
            return new ArrayList<>();
        } finally{
            if (in != null){
                try{
                    in.close();
                } catch(IOException e){
                    MensajesDeExcepciones.mostrarError("Ocurrio un error al cerrar el archivo de clientes");
                }
            }
        }
    }
}
