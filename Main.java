public class Main {
   public static void main(String [] args){
      String file = ("test1.txt");
      Compiler compiler = new Compiler(file);
      System.out.print(compiler.parser.parse());
   }
}
