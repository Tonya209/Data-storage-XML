package dataStorageXML;

import java.util.regex.Pattern;

/**
 *
 * @author Antonina Hales
 */
public class Utilidades {
    
     /**
     * Comprueba que sea un DNI valido.
     * @param dato Dato para comprobar si es un DNI.
     * @return true si es un DNI valido.
     */
    public static boolean esDNI(String dato){
        Pattern p = Pattern.compile("([XY]?)([0-9]{1,9})([A-Za-z])");
        return p.matcher(dato).matches();
    }
    
    /**
     * Comprueba que sea un NIF valido.
     * @param dato Dato para comprobar si es un NIF.
     * @return true si es un NIF valido.
     */
    public static boolean esNIF(String dato){
        Pattern p = Pattern.compile("([XY]?)([0-9]{1,9})([A-Za-z])");
        return p.matcher(dato).matches();
    }
    
    /**
     * Comprueba que sea un correo valido.
     * @param dato Dato para comprobar si es un correo.
     * @return true si es un correo valido.
     */
    
    public static boolean esMail(String dato){
        Pattern p = Pattern.compile("\\w+(\\.|\\_)?\\w+@\\w+\\.\\w+");
        return p.matcher(dato).matches();
    }
    
    /**
     * Comprueba que el error pertenece a un numero o a un mail.
     * @param dato Cadena que contiene el dato erroneno.
     * @return true es un telefono no valido.
     */
    
    public static boolean esTelErr(String dato){
        Pattern p = Pattern.compile("\\+?\\d+");
        return p.matcher(dato).matches();
    }
     
    
    
    /**
     * Comprueba que varios formatos de telefonos  validos.
     * @param dato Dato para comprobar si es un telefono valido.
     * @return true si es un telefono valido.
     */
    public static boolean esTelf(String dato){
        Pattern fm = Pattern.compile("^(\\+34|0034|34)?[6789]\\d{8}$");
        Pattern fn = Pattern.compile("^(\\+34|0034|34)?[89]\\d{8}$");
        
        Pattern ml = Pattern.compile("^(\\+34|0034|34)?[67]\\d{8}$");
        
        Pattern inter = Pattern.compile("^\\+(?:[0-9]?){6,14}[0-9]$");
        
        return fm.matcher(dato).matches() || fn.matcher(dato).matches() || ml.matcher(dato).matches()
                || inter.matcher(dato).matches();
    }
    
    
    /**
     * Limpia la cadena de signos y espacios.
     * @param dato cadena para quitarle los espacios y signos.
     * @return cadena limpia sin signos y espacios.
     */
     
    public static String limpiarStr(String dato){
         return dato.replace("\"", "")
                    .replace("'","")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("&quot;", "").trim();
    }
}
