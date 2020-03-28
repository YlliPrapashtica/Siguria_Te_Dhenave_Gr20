package Ciphers;

import java.util.Scanner;

public class Ciphers {

	public static StringBuffer encrypt(String text, int s) {
		StringBuffer ecnryptedText = new StringBuffer();

		for (int i = 0; i < text.length(); i++) {
			
			if (Character.isUpperCase(text.charAt(i))) {
				
				char ch = (char) (((int) text.charAt(i) + s - 65) % 26 + 65);
				ecnryptedText.append(ch);
			} 
			else {
				
				char ch = (char) (((int) text.charAt(i) + s - 97) % 26 + 97);
				ecnryptedText.append(ch);
			}
		}
		return ecnryptedText;
	}

	public static String Decrypt(String ciphertext, int key) {
		
		String decryptedMessage = "";
		char ch;
		
 
		for(int i = 0; i < ciphertext.length(); ++i){
			ch = ciphertext.charAt(i);
			
			if(ch >= 'a' && ch <= 'z'){
	            ch = (char)(ch - key);
	            
	            if(ch < 'a'){
	                ch = (char)(ch + 'z' - 'a' + 1);
	            }
	            
	            decryptedMessage += ch;
	        }
	        else if(ch >= 'A' && ch <= 'Z'){
	            ch = (char)(ch - key);
	            
	            if(ch < 'A'){
	                ch = (char)(ch + 'Z' - 'A' + 1);
	            }
	            
	            decryptedMessage += ch;
	        }
	        else {
	        	decryptedMessage += ch;
	        }
		}
		
		return( decryptedMessage);
	}
	
	public static void bruteForce(String cipherText) {
	
	
	for(int i = 1; i <= 26; i++) {
		int key = i;
		System.out.println( "BruteForce Message with the key at : " +key+" is = "+Decrypt(cipherText, key));
	}
	
	
	}
	// Driver code
	public static void main(String[] args) {
		String text = "ATTACKATONCE";
		int s = 7;
		System.out.println("Text  : " + text);
		System.out.println("Shift : " + s);
		System.out.println("Cipher: " + encrypt(text, s).toString());
		System.out.println("Decrypted Message = " + Decrypt(encrypt(text, s).toString(), s));
		System.out.println();
		bruteForce(encrypt(text, s).toString());
	}

}
	
	
	
	//char c;
//	  for(int i=0;i< allChar.length();i++)
//	  {
//	   if(allChar.charAt(i)==c)
//	    return i;
//	  }
//	  return -1;
//	 }
//	  
//	 char charAtIndex(int pos)
//	 {
//	  return allChar.charAt(pos);
//	 }
//	
//	  
//	   cipherText=cipherText.toUpperCase();
//	   
//	   for(int k=0;k< 26;k++)
//	   {
//	    String decryptedText="";
//	    int key=k;
//	    for(int i=0;i< cipherText.length();i++)
//	    {
//	     int index=b.indexOfChar(cipherText.charAt(i));
//	     
//	     if(index==-1)
//	     {
//	      decryptedText+=cipherText.charAt(i);
//	      continue;
//	     }
//	     if((index-key)>=0)
//	     {
//	      decryptedText+=b.charAtIndex(index-key);
//	     }
//	     else
//	     {
//	      decryptedText+=b.charAtIndex((index-key)+26);
//	     }
//	    }
//	     
//	    System.out.println("Decrypted Text Using key"+key+":"+decryptedText);
//	   }
//	  }
