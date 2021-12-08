package com.wordcount;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/*
  @author  Ahmed Salem
 */

public class Wordcount {


    private static Map <Integer, Integer> occurence_map = null;


    public Wordcount(){
        getOccurenceMap();
    }


    // get singleton occurence_map instance

    public Map <Integer, Integer> getOccurenceMap(){
        if(null == occurence_map)
            occurence_map = new HashMap<Integer, Integer>();

        return occurence_map;
    }



   // reads the word file line by line
     private void readText(String fileStrLoc){

        File file = new File(fileStrLoc);

        try{
            FileReader reader = new FileReader(file);

            BufferedReader bfr = new BufferedReader(reader);

            String line = bfr.readLine();

            int wordCount = 0;

            while(null != line){
//                System.out.println(line);
                wordCount += countWordsInText(line);
                line =  bfr.readLine();
            }

            Map<Integer, Integer> occurence_map = getOccurenceMap();

            String avgLength_str = findAverageLength(occurence_map);

            int mostFrequent = findMostFrequent(occurence_map);

            List <Integer> mostFrequentList = getMostFrequentList(mostFrequent);

            printSummary(wordCount, avgLength_str, mostFrequent, mostFrequentList);

        }catch(IOException ioex){
            System.out.println(ioex.getMessage());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }


    // counts words in the line & updates chars occurence count
    public int countWordsInText(String line){

        int wordCount = 0;

        line = Util.removeSpecialChars(line);

        if(null != line){

            char [] ch_arr = line.toCharArray();

            int charCount = 0;

            for(int i = 0;  i < ch_arr.length; i++){

                // if not space char

                if(!Character.isSpace(ch_arr[i]) && ! Character.isSpaceChar(ch_arr[i])){
                    charCount++;
                    // if last word in line
                    if(ch_arr.length - i == 1){

                        // update occurence map
                        updateOccurenceMap(charCount);
                    }

                }else{
                    // if space char - presumably this is end of word

                    if(charCount > 0){
                        wordCount++;

                        // update occurence map
                        updateOccurenceMap(charCount);

                        // reset charCount
                        charCount = 0;
                    }
                }

            }

            if(charCount > 0){
                wordCount+=1;
            }
        }

        return wordCount;
    }



    // updates occurenece map
     void updateOccurenceMap(int charCount){

        Integer occurence_int = getOccurenceMap().get(charCount);

        if(null != occurence_int){
            occurence_map.replace(charCount, ++ occurence_int);
        }else{
            occurence_map.put(charCount, 1);
        }
    }



    // calculates avg length
     public String findAverageLength(Map<Integer, Integer> occurence_map){

        Set <Integer> wordLengths_set =  occurence_map.keySet();

        int size= wordLengths_set.size();

        String avg_str = "";

        if(size > 0){

            double sum = wordLengths_set.stream().mapToInt(i -> i.intValue()).sum();

            double avg = sum / size;
            DecimalFormat df = new DecimalFormat("0.00");
            avg_str  = df.format(avg);
        }
        return avg_str;
    }



  // finds most frequent chars occurence
    public int findMostFrequent(Map<Integer, Integer> occurence_map){
        Optional<Entry<Integer, Integer>> MostFrequent = occurence_map.entrySet().stream()
                .max((Entry<Integer, Integer> e1, Entry<Integer, Integer> e2) -> e1.getValue()
                        .compareTo(e2.getValue())
                );

        return MostFrequent.get().getValue();
    }


//     private int findMostFrequent(){
//
//        Set <Entry<Integer, Integer>> entrySet =  getOccurenceMap().entrySet();
//
//        int size= entrySet.size();
//        int biggest  = 0;
//
//        if(size > 0){
//
//            Iterator iterator = entrySet.iterator();
//
//            int index = 0;
//
//            while(iterator.hasNext()){
//
//                Entry <Integer, Integer> entry = (Entry<Integer, Integer>) iterator.next();
//                int value = entry.getValue();
//
//                // init biggest with first in iterator
//                if(index <= 0){
//                    biggest = value;
//                }else{
//                    if(value > biggest){
//                        biggest = value;
//                    }
//                }
//                // iterate index
//                index ++;
//            }
//        }
//        return biggest;
//    }


    // gets most frequent occurences' list
    private List<Integer> getMostFrequentList(int biggest){

        Set <Entry<Integer, Integer>> entrySet =  getOccurenceMap().entrySet();

        List<Integer> key_list = null;

        int size= entrySet.size();


        if(size> 0){

            key_list = new ArrayList<Integer>();

            Iterator iterator = entrySet.iterator();
            // find the values that corresponds to the biggest
            while(iterator.hasNext()){

                Entry <Integer, Integer> entry = (Entry<Integer, Integer>) iterator.next();
                int value = entry.getValue();
                int key = entry.getKey();

                if(value == biggest){
                    key_list.add(key);
                }
            }
        }
        return key_list;
    }



      // out.print results
     private void printSummary(int wordCount, String avgLength_str, int mostFrequent, List<Integer> mostFrequentList){

        Set <Entry<Integer, Integer>> entrySet = getOccurenceMap().entrySet();

        System.out.println("Word count= " + wordCount);
        System.out.println("Average word length= " + avgLength_str);

         int size= entrySet.size();

        if(size > 0){

            Iterator iterator = entrySet.iterator();

            while(iterator.hasNext()){

                Entry <Integer, Integer> entry = (Entry<Integer, Integer>) iterator.next();
                int key = entry.getKey();
                int value = entry.getValue();
                System.out.println("Number of words of length " + entry.getKey() + " is " + entry.getValue());
            }

            System.out.println("The most frequently occurring word length is " + mostFrequent + ", for word lengths of ");
            mostFrequentList.stream().forEach(System.out::println);
        }

    }


    // main
    public static void main(String[]args){
        Wordcount wordCount = new Wordcount();

        // reads file path from config.properties
        try (InputStream input = Wordcount.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Unable to find config.properties");
                return;
            }

            prop.load(input);

            wordCount.readText(prop.getProperty("file.path"));

        } catch (IOException ioex) {
            ioex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}