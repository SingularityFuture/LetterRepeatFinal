package com.example.michael.letterrepeat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2/2/2017.
 *
 * Run processing on each string in list to determine number repeats.
 */

public class Processor {
    private ArrayList<String> results = new ArrayList<>(); // Declare list of simulation results


    public ArrayList<String> runProcessor(List<String> stringList, int lookback){

        // TODO: implement algorithm :)
        for(String stringItem : stringList){ // For each string in the list
            int[] characterCount=new int[26]; // Keep track of the count of each character; initialize at 0
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Store the alphabet
            boolean[] countedAlready=new boolean[stringItem.length()]; // Keep track if each character has been counted yet; initialized to false

            for(int location=stringItem.length()-1; location>=0; location--) { // Iterate through each index of the string
                char currentChar = stringItem.charAt(location); // Get the current letter
                if ((Character.toUpperCase(currentChar) >= 'A' && Character.toUpperCase(currentChar) <= 'Z')){ // Check if it's an English letter
                    for(int lookIndex=1;lookIndex<=lookback && (location-lookIndex)>=0;lookIndex++) { // Look back over the allowed territory without going past 0
                        char currentLookChar = stringItem.charAt(location-lookIndex); // Find the char you're looking at
                        if(currentChar==currentLookChar && !countedAlready[location-lookIndex]){ // If it matches the current character and hasn't been counted already
                            characterCount[alphabet.indexOf(Character.toUpperCase(currentChar))]++; // Find the index of our count array and increment it
                            countedAlready[location-lookIndex]=true; // And then make sure you tell countedAlready that this has been counted
                        }
                    }
                }
            }
            results.add(stringItem + "  Results: ");
            for(int alpha=0;alpha<characterCount.length;alpha++){
                results.add(String.valueOf(alphabet.charAt(alpha))+":"+String.valueOf(characterCount[alpha]));
            }
        }
        return results;
    }
}
