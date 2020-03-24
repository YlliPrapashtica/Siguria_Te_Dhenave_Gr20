package playfaircipher2;
 
import java.util.Scanner;
 
public class PlayfairCipherDecrypt
{
    private String Keyword = new String();
    private String Key = new String();
    private char   mat_array[][] = new char[5][5];
 
    public void setKey(String k)
    {
        String K_fit = new String();
        boolean SIG = false;
        K_fit = K_fit + k.charAt(0);
        for (int i = 1; i < k.length(); i++)
        {
            for (int j = 0; j < K_fit.length(); j++)
            {
                if (k.charAt(i) == K_fit.charAt(j))
                {
                    SIG = true;
                }
            }
            if (SIG == false)
                K_fit = K_fit + k.charAt(i);
            SIG = false;
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
            current = (char) (i + 97);
            if (current == 'j')
                continue;
            for (int j = 0; j < Keyword.length(); j++)
            {
                if (current == Keyword.charAt(j))
                {
                    flag = false;
                    break;
                }
            }
            if (flag)
                Key = Key + current;
            flag = true;
        }
        matrix();
    }
 
    private void matrix()
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
            if (plaintxt.charAt(tmp) == 'j')
            {
                text = text + 'i';
            }
            else
                text = text + plaintxt.charAt(tmp);
        }
        len = text.length();
        for (i = 0; i < len; i = i + 2)
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
            Plaintext = Plaintext + 'x';
        }
        String x[] = new String[size / 2];
        int counter = 0;
        for (int i = 0; i < size / 2; i++)
        {
            x[i] = Plaintext.substring(counter, counter + 2);
            counter = counter + 2;
        }
        return x;
    }
 
    public int[] GetDimensions(char letter)
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
        String Original = new String();
        String src_arr[] = DivPair(Code);
        char one;
        char two;
        int part1[] = new int[2];
        int part2[] = new int[2];
        for (int i = 0; i < src_arr.length; i++)
        {
            one = src_arr[i].charAt(0);
            two = src_arr[i].charAt(1);
            part1 = GetDimensions(one);
            part2 = GetDimensions(two);
            if (part1[0] == part2[0])
            {
                if (part1[1] > 0)
                    part1[1]--;
                else
                    part1[1] = 4;
                if (part2[1] > 0)
                    part2[1]--;
                else
                    part2[1] = 4;
            }
            else if (part1[1] == part2[1])
            {
                if (part1[0] > 0)
                    part1[0]--;
                else
                    part1[0] = 4;
                if (part2[0] > 0)
                    part2[0]--;
                else
                    part2[0] = 4;
            }
            else
            {
                int temp = part1[1];
                part1[1] = part2[1];
                part2[1] = temp;
            }
            Original = Original + mat_array[part1[0]][part1[1]]
                    + mat_array[part2[0]][part2[1]];
        }
        return Original;
    }
    
 
    public static void main(String[] args)
    {
        PlayfairCipherDecrypt x = new PlayfairCipherDecrypt();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter key: ");
        String keyword = sc.next();
        x.setKey(keyword);
        x.KeyGen();
        System.out
                .println("Enter ciphertext: ");
        String key_input = sc.next();
        if (key_input.length() % 2 == 0)
        {
            System.out.println("Decryption: " + x.decryptMessage(key_input));
            
        }
        else
        {
            System.out.println("Message length is not even!");
        }
        sc.close();
    }
}
