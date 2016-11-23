
import static spark.Spark.*;

import static spark.Spark.get;

public class Main {
    
    

  public static void main(String[] args) {
      
 
    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

  
    get("/test", (req, res) -> {
        
        return "Sampletext Test";
        
    }, responseTransformer.JsonUtil.json());
    
    

    get("/", (req, res) -> {
            
            res.status(400);
            return "No access: Contact whslernbot@gmail.com";
        });
    
  }
    

}
