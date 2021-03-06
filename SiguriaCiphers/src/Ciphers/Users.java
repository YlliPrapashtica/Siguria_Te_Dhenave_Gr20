package com.createuser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.security.InvalidKeyException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class Users {
	private static String Keyword = new String();
	private static String Key = new String();
	private static char mat_array[][] = new char[5][5];

	private static String EncryptCeasar(String text, int key) {
		
		
		StringBuffer ecnryptedText = new StringBuffer();

		for (int i = 0; i < text.length(); i++) {

			if (Character.isUpperCase(text.charAt(i))) {	// if upper case  +A

				char ch = (char) (((int) text.charAt(i) + key - 65) % 26 + 65);
				ecnryptedText.append(ch);												
			} else {																	// if lower case +a 

				char ch = (char) (((int) text.charAt(i) + key - 97) % 26 + 97);
				ecnryptedText.append(ch);
			}
		}
		return ecnryptedText.toString();
	}

	private static String DecryptCeasar(String ciphertext, int key) {

		String decryptedMessage = "";				
		char ch;

		for (int i = 0; i < ciphertext.length(); ++i) {
			ch = ciphertext.charAt(i);

			if (ch >= 'a' && ch <= 'z') {	//if lower case heke key
				ch = (char) (ch - key);

				if (ch < 'a') {
					ch = (char) (ch + 'z' - 'a' + 1);
				}

				decryptedMessage += ch;
			} else if (ch >= 'A' && ch <= 'Z') {	// if upper case heke key
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

	private static void BruteForceCeasar(String cipherText) {

		for (int i = 1; i <= 26; i++) {
			int key = i;
			System.out.println("BruteForce Message with the key at : " + key + " is = " + DecryptCeasar(cipherText, key)); // provon te gjitha qelsat ne decrypt
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	static int[] EncryptBeale(String message, String book) {
		int[] output = new int[message.length()];	// ruhet output qe del
		char[] bookArr = book.toUpperCase().toCharArray();	// kthen book.txt ne karaktere dhe Uppercase

		for (int i = 0; i < message.length(); i++) {	//krejt shkronjat e tekstit krahasohen dhe zevendsohen me pozitat ne liber
			for (int j = 0; j < bookArr.length; j++) {

				if (bookArr[j] == message.charAt(i)) {
					output[i] = j;												// e merr vleren output-i
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
				if (j == encrypted[i] - 1) { // -1 se nuk e merr Shkronjen e pare pasi qe del enkriptimi +1 ne console per bukurshkrim.

					plaintextArr[i] = bookArr[j]; // replace krejt shkronjat e enkriptume
					break;
				}
			}
		}
		return new String(plaintextArr);    //i bon krejt chars bashke ne nje string
	}

	static void DisplayBeale(int[] showArray) {
		for (int i = 0; i < showArray.length; i++) {
			System.out.print(showArray[i] + 1 + " , ");	// printon ne menyre me te mire pjesen e enkriptuar e jo new line pas qdo numer
		}
		System.out.println("\n\n");
	}

	public static String readFileAsString(String fileName) throws Exception {
		String file;
		String currentPath = System.getProperty("user.dir").toString();    //  pasi qe ndrron Directory zakonisht prej pc ne pc, e merr ate aktualen
		file = new String(Files.readAllBytes(Paths.get(currentPath, fileName.concat(".txt"))));   // nuk ka nevoje me e shenu book.txt, por vetem book
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
		for (i = 0; i < len-1; i = i + 2)
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

	private static int[] GetDimensions(char letter) // Finds out the positions of characters a and b.
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

	private static String encryptPlayfair(String Source) {
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
			} else if (row[1] == col[1]) // else if the characters are on the same column, then select the two characters below
											
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

	private static String decryptMessage(String Code) {
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


	
	protected final static Logger LOGGER = Logger.getLogger(Users.class);

	public static final int KEY_SIZE = 1024;
	private static byte[] DESKey;
	private static SecretKey DESSecretKey;
	private static byte[] rndIV;

	public static void main(String[] args) throws Exception {
		
		if (args[0].equalsIgnoreCase("ceasar")) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

				String text = args[2];
				int key = Integer.parseInt(args[3]);

				System.out.println("Plain text is : " + text);
				System.out.println("Key is : " + key);
				System.out.println("Cipher text is : " + EncryptCeasar(text, key).toString());
				System.out.println("\n\n");
			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

				String text = args[2];
				int key = Integer.parseInt(args[3]);
				System.out.println("Decrypted Message = " + DecryptCeasar(text, key));
				System.out.println("\n");
				BruteForceCeasar(EncryptCeasar(text, key).toString());
			
			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}	
			// ------------------------------------------------------------------------------------------------------------
				// DERI KETU EKZEKUTOHET CEASAR

		} else if (args[0].equalsIgnoreCase("playfair")) {
			if (args[1].equalsIgnoreCase("e") || (args[1].equalsIgnoreCase("encrypt"))) {

				String keyword = args[2];
				setKey(keyword);
				KeyGen();

				String key_input = args[3];

				System.out.println("Encryption: " + encryptPlayfair(key_input));

			} else if (args[1].equalsIgnoreCase("d") || (args[1].equalsIgnoreCase("decrypt"))) {

				String keyword = args[2];
				setKey(keyword);
				KeyGen();
				String key_input = args[3];

				System.out.println("Decryption: " + decryptMessage(key_input));

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

				String message = args[3];												//
				String[] messageSplit = message.split(" , ");	// Pasi qe mesazhi nga cmd na vjen si String, neve na duhet te jete nje
				int[] encrypted = new int[messageSplit.length];	 // int array per t'iu pershtatur kodit. Pra ketu vjen si String dhe
				for (int i = 0; i < messageSplit.length; i++) {	 //  ndahet ne String array me delimiter ' , ' dhe iu shtohet
																						// int array qe na duhet.
					encrypted[i] = Integer.parseInt(messageSplit[i]);
					//	Per me parse nga String ne int pra nevojitet ky kod.
				}
				String book = file;

				String decrypted = DecryptBeale(encrypted, book);
				System.out.println("Decrypted Message : ");
				System.out.println(decrypted);
				System.out.println("\n\n");

			} else {
				System.out.println("Error : Wrong Command name. Try entering it again ! ");
			}

		}

			if(args[0].equals("create-user")) {
				if (args[1].matches("[a-zA-Z0-9_]*"))
				createUser(args[1]);
				else {
					System.out.println("Name should only contain a-z, A-Z, 0-9 or _");
				}
				
		}
				else if(args[0].equals("login")){ 
				login(args[1]);
				
			} 
				else if(args[0].equals("delete-user")) {
				deleteUser(args[1]);
				
			} else if(args[0].equals("export-key")) {
				if(args.length==4 ) {
					exportKey(args[1], args[2],args[3]);
					
				}else {
					printKey(args[1],args[2]);
				}
				
			} else if(args[0].equals("import-key")) {
				importKey(args[1], args[2]);
				
				
			}else if(args[0].equals("status")) {
				if(args.length==3 ) {
					verifyToken(args[1], args[2]);
				}else {
					verifyToken(args[1]);
				}
			}
			else if(args[0].equals("write-message")) {
				if(args.length==4) {
					if(args[3].contains(".")) {
						writeMessageURL(args[1], args[2],args[3]);
					}else{
						String encryptedText = writeMessage(args[1], args[2],args[3]);
						System.out.println("The encrypted text with token is: \n"+encryptedText);
						}
					}
				else if(args.length==5){
					writeMessage(args[1], args[2],args[3],args[4]);
					}
				else {
				String encryptedText = writeMessage(args[1], args[2]);
				System.out.println("The encrypted text is: "+encryptedText);
				}}
			 else if(args[0].equals("read-message")) {			
					decryptDes(args[1]);
			}else {
				System.out.println("Wrong command name! ");
			}
	}	

	private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(KEY_SIZE);
		KeyPair keyPair = generator.generateKeyPair();
		return keyPair;
	}
	
	private static void writeMessageURL(String name, String message, String url)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
		
		File file = new File(url+name+".encrypted.txt");
		
		FileWriter myWriter = new FileWriter(file);
	    myWriter.write(base64Name(name) + " . " + base64IV() + " . " + encryptRSA(Users.rndIV, getpubKeyFromFile(name))
		+ " . " + encryptDes(name, message));
	    myWriter.close();
	    System.out.println("Successfully created the encrypted text file.");
	}

	private static String writeMessage(String name, String message)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
		
		
		return (base64Name(name) + " . " + base64IV() + " . " + encryptRSA(Users.rndIV, getpubKeyFromFile(name))
				+ " . " + encryptDes(name, message));
	}

	private static String writeMessage(String name, String message, String sender)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
	
			RSAPublicKey publicKey = (RSAPublicKey) getpubKeyFromFile(sender);
			RSAPrivateKey privateKey = getprivKeyFromFile(sender);
			try {
			    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			    JWTVerifier verifier = JWT.require(algorithm)
			        .withIssuer(sender).build(); //Reusable verifier instance
			    DecodedJWT jwt = verifier.verify(readToken(sender));
			    Signature sign = Signature.getInstance("SHA256withRSA");
			      sign.initSign(privateKey);
			      byte[] bytes = message.getBytes();
			      sign.update(bytes);
			      byte[] signature = sign.sign();
			    String user = jwt.getIssuer();
			    return (base64Name(name) + " . " + base64IV() + " . " + encryptRSA(Users.rndIV, getpubKeyFromFile(name))
				+ " . " + encryptDes(name, message) + " . " + base64Name(user) + " . " + base64Signature(signature));
			} catch (JWTVerificationException exception){
			    System.out.println("Token not valid!");
			}
		return null;
	}
	
	private static String writeMessage(String name, String message, String sender, String url)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
	
			RSAPublicKey publicKey = (RSAPublicKey) getpubKeyFromFile(sender);
			RSAPrivateKey privateKey = getprivKeyFromFile(sender);
			try {
			    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			    JWTVerifier verifier = JWT.require(algorithm)
			        .withIssuer(sender).build(); //Reusable verifier instance
			    DecodedJWT jwt = verifier.verify(readToken(sender));
			    Signature sign = Signature.getInstance("SHA256withRSA");
			      sign.initSign(privateKey);
			      byte[] bytes = message.getBytes();
			      sign.update(bytes);
			      byte[] signature = sign.sign();
			    String user = jwt.getIssuer();

			    File file = new File(url+name+"."+sender +".encrypted.txt");
				
				FileWriter myWriter = new FileWriter(file);
			    myWriter.write(base64Name(name) + " . " + base64IV() + " . " + encryptRSA(Users.rndIV, getpubKeyFromFile(name))
				+ " . " + encryptDes(name, message) + " . " + base64Name(user) + " . " + base64Signature(signature));
			    myWriter.close();
			    System.out.println("Successfully created the encrypted text file.");
			} catch (JWTVerificationException exception){
			    System.out.println("Token not valid!");
			}
		return null;
	}
	
	private static String base64Signature(byte [] signature) { 
		
		return Base64.getEncoder().encodeToString(signature);
	}
	
	private static void writePemFile(Key key, String description, String filename)
			throws FileNotFoundException, IOException {
		PemFile pemFile = new PemFile(key, description);
		pemFile.write(filename);

		LOGGER.info(String.format("%s successfully written in file %s.", description, filename));
	}

	private static void printKey(String pubPriv, String name) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException{

		if(pubPriv.equals("public")) {
			
			File filePub = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".pub.pem");
			if(!filePub.exists()) {
				System.out.println("File does not exist!");
				System.exit(0);	
			}
			
			RSAPublicKey pubKey = (RSAPublicKey) getpubKeyFromFile(name);
			
			BigInteger modulus = pubKey.getModulus();
			BigInteger publicExponent = pubKey.getPublicExponent();
			
			System.out.println("Public key content: ");
			System.out.println("Modulus: "+modulus);
			System.out.println("Exponent: "+publicExponent);
		}
		else if(pubPriv.equals("private")) {
			File filePriv = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".pem");
			if(!filePriv.exists()) {
				System.out.println("File does not exist!");
				System.exit(0);	
			}
			
			RSAPrivateKey privKey = (RSAPrivateKey) getprivKeyFromFile(name);
			
			BigInteger modulus = privKey.getModulus();
			BigInteger publicExponent = privKey.getPrivateExponent();
			
			
			
			System.out.println("Private key content: ");
			System.out.println("Modulus: "+modulus);
			System.out.println("Exponent: "+publicExponent);
		}
	}
	
	private static void exportKey(String PubPriv, String file, String UrL) throws IOException {
		
		File filePub = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + file + ".pub.pem");
		File filePriv = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + file + ".pem");
		
		if(PubPriv.equals("public")) {
			if(!filePub.exists()) {
			System.out.println("File does not exist!");
			System.exit(0);
		}
		}else if(PubPriv.equals("private")) {
			if(!filePriv.exists()) {
				System.out.println("File does not exist!");
				System.exit(0);
				}}

		
			if(PubPriv.equals("public")) {
				Path source = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/"+file+".pub.pem");
				Path newdir = Paths.get(UrL+file+".pub.pem");
				Files.move(source, newdir);
				System.out.println("File succesfully exported!");
			}else if(PubPriv.equals("private")) {
				Path source = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/"+file+".pem");
				Path newdir = Paths.get(UrL+file+".pem");
				Files.move(source, newdir);
				System.out.println("File succesfully exported!");
			}else {
				System.out.println("Failed to export file!");
			}
			
			
	}

	private static void importKey(String name, String UrL) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		
		File filePubPriv = new File(UrL);

	
		if(!UrL.contains(".pub")) {
			if(!filePubPriv.exists()) {
				System.out.println("Private key does not exist!");
				System.exit(0);
				}
			Path source = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/"+name+".pem");
			Path newdir = Paths.get(UrL);
			Files.move(newdir,source);
			System.out.println("File succesfully imported!");
		}else if(UrL.contains("pub")){
			if(!filePubPriv.exists()) {
				System.out.println("Public key does not exist!");
				System.exit(0);
			}
			Path source = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/"+name+".pub.pem");
			Path newdir = Paths.get(UrL);
			Files.move(newdir,source);
			System.out.println("File succesfully imported!");
		}else {
			System.out.println("Failed to import file!");
		}
		
		
	}

	private static void createUser(String name)
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		
		Scanner input = new Scanner(System.in);
		        String password, rptpassword;
		            System.out.print("Password: ");
		            password = input.next();
		            String regex = "((?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,})";
		            Pattern pattern = Pattern.compile(regex);
		            Matcher matcher = pattern.matcher(password);
		            if(matcher.matches()) {
		            	System.out.print("Confirm Password: ");
			            rptpassword = input.next();
			            input.close();
		            	if (password.equals(rptpassword)) {
			            	System.out.println("User "+name+" has been created.");
			            		Security.addProvider(new BouncyCastleProvider());
			            		File f = new File("keys/" + name + ".pem");
			            		if (f.exists()) {
			            			System.out.println("User's RSA KEY Exists");
			            		} else {
			            		KeyPair keyPair = generateRSAKeyPair();
			            		RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
			            		RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();

			            		writePemFile(priv, "RSA PRIVATE KEY", "keys/" + name + ".pem");
			            		writePemFile(pub, "RSA PUBLIC KEY", "keys/" + name + ".pub.pem");
			            	}
			            		File file = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".password.xml");
					    		FileWriter myWriter = new FileWriter(file);
					    		SecureRandom random = new SecureRandom();
					    		byte[] salt = new byte[16];
					    		random.nextBytes(salt);
					    		String password2=new String(salt).concat(password);
					    		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
					    		SecretKeyFactory f1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
					    		byte[] hash = f1.generateSecret(spec).getEncoded();
					    		Base64.Encoder enc = Base64.getEncoder();
					    		String hashpw = enc.encodeToString(hash);
					    		String saltpw =enc.encodeToString(password2.getBytes());
					    		
					    		myWriter.write(hashpw + " . " +enc.encodeToString(salt)+" . "+saltpw);
					    	    myWriter.close();
					    	    System.out.println("Successfully created the hashed password.");
			            		}else {
			            		System.out.println("Passwords do not match. Please try again.");
			            }   
		            }else {
		            	System.out.println("Password must contain: \nAt least 6 characters,\nAt least one letter,\nAt least one digit,\nAt least one of the following symbols: @, $, !, %, *, #, ?, & ");
		            }
		}
	
	private static void login(String name) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
		
		if(checkpassword(name)) {
			
			RSAPublicKey publicKey = (RSAPublicKey) getpubKeyFromFile(name);
			RSAPrivateKey privateKey = getprivKeyFromFile(name);
			try {
					Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
					Calendar date = Calendar.getInstance();
					long current= date.getTimeInMillis();
					Date now = new Date();
					Date expire = new Date(current + 20*60000);
					String token = JWT.create().withIssuer(name).withIssuedAt(now).withExpiresAt(expire).sign(algorithm);
					writeToken(token,name);
					System.out.println("Token: "+token);
					} catch (JWTCreationException exception){
					
					}
			
		}
	}   
	
	private static void writeToken(String token, String name) throws IOException {
		
		File file = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/"+name+".jwt");
		FileWriter myWriter = new FileWriter(file);
	    myWriter.write(token);
	    myWriter.close();
	    System.out.println("Successfully created the token file.");
	}
	
	private static String readToken(String name) {
		
		File file = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".jwt");
		 String token = readLine(file.toString());
		 return token;
		
	}
	
	private static void verifyToken(String name) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
		
		RSAPublicKey publicKey = (RSAPublicKey) getpubKeyFromFile(name);
		RSAPrivateKey privateKey = getprivKeyFromFile(name);
		try {
		    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(name).build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(readToken(name));
		    Date expired = jwt.getExpiresAt();
		    String user = jwt.getIssuer();
		    System.out.println("User: "+user);
		    System.out.println("Valid!");
		    System.out.println("Expiration date: "+expired);
		} catch (JWTVerificationException exception){
		    System.out.println("Token not valid!");
		}
	}
	
	private static void verifyToken(String name, String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
	
			RSAPublicKey publicKey = (RSAPublicKey) getpubKeyFromFile(name);
			RSAPrivateKey privateKey = getprivKeyFromFile(name);
			try {
			    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			    JWTVerifier verifier = JWT.require(algorithm)
			        .withIssuer(name).build(); //Reusable verifier instance
			    DecodedJWT jwt = verifier.verify(token);
			    Date expired = jwt.getExpiresAt();
			    String user = jwt.getIssuer();
			    System.out.println("User: "+user);
			    System.out.println("Valid!");
			    System.out.println("Expiration date: "+expired);
			} catch (JWTVerificationException exception){
			    System.out.println("Token not valid!");
		}
			}
	
	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
	    Signature publicSignature = Signature.getInstance("SHA256withRSA");
	    try {
	    	try {
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(plainText.getBytes());
	    	}catch (InvalidKeyException e) {
				System.out.println("\nNo Public Key.");
			}
	    byte[] signatureBytes = Base64.getDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}catch(IllegalArgumentException e){
		return false;
	}}
		
	private static void deleteUser(String name) {

		File filePub = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pub.pem");
		File filePriv = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pem");
		File password = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".password.xml");
		File token = new File("C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".jwt");

		if(filePub.exists() || filePriv.exists() || password.exists() || token.exists()) {
		if (filePub.delete() ) {
			System.out.println("Deleted file: " + name + ".pub.pem");
		} if(filePriv.delete()) {
			System.out.println("Deleted file: " + name + ".pem");
		} if(password.delete()) {
			System.out.println("Deleted file: " + name + ".password.xml");
		}if(token.delete()) {
			System.out.println("Deleted file: " + name + ".jwt");
		}}else {
			System.out.println("Failed to delete the files.");
		}
		}

	private static boolean checkpassword(String name) {
		File file = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".password.xml");
		
		Scanner input = new Scanner(System.in);
        String password1;
        boolean same = false;
            System.out.print("Password: ");
            password1 = input.nextLine();
            input.close();
            String cipher = readLine(file.toString());
			String messageSplit[] = cipher.split(" . ");
			Base64.Encoder enc = Base64.getEncoder();
			byte[] decodedsalt = Base64.getDecoder().decode(messageSplit[1]);
			
			String filepw = messageSplit[2];
			filepw= filepw.replaceAll("[\\n]", "");
			String password2=new String(decodedsalt).concat(password1);
			password2 =  enc.encodeToString(password2.getBytes());
		
			if(password2.equals(filepw)) {
				System.out.println("Correct");
				same = true;
			}else {
				System.out.println("Incorrect");
			}
			return same;
	}

	private static byte[] generateIV() {

		SecureRandom random = new SecureRandom();
		byte iv[] = new byte[8]; // generate random 8 byte IV.
		random.nextBytes(iv);
		Users.rndIV = iv;
		return Users.rndIV;
	}

	private static String base64IV() {
		
		return Base64.getEncoder().encodeToString(generateIV());

	}

	private static String base64Name(String name) {

		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(name.getBytes(StandardCharsets.UTF_8));
		return encodedString;
	}

	public static String encryptRSA(byte[] iv, PublicKey publicKey) throws Exception {

		Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return Base64.getEncoder().encodeToString(iv);
	}

	public static String encryptRSAText (String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA/ECB/PKCS1Padding");
        cipher.init (Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal (rawText.getBytes ("UTF-8")));
    }
	
	public static byte[] decryptRSAText (String string, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA/ECB/PKCS1Padding");
        cipher.init (Cipher.DECRYPT_MODE, privateKey);
        return (cipher.doFinal (Base64.getDecoder().decode(string)));
    }
	
	private static PublicKey getpubKeyFromFile(String name)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
		try {
		String filePath = "C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + name + ".pub.pem";
		
		String keypub = readLine(filePath);

		PemObject pem = new PemReader(new StringReader(keypub)).readPemObject();
		
		byte[] pubKeyBytes = pem.getContent();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
		RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
		
		return pubKey;
	}catch (Exception e) {
	}
		return null;
		}

	private static String readLine(String filePath) {
		final StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
		}

		return contentBuilder.toString();
	}

	private static RSAPrivateKey getprivKeyFromFile(String decodedUser)
			throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {

		String filePath = "C:/Users/IFES Yoga/Downloads/RSA Create-User/keys/" + decodedUser + ".pem";

		RSAPrivateKey privKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(filePath, "RSA");
		return privKey;

	}

	private static void getDESkey(String name)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {

		byte[] decodedKey = Base64.getDecoder().decode(encryptRSA(Users.rndIV, getpubKeyFromFile(name)));
		Users.DESKey = decodedKey;
	}

	private static void getSecretDES() {
		SecretKey originalKey = new SecretKeySpec(Users.DESKey, 0, Users.DESKey.length, "DES");
		Users.DESSecretKey = originalKey;
		

	}

	private static String encryptDes(String name, String message)
			throws InvalidKeySpecException, IOException, URISyntaxException, Exception {
		getDESkey(name);
		getSecretDES();
		Cipher desCipher;
		
		message = encryptRSAText(message, getpubKeyFromFile(name));
		// Create the cipher
		desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// Initialize the cipher for encryption
		desCipher.init(Cipher.ENCRYPT_MODE, DESSecretKey);

		// sensitive information
		byte[] text = message.getBytes();
		// Encrypt the text
		byte[] textEncrypted = desCipher.doFinal(text);

		return Base64.getEncoder().encodeToString(textEncrypted).toString();
	}

	private static void decryptDes(String cipherText)
			throws InvalidKeySpecException, IOException, URISyntaxException, Exception {
		if(cipherText.contains(" . ")) {
			String messageSplit[] = cipherText.split(" . ");
			
			if(messageSplit.length>4) {
				byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
				System.out.println("Reciever: " + new String(decodedUser));
				
				byte[] decodedIV = Base64.getDecoder().decode(messageSplit[1]);
				Users.DESKey = decodedIV;
				getSecretDES();
				 
				byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[3]);
				byte[] decodedSender = Base64.getDecoder().decode(messageSplit[4]);
				
				Cipher desCipher;
				// Create the cipher
				desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

				desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

				// Decrypt the text
				byte[] textDecrypted = desCipher.doFinal(decodedMessage);
				textDecrypted = decryptRSAText(new String(textDecrypted), getprivKeyFromFile(new String(decodedUser)));
				
				System.out.println("Decrypted text: " + new String(textDecrypted));
				boolean isCorrect = verify(new String(textDecrypted), messageSplit[5], getpubKeyFromFile(new String(decodedSender)));
				System.out.println("Sender: " + new String(decodedSender));
				System.out.println("Signature: " + isCorrect);
			}else {
			byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
			System.out.println("Reciever: " + new String(decodedUser));
			
			byte[] decodedIV = Base64.getDecoder().decode(messageSplit[1]);
			Users.DESKey = decodedIV;
			getSecretDES();
			 
			byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[3]);
			Cipher desCipher;
			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			

			// Initialize the same cipher for decryption
			desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

			// Decrypt the text
			byte[] textDecrypted = desCipher.doFinal(decodedMessage);
			textDecrypted = decryptRSAText(new String(textDecrypted), getprivKeyFromFile(new String(decodedUser)));
			
			System.out.println("Decrypted text : " + new String(textDecrypted));
			}
		}
		else {
			if(cipherText.contains("encrypted.txt")) {
			String cipher = readLine(cipherText);
			
			String messageSplit[] = cipher.split(" . ");
			if(messageSplit.length>4) {
				byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
				System.out.println("Reciever: " + new String(decodedUser));
				
				byte[] decodedIV = Base64.getDecoder().decode(messageSplit[1]);
				Users.DESKey = decodedIV;
				getSecretDES();

				byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[3].trim());
				Cipher desCipher;
				// Create the cipher
				desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			
				// Initialize the same cipher for decryption
				desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

				// Decrypt the text
				byte[] textDecrypted = desCipher.doFinal(decodedMessage);
				textDecrypted = decryptRSAText(new String(textDecrypted), getprivKeyFromFile(new String(decodedUser)));
				
				System.out.println("Decrypted text : " + new String(textDecrypted));
				byte[] decodedSender = Base64.getDecoder().decode(messageSplit[4]);
				boolean isCorrect = verify(new String(textDecrypted), messageSplit[5], getpubKeyFromFile(new String(decodedSender)));
				System.out.println("Sender: " + new String(decodedSender));
				System.out.println("Signature: " + isCorrect);
			}else {
				
			byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
			System.out.println("Reciever: " + new String(decodedUser));
			
			byte[] decodedIV = Base64.getDecoder().decode(messageSplit[1]);
			Users.DESKey = decodedIV;
			getSecretDES();

			byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[3].trim());
			Cipher desCipher;
			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		
			// Initialize the same cipher for decryption
			desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

			// Decrypt the text
			byte[] textDecrypted = desCipher.doFinal(decodedMessage);
			textDecrypted = decryptRSAText(new String(textDecrypted), getprivKeyFromFile(new String(decodedUser)));
			
			System.out.println("Decrypted text : " + new String(textDecrypted));
			}}
			else {
				System.out.println("File doesnt exist!");
			}
		}

	}
	
	
}
