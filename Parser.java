/*
 * Zoe Boyle
 * Final -- Programming language concepts CSC 4330
 * 12/09/2022
 * Compiler Class
 */
import java.util.ArrayList;

public class Parser {
   
   private ArrayList<Token> tokens;
   private int index;
   private ArrayList<String> declaredVariables;
   
   // Constructor takes in list of tokens
   Parser(ArrayList<Token> tokens){
      this.tokens = tokens;
      this.index = 0;
      this.declaredVariables = new ArrayList<String>();
   }
   
   private void nextToken() {
      index ++;
      if(index >= tokens.size()) {
         throw new Error("Error: } expected to end program");
      }
   }
   
   // Returns true if program starts and ends with no syntax errors
   boolean parse() {
      // If program starts with tree{
      if(tokens.get(index).getTokenCode() == 0 && 
         tokens.get(index + 1).getTokenCode() == 10) {
         // Skipping a token here
         nextToken();
         statement();
         nextToken();
         // If program ends with }
         if(tokens.get(index).getTokenCode() == 11) {
            return true;
         }
      }
      return false;
   }
   boolean typeCheck(Token val1, Token val2) {
     switch(val2.getTokenCode()) {
         case 3:
            break;
         case 4:
         
         case 5:
         case 6:
         case 7:
         case 8:
         default:
            throw new Error("Error: Invalid type");

      }
      return false;
   }   
   /* Grammar rule for statement
    * <stmt> --> <func><stmt>|<call> leaf<stmt>|<forloop><stmt>|
    *            <whileloop><stmt>|<dowhile><stmt>|<ifstmt><stmt>|
    *            <switch><stmt>|<var> leaf <stmt>|<assertion> leaf <stmt>|null
    */
   void statement() {
      nextToken();
         
      switch (tokens.get(index).getTokenCode()) {
         // Function creation
         case 1:
            function();
            break;
               
         // Function call
         case 2:
            call();
            nextToken();
            // Throw error if no line delimiter
            if(tokens.get(index).getTokenCode() != 14) {
               throw new Error("Syntax Error: Expected 'leaf'");
            }   
         break;
               
         // For loop
         case 35:
            forLoop();
            break;
            
         // While loop
         case 36:
            whileLoop();
            break;
               
         // Do while
         case 38:
            doWhile();
            break;
               
         // If statement
         case 30:
            ifStatement();
            break;
               
         // Switch case
         case 33:
            switchStatement();
            break;
         
         // Assertion
         case 3:
            assertion();
            nextToken();
            // Throw error if no line delimiter
            if(tokens.get(index).getTokenCode() != 14) {
               throw new Error("Syntax Error: expected 'leaf'");
            }
            break;
            
         // Null
         case 9:
         case 11:
         case 15:
         case 31:
         case 32:
         case 34:
         case 37:
            break;
         
         default:
            // Initialize variable
            if(type()){
               var();
               nextToken();
               // Throw error if no line delimiter
               if(tokens.get(index).getTokenCode() != 14) {
                  throw new Error("Syntax Error: expected 'leaf'");
               }
            } else if(index < tokens.size()-1) {
               throw new Error("Syntax Error: invalid start to statement");
            // Throw Error for unexpected end
            } else if(index >= tokens.size()-1) {
               throw new Error("Syntax Error: Expected '}'");
            }
            break;   
      }
      // If program has no end bracket, call statement again
      if(tokens.get(index + 1).getTokenCode() != 11) {
         statement();
      }
   }
   
