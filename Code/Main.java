package project2;

import java.util.*;
import java.io.*;
public class Main {

	public static void main(String[] args)throws IOException {
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
		
		//All sentences in english corpus
		ArrayList<ArrayList<Integer>> eng_sentence = new ArrayList<ArrayList<Integer>>();
		
		//All sentences in french corpus
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
		
		//System.out.println(eng_sentence.size()+" "+fren_sentence.size());
		
		//Initializing all the t(e|f)
		double initial = 1.0/eng_str_int.keySet().size();
		HashMap<Integer,HashMap<Integer,Double>> words = new HashMap<Integer,HashMap<Integer,Double>>();
		for (int j = 30000; j < 40000; j++) {
			for(int eng : eng_sentence.get(j)){
				for (int fren : fren_sentence.get(j)) {
					if(words.containsKey(eng)){
						HashMap<Integer,Double> temp = words.get(eng);
						if(!temp.containsKey(fren)){
							temp.put(fren,initial);
						}
					}
					else{
						words.put(eng, new HashMap<Integer,Double>());
						HashMap<Integer,Double> temp = words.get(eng);
						if(!temp.containsKey(fren)){
							temp.put(fren, initial);
						}
					}
				}
			}
		}
		
		
		
		

		for(int i=0;i<20;i++){
			//System.out.println(i);
			HashMap<ArrayList<Integer>,pair> count_help = new HashMap<ArrayList<Integer>,pair>();
			HashMap<pair,Double> count=new HashMap<pair,Double>();
			for (int j = 30000; j < 40000; j++) {
				for(int eng : eng_sentence.get(j)){
					
					for (int fren : fren_sentence.get(j)) {
						pair p = new pair(eng,fren);
						if(!count.containsKey(p)){
							count.put(p, 0.0);
							ArrayList<Integer> temp=new ArrayList<Integer>();
							temp.add(eng);
							temp.add(fren);
							count_help.put(temp, p);
						}
					}
				}
			}
			
			HashMap<Integer,Double> total=new HashMap<Integer,Double>();
			for(String s : fren_str_int.keySet()){
				if(!total.containsKey(s)){
					total.put(fren_str_int.get(s), 0.0);
				}
			}
			
			for (int j = 30000; j < 40000; j++) {
				HashMap<Integer,Double> stotal = new HashMap<Integer,Double>();
				for(int eng : eng_sentence.get(j)){
					if(!stotal.containsKey(eng))
						stotal.put(eng, 0.0);
					for (int fren : fren_sentence.get(j)) {
						double d = stotal.get(eng);
						d+=words.get(eng).get(fren);
						stotal.put(eng,d);
					}
				}
				
				
				for(int eng : eng_sentence.get(j)){
					for (int fren :  fren_sentence.get(j)) {
						//System.out.println("hulli");
						
						pair p = new pair(eng,fren);
						ArrayList<Integer> temp=new ArrayList<Integer>();
						temp.add(eng);
						temp.add(fren);
						//System.out.println(p.a +" "+p.b);
						p=count_help.get(temp);
						double d = count.get(p);
						d+=(words.get(eng).get(fren)/stotal.get(eng));
						count.put(p,d);
						double d2=total.get(fren);
						d2+=(words.get(eng).get(fren)/stotal.get(eng));
						total.put(fren,d2);
					}
				}
				
				
			}
			for(int j=30000;j< 40000;j++){
				for (int fren : fren_sentence.get(j)) {
					for (int eng : eng_sentence.get(j)) {
						pair p = new pair(eng,fren);
						
						//System.out.println();
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(p.a);
						temp.add(p.b);
						p=count_help.get(temp);
						if(count.containsKey(p) && total.containsKey(fren)){
							//System.out.println("hi");
							words.get(eng).put(fren,count.get(p)/total.get(fren));
							
						}
					}
				}
			}
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("f4.txt"));
		for(int h : words.keySet()){
			double max=0.0;
			for(int k : words.get(h).keySet()){
				if(words.get(h).get(k) > max){
					max=words.get(h).get(k);
				}
				
				//System.out.println(eng_int_str.get(h)+" "+fren_int_str.get(k)+" = "+words.get(h).get(k));
			}
			for(int k : words.get(h).keySet()){
				if(words.get(h).get(k).doubleValue() == max){
					String temp=h+" "+k+" "+words.get(h).get(k);
					System.out.println(eng_int_str.get(h)+" "+fren_int_str.get(k)+" "+words.get(h).get(k));
					bw.write(temp);
					bw.newLine();
					
				}
			}
		}
		bw.close();
		//System.out.println("Naacho");
	}
}

class pair{
	int a;
	int b;
	pair(int a,int b){
		this.a=a;
		this.b=b;
	}
}