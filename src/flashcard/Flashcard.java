/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author Matthew
 */
public class Flashcard {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String url = "https://studyrocket.co.uk/revision/gcse-biology-triple-aqa/triple-cell-biology/osmosis-active-transport";
        try {
            String pageContent="";
            
            ArrayList pageContentList = new ArrayList();
            final Document doc = Jsoup.connect(url).get();
            Elements para= doc.select("p");
            for(Element p : para)
              pageContent = pageContent.concat(p.text());  
            int first=0;
            int second =0;
             for (int i = 0; i < pageContent.length(); i++) {
                
                 if (pageContent.charAt(i)=='.') {
                     first=second;
                     second=i+1;
                     pageContentList.add(pageContent.substring(first, second));
                     
                 }
            }
             //removes all leading white space within each element
             for (int i = 0; i < pageContentList.size(); i++) {
                 String temp = pageContentList.get(i).toString().trim();
                 pageContentList.set(i, temp);
            }
             
             //checks for sentences with missing fullstops
             for (int i = 0; i < pageContentList.size(); i++) {
                    String temp = Objects.toString(pageContentList.get(i));
                    
                    Pattern pattern = Pattern.compile("[a-z][A-Z]");
                    Matcher matcher = pattern.matcher(temp);
                    boolean matchFound = matcher.find();

                    if(matchFound) {
                        
                      int breakPoint = 0;
                        for (int j = 1; j < temp.length(); j++) {
                            if (Character.isLowerCase(temp.charAt(j-1)) && Character.isUpperCase(temp.charAt(j))) {
                                breakPoint = j;
                            }
                        }
                        
                    pageContentList.add(temp.substring(0, breakPoint));
                    pageContentList.add(temp.substring(breakPoint, temp.length()-1));
                    pageContentList.remove(i);
                    } 
                    
                    else {
                      
                    }
             }
           
             FileOutputStream fos = new FileOutputStream("D:\\FlashcardExport.csv");
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             CSVWriter writer = new CSVWriter(osw);
             
             for (int i = 0; i < pageContentList.size(); i++) {
                 int position = 0;
                 String currentSentence = Objects.toString(pageContentList.get(i));
                 
                 String[] listOfIdentifiers = {"produced from","which undergo","are considered","are normally","by a","because","followed by","which has","caused by"};
                 
                 for (int j = 0; j < listOfIdentifiers.length; j++) {
                     
                     if (currentSentence.contains(listOfIdentifiers[j])) {
                        position = currentSentence.indexOf(listOfIdentifiers[j])+listOfIdentifiers[j].length();
                        String[] jawn = {currentSentence.substring(0, position)+"...",currentSentence.substring(position+1, currentSentence.length())};

                       
                       writer.writeNext(jawn);
                       
                 }
                 }
             }
             writer.close();
             
             
             //prints out the list with spaces
             for (int i = 0; i < pageContentList.size(); i++) {
                 System.out.println(pageContentList.get(i));
                 System.out.println("\n");
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
