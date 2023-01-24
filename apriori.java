package dbms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class apriori
{
    static List <Set<String>> Database = new ArrayList<>();
    static HashMap<Set<String>, Integer> result=new HashMap<Set<String>, Integer>();
    public static void main(String[] args) throws IOException
    {
        int minSupCount=0;
        String confidence;
        System.out.println("Enter the minimum support count: ");
        Scanner sc=new Scanner(System.in);
        minSupCount=sc.nextInt();
        addTransactions("C:\\Users\\User\\Desktop\\Book.csv");
        uniqueItems(minSupCount);
        generateFinalResult(minSupCount);
        System.out.println("Do you want to calculate confidence? ");
        confidence=sc.next();
        if(confidence.equals("yes"))
        {confidence();}
        else
        {System.out.println("Done");}
    }
    public static void addTransactions(String filename)
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines)
            {
                String[] items = line.split(",");
                Set<String> transaction = new HashSet<>();
                for (String item : items)
                {
                    transaction.add(item);
                }
                Database.add(transaction);
            }
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Could not read " + filename);
        }
    }
    public static void uniqueItems(int minSupCount)
    {
        Map<String, Integer> uniqueItemsC = new HashMap<>();
        for (Set<String> transaction : Database)
        {
            for (String item : transaction)
            {
                int count = uniqueItemsC.getOrDefault(item, 0);
                uniqueItemsC.put(item, count + 1);
            }
        }
//		      System.out.println(uniqueItemsC);
        for (Map.Entry<String, Integer> entry : uniqueItemsC.entrySet())
        {
            if (entry.getValue() >= minSupCount)
            {
                Set<String> item = new HashSet<>();
                item.add(entry.getKey());
                result.put(item, entry.getValue());
            }
        }

        //  System.out.println(result);
    }
    public static void generateFinalResult(int minSupCount)
    {
        int i = 1;
        while (true)
        {
            HashMap<Set<String>, Integer> temp1 = new HashMap<>(result);
            HashMap<Set<String>, Integer> temp2 = new HashMap<>();

            for (Map.Entry<Set<String>, Integer> entry : temp1.entrySet())
            {
                for (Map.Entry<Set<String>, Integer> entry2 : temp1.entrySet())
                {
                    Set<String> items = new HashSet<>(entry.getKey());
                    items.addAll(entry2.getKey());
                    if (items.size() > i)
                    {
                        temp2.put(items, 0);
                    }
                }
            }

            HashMap<Set<String>, Integer> temp3 = new HashMap<>();

            for (Set<String> items : Database)
            {
                for (Map.Entry<Set<String>, Integer> entry : temp2.entrySet())
                {
                    if (items.containsAll(entry.getKey()))
                    {
                        temp3.put(entry.getKey(), temp3.getOrDefault(entry.getKey(), 0) + 1);
                    }
                }
            }

            HashMap<Set<String>, Integer> temp4 = new HashMap<>();
            for (Map.Entry<Set<String>, Integer> entry : temp3.entrySet())
                        {
                            if (entry.getValue() >= minSupCount)
                            {
                                temp4.put(entry.getKey(), entry.getValue());
                            }
                        }
            temp3=temp4;

            if (temp3.isEmpty())
            {
                break;
            }

            result.putAll(temp3);
            i++;
        }

        for (Map.Entry<Set<String>, Integer> entry : result.entrySet())
        {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }
    }

    public static void confidence()
    {
        String str1, str2;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the 1st value");
        str1 = sc.nextLine();
        Set<String> str1Set = new HashSet<String>(Arrays.asList(str1.split(",")));
        System.out.println("Enter the 2nd value ");
        str2 = sc.nextLine();
        Set<String> str2Set = new HashSet<String>(Arrays.asList(str2.split(",")));
        Set<String> combined = new HashSet<String>(str1Set);
        combined.addAll(str2Set);

        float str1Support = result.getOrDefault(str1Set, 0);
        float combinedSupport = result.getOrDefault(combined, 0);
        float confidence;
        if (str1Support == 0)
        {
            confidence = 0;
        }
        else
        {
            confidence = (combinedSupport / str1Support) * 100;
        }
        System.out.println(str1 + " -> " + str2 + " :" + confidence + "%");
    }

}