   /* Grammar rule for function
    * <func> --> sapling <type> id(<param>) : <statement> <return> :
    */
   void function() {
      nextToken();
      // Throw error for no typing
      if(tokens.get(index).getTokenCode() < 20 ||
         tokens.get(index).getTokenCode() > 27) {
         throw new Error("Syntax Error: Requires type");
      } else {
         nextToken();
         // Throw errors if no id followed by opening parentheses
         if(tokens.get(index).getTokenCode() != 3) {
            throw new Error("Syntax Error: Expected function name");
         } else if(tokens.get(index + 1).getTokenCode() != 12) {
            throw new Error("Syntax Error: Expected '('");
         } else {
            // Skipping a token here
            nextToken();
            param();
            nextToken();
            // Throw error if no closing parentheses followed by colon
            if(tokens.get(index).getTokenCode() != 12) {
               throw new Error("Syntax Error: Expected ')'");
            } else if(tokens.get(index + 1).getTokenCode() != 15) {
               throw new Error("Syntax Error: Expected ':'");
            } else {
               // Skipping a token here
               nextToken();
               statement();
               returnStatement();
               nextToken();
               // Throw error if no colon after function statements
               if(tokens.get(index).getTokenCode() != 15) {
                  throw new Error("Syntax Error: Expected ':'");
               }
            }
         }
      } 
   }
   /* Grammar rule for parameter
    * <param> --> <type> id<param>|, <type> id<param>|null
    */
   void param() {
      nextToken();
      // Throw error for no typing
      if(!type()) {
         throw new Error("Syntax Error: Requires type");
      } else {  
         nextToken();
         // Throw error if theres no identifier
         if(tokens.get(index).getTokenCode() != 3) {
            throw new Error("Syntax Error: Expected identifier");
         } else {
            // Call param again if there is a comma
            if(tokens.get(index+1).getTokenCode() == 16) {
               nextToken();
               param();
            }
         }
      }
   }
   /* Grammar rule for return statement
    * <return> --> give <expr> leaf <statement><return>|null
    */
   void returnStatement() {
      nextToken();
      // Throw error for no return keyword
      if(tokens.get(index).getTokenCode() != 9) {
         throw new Error("Syntax Error: Expected 'give'");
      } else {
         expr();
         nextToken();
         // Throw error for no line delimiter
         if(tokens.get(index).getTokenCode() != 14) {
            throw new Error("Syntax Error: Expected 'leaf'");
         } else {
            statement();
            // Call a new return statement if it exists
            if(tokens.get(index + 1).getTokenCode() == 9) {
               returnStatement();
            }
         }
      }
   }
   /* Grammar rule for call
    * <call> --> planted id(<call2>)
    */
   void call() {
      nextToken();
      // Throw error if there is no id
      if(tokens.get(index).getTokenCode() != 3) {
         throw new Error("Syntax Error: No function called");
      // Throw error if there is no opening parentheses
      } else if(tokens.get(index + 1).getTokenCode() != 12) {
         throw new Error("Syntax Error: Expected '('");
      } else {
         // Skipping a token here
         nextToken();
         call2();
         nextToken();
         // Throw error if no closing parentheses
         if(tokens.get(index).getTokenCode() != 13) {
            throw new Error("Syntax Error: Expected ')'");
         } 
      }
   }
   /* Grammar rule for call 2
    * <call2> --> <term><call2>|, <term><call2>|null
    */
    void call2() {
      // Throw error if 
      term();
      nextToken();
      // Call term and again if there are more comma delimiters
      while(tokens.get(index).getTokenCode() == 16) {
         term();
      }
    }
    
