package com.createuser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class Main {

	protected final static Logger LOGGER = Logger.getLogger(Main.class);

	public static final int KEY_SIZE = 1024;
	private static byte[] DESKey;
	private static SecretKey DESSecretKey;

	public static void main(String[] args) throws Exception {

		String name = "Ylli332332_3";
		createUser(name);

		String encryptedText = writeMessage(name);
		System.out.println(encryptedText);
		decryptDes(encryptedText);
	}

	private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(KEY_SIZE);

		KeyPair keyPair = generator.generateKeyPair();
		LOGGER.info("RSA key pair generated.");
		return keyPair;
	}

	private static String writeMessage(String name) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception{
		return(base64Name(name) + " . " + base64IV() + " . " + encryptRSA(generateIV(), getpubKeyFromFile(name))+ " . " + encryptDes(name, "Yll Baba i Programimit"));
	}
	
	private static void writePemFile(Key key, String description, String filename)
			throws FileNotFoundException, IOException {
		PemFile pemFile = new PemFile(key, description);
		pemFile.write(filename);

		LOGGER.info(String.format("%s successfully writen in file %s.", description, filename));
	}

	private static void exportKey(File file, String UrL) throws IOException {

		Path source = Paths.get(file.toString());
		Path newdir = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\" + UrL);
		Files.move(source, newdir);
	}

	private static void importKey(File file, String UrL) throws IOException {

		Path source3 = Paths.get(file.toString());
		Path newdir3 = Paths.get("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\" + UrL);
		Files.move(source3, newdir3);
	}

	private static void createUser(String name)
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {

		Security.addProvider(new BouncyCastleProvider());
		if(name.matches("[a-zA-Z0-9_]*")) {
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
		}else {
			System.out.println("Name should only contain a-z, A-Z, 0-9 or _");
		}
		
	}

	private static void deleteUser(String name) {

		File file = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pub.pem");
		File file3 = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pem");

		if (file.delete() && file3.delete()) {
			System.out.println("Deleted file:" + name);
			System.out.println("Deleted file:" + name + ".pub");
		} else {
			System.out.println("Failed to delete the file");
		}
	}

	private static byte[] generateIV() {

		SecureRandom random = new SecureRandom();
		byte iv[] = new byte[8]; // generate random 8 byte IV.
		random.nextBytes(iv);
		
		return iv;
	}

	private static String base64IV() {

		return Base64.getEncoder().encodeToString(generateIV().toString().getBytes()).toString();

	}

	private static String base64Name(String name) {

		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(name.getBytes(StandardCharsets.UTF_8));
		return encodedString;
	}

	public static String encryptRSA(byte[] iv, PublicKey publicKey) throws Exception {

		Cipher encryptCipher = Cipher.getInstance("RSA");

		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return Base64.getEncoder().encodeToString(iv);
	}

	private static PublicKey getpubKeyFromFile(String name)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
		String filePath = "C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pub.pem";
		String keypub = readLine(filePath);

		PemObject pem = new PemReader(new StringReader(keypub)).readPemObject();
		byte[] pubKeyBytes = pem.getContent();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
		RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);

		return pubKey;
	}

	private static String readLine(String filePath) {
		final StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder.toString();
	}

	private static RSAPrivateKey getprivKeyFromFile(String name)
			throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {
	
		String filePath = "C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pem";
		String keypriv = readLine(filePath);

		PemObject pem = new PemReader(new StringReader(keypriv)).readPemObject();
		byte[] pubKeyBytes = pem.getContent();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
		RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(pubSpec);

		return privKey;
	}

	private static String decryptRSA(byte[] buffer, String name) {
	    try {
	        Cipher rsa;
	        rsa = Cipher.getInstance("RSA");
	        rsa.init(Cipher.DECRYPT_MODE, getprivKeyFromFile(name));
	        byte[] utf8 = rsa.doFinal(buffer);
	        return new String(utf8, "UTF8");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
	
	private static  void getDESkey(String name) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
		
		byte[] decodedKey = Base64.getDecoder().decode(encryptRSA(generateIV(), getpubKeyFromFile(name)));
		Main.DESKey = decodedKey;
	}
	private static void getSecretDES() {
		SecretKey originalKey = new SecretKeySpec(Main.DESKey, 0, Main.DESKey.length, "DES");
		Main.DESSecretKey = originalKey;
		
	}
	private static String encryptDes(String name, String message)
			throws InvalidKeySpecException, IOException, URISyntaxException, Exception {
		getDESkey(name);
		getSecretDES();
		Cipher desCipher;

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

	private static void decryptDes(String cipherText) throws InvalidKeySpecException, IOException, URISyntaxException, Exception {
		String messageSplit[] = cipherText.split(" . ");

		System.out.println(messageSplit[0]);
		System.out.println(messageSplit[1]);
		System.out.println(messageSplit[2]);
		System.out.println(messageSplit[3]);
		byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
		System.out.println(new String(decodedUser));

		byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[0]);
//		byte[] decodedIV = Base64.getDecoder().decode(messageSplit[1]);
//		System.out.println(new String(decodedIV));
//		
//		byte[] decodedRSAKey = Base64.getDecoder().decode(messageSplit[2]);
//		System.out.println(new String(decodedRSAKey));
		
		
		
	    Cipher desCipher;
	    // Create the cipher
	    desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

	    //Initialize the same cipher for decryption
	    desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

	    //Decrypt the text
	    byte[] textDecrypted = desCipher.doFinal(decodedMessage);

	    System.out.println("Text Decryted : " + new String(textDecrypted));
	    
	    //return textDecrypted.toString();
	
	}
}
