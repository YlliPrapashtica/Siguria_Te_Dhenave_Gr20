package Ciphers;

import java.nio.file.Files;
import java.nio.file.Paths;

public class BookCypher{

	public static void main(String[] args) throws Exception {
		String file = readFileAsString(args[3]);
		System.out.println(file);

		String message = args[4];
		String book = file;

		int[] encrypted = Encrypt(message.toUpperCase(), book);
		System.out.println("Encrypted Message :");
		Display(encrypted);

		String decrypted = Decrypt(encrypted, book);
		System.out.println("Decrypted Message : ");
		System.out.println(decrypted);
	}

	static int[] Encrypt(String message, String book) {
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

	static String Decrypt(int[] encrypted, String book) {
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

	static void Display (int[] showArray)
	{
		for (int i = 0; i < showArray.length; i++) {
			System.out.print(showArray[i]+1 + " , ");
		}
		System.out.println();
	}
	public static String readFileAsString(String fileName) throws Exception {
		String file;
		file = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), fileName)));
		return file;
	}
}
