package com.example.projectmobilecharactergenerator;

import java.util.Random;

public class NameGeneratorAtHome {
    public static String getName(int maxLength, int readability)
    {
        //Declaring Arrays
        char[] consonants = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };
        char[] vowels = { 'a', 'e', 'i', 'o', 'u' };

        Random random = new Random();
        String name = "";
        //char letter starts at int 0
        char letter = '0';
        char temp = '0';
        //How many guaranteed vowels + consonants chunks in the name
        int length = random.nextInt(maxLength) + 2;

        //Repeats for each Syllable
        for (int syllableLoop = 0; syllableLoop < length; syllableLoop++)
        {
            int syllable = random.nextInt(readability) + 1;

            //For each letter in the syllable
            for (int letterLoop = 0; letterLoop < syllable; letterLoop++)
            {
                temp = (char)(random.nextInt(26) + 97);
                while (temp ==  letter)
                    temp = (char)(random.nextInt(26) + 97);

                letter = temp;
                if (name == "")
                    name += Character.toUpperCase(letter);
                else
                    name += letter;
            }
            temp = vowels[random.nextInt(vowels.length)];
            while (temp == letter)
                temp = vowels[random.nextInt(vowels.length)];
            letter = temp;
            name += letter;
        }

        //Adds a consonant at the end of the name maybe
        if (random.nextInt(2) == 1)
            name += consonants[random.nextInt(consonants.length)];

        return name;
    }
}
