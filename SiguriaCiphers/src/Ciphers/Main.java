package com.createuser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
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
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class Main {

	protected final static Logger LOGGER = Logger.getLogger(Main.class);

	public static final int KEY_SIZE = 1024;

	public static void main(String[] args) throws Exception {

		String name= "Ylli33323";
		createUser(name);
   
		System.out.println(base64Name(name)+" . "+base64IV()+" . "+encrypt("Helo", getpubKeyFromFile(name)));
		
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
		File f = new File("keys/" + name+".pem");
		if (f.exists()) {
			System.out.println("User's RSA KEY Exists");
		} else {
			KeyPair keyPair = generateRSAKeyPair();
			RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();

			writePemFile(priv, "RSA PRIVATE KEY", "keys/" + name+".pem");
			writePemFile(pub, "RSA PUBLIC KEY", "keys/" + name + ".pub.pem");
		}
	}

	private static void deleteUser(String name) {

		File file = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name + ".pub.pem");
		File file3 = new File("C:\\Users\\IFES Yoga\\Downloads\\RSA Create-User\\keys/" + name+".pem");

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
	
	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		
        Cipher encryptCipher = Cipher.getInstance("RSA");
       
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
	}
	
	private static PublicKey getpubKeyFromFile(String name) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, URISyntaxException {
//	        byte[] keyBytes = Files.readAllBytes(Paths.get("C:/Users/me/Downloads/RSA Create-User/keys/"+name+".pub.pem"));
//	        System.out.println(keyBytes);
//	        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//	        KeyFactory kf = KeyFactory.getInstance("RSA");
//	        return kf.generatePublic(spec);
		
//		String filePath = 
//		File file1 = new File(filePath);
//		FileInputStream fis1 = new FileInputStream(file1);
//        DataInputStream dis1 = new DataInputStream(fis1);
//        byte[] keyBytes1 = new byte[(int) file1.length()];
//        dis1.readFully(keyBytes1);
//        dis1.close();
//
//        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(keyBytes1);
//        KeyFactory kf1 = KeyFactory.getInstance("RSA");
//        RSAPublicKey pubKey = (RSAPublicKey) kf1.generatePublic(spec1);
//
//        System.out.println("Exponent :" + pubKey.getPublicExponent());
//        System.out.println("Modulus" + pubKey.getModulus());
//		return pubKey;
		String filePath = "C:/Users/me/Downloads/RSA Create-User/keys/"+name+".pub.pem";
		String keypub = readLine( filePath );
		  PemObject pem = new PemReader(new StringReader(keypub)).readPemObject();
		    byte[] pubKeyBytes = pem.getContent();
		    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		    X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
		    RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
		    return pubKey;
	    }
	public static void ain5(String[] args) 
    {
        
 
        System.out.println(  );
    }
 
 
    //Read file content into string with - Files.lines(Path path, Charset cs)
 
    private static String readLine(String filePath) 
    {
        final StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
	
	private static RSAPrivateKey getprivKeyFromFile(String name) throws NoSuchAlgorithmException, IOException, URISyntaxException, InvalidKeySpecException {
		 String privateKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(name+".pem").toURI())));
	       
	        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
	       
	        KeyFactory kf = KeyFactory.getInstance("RSA");

	        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
	        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);
	        return(privKey);
	       
	    }
	
	}


