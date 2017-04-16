package project2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.*;

public class Converter {

	public static void main(String[] args) throws IOException {
				//Hashing strings to integer -- English
				//Each String is given an integer ID
				HashMap<String,Integer> eng_str_int = new HashMap<String,Integer>();
				//hashing integer  back to string  -- English
				HashMap<Integer,String> eng_int_str = new HashMap<Integer,String>();
				
				//Hashing strings to integer -- French
				//Each String is given an integer ID
				HashMap<String,Integer> fren_str_int = new HashMap<String,Integer>();
				//Hashing integer to string -- French
				HashMap<Integer,String> fren_int_str = new HashMap<Integer,String>();
				
				//ArrayList to store all sentences in english corpus
				ArrayList<ArrayList<Integer>> eng_sentence = new ArrayList<ArrayList<Integer>>();
				
				//ArrayList to store all sentences in french corpus
				ArrayList<ArrayList<Integer>> fren_sentence = new ArrayList<ArrayList<Integer>>();
				
				BufferedReader br = new BufferedReader(new FileReader("english.txt"));
				
				//Total number of words in the english corpus is stored in eng_counter;
				int eng_counter=0;
				
				while(true){
					ArrayList<Integer> eng_temp = new ArrayList<Integer>();
					String line = br.readLine();
					if(line==null){
						break;
					}
					StringTokenizer st = new StringTokenizer(line," .,;][)\"'(&?-=^%*#@!");
					
					while(st.hasMoreTokens()){
						
						//Converting all the words to lowercase
						//Reading all the words in the current line
						String word = st.nextToken().toLowerCase();
						
						//Assigning each word an integer ID
						if(!eng_str_int.containsKey(word)){
							//Putting into the english word to integer ID hashmap
							eng_str_int.put(word, eng_counter);
							//Putting into the integer ID to enlish word hashmap
							eng_int_str.put(eng_counter, word);
							//Incrementing the word number
							eng_counter++;
						}
							//Adding the word into the current sentence
							eng_temp.add(eng_str_int.get(word));
					}
					//Adding the current sentence into the final list
					eng_sentence.add(eng_temp);
				}
				
				br = new BufferedReader(new FileReader("french.txt"));
				//Total number of words in the french corpus is stored in fren_counter;
				int fren_counter=0;
				
				while(true){
					ArrayList<Integer> fren_temp = new ArrayList<Integer>();
					String line = br.readLine();
					if(line==null){
						break;
					}
					StringTokenizer st = new StringTokenizer(line," .,;][)\"'(&?^-=%*#@!");
					while(st.hasMoreTokens()){
						
						//Converting all the words to lowercase
						//Reading all the words in the current line
						String word = st.nextToken().toLowerCase();
						
						//Assigning each word an integer ID
						if(!fren_str_int.containsKey(word)){
							//Putting into the english word to integer ID hashmap
							fren_str_int.put(word, fren_counter);
							//Putting into the integer ID to enlish word hashmap
							fren_int_str.put(fren_counter, word);
							//Incrementing the word number
							fren_counter++;
						}
						
							//Adding the word into the current sentence
							fren_temp.add(fren_str_int.get(word));	
					}
					//Adding the current sentence into the final list
					fren_sentence.add(fren_temp);
				}	
				
				//HashMap to store the various english words and their corresponding french translations 
				//and the corresponding probabilities for the french translation
				HashMap<Integer,HashMap<Integer,Double>> eng_to_fren = new HashMap<Integer,HashMap<Integer,Double>>();
				//HashMap to store the various french words and their corresponding english translations 
				//and the corresponding probabilities for the english translation
				HashMap<Integer,HashMap<Integer,Double>> fren_to_eng = new HashMap<Integer,HashMap<Integer,Double>>();
				
				//Running the loop on all the training data
				for (int i = 1; i <= 77; i++) {
					br=new BufferedReader(new FileReader("/Users/Anshul_Chhabra/Desktop/IR_Assignment/Pre_Processed_Files/" + "f"+i+".txt"));
					while(true){
						String w=br.readLine();
						if(w==null){
							break;
						}
						String s[]=w.split(" ");
						
						//Training files result contains 3 columns
						//column 1 is the english word
						int eng = Integer.parseInt(s[0]);
						//column 2 is the french word
						int fren = Integer.parseInt(s[1]);
						//column 3 is the probability
						double prob = Double.parseDouble(s[2]);
						
						//Putting into the hashmap and putting the corresponding probability for that translation
						if(eng_to_fren.containsKey(eng)){
							HashMap<Integer,Double> temp = eng_to_fren.get(eng);
							if(temp.containsKey(fren)){
								double count=temp.get(fren);
								//count++;
								temp.put(fren, count + prob);
							}
							else{
								temp.put(fren, prob);
							}
						}
						else{
							eng_to_fren.put(eng, new HashMap<Integer,Double>());
							eng_to_fren.get(eng).put(fren, prob) ;
						}
						
						//Putting into the hashmap and putting the corresponding probability for that translation
						if(fren_to_eng.containsKey(fren)){
							HashMap<Integer,Double> temp = fren_to_eng.get(fren);
							if(temp.containsKey(eng)){
								double count=temp.get(eng);
								//count++;
								temp.put(eng, count + prob);
							}
							else{
								temp.put(eng, prob);
							}
						}
						else{
							fren_to_eng.put(fren, new HashMap<Integer,Double>());
							fren_to_eng.get(fren).put(eng, prob) ;
						}
					}
				}
				
				//Final translation of the english word to french word is stored in this HashMap
				//The french word which has maximum probability is kept finally
				HashMap<Integer,Integer> final_eng_to_fren = new HashMap<Integer,Integer>();
				
				//Final translation of the french word to english word is stored in this HashMap
				//The english word which has maximum probability is kept finally
				HashMap<Integer,Integer> final_fren_to_eng = new HashMap<Integer,Integer>();
				
				//Running the loop and finding the word with maximum probability
				for(int eng : eng_to_fren.keySet()){
					double max=0;
					int i=-1;
					int j=-1;
					for(int fren : eng_to_fren.get(eng).keySet()){
						if(eng_to_fren.get(eng).get(fren) > max){
							max=eng_to_fren.get(eng).get(fren);
							i=eng;
							j=fren;
						}
					}
					//Checking if the word has translation or not
					//If it does not have translation
					//Then it nothing is put in hashmap
					if(i!=-1 && j!=-1){
						final_eng_to_fren.put(i,j);
						final_fren_to_eng.put(j, i);
					}
				}
				
				while(true)
				{
					//Menu option for the user
					System.out.println("Enter 1 if you want to continue");
					System.out.println("Enter 0 if you want to exit");
					BufferedReader finalinput = new BufferedReader(new InputStreamReader(System.in));
					int input = Integer.parseInt(finalinput.readLine());
					if(input == 0)
					{
						break;
					}
					
					System.out.println("Enter 1 to convert english to french");
					System.out.println("Enter 2 to convert french to english");
					input = Integer.parseInt(finalinput.readLine());
					if(input == 1)
					{
						System.out.println("Please Enter the file name");
						String testcase = finalinput.readLine();
						//Inputting the test file
						BufferedReader br_final_read = new BufferedReader(new FileReader(testcase));
						//Outputting the result file
						BufferedWriter br_final_write = new BufferedWriter(new FileWriter("Translated.txt"));
						while(true){
							
							String line = br_final_read.readLine();
							//System.out.println(line);
							if(line==null){
								break;
							}
							StringTokenizer st = new StringTokenizer(line," .,;][)\"'(&?-=^%*#@!");
							while(st.hasMoreTokens()){
								//Getting the tranlastion for the word from the hashmap
								String word = st.nextToken().toLowerCase();
								int eng = eng_str_int.get(word);
								if(final_eng_to_fren.containsKey(eng)){
									//Wrinting the tranlation into the file
									br_final_write.write(fren_int_str.get(final_eng_to_fren.get(eng))+" ");
								}
							}
							//Writing newline to the file
							br_final_write.newLine();
						}
						//Closing the file
						br_final_write.close();
						
						//Outputing the cosine similarity
						cosine_similarity cs = new cosine_similarity();
						cs.ComputeSimilarity("Translated.txt","Answer.txt");
					}
					else
					{
						System.out.println("Please Enter the file name");
						String testcase = finalinput.readLine();
						//Inputting the test file
						BufferedReader br_final_read = new BufferedReader(new FileReader(testcase));
						//Outputting the result file
						BufferedWriter br_final_write = new BufferedWriter(new FileWriter("Translated.txt"));
						while(true){
							
							String line = br_final_read.readLine();
							//System.out.println(line);
							if(line==null){
								break;
							}
							StringTokenizer st = new StringTokenizer(line," .,;][)\"'(&?-=^%*#@!");
							while(st.hasMoreTokens()){
								//Getting the tranlastion for the word from the hashmap
								String word = st.nextToken().toLowerCase();
								int fre = fren_str_int.get(word);
								if(final_fren_to_eng.containsKey(fre)){
									//Wrinting the tranlation into the file
									br_final_write.write(eng_int_str.get(final_fren_to_eng.get(fre))+" ");
								}
							}
							//Writing newline to the file
							br_final_write.newLine();
						}
						//Closing the file
						br_final_write.close();
						
						//Outputing the cosine similarity
						cosine_similarity cs = new cosine_similarity();
						cs.ComputeSimilarity("Translated.txt","Answer.txt");
					}
					
				}
				
				
				
	}

}

