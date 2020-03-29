package Ciphers;
 
import java.util.Scanner;
 
public class PlayFairDecrypt
{
    private String Keyword = new String();
    private String Key = new String();
    private char   mat_array[][] = new char[5][5];
 
    public void setKey(String k) // Adds the keyword to the matrix, not allowing same letters to appear twice.
    {
        String K_fit = new String();
        boolean Next = false;
        K_fit = K_fit + k.charAt(0);
        for (int i = 1; i < k.length(); i++)
        {
            for (int j = 0; j < K_fit.length(); j++)
            {
                if (k.charAt(i) == K_fit.charAt(j))
                {
                    Next = true;
                }
            }
            if (Next == false)
                K_fit = K_fit + k.charAt(i);
            Next = false;
        }
        Keyword = K_fit;
    }
 
    public void KeyGen()
    {
        boolean flag = true;
        char current;
        Key = Keyword;
        for (int i = 0; i < 26; i++)
        {
            current = (char) (i + 97); // Alphabet adding 'a' ASCII.
            if (current == 'j')
                continue;  // Ignores letter j whenever it appears in the keyword.
            for (int j = 0; j < Keyword.length(); j++)
            {
                if (current == Keyword.charAt(j))
                {
                    flag = false;
                    break;
                }
            }
            if (flag)
                Key = Key + current; // Shows how keyword applies to the alphabet without repeated letters.
            flag = true;
        }
        //System.out.println(Key);
        
        matrix();
    }
 
    private void matrix() //Shows the created matrix.
    {
        int counter = 0;
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                mat_array[i][j] = Key.charAt(counter);
                System.out.print(mat_array[i][j] + " ");
                counter++;
            }
            System.out.println();
        }
    }
 
    private String format(String plaintxt)
    {
        int i = 0;
        int len = 0;
        String text = new String();
        len = plaintxt.length();
        for (int tmp = 0; tmp < len; tmp++)
        {
            if (plaintxt.charAt(tmp) == 'j') //Replaces j with i.
            {
                text = text + 'i';
            }
            else
                text = text + plaintxt.charAt(tmp);
        }
        len = text.length();
        for (i = 0; i < len-1; i = i + 2)
        {
            if (text.charAt(i + 1) == text.charAt(i))
            {
                text = text.substring(0, i + 1) + 'x' + text.substring(i + 1);
            }
        }
        return text;
    }
 
    private String[] DivPair(String new_string)
    {
        String Plaintext = format(new_string);
        int size = Plaintext.length();
        if (size % 2 != 0)
        {
            size++;
            Plaintext = Plaintext + 'x'; //Adds an x in between when the pair is same letter.
        }
        String x[] = new String[size / 2];
        int counter = 0;
        for (int i = 0; i < size / 2; i++)
        {
            x[i] = Plaintext.substring(counter, counter + 2); //Divides characters into pairs.
            counter = counter + 2;
        }
        return x;
    }
 
    public int[] GetDimensions(char letter) // Finds out the positions of characters a and b.
    {
        int[] key = new int[2];
        if (letter == 'j')
            letter = 'i';
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (mat_array[i][j] == letter)
                {
                    key[0] = i;
                    key[1] = j;
                    break;
                }
            }
        }
        return key;
    }
 
     public String decryptMessage(String Code)
    {
        String Plaint = new String();
        String src_arr[] = DivPair(Code);
        char a;
        char b;
        int row[] = new int[2];
        int col[] = new int[2];
        for (int i = 0; i < src_arr.length; i++)
        {
            a = src_arr[i].charAt(0);
            b = src_arr[i].charAt(1);
            row = GetDimensions(a);
            col = GetDimensions(b);
            if (row[0] == col[0]) //if the characters are on the same row, then select the two characters to the left of each
            {
                if (row[1] > 0)
                    row[1]--;
                else
                    row[1] = 4;
                if (col[1] > 0)
                    col[1]--;
                else
                    col[1] = 4;
            }
            else if (row[1] == col[1]) // else if the characters are on the same column, then select the two characters above
            {
                if (row[0] > 0)
                    row[0]--;
                else
                    row[0] = 4;
                if (col[0] > 0)
                    col[0]--;
                else
                    col[0] = 4;
            }
            else // else the two characters are in different rows and columns, so create a square using the two points,
    			// and select the two characters that create the two other points.
            {
                int temp = row[1];
                row[1] = col[1];
                col[1] = temp;
            }
            Plaint = Plaint + mat_array[row[0]][row[1]]
                    + mat_array[col[0]][col[1]];
        }
        return Plaint;
    }
    
    public static void main(String[] args)
    {
        PlayFairDecrypt x = new PlayFairDecrypt();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter key: ");
        String keyword = sc.next();
        x.setKey(keyword);
        x.KeyGen();
        System.out.println("Enter ciphertext: ");
        String key_input = sc.next();
        System.out.println("Decryption: " + x.decryptMessage(key_input));
        sc.close();
    }
}
