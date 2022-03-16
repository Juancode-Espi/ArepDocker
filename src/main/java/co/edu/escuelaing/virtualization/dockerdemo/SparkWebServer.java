/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.escuelaing.virtualization.dockerdemo;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.*;

import co.edu.escuelaing.virtualization.dockerdemo.Connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;


public class SparkWebServer {
    
    public static void main(String... args){
          Connection db = new Connection();
          ArrayList<String[]> result =new ArrayList<String[]>();;
          staticFileLocation("/public");
          port(getPort());
          get("/", (req,res) -> {res.redirect("/index.html");
                                return "";});    
          get("/testMongo", (req,res) ->{
            try{
                StringBuilder d = new StringBuilder();
                System.out.println(req.queryParams("string"));
                
                /*for(String[] s: db.getNames()){
                    result.add(Arrays.toString(s)); 
                }
                return result;*/
                result.clear();
                ArrayList<String[]> help =db.getNames();
                
                if(help.size()>10){
                    for(int i = help.size()-1; i > help.size()-11; i--){
                        result.add(help.get(i));
                    }
                }else{
                    for(int i=0;i<help.size();i++){
                        result.add(help.get(i));
                    }
                }
                for (String[] s: result){
                    System.out.println(Arrays.toString(s));
                    d.append("  <tr>\n" + "    <td>").append(s[0]).append("</td>\n").append("<td> ").append(s[1]).append( "</td> </tr>");
                }
                String message ="<!DOCTYPE html>\n"
                      + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>AREP</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>AREPMongo: </h1>\n"
                        + "<table>\n" +
                        "  <tr>\n" +
                        "    <th>Cadena</th>\n" +
                        "    <th>Fecha</th>\n" +
                        "  </tr>\n"
                        + d.toString()
                        + "</body>\n"
                        + "</html>\n";  

                return message;
            }catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
          }); 

        get("/testInsert",(req,res) -> {
            try {
                System.out.println(req.queryParams("string"));
                db.insertData(req.queryParams("string"));
                return "La palabra"+req.queryParams("string")+" fue añadida con exito a la base de datos";
            } catch(Exception e){
                e.printStackTrace();
                return "Error al añadir";
            }
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
    
}