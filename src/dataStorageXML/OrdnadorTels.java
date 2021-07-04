package dataStorageXML;

import java.util.Comparator;

/**
 *
 * @author Antonina Hales
 */
public class OrdnadorTels implements Comparator<String>{

//los metodos para ordenar los telefonos
    @Override
    public int compare(String t, String t1) {
       int r = 0;
       if (!t.startsWith("+") && !t1.startsWith("+")){
           r = compareInt(Long.parseLong(t), Long.parseLong(t1));
       }else{
           if (t.startsWith("+") && t1.startsWith("+")){
               r = compareInt(Long.parseLong(t.substring(1)), Long.parseLong(t1.substring(1)));
           }else if (t.startsWith("+")){
               r = 1;
           }else if (t1.startsWith("+")){
               r = -1;
           }
       }
       
       return r;
    }
    
    
    
    public int compareInt(long i, long a){
        if (i == a) return 0;
        if (i < a) return 1;
        if (i > a) return -1;
        return 0;
    }

    
    
}
