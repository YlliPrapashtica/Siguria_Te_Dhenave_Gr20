 # Siguria_Te_Dhenave_Gr20

~ Caesar Cipher

It is a type of substitution cipher in which each letter in the plaintext is replaced by a letter some fixed number of positions down the alphabet.
For example, with a left shift of 7, H would be replaced by A. The method is named after Julius Caesar, who used it in his private correspondence.
When it comes to the decryption, Caesar code replaces a letter another with an inverse alphabet shift: a previous letter in the alphabet.
BruteForce applied.

Instructions:
Encryption Command:
Input: java Ciphers.java ceasar encrypt Attack 7 (can use only e to represent encrypt in Command Prompt)
Output: Haahjr

Decryption Command:
Input: java Ciphers.java ceasar decrypt Haahjr 7 (can use only d to represent decrypt in Command Prompt)
Output: ATTACK


~ PlayFair Cipher

https://github.com/AhmedOthman95/playfair-algorithm/blob/master/PlayfairCipherEncryption.java

The encryption Algorithm consists of 2 steps:
Generate the key Matrix(5Ã—5):
The key square is a 5Ã—5 grid of alphabets that acts as the key for encrypting the plaintext.
Each of the 25 alphabets must be unique and one letter of the alphabet (usually J) is omitted from the table (as the table can hold only 25 letters).
If the plaintext contains J, then it is replaced by I.
The initial alphabets in the key square are the unique alphabets of the key in the order in which they appear followed by the remaining letters of the alphabet in order.
Algorithm to encrypt the plain text: 
The plaintext is split into pairs of two letters. If there is an odd number of letters, a X is added to the last letter.(The condition in main doesn't allow that in our code).
If both the letters are in the same column: 
Take the letter below each one (going back to the top if at the bottom).
If both the letters are in the same row: 
Take the letter to the right of each one (going back to the leftmost if at the rightmost position).
If neither of the above rules is true: 
Form a rectangle with the two letters and take the letters on the horizontal opposite corner of the rectangle.
Decrypting PlayFair goes in reverse of encryption.
Matrix with the KeyWord applied.

Directions:
Encryption Command: 
Input: java Ciphers.java playfair encrypt demo anarchy (can use only e to represent encrypt in Command Prompt)
Output: opeufbzy

Decryption Command:
Input: java Ciphers.java playfair decrypt demo opeufbzy (can use only d to represent decrypt in Command Prompt)
Output: anarchy

*Given plaintext/ciphertext is vulnerable to whitespace, doesn't follow all rules after the whitespace.


~ Beale Cipher

The second level of the Beale's Cipher shows the amount of the treasure he hid, two other parts of the code remain unsolved.
Based on the Declaration of Independence, the code was solved using the number in the code and replacing it with the indicated first letter of the number based positioned letter.
We solved it by representing each letter/space with an indicated number, based on the given key, same letters were replaced with the indicated position number.
Decryption process goes in reverse, numbers replace letters positioned in indicated letters.

Instructions:
Encryption Command:
Input: java Ciphers.java beale (or book)  encrypt book "Hello Beale" (can use only e to represent encrypt in Command Prompt)
Output: 1 , 2 , 64 , 64 , 13 , 4 , 38 , 2 , 25 , 64 , 2 ,

Decryption Command:
Input: java Ciphers.java beale (or book)  decrypt book "1 , 2 , 64 , 64 , 13 , 4 , 38 , 2 , 25 , 64 , 2" (can use only d to represent decrypt in Command Prompt)
Output: HELLO BEALE



 
