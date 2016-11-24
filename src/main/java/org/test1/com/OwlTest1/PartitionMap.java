package org.test1.com.OwlTest1;
import java.util.*;
public class PartitionMap {
	public static List<HashMap> partionMapBySize(HashMap inputMap , int size)
    {
        List<HashMap> maps = new ArrayList<HashMap>();
        if(inputMap!=null && inputMap.size() > 0 && size>1)
        {
            int noOfElement = inputMap.size()/size;
            Set entrySet1 = inputMap.entrySet();
            Iterator iterator = entrySet1.iterator();
            int temp =noOfElement;
            HashMap tempMap = new HashMap();
            Map.Entry entry;
            while(iterator.hasNext())
            {
                if(temp == noOfElement)
                {
                    if(tempMap.size()>0)
                    maps.add(tempMap);
                    tempMap = new HashMap();
                }
                entry = (Map.Entry) iterator.next();
                tempMap.put(entry.getKey(),entry.getValue());
                temp--;
                if(temp == 0)
                    temp = noOfElement;
            }
            maps.add(tempMap);
        }
        return maps;
    }
}
