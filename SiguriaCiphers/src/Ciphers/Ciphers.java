package Ciphers;

public class Ciphers {

	public static String encrypt(String text, int key) {
		StringBuffer ecnryptedText = new StringBuffer();

		for (int i = 0; i < text.length(); i++) {

			if (Character.isUpperCase(text.charAt(i))) {

				char ch = (char) (((int) text.charAt(i) + key - 65) % 26 + 65);
				ecnryptedText.append(ch);
			} else {

				char ch = (char) (((int) text.charAt(i) + key - 97) % 26 + 97);
				ecnryptedText.append(ch);
			}
		}
		return ecnryptedText.toString();
	}

	public static String Decrypt(String ciphertext, int key) {

		String decryptedMessage = "";
		char ch;

		for (int i = 0; i < ciphertext.length(); ++i) {
			ch = ciphertext.charAt(i);

			if (ch >= 'a' && ch <= 'z') {
				ch = (char) (ch - key);

				if (ch < 'a') {
					ch = (char) (ch + 'z' - 'a' + 1);
				}

				decryptedMessage += ch;
			} else if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch - key);

				if (ch < 'A') {
					ch = (char) (ch + 'Z' - 'A' + 1);
				}

				decryptedMessage += ch;
			} else {
				decryptedMessage += ch;
			}
		}

		return (decryptedMessage);
	}

	public static void bruteForce(String cipherText) {

		for (int i = 1; i <= 26; i++) {
			int key = i;
			System.out.println("BruteForce Message with the key at : " + key + " is = " + Decrypt(cipherText, key));
		}
	}

	
	public static void main(String[] args) {

		String text = args[2];
		int key = Integer.parseInt(args[3]);
		System.out.println("Text  : " + text);
		System.out.println("Shift : " + key);
		System.out.println("Cipher: " + encrypt(text, key).toString());
		System.out.println("Decrypted Message = " + Decrypt(encrypt(text, key).toString(), key));
		System.out.println();
		bruteForce(encrypt(text, key).toString());
	}

}