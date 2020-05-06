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
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class Users {

	protected final static Logger LOGGER = Logger.getLogger(Users.class);

	public static final int KEY_SIZE = 1024;
	private static byte[] DESKey;
	private static SecretKey DESSecretKey;

	public static void main(String[] args) throws Exception {

		
			if(args[0] == "create-user") {
				System.out.println("User has been created!");
				createUser(args[1]);
					
			} else if(args[0] == "delete-user") {
				deleteUser(args[1]);
				
			} else if(args[0] == "export-key") {
				if(args[3] !=null) {
					exportKey(args[1], args[2],args[3]);
				}else {
					printKey(args[1],args[2]);
					
				}
				
			} else if(args[0] == "import-key") {
				importKey(args[1], args[2]);
				
				
			} else if(args[0] == "write-message") {
		
				if(args[3] !=null) {
					writeMessage(args[1], args[2],args[3]);
				}else {
				String encryptedText = writeMessage(args[1], args[2]);
				System.out.println(encryptedText);
				}
				
			} else if(args[0] == "read-message") {
				
					decryptDes(args[1]);
				
				
			} else {
				System.out.println("Wrong command name! ");
			}
			
	
			
	}	

	private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(KEY_SIZE);

		KeyPair keyPair = generator.generateKeyPair();
		
		return keyPair;
	}

	private static void writeMessage(String name, String message, String url)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
		
		File file = new File(url+name+".ecnrypted.txt");
		
		FileWriter myWriter = new FileWriter(file);
	    myWriter.write(base64Name(name) + " . " + base64IV() + " . " + encryptRSA(generateIV(), getpubKeyFromFile(name))
		+ " . " + encryptDes(name, message));
	    myWriter.close();
	    System.out.println("Successfully wrote to the file.");
		
	}

	private static String writeMessage(String name, String message)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {
		
		
		return (base64Name(name) + " . " + base64IV() + " . " + encryptRSA(generateIV(), getpubKeyFromFile(name))
				+ " . " + encryptDes(name, message));
	}

	private static void writePemFile(Key key, String description, String filename)
			throws FileNotFoundException, IOException {
		PemFile pemFile = new PemFile(key, description);
		pemFile.write(filename);

		LOGGER.info(String.format("%s successfully writen in file %s.", description, filename));
	}

	private static void printKey(String name, String pubPriv) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException{
		File filePub = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pub.pem");
		File filePriv = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pem");
	
		if(!filePub.exists() || !filePriv.exists()) {
			System.out.println("File doesnt exist!");
			System.exit(0);
			
		}
		if(pubPriv == "public") {
			RSAPublicKey pubKey = (RSAPublicKey) getpubKeyFromFile(name);
			
			BigInteger modulus = pubKey.getModulus();
			BigInteger publicExponent = pubKey.getPublicExponent();
			
			System.out.println("Public key content: ");
			System.out.println("Modulus: "+modulus);
			System.out.println("Exponent: "+publicExponent);
		}
		else if(pubPriv == "private") {
			RSAPrivateKey privKey = (RSAPrivateKey) getprivKeyFromFile(name);
			
			BigInteger modulus = privKey.getModulus();
			BigInteger publicExponent = privKey.getPrivateExponent();
			
			
			
			System.out.println("Private key content: ");
			System.out.println("Modulus: "+modulus);
			System.out.println("Exponent: "+publicExponent);
		}
	}
	
	private static void exportKey(String file, String UrL, String PubPriv) throws IOException {
		
		File filePub = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + file + ".pub.pem");
		File filePriv = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + file + ".pem");
	
		if(!filePub.exists() || !filePriv.exists()) {
			System.out.println("File doesnt exist!");
			System.exit(0);
			
		}
			if(PubPriv=="public") {
				Path source = Paths.get("C:\\Users\\me\\Downloads\\RSA Create-User\\keys/"+file+".pub.pem");
				Path newdir = Paths.get(UrL+".pub.pem");
				Files.move(source, newdir);
				System.out.println("File succesfully exported!");
			}else if(PubPriv == "private") {
				Path source = Paths.get("C:\\Users\\me\\Downloads\\RSA Create-User\\keys/"+file+".pem");
				Path newdir = Paths.get(UrL+".pem");
				Files.move(source, newdir);
				System.out.println("File succesfully exported!");
			}else {
				System.out.println("Failed to export file!");
			}
			
			
	}

	private static void importKey(String name, String UrL) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		
		File filePub = new File(UrL+"/"+  name + ".pub.pem");
		File filePriv = new File(UrL +"/"+ name + ".pem");
	
		if(!filePub.exists() || !filePriv.exists()) {
			System.out.println("File doesnt exist!");
			System.exit(0);
			
		}
		
		if(!UrL.contains(".pub")) {
			deleteUser(name);
			createUser(name);
		}else if(UrL.contains("pub")){
			Path source = Paths.get("C:\\Users\\me\\Downloads\\RSA Create-User\\keys/"+name+".pem");
			Path newdir = Paths.get(UrL);
			Files.move(newdir,source);
			System.out.println("File succesfully imported!");
		}else {
			System.out.println("Failed to import file!");
		}
		
		
	}

	private static void createUser(String name)
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {

		Security.addProvider(new BouncyCastleProvider());
		if (name.matches("[a-zA-Z0-9_]*")) {
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
		} else {
			System.out.println("Name should only contain a-z, A-Z, 0-9 or _");
		}

	}

	private static void deleteUser(String name) {

		File filePub = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pub.pem");
		File filePriv = new File("C:/Users/me/Downloads/RSA Create-User/keys/" + name + ".pem");

		if (filePub.delete() ) {
			if(filePriv.delete()) {
				System.out.println("Deleted file:" + name+".pem");
				System.out.println("Deleted file:" + name + ".pub.pem");
			}else {
				System.out.println("Deleted file:" + name + ".pub.pem");
			}
		} else if(filePriv.delete()){
			System.out.println("Deleted file:" + name+".pem");
		}
		else {
			System.out.println("Failed to delete the files!");
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

	public static String encryptRSAText (String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA");
        cipher.init (Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal (rawText.getBytes ("UTF-8")));
    }
	
	public static byte[] decryptRSAText (String string, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA");
        cipher.init (Cipher.DECRYPT_MODE, privateKey);
        return (cipher.doFinal (Base64.getDecoder().decode(string)));
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

	private static RSAPrivateKey getprivKeyFromFile(String decodedUser)
			throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {

		String filePath = "C:/Users/me/Downloads/RSA Create-User/keys/" + decodedUser + ".pem";

		RSAPrivateKey privKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(filePath, "RSA");
		return privKey;

	}

	private static void getDESkey(String name)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException, Exception {

		byte[] decodedKey = Base64.getDecoder().decode(encryptRSA(generateIV(), getpubKeyFromFile(name)));
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
		String messageSplit[] = cipherText.split(" . ");

		byte[] decodedUser = Base64.getDecoder().decode(messageSplit[0]);
		System.out.println("Reciever: " + new String(decodedUser));

		byte[] decodedMessage = Base64.getDecoder().decode(messageSplit[3]);
		Cipher desCipher;
		// Create the cipher
		desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// Initialize the same cipher for decryption
		desCipher.init(Cipher.DECRYPT_MODE, DESSecretKey);

		// Decrypt the text
		byte[] textDecrypted = desCipher.doFinal(decodedMessage);
		textDecrypted = decryptRSAText(new String(textDecrypted), getprivKeyFromFile(new String(decodedUser)));
		
		System.out.println("Text Decryted : " + new String(textDecrypted));

		// return textDecrypted.toString();

	}
	
	
}
