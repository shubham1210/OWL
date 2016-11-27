package org;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by User on 2016-11-24.
 */
public class AutoTestingResult {

    HashMap<String, Set<String>> mapA = new HashMap<>();
    HashMap<String, Set<String>> mapB = new HashMap<>();

    Set<String> SetA = new HashSet<>();
    Set<String> SetB = new HashSet<>();

    public boolean mapsAreEqual() {
        SetA.add("1");
        SetA.add("2");
        SetA.add("3");
        SetB.add("4");
        SetB.add("5");
        SetB.add("3");

        mapA.put("A",SetA);
        mapA.put("B",SetB);

        mapB.put("D", SetA);
        mapB.put("A", SetB);

        try{
            for (String k : mapB.keySet()) {
                for (String value : mapB.get(k)) {
                    if (mapA.get(k).equals(mapB.get(k))) {
                        if (!mapA.get(value).equals(mapB.get(value))) {
                            System.out.println("This key is not found" + k);
                            return false;
                        }
                        else {
                            System.out.println("This key is found...." + k + "with value" + value);
                        }
                    }
                            System.out.println("This key is not found" + k);
                            return false;
                    }
                }


            for (String y : mapA.keySet())
            {
                for (String value : mapA.get(y)) {
                if (!mapB.containsKey(y)) {
                    System.out.println("This key is not found"+ y);
                    return false;
                }
                    else if((mapB.containsKey(y))){

                        if (!mapB.get(value).equals(mapA.get(value))) {
                        System.out.println("This key is not found" + y);
                        return false;
                    }
                        else{
                            System.out.println("This key is found----"+ y);
                        }
                }
                }

            }

        } catch (NullPointerException np) {
            return false;
        }
        return true;
    }

    public static void main(String []args){

        AutoTestingResult atr = new AutoTestingResult();
        atr.mapsAreEqual();

    }
}
