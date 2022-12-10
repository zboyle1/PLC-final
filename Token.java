/*
 * Zoe Boyle
 * Final -- Programming language concepts CSC 4330
 * 12/09/2022
 * Token Class
 */
 
public class Token {

   // Values for lexeme and token code
   private String tokenName;
   private int tokenCode;
   
   // Constructor
   Token(String tokenName, int tokenCode) {
      this.tokenName = tokenName;
      this.tokenCode = tokenCode;
   }
   
   // Getter methods
   public String getTokenName() {
      return tokenName;
   }
   public int getTokenCode() {
      return tokenCode;
   }
   public String toString() {
      return "(" + tokenName + ", " + tokenCode + ")";
   }
}