   /* Grammar rule for for loop
    * <forloop> --> growfor(<var>,<boolexpr>,<expr>) : <stmt> stop
    */
   void forLoop() {
      nextToken();
      // Throw error if no opening parentheses
      if(tokens.get(index).getTokenCode() != 12) {
         throw new Error("Syntax Error: expected '('");
      } else {
         nextToken();
         // Throw error if there is no type
         if(tokens.get(index).getTokenCode() < 20 ||
            tokens.get(index).getTokenCode() > 27) {
            throw new Error("Syntax Error: Requires tpying");   
         } else {
            var();
            nextToken();
            // Throw error if there is no comma
            if(tokens.get(index).getTokenCode() != 16) {
               throw new Error("Syntax Error: Expected ','");
            } else {
               boolExpr();
               nextToken();
               // Throw error if there is no second comma
               if(tokens.get(index).getTokenCode() != 16) {
                  throw new Error("Syntax Error: Expected ','");
               } else {
                  expr();
                  // Throw error if no closing parentheses
                  if(tokens.get(index).getTokenCode() != 13) {
                     throw new Error("Syntax Error: Expected ')'");
                  // Throw error if there is no colon
                  } else if(tokens.get(index + 1).getTokenCode() != 15) {
                     throw new Error("Syntax Error: Expected ':'");
                  } else {
                     // Skipping token here
                     nextToken();
                     statement();
                     nextToken();
                     // Throw error if no 'stop'
                     if(tokens.get(index).getTokenCode() != 37) {
                        throw new Error("Syntax Error: Expected 'stop'");
                     }
                  }
               }
            }
         }
      }
   }
   /* Grammar rule for while loop
    * <whileloop> --> grow(<boolexpr>) : <stmt> stop
    */
   void whileLoop() {
      nextToken();
      // Throw error if no opening parentheses
      if(tokens.get(index).getTokenCode() != 12) {
         throw new Error("Syntax Error: expected '('");
      } else {
         nextToken();
         boolExpr();
         nextToken();
         // Throw error if no closing parentheses
         if(tokens.get(index).getTokenCode() != 13) {
            throw new Error("Syntax Error: Expected ')'");
            // Throw error if there is no colon
         } else if(tokens.get(index + 1).getTokenCode() != 15) {
            throw new Error("Syntax Error: Expected ':'");
         } else {
            // Skipping token here
            nextToken();
            statement();
            nextToken();
            // Throw error if no 'stop'
            if(tokens.get(index).getTokenCode() != 37) {
               throw new Error("Syntax Error: Expected 'stop'");
            }
         }
      }
   }
   /* Grammar rule for do while
    * <dowhile> --> root : <stmt> stop grow(<boolexpr>)
    */
   void doWhile() {
      nextToken();
      // Throw error if no colon
      if(tokens.get(index).getTokenCode() != 15) {
         throw new Error("Syntax Error: expected ':'");
      } else {
            statement();
            nextToken();
            // Throw error if no 'stop'
            if(tokens.get(index).getTokenCode() != 37) {
               throw new Error("Syntax Error: Expected 'stop'");
            } else if(tokens.get(index + 1).getTokenCode() != 12) {
               throw new Error("Syntax Error: expected '('");
            } else {
               // Skipping token here
               nextToken();
               boolExpr();
               nextToken();
               // Throw error if no closing parentheses
               if(tokens.get(index).getTokenCode() != 13) {
                  throw new Error("Syntax Error: Expected ')'");
               }
            }
         }
      }
   
   /* Grammar rule for if statement
    * <ifstmt> --> apple(<boolexpr>) : <stmt> <elsestmt> eat
    */
   void ifStatement() {
      nextToken();
      // Throw error if no colon
      if(tokens.get(index).getTokenCode() != 15) {
         throw new Error("Syntax Error: expected ':'");
      } else {
         statement();
         // Go to else statement if there is an else clause
         if(tokens.get(index).getTokenCode() == 31) {
            elseStatement();
         }
         // Throw error if no 'eat'
         if(tokens.get(index).getTokenCode() != 32) {
            throw new Error("Syntax Error: Expected 'eat'");
         }
      }
   }
   /* Grammar rule for else statement
    * <elsestmt> --> orange : <stmt>|null
    */
   void elseStatement() {
      nextToken();
      // Throw error if no colon
      if(tokens.get(index).getTokenCode() != 15) {
         throw new Error("Syntax Error: expected ':'");
      } else {
            statement();
      }
   }
   
   /* Grammar rule for switch statement
    * <switch> --> climb(id) : <case> stop
    */
   void switchStatement() {
      nextToken();
      // Throw error if no opening parentheses
      if(tokens.get(index).getTokenCode() != 12) {
         throw new Error("Syntax Error: expected '('");
      } else {
         nextToken();
         // Throw error if no id
         if(tokens.get(index).getTokenCode() != 3) {
            throw new Error("Syntax Error: expected identifiers");
         } else {
            nextToken();
         }
      }
      // Throw error if no closing parentheses
      if(tokens.get(index).getTokenCode() != 13) {
         throw new Error("Syntax Error: expected ')'");
      } else if(tokens.get(index + 1).getTokenCode() != 15 ) {
         throw new Error("Syntax Error: expected ':'");
      } else {
         // Skipping two tokens here
         nextToken();
         nextToken();
         // Throw Error if no branch
         if(tokens.get(index).getTokenCode() == 34) {
            throw new Error("Syntax Error: Requires a case");
         } else {
            caseStatement();
         }
         // Throw error if no 'stop'
         if(tokens.get(index).getTokenCode() != 37) {
            throw new Error("Syntax Error: Expected 'stop'");
         }
      }
   }
   /* Grammar rule for a case
    * <case> --> branch val : <stmt> <case>|null
    */
   void caseStatement() {
      nextToken();
      // Throw error for no evaluation value
      if(tokens.get(index).getTokenCode() < 4 ||
         tokens.get(index).getTokenCode() > 8) {   
          throw new Error("Syntax Error: Expected a value");
      // Throw error for no colon
      } else if(tokens.get(index+1).getTokenCode() != 15) {
          throw new Error("Syntax Error: Expected ':'");
      } else {
         // Skipping a token here
         nextToken();
         statement();
         // Call case again if there is another branch
         if(tokens.get(index).getTokenCode() == 34) {
            caseStatement();
         }
      }
   }
   
