package Ciphers;

public class Ciphers {

	public static StringBuffer encrypt(String text, int s)
    { 
        StringBuffer ecnryptedText= new StringBuffer(); 
  
        for (int i=0; i<text.length(); i++) 
        { 
            if (Character.isUpperCase(text.charAt(i))) 
            { 
                char ch = (char)(((int)text.charAt(i) + 
                                  s - 65) % 26 + 65); 
                ecnryptedText.append(ch); 
            } 
            else
            { 
                char ch = (char)(((int)text.charAt(i) + 
                                  s - 97) % 26 + 97); 
                ecnryptedText.append(ch); 
            } 
        } 
        return ecnryptedText; 
    } 
  
    // Driver code 
    public static void main(String[] args) 
    { 
        String text = "ATTACKATONCE"; 
        int s = 7; 
        System.out.println("Text  : " + text); 
        System.out.println("Shift : " + s); 
        System.out.println("Cipher: " + encrypt(text, s)); 
    } 
}