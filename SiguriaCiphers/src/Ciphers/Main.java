package com.createuser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

	protected final static Logger LOGGER = Logger.getLogger(Main.class);

	public static final int KEY_SIZE = 1024;

	public static void main(String[] args) throws Exception {
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(KEY_SIZE);

		KeyPair keyPair = generator.generateKeyPair();
		RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
		System.out.println(pub);
	}

	private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(KEY_SIZE);

		KeyPair keyPair = generator.generateKeyPair();
		LOGGER.info("RSA key pair generated.");
		return keyPair;
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

	private static void createUser(String name)throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {

		Security.addProvider(new BouncyCastleProvider());
		File f = new File("keys/" + name);
		if (f.exists()) {
			System.out.println("User's RSA KEY Exists");
		} else {
			KeyPair keyPair = generateRSAKeyPair();
			RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();

			writePemFile(priv, "RSA PRIVATE KEY", "keys/" + name);
			writePemFile(pub, "RSA PUBLIC KEY", "keys/" + name + ".pub");
		}
	}

	private static void deleteUser(String name) {

		File file = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pub");
		File file3 = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name);

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
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		return iv;
	}
	
	private static String base64IV() {
		
		
		return Base64.getEncoder().encodeToString(generateIV().toString().getBytes()).toString();
		
	}
	
	private static String base64Name(String name) {
		
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(name.getBytes(StandardCharsets.UTF_8) );
		return encodedString;
	}
	
	public static String encrypt(String plainText, RSAPublicKey publicKey) throws Exception {
		
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
	}
	
}
