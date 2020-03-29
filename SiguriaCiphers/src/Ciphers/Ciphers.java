package Ciphers;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Ciphers {

	private static String Keyword = new String();
	private static String Key = new String();
	private static char mat_array[][] = new char[5][5];

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
		decryptedMessage = decryptedMessage.toUpperCase();
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

	public static void BruteForceCeasar(String cipherText) {

		for (int i = 1; i <= 26; i++) {
			int key = i;
			System.out
					.println("BruteForce Message with the key at : " + key + " is = " + DecryptCeasar(cipherText, key));
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------

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
				if (j == encrypted[i] - 1) { // -1 se nuk e merr Shkronjen e pare for some reason

					plaintextArr[i] = bookArr[j]; // replace krejt shkronjat e enkriptume
					break;
				}
			}
		}
		return new String(plaintextArr);
	}

	static void DisplayBeale(int[] showArray) {
		for (int i = 0; i < showArray.length; i++) {
			System.out.print(showArray[i] + " , ");
		}
		System.out.println("\n\n");
	}

	public static String readFileAsString(String fileName) throws Exception {
		String file;
		file = new String(Files.readAllBytes(Paths.get(
				"D:\\Ylli\\Eclipse\\Siguria_Te_Dhenave_Gr20\\SiguriaCiphers\\src\\Ciphers", fileName.concat(".txt"))));
		return file;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	public static void setKey(String k) // Adds the keyword to the matrix, not allowing same letters to appear twice.
	{
		String K_fit = new String();
		boolean Next = false;
		K_fit = K_fit + k.charAt(0);
		for (int i = 1; i < k.length(); i++) {
			for (int j = 0; j < K_fit.length(); j++) {
				if (k.charAt(i) == K_fit.charAt(j)) {
					Next = true;
				}
			}
			if (Next == false)
				K_fit = K_fit + k.charAt(i);
			Next = false;
		}
		Keyword = K_fit;
	}

	public static void KeyGen() {
		boolean Next = true;
		char current;
		Key = Keyword;
		for (int i = 0; i < 26; i++) {
			current = (char) (i + 97); // Alphabet adding 'a' ASCII.
			if (current == 'j')
				continue; // Ignores letter j whenever it appears in the keyword.
			for (int j = 0; j < Keyword.length(); j++) {
				if (current == Keyword.charAt(j)) {
					Next = false;
					break;
				}
			}
			if (Next)
				Key = Key + current; // Shows how keyword applies to the alphabet without repeated letters.
			Next = true;
		}
		// System.out.println(Key);

		matrix();

	}

	private static void matrix() // Shows the created matrix.
	{
		int counter = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				mat_array[i][j] = Key.charAt(counter);
				System.out.print(mat_array[i][j] + " ");
				counter++;
			}
			System.out.println();
		}
	}

	private static String format(String plaintxt) {
		int i = 0;
		int len = 0;
		String text = new String();
		len = plaintxt.length();
		for (int tmp = 0; tmp < len; tmp++) {
			if (plaintxt.charAt(tmp) == 'j') // Replaces j with i.
			{
				text = text + 'i';
			} else
				text = text + plaintxt.charAt(tmp);
		}
		len = text.length();
		for (i = 0; i < len; i = i + 2) // If len-1, adds an x at the end of the keyword id odd.
		{
			if (text.charAt(i + 1) == text.charAt(i)) {
				text = text.substring(0, i + 1) + 'x' + text.substring(i + 1);
			}
		}
		return text;
	}

	private static String[] DivPair(String new_string) {
		String Plaintext = format(new_string);
		int size = Plaintext.length();
		if (size % 2 != 0) {
			size++;
			Plaintext = Plaintext + 'x'; // Adds an x in between when the pair is same letter.
		}
		String x[] = new String[size / 2];
		int counter = 0;
		for (int i = 0; i < size / 2; i++) {
			x[i] = Plaintext.substring(counter, counter + 2); // Divides characters into pairs.
			counter = counter + 2;
		}
		return x;
	}

	public static int[] GetDimensions(char letter) // Finds out the positions of characters a and b.
	{
		int[] key = new int[2];
		if (letter == 'j')
			letter = 'i';
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (mat_array[i][j] == letter) {
					key[0] = i;
					key[1] = j;
					break;
				}
			}
		}
		return key;
	}

	public static String encryptPlayfair(String Source) {
		String src_arr[] = DivPair(Source);
		String Ciphert = "";
		char a;
		char b;
		int row[] = new int[2];
		int col[] = new int[2];
		for (int i = 0; i < src_arr.length; i++) {
			a = src_arr[i].charAt(0);
			b = src_arr[i].charAt(1);
			row = GetDimensions(a);
			col = GetDimensions(b);

			if (row[0] == col[0]) // if the characters are on the same row, then select the two characters to the
									// right of each
			{
				if (row[1] < 4)
					row[1]++;
				else
					row[1] = 0;
				if (col[1] < 4)
					col[1]++;
				else
					col[1] = 0;
			} else if (row[1] == col[1]) // else if the characters are on the same column, then select the two
											// characters below
			{
				if (row[0] < 4)
					row[0]++;
				else
					row[0] = 0;
				if (col[0] < 4)
					col[0]++;
				else
					col[0] = 0;
			} else // else the two characters are in different rows and columns, so create a square
					// using the two points,
					// and select the two characters that create the two other points.
			{
				int temp = row[1];
				row[1] = col[1];
				col[1] = temp;
			}
			Ciphert = Ciphert + mat_array[row[0]][row[1]] + mat_array[col[0]][col[1]];
		}
		return Ciphert;
	}

	public static String decryptMessage(String Code) {
		String Plaint = new String();
		String src_arr[] = DivPair(Code);
		char a;
		char b;
		int row[] = new int[2];
		int col[] = new int[2];
		for (int i = 0; i < src_arr.length; i++) {
			a = src_arr[i].charAt(0);
			b = src_arr[i].charAt(1);
			row = GetDimensions(a);
			col = GetDimensions(b);
			if (row[0] == col[0]) // if the characters are on the same row, then select the two characters to the
									// left of each
			{
				if (row[1] > 0)
					row[1]--;
				else
					row[1] = 4;
				if (col[1] > 0)
					col[1]--;
				else
					col[1] = 4;
			} else if (row[1] == col[1]) // else if the characters are on the same column, then select the two
											// characters above
			{
				if (row[0] > 0)
					row[0]--;
				else
					row[0] = 4;
				if (col[0] > 0)
					col[0]--;
				else
					col[0] = 4;
			} else // else the two characters are in different rows and columns, so create a square
					// using the two points,
					// and select the two characters that create the two other points.
			{
				int temp = row[1];
				row[1] = col[1];
				col[1] = temp;
			}
			Plaint = Plaint + mat_array[row[0]][row[1]] + mat_array[col[0]][col[1]];
		}
		return Plaint;
	}

// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
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
				BruteForceCeasar(EncryptCeasar(text, key).toString());
				// ------------------------------------------------------------------------------------------------------------
				// DERI KETU EKZEKUTOHET CEASAR
			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}

		} else if (args[0].equalsIgnoreCase("playfair")) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

				String keyword = args[2];
				setKey(keyword);
				KeyGen();

				String key_input = args[3];
				if (key_input.length() % 2 == 0) {
					System.out.println("Encryption: " + encryptPlayfair(key_input));

				} else {
					System.out.println("Message length is not even!");
				}

			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

				String keyword = args[2];
				setKey(keyword);
				KeyGen();
				String key_input = args[3];
				if (key_input.length() % 2 == 0) {
					System.out.println("Decryption: " + decryptMessage(key_input));

				} else {
					System.out.println("Message length is not even!");
				}

			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}
			// ------------------------------------------------------------------------------------------------------------
			// DERI KETU EKZEKUTOHET PLAYFAIR

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
				String[] messageSplit = message.split(" , ");
				int[] encrypted = new int[messageSplit.length];
				for (int i = 0; i < messageSplit.length; i++) {

					encrypted[i] = Integer.parseInt(messageSplit[i]);
					System.out.println(encrypted[i]);
				}
				String book = file;

				String decrypted = DecryptBeale(encrypted, book);
				System.out.println("Decrypted Message : ");
				System.out.println(decrypted);
				System.out.println("\n\n");

			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}
			// ------------------------------------------------------------------------------------------------------------
			// DERI KETU EKZEKUTOHET BEALE

		} else {
			System.out.println("Error : Wrong Command name. Try entering it again ! ");
		}

	}

	{

	}

}