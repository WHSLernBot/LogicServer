package DBBot;

import java.util.List;

/**
 *
 * @author Seve
 */
public class DeepThought {
    
    public static void berechnePrioritaet(List<aufgabenItem> aufgaben) {
        
        for(aufgabenItem item : aufgaben) {
            item.setPrio(100);
        }
        
    }
    
}
