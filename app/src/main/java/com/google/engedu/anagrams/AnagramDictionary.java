/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static String TAG = "jks";
    private static List<String> words = new ArrayList<String>();
    private static HashSet<String> wordSet = new HashSet<String>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            addWord(word);
        }
    }

    public void addWord(String word) {
        if (!lettersToWord.containsKey(sortLetters(word))) {
            lettersToWord.put(sortLetters(word), new ArrayList<String>());
        } else {
            ArrayList<String> temp = lettersToWord.get(sortLetters(word));
            temp.add(word);
            lettersToWord.put(sortLetters(word), temp);
        }
    }
    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }
        if (word.contains(base)){
            return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        if (wordSet.contains(targetWord)) {
            result = lettersToWord.get(sortLetters(targetWord));
        }
        return result;
    }

    public String sortLetters(String str) {
        char[] temp = str.toCharArray();
        Arrays.sort(temp);
        return String.valueOf(temp);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<String>();
            for (char character = 'a'; character <= 'z'; character++) {
                String temp = sortLetters(word + character);
                result.addAll(getAnagrams(temp));
            }

        return result;
    }

    public String pickGoodStarterWord() {
        String word;
        List<String> anagrams = new ArrayList<>();

        do
        {
            word = words.get(Math.abs(random.nextInt()) % words.size());
            if (word.length() < DEFAULT_WORD_LENGTH || word.length() > MAX_WORD_LENGTH)
            {
                continue;
            }
            anagrams = getAnagramsWithOneMoreLetter(word);
        } while (anagrams.size() < MIN_NUM_ANAGRAMS);
        return word;

    }
}
