# Siguria_Te_Dhenave_Gr20
 *******IMPORTANT NOTE AT THE BOTTOM*****
 
 
~RSA Algorithm
RSA (Rivest, Shamir and Adleman) algorithm is asymmetric cryptography algorithm. Asymmetric actually means that it works on two different keys i.e. Public Key and Private Key.
The Public Key is given to everyone and it is used for encrypting messages, the Private key is kept private, which is used for decryption..
The RSA algorithm involves four steps: key generation, key distribution, encryption and decryption.
The public key consists of the modulus n and the public (or encryption) exponent e. The private key consists of the private (or decryption) exponent d, which must be kept secret. 
p, q, and λ(n) must also be kept secret because they can be used to calculate d. 
In fact, they can all be discarded after d has been computed.

~DES Algorithm
DES (Data Encryption Standard) is a symmetric key algorithm, which means that the same key is used for encrypting and decrypting data.
The DES algorithm is a block cipher algorithm.
The data block size of the DES algorithm is 64 bit (8 bytes).
Key size is 64 bit (8 bytes), but 1 byte is used for parity, so the actual key size is 56 bit.
Without ECB (Electronic Code Book), for others, IV (Initialization Vector) is mandatory.
For implementation, we have to use a security provider. In this case, we used the BouncyCastle Provider.

Methods included in each command:

Create-User Command
Generates an RSA keypair for each user we create as PEM files.
KeyPair
writePemFile

Delete-User Command
Deletes already created users.

Export-Key Command
Exports keys from keys directory into another file.
<P>+EZth9...</P>
<Q>zG6WRR...</Q>
<DP>khmvvu...</DP>
<DQ>IjdMLW...</DQ>
<InverseQ>kX+viS...</InverseQ>
<D>IOrUqe...</D>
Are not applied to the code.

Import-Key Command
Imports keys from other files into keys directory.
Nëse shtegu <path> fillon me http:// ose https://, atëherë do të dërgohet një GET request në
URL <path> dhe do të merret trupi i përgjigjes si vlera e çelësit.
Request was not fully completed.

Write-Message Command
readline
generateIV
base64Name
encryptRSA
getPubkeyFromFile

Read-Message Command
readline
getDESkey
getSecretDES
encryptDES
decryptDES
decryptRSA
getPrivFromFile

********IMPORTANT***************
IN ORDER TO RUN THE PROJECT, SOME EXTRA FILES NEED TO BE DOWNLOADED:
        1.THE BOUNCYCASTLE JAR THROUGH THE FOLLOWING LINK -- https://www.bouncycastle.org/latest_releases.html
        1.1. INSTRUCTIONS TO INSTALL BOUNCYCASTLE https://justrocketscience.com/post/install-bouncy-castle/
        2. INSTALL LOGGER LIB THROUGH LINK https://logging.apache.org/log4j/2.x/download.html
        2.1 INSTRUCTIONS FOR LOGGER LIB INSTALLATION https://www.tutorialspoint.com/log4j/log4j_installation.htm
        
