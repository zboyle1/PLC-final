/*
 * Zoe Boyle
 * Final -- Programming language concepts CSC 4330
 * 12/09/2022
 * Compiler Class
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Compiler {
   // Variables; file name and instance of lex class
   private String fileName;
   private String inp;
   private Lex lex;
   Parser parser;
   
   // Constructor
   Compiler(String fileName) {
      this.fileName = fileName;
      try {
         this.inp = readFile(fileName);
      } catch(Exception e) {
         System.out.println(e.getMessage());
      }
      this.lex = new Lex(inp);
      this.parser = new Parser(lex.getTokenList());
   }
   
   // Convert input file into single string
   public static String readFile (String fileName) throws FileNotFoundException {   
      
      // Variables; file to be parsed and a string to hold file contents
      File file = new File(fileName);
      String input;
      
      // Error if file is not found
      if(!file.exists()) {
         throw new FileNotFoundException("File was not found");
      } else {
         // Parse file
         Scanner sc = new Scanner(file);
         input = sc.nextLine();
         while(sc.hasNextLine()) {
               input = input + " \n ";
               input = input + sc.nextLine();
         }
      }
      return input;
   } 
}