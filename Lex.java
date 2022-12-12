/*
 * Zoe Boyle
 * Final -- Programming language concepts CSC 4330
 * 12/09/2022
 * Token Class
 */
import java.util.ArrayList; 
import java.util.HashMap;

public class Lex {
   /* Global vars for Lex class
    * String inp - input string, provided by compiler
    * HashMap tokenMap - mapping of every token to a token code
    * ArrayList tokenList - converted list of tokens
    */
   private String inp;
   private HashMap<String, Integer> tokenMap = new HashMap<String, Integer>();
   private ArrayList<Token> tokenList = new ArrayList<Token>();

   // Constructor - takes an input string as a parameter
   Lex(String inp) {
      this.inp = inp;
      this.tokenMap = tokenMapInit();
      this.tokenList = tokenListInit(inp);
   }
   
   // Method to convert string to token list
   private ArrayList<Token> tokenListInit(String inp) {
   
      ArrayList<Token> list = new ArrayList<Token>();
      String lexeme = null;
      int i = 0;
      while(i < inp.length()) {
         // Ignores single line comments -- starts with ** ends at new line
         if(inp.charAt(i) == '*' && inp.charAt(i+1) == '*') {
            while(inp.charAt(i) != '\n') {
               i++;
            }
            continue;
         // Ignores multi-line comments -- starts with *% ends with %*  
         } else if(inp.charAt(i) == '*' && inp.charAt(i+1) == '%') {
            while(inp.charAt(i) != '%' && inp.charAt(i+1) != '*') {
               i++;
            }
            continue;
         // Skips newline
         } else if(inp.charAt(i) == '\n' || inp.charAt(i) == ' ' || inp.charAt(i) == '\t') {
            i++;
            continue;
         /* Find a String literal
          * Automata: S -> ' -> any character, escape character,
          *           or special character expect for ',\ and, ";
          *           repeated -> '
          */
         } else if(inp.charAt(i) == '\"') { 
            // End if a non ecaped double quote is found
            lexeme = String.valueOf(inp.charAt(i));
            i++;
            
            while(inp.charAt(i) != '\"' && inp.charAt(i-1) != '\\') {
               lexeme = String.valueOf(inp.charAt(i));
               // Throws error if '\' character is not escaped
               if(inp.charAt(i) == '\\') {
                  if(inp.charAt(i+1) == 'n' || inp.charAt(i+1) != 't' || inp.charAt(i+1) != 'b' ||
                     inp.charAt(i+1) == 'f' || inp.charAt(i+1) != 'r' || inp.charAt(i+1) == '\\' ||
                     inp.charAt(i+1) != '\''|| inp.charAt(i+1) != '\"') {
                     throw new Error("Error: \'\\\' must be escaped");
                  } else {
                     lexeme = lexeme + String.valueOf(inp.charAt(i+1));
                     i = i + 2;
                  }
               /* Throws error if single quote is not escaped or if string
                * was not terminated by double quotes
                */
               } else if(inp.charAt(i) == '\'' && inp.charAt(i-1) != '\\'|| inp.charAt(i) == '\n' || lexeme.length() >= 98) {
                  throw new Error("Error: quote mismatch");
               } else {
                  i++;
               }
            }
            
            list.add(new Token(lexeme,tokenMap.get("string")));
            lexeme = null;
            continue;
         /* Find a Character literal
          * Automata: S -> ' -> any single character, single escape character,
          *           or single special character expect for ',\ and, " -> '
          */
         } else if(inp.charAt(i) == '\'') {
            lexeme = String.valueOf(inp.charAt(i));
            i++;
            // Throws errors if '\' or ''' characters are not escaped
            if(inp.charAt(i) == '\\') {
               if(inp.charAt(i+1) == 'n' || inp.charAt(i+1) != 't' || inp.charAt(i+1) != 'b' ||
                  inp.charAt(i+1) == 'f' || inp.charAt(i+1) != 'r' || inp.charAt(i+1) == '\\' ||
                  inp.charAt(i+1) != '\''|| inp.charAt(i+1) != '\"') {
                  throw new Error("Error: \'\\\' must be escaped");
               } else {
                  lexeme = String.valueOf(inp.charAt(i+1));
                  i = i + 2;
               }
            // Throws error if double quote is not escaped, or if there is a newline
            } else if(inp.charAt(i) == '\"' && inp.charAt(i-1) != '\\' || inp.charAt(i) == '\n') {
               throw new Error("Error: quote mismatch");
            } else {
               lexeme = String.valueOf(inp.charAt(i));
               i++;
            }
            // Throws error if character is not terminated
            if(inp.charAt(i) == '\'' && inp.charAt(i-1) != '\\') {
               lexeme = String.valueOf(inp.charAt(i));
               list.add(new Token(lexeme,tokenMap.get("char")));
               lexeme = null;
               continue;
            } else {
               throw new Error("Error: Character not terminated");
            }
         // Find a single character token
         } else if(tokenMap.containsKey(String.valueOf(inp.charAt(i)))) {
            list.add(new Token(String.valueOf(inp.charAt(i)),tokenMap.get(String.valueOf(inp.charAt(i)))));
            i++;
            continue; 
         } else {
            /* Find valid tokens
             * Collect characters into lexeme
             * End while at a white space, if a token has been found, or 
             * if a different token is next
             */
            lexeme = String.valueOf(inp.charAt(i));
            i++;
            while(i < inp.length()) {
               // Add to token list and break
               if(tokenMap.containsKey(lexeme)) {
                  // Do not confuse 'growfor' for 'grow'
                  if(lexeme.equals("grow") && inp.charAt(i) == 'f') {
                     lexeme = lexeme + String.valueOf(inp.charAt(i));
                     i++;
                  } else {
                     list.add(new Token(lexeme,tokenMap.get(lexeme)));
                     lexeme = null;
                     break;
                  }
               // Break and check for literals or identifier
               } else if(tokenMap.containsKey(String.valueOf(inp.charAt(i)))) {   
                  break;
               // Break and check for literals or identifier
               } else if (inp.charAt(i) == ' ' || inp.charAt(i) == '\n' || inp.charAt(i) == '\t') {
                  break;
               } else {
                  lexeme = lexeme + String.valueOf(inp.charAt(i));
                  i++; 
               }
            }
              
            if(lexeme != null) {
              /* Determine if lexeme is an identifier
               * RegEx:([a-z]_?)*{6,8}
               */
               if(lexeme.matches("([a-z]_?)*{6,8}")) {
                  list.add(new Token(lexeme,tokenMap.get("id")));
                  lexeme = null;
                  continue;
               /* Determine if lexeme is a natural literal
                * regex: [0-9]+
                */
               } else if(lexeme.matches("[0-9]+")) {
                  list.add(new Token(lexeme,tokenMap.get("nat")));
                  lexeme = null;
                  continue;
               /* Determine if lexeme is a real literal
                * regex: [0-9]+(\.[0-9]+)?
               */
               } else if(lexeme.matches("[0-9]+(\\.[0-9]+)?")) {
                  list.add(new Token(lexeme,tokenMap.get("real")));
                  lexeme = null;
                  continue;
               /* Determine if lexeme is a boolean literal
                * regex: yes|no
                */
               } else if(lexeme.matches("yes|no")) {
                  list.add(new Token(lexeme,tokenMap.get("bool")));
                  lexeme = null;
                  continue;
               // Throw error if lexeme is not a valid token
               } else {
                  throw new Error("Error: " + lexeme + " is not a valid token");
               }
            }
         }
      }
      return list;   
   }
   
