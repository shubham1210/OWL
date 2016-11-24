package org.test1.com.OwlTest1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 2016-10-13.
 */
public class IteratorExample {
    public static void main(String args[]){

        //create a list of names
        List<String> names = new ArrayList();
        names.add("Abhay");
        names.add("binita");
        names.add("Zack");
        names.add("arjun");

        //iterate using for loop
        for(int i =0; i<names.size();i++){
            System.out.println("Simple for loop...."+names.get(i));
        }

        //iterate using advance for loop
        for(String nam: names){
            System.out.println("Advance for loop.."+nam);
        }

        //iterate using iterator
        Iterator it = names.iterator();
            while (it.hasNext()){
                System.out.println("Iterator.."+it.next());
            }

        //iterate using while loop
        int index = 0;
        while(names.size()>index){
            System.out.println("While loop...."+names.get(index));
            index++;
        }
    }
}