   /* Grammar rule for variable
    * <var> --> <type> id <declare>
    */
   void var() {
      nextToken();
      // Throw error if no identifier
      if(tokens.get(index).getTokenCode() != 3) {
         throw new Error("Syntax Error: Requires identifier");
      } else {
         declare();
      }
   }
   /* Grammar rule for type
    * <type> --> seed|stick|twig|trunk|petal|flower|planted|sap
    */
   boolean type() {
      // True if token is a type
      if(tokens.get(index).getTokenCode() >= 20 &&
         tokens.get(index).getTokenCode() <= 27) {
         return true;   
      }
      return false;
   }
   /* Grammar rule for declaration
    * <declare> --> = <expr>|null
    */
   void declare() {
      if(tokens.get(index + 1).getTokenCode() == 56) {
          // Skipping a token here
          nextToken();
          expr(); 
      }
   }
   
   /* Grammar rule for assertion
    * <assertion> --> id = <expr>
    */
   void assertion() {
      nextToken();
      // Throw error if no '='
      if(tokens.get(index).getTokenCode() != 56) {
         throw new Error("Syntax Error: Expected '='");
      } else {
         expr();
      }
   }

   /* Grammar rule for boolean expression
    * <boolexpr> --> <expr> <op> <expr> | <unary> <expr>
    */
   void boolExpr() {
      // Unary expression
      if(unary()) {
         // Skipping token here
         nextToken();
         expr();
      } else {
         expr();
         nextToken();
         // Throw error if there is no boolean operator
         if(!boolOp()) {
            throw new Error("Syntax error: Expected operator");
         } else {
            expr();
            nextToken();
         }
      }
   }
   /* Grammar rule for unary operator
    * <unary> --> !!!|null
    */
   boolean unary() {
      if(tokens.get(index + 1).getTokenCode() == 46) {
         return true;
      }
      return false;
   }
   /* Grammar rule for boolean operator
    * <op> --> >>|>>>=|<<|<<<=|===|!!!=
    */
   boolean boolOp() {
      if(tokens.get(index).getTokenCode() < 40 ||
         tokens.get(index).getTokenCode() > 48) {
         return true;
      }
      return false;
   }
    
   /* Grammar rule for expression
    * <expr> --> (<expr>)|<term>|<binexpr>
    */
   void expr() {
      nextToken();
      // If there is an open parentheses call expression again
      if(tokens.get(index).getTokenCode() == 37) {
         expr();
         nextToken();
         // Throw error if there is no closing parentheses
         if(tokens.get(index).getTokenCode() != 13) {
            throw new Error("Syntax Error: Expected ')'"); 
         }
      } else {
         term();
         factor();
      }       
   }
   /* Grammar rule for op
    * <factor> --> + <expr>|- <expr>|* <expr>|/ <expr>|% <expr>|*^ <expr>|null
    */
   void factor() {
      nextToken();
      switch(tokens.get(index).getTokenCode()) {
         // If token indicates exponent
         case 50:
            expr();
            break;
      
         // If token indicates division, modulo, or multiplication   
         case 51:
         case 52:  
         case 53:
            expr();
            break;
         // If token indicates addition or subtraction
         case 55:
         case 54:
            expr();
            break;
            
         default:
            throw new Error("Syntax Error: Invalid operator");
      }
   }
   /* Grammar rule for term
    * <term> --> id|val|<call>
    */
   void term() {
      nextToken();
      // Throw error if no identifier, value, or function call
      if(tokens.get(index).getTokenCode() < 2 ||
         tokens.get(index).getTokenCode() > 8) {
         throw new Error("Syntax Error: Invalid term");
      } 
   }
}