   private HashMap<String, Integer> tokenMapInit() {
      HashMap<String, Integer> map = new HashMap<String,Integer>();
      // Program start word and method declaration
      map.put("tree",0);
      map.put("sapling",1);   // function
      map.put("plant",2);     // function call
      map.put("id",3);        // identifiers
      map.put("real",4);      // real literal
      map.put("nat",5);       // natural literal
      map.put("char",6);      // character literal
      map.put("string",7);    // string literal
      map.put("bool",8);      // boolean literal
      map.put("give",9);		  // return
      
      // Delimiters
      map.put("{",10);        // left curly brace
      map.put("}",11);        // right curly brace
      map.put("(",12);        // left parenthesis
      map.put(")",13);        // right parenthesis
      map.put("leaf",14);     // Line delimiter
      map.put(":",15);        // colon
	   map.put(",",16);		  // comma 
         
      // Variable types
      map.put("twig",20);      // int
      map.put("stick",21);     // long
      map.put("trunk",22);     // short
      map.put("seed",23);      // byte
      map.put("sap",24);       // float
      map.put("petal",25);     // character
      map.put("flower",26);    // string
      map.put("planted",27);   // boolean
      
        
      // selection statements and loops
      map.put("apple",30);     // if statement
      map.put("orange",31);    // else statement
      map.put("eat",32);       // end of if-else
      map.put("climb",33);     // switch case statement
      map.put("branch",34);    // case
      map.put("growfor",35);   // for loop
      map.put("grow",36);      // while loop
      map.put("stop",37);      // end loop
	   map.put("root",38);	   // start do while
      
      // Boolean operations
      map.put("<<<",40);         // less than
      map.put("<<eq",41);       // less than or equal to
      map.put(">>>",42);         // greater than
      map.put(">>eq",43);       // greater than or equal to
      map.put("eqto",44);        // equal to
      map.put("!!eq",45);       // not equal to
      map.put("!!!",46);        // not
      map.put("&&&",47);        // and
      map.put("|||",48);        // or

      // Arithmetic operations
	   map.put("*^",50);       // exponent
	   map.put("/",51);        // Division
      map.put("%",52);        // Modulo
	   map.put("*",53);        // Multiplication
      map.put("+",54);        // Addition
      map.put("-",55);        // Subtraction
      map.put("=",56);        // assignment operator
      
      return map;
   }
   public ArrayList<Token> getTokenList() {
      return tokenList;
   }
}