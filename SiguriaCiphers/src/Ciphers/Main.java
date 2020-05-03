package com.createuser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class Main {
	
	protected final static Logger LOGGER = Logger.getLogger(Main.class);
	
	public static final int KEY_SIZE = 1024;

	public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		LOGGER.info("BouncyCastle provider added.");
		String name = args[0];
		File f = new File("keys/"+name);
		if(f.exists()) {
			System.out.println("User's RSA KEY Exists");
		}else {
			KeyPair keyPair = generateRSAKeyPair();
			RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
			
			writePemFile(priv, "RSA PRIVATE KEY", "keys/"+name);
			writePemFile(pub, "RSA PUBLIC KEY", "keys/"+name+".pub");
		}
		
		
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

}