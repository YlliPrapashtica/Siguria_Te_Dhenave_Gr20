package Ciphers;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Ciphers {

	public static String EncryptCeasar(String text, int key) {
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

	public static String DecryptCeasar(String ciphertext, int key) {

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
			if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch - key);

				if (ch < 'A') {
					ch = (char) (ch + 'Z' - 'A' + 1);
				}

				decryptedMessage += ch;
			} else {
				decryptedMessage += ch;
		}

		
		}}
			return (decryptedMessage);
	}

	public static void BruteForceCeasar(String cipherText) {

		for (int i = 1; i <= 26; i++) {
			int key = i;
			System.out.println("BruteForce Message with the key at : " + key + " is = " + DecryptCeasar(cipherText, key));
		}
	}

	static int[] EncryptBeale(String message, String book) {
		int[] output = new int[message.length()];
		char[] bookArr = book.toUpperCase().toCharArray();

		for (int i = 0; i < message.length(); i++) {
			for (int j = 0; j < bookArr.length; j++) {

				if (bookArr[j] == message.charAt(i)) {
					output[i] = j;
					break;
				}
			}
		}
		return output;
	}

	static String DecryptBeale(int[] encrypted, String book) {
		char[] plaintextArr = new char[encrypted.length];
		char[] bookArr = book.toCharArray();

		for (int i = 0; i < encrypted.length; i++) {
			for (int j = 0; j < bookArr.length; j++) {
				if (j == encrypted[i]) {
					plaintextArr[i] = bookArr[j];
					break;
				}
			}
		}
		return new String(plaintextArr);
	}

	static void DisplayBeale(int[] showArray) {
		for (int i = 0; i < showArray.length; i++) {
			System.out.print(showArray[i] + 1 + " , ");
		}
		System.out.println("\n\n");
	}

	public static String readFileAsString(String fileName) throws Exception {
		String file;
		file = new String(Files.readAllBytes(Paths.get(
				"D:\\Ylli\\Eclipse\\Siguria_Te_Dhenave_Gr20\\SiguriaCiphers\\src\\Ciphers", fileName.concat(".txt"))));
		return file;
	}

	public static void main(String[] args) throws Exception {

		if (args[0].equalsIgnoreCase("ceasar")) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

				String text = args[2];
				int key = Integer.parseInt(args[3]);

				System.out.println("Plain text is : " + text);
				System.out.println("Key is : " + key);
				System.out.println("Cipher text is : " + EncryptCeasar(text, key).toString());
				System.out.println("\n\n");
				BruteForceCeasar(EncryptCeasar(text, key).toString());
			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

				String text = args[2];
				int key = Integer.parseInt(args[3]);
				System.out.println("Decrypted Message = " + DecryptCeasar(text, key));
				System.out.println("\n");
				BruteForceCeasar(EncryptCeasar(text, key).toString()); // DERI KETU EKZEKUTOHET CEASAR
			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}

		} else if (args[0].equalsIgnoreCase("playfair")) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			} // DERI KETU EKZEKUTOHET PLAYFAIR

		} else if (args[0].equalsIgnoreCase("beale") || (args[0].equalsIgnoreCase("book"))) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

				String file = readFileAsString(args[2]);
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");
				System.out.println("\n" + file + "\n");

				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");

				String message = args[3];
				String book = file;

				int[] encrypted = EncryptBeale(message.toUpperCase(), book);
				System.out.println("Encrypted Message :");
				DisplayBeale(encrypted);

			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

				String file = readFileAsString(args[2]);
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");
				System.out.println("\n" + file + "\n");

				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------");

				String message = args[3];
				String book = file;
				int[] encrypted = EncryptBeale(message.toUpperCase(), book);
				String decrypted = DecryptBeale(encrypted, book);
				System.out.println("Decrypted Message : ");
				System.out.println(decrypted);
				System.out.println("\n\n");

			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			} // DERI KETU EKZEKUTOHET BEALE

		} else {
			System.out.println("Error : Wrong Command name. Try entering it again ! ");
		}

	}

}