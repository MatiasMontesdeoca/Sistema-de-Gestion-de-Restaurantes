package Serializacion;

import ExcepcionesPersonalizadas.MensajesDeExcepciones;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ArchivoDatos {

    // Guarda una lista de objetos serializados en un archivo binario
    public static void guardar(ArrayList<?> lista, String nombreArchivo) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(nombreArchivo));
            out.writeObject(lista);
        } catch (IOException e) {
            MensajesDeExcepciones.mostrarError("No se pudo guardar el archivo " + nombreArchivo);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    MensajesDeExcepciones.mostrarError("Error al cerrar el archivo " + nombreArchivo);
                }
            }
        }
    }

    // Carga y deserializa una lista de objetos desde un archivo binario
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> cargar(String nombreArchivo) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(nombreArchivo));
            return (ArrayList<T>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            MensajesDeExcepciones.mostrarError("No se pudo leer el archivo " + nombreArchivo);
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            MensajesDeExcepciones.mostrarError("Los datos del archivo son incompatibles.");
            return new ArrayList<>();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    MensajesDeExcepciones.mostrarError("Error al cerrar el archivo " + nombreArchivo);
                }
            }
        }
    }
}