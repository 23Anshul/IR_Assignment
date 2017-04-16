package project2;

import java.io.*;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class cosine_similarity {

    /**
     *
     * @param name_doc1
     * @param name_doc2
     * @throws FileNotFoundException
     */
    public static int intersect(String str1, String str2)
    {
        int cnt=0;
        List<String> wordsOfSecond = Arrays.asList(str2.split(" "));
        for (String word : str1.split(" ")) 
        {
            if(wordsOfSecond.contains(word))
            {
                    //System.out.println(word);
                    cnt++;
            }
        }
        //System.out.println("count="+cnt);
        return cnt;
    }
    
    public static int union(String str1, String str2)
    {
        String strunion=str1+" "+str2;
        int commonwords=intersect(str1,str2);
        List<String> wordsOfStrunion = Arrays.asList(strunion.split(" "));
        int unionwords=wordsOfStrunion.size()-commonwords;
        //System.out.println("union="+unionwords);
        return (unionwords);
    }
    /*public static void PrintArr(String arr[], int r)
    {
        System.out.println("print arr");
        for(int i=0;i<r;i++)
            System.out.println(arr[i]);
    }*/
    public static void PrintArr(float arr[][], int r)
    {
        System.out.println("print arr");
        for(int i=0;i<r;i++)
            System.out.println(arr[i][0]+" "+arr[i][1]);
    }
    public static float cosine_sim(String str1, String str2)
    {
        String combined=str1+" "+str2;
        //List<String> listofWords = Arrays.asList(combined.split(" "));
        //System.out.println(combined);
        Set<String> set = new HashSet<>(Arrays.asList(combined.split(" ")));
        String [] wordsOfCombined = set.toArray(new String[set.size()]);
        //PrintArr(wordsOfCombined,wordsOfCombined.length);
        List<String> wordsOfstr1 = Arrays.asList(str1.split(" "));
        List<String> wordsOfstr2 = Arrays.asList(str2.split(" "));
        float[][] tfmatrix=new float[wordsOfCombined.length][2];
        
        for (int i=0;i<wordsOfCombined.length;i++)
        {
            for(int j=0;j<wordsOfstr1.size();j++)
                if(wordsOfCombined[i].equals(wordsOfstr1.get(j)))
                    tfmatrix[i][0]=(float) (tfmatrix[i][0]+1.0);
            for(int j=0;j<wordsOfstr2.size();j++)
                if(wordsOfCombined[i].equals(wordsOfstr2.get(j)))
                    tfmatrix[i][1]=(float) (tfmatrix[i][1]+1.0);
        }
        //PrintArr(tfmatrix,wordsOfCombined.length);
        for (int i=0;i<wordsOfCombined.length;i++)
        {
            tfmatrix[i][0]=(tfmatrix[i][0]==0?0:(float) (1+log(tfmatrix[i][0])));
            tfmatrix[i][1]=(tfmatrix[i][1]==0?0:(float) (1+log(tfmatrix[i][1])));
        }
        
        float Lnorm1=0.0f,Lnorm2=0.0f;
        for (int i=0;i<wordsOfCombined.length;i++)
        {
            Lnorm1+=(tfmatrix[i][0]*tfmatrix[i][0]);
            Lnorm2+=(tfmatrix[i][1]*tfmatrix[i][1]);
        }
        Lnorm1=(float) pow(Lnorm1,0.5);
        Lnorm2=(float) pow(Lnorm2,0.5);
        for (int i=0;i<wordsOfCombined.length;i++)
        {
            tfmatrix[i][0]=(float)tfmatrix[i][0]/Lnorm1;
            tfmatrix[i][1]=(float)tfmatrix[i][1]/Lnorm2;
        }
        float cosSim=0f;
        for (int i=0;i<wordsOfCombined.length;i++)
        {
            cosSim+=tfmatrix[i][0]*tfmatrix[i][1];
        }
       // System.out.println("cosine_sim()="+cosSim);
        return cosSim;
    }
    public static void ComputeSimilarity(String name_doc1, String name_doc2) throws FileNotFoundException,IOException
    {
        //File file1 = new File(name_doc1);
        //File file2 = new File(name_doc2);
        BufferedReader br1 = new BufferedReader(new FileReader(name_doc1));
        BufferedReader br2 = new BufferedReader(new FileReader(name_doc2));
        //Scanner doc1;
        //doc1 = new Scanner(file1);
        //doc1.useDelimiter(".");
        //Scanner doc2=new Scanner(file2);
        //doc2.useDelimiter(".");
        float JC=0,numLines=0,cosSim=0;
        while(true)
        {
            
            String str1=br1.readLine();
            if(str1 == null)
            {
            	break;
            }
            
            str1=str1.toLowerCase();
            StringTokenizer st = new StringTokenizer(str1," .,;][)\"(&?^%*#@!");
            StringBuilder sb = new StringBuilder();
            while(st.hasMoreTokens())
            {
            	sb.append(st.nextToken());
            	sb.append(" ");
            }
            str1 = sb.toString();
            //System.out.println(str1);
            String str2=br2.readLine();
            if(str2 == null)
            {
            	break;
            }
            numLines++;
            str2=str2.toLowerCase();
            StringTokenizer st2 = new StringTokenizer(str2," .,;][)\"(&?^%*#@!");
            StringBuilder sb2 = new StringBuilder();
            while(st2.hasMoreTokens())
            {
            	sb2.append(st2.nextToken());
            	sb2.append(" ");
            }
            str2 = sb2.toString();
            //System.out.println(str2);
            JC+=intersect(str1,str2)/(float)union(str1,str2);
            cosSim+=cosine_sim(str1,str2);
            
        }
        System.out.println("Jaccard Coefficient="+JC/numLines);
        System.out.println("Cosine similarity="+cosSim/numLines);
        System.out.println();
        System.out.println();
        
    }
    
    public static void main(String[] args) throws FileNotFoundException,IOException {
        String s1="french_final_test.txt";
        String s2="french_final_test_answer.txt";
        
        ComputeSimilarity(s1, s2);

    }
}


