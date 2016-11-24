//Main class with main method to execute all the methods of other classes
package MainOWLFiles;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;

public class LauncherClass {
    // Pizza Ontology file use for processing
    //private static final String pizzalink = "http://protege.stanford.edu/ontologies/pizza/pizza.owl";
    //private static final String pizzalink ="http://www.cs.ox.ac.uk/isg/ontologies/lib/ProPreO/2008-02-09/00772.owl";
   private static final File pizzalink = new File("C:\\Users\\User\\Desktop\\owl files\\00721.owl.xml");
    static OWLOntology ontology;
    static OWLDataFactory df;

    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        long startTime, endTime; float duration;
        CopyOnWriteArrayList<DataImplementationCls> finalGraphList = new CopyOnWriteArrayList<DataImplementationCls>();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument(IRI.create(pizzalink));////Load the ontology file
        //Reasoner to parse the owl parse
        Reasoner herm=new Reasoner(ontology);
       // System.out.println(hermit.isConsistent());

        df = manager.getOWLDataFactory();
        //OWLReasoner reasoner = new StructuralReasonerFactory().createReasoner(ontology);

        //calling Graph creation class
        OwlSequentialParsing parser = new OwlSequentialParsing(herm);
        OwlUnreasoningClass.subClassFetch();
        parser.ontologyClassList();
        parser.topDownParsing();
        System.out.println(OwlSequentialParsing.randomClassListBFS);


        //==================SINGLE THREAD EXECUTION START================

        //adding top node to tree starts
        finalGraphList = resetListAndAddRoot(finalGraphList);
        //adding top node to tree ends
        startTime = System.currentTimeMillis();
        parser.graphPopulation(finalGraphList, OwlSequentialParsing.randomClassListBFS);
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);

        if(OwlSequentialParsing.removeDuplicateCheck == false)
        {
            //removing duplicate element starts
            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate element ends
        }


        System.out.println("========================================SINGLE Threading Framework ==================== ");
        System.out.println("========================================Time consumption ==================================" + duration);
        System.out.println(finalGraphList);
        //==================SINGLE THREAD EXECUTION END================

        //==================MULTI THREAD EXECUTION START================

        //adding top node to tree starts
        finalGraphList = resetListAndAddRoot(finalGraphList);
        //adding top node to tree ends
        startTime = System.currentTimeMillis();

        startThreadOnBasisOFParsingNumber(4,finalGraphList,parser,OwlSequentialParsing.randomClassListBFS);

        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);

        /*if(OwlSequentialParsing.removeDuplicateCheck == false)
        {
            //removing duplicate element starts
            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate element ends
        }*/

        //thread approach starts
        System.out.println("========================================MULTIPLE Threading Framework ==================== ");
        System.out.println("========================================Time consumption ==================================" + duration);
        System.out.println(finalGraphList);

        //==================MULTI THREAD EXECUTION END================

        //==================FORK AND JOIN EXECUTION START================
        //fork and join
        finalGraphList = resetListAndAddRoot(finalGraphList);
        startTime = System.currentTimeMillis();
        startForkOnBasisOFParsingNumber(3,finalGraphList,parser,OwlSequentialParsing.randomClassListBFS);
        endTime = System.currentTimeMillis();
        /*if(OwlSequentialParsing.removeDuplicateCheck == false)
        {
            //removing duplicate element starts\
            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate element ends
        }*/

        duration = (endTime - startTime);
        System.out.println("========================================FORK Threading Framework ==================== ");
        System.out.println("========================================Time consumption ==================================" + duration);
        System.out.println(finalGraphList);

        //==================FORK AND JOIN EXECUTION START================

        System.out.println("Test performed == "+parser.countNumberOfTest);

    }

    /*public static void printThreadDetailsFork(ForkJoinPool forkJoinPool)
    {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");
    }*/


    public static CopyOnWriteArrayList<DataImplementationCls> removeDuplicateElement(CopyOnWriteArrayList<DataImplementationCls> finalGraphList)
    {
        //removing duplicate element starts
        Set<OWLClass> sucess2 = finalGraphList.get(OwlSequentialParsing.rootElementIndex).getSuccessorElements();
        DataImplementationCls currentInsertNodeObj = finalGraphList.get(0);
        currentInsertNodeObj.setSuccessorElements(sucess2);
            if(sucess2 != null) {
                finalGraphList.remove(OwlSequentialParsing.rootElementIndex);
            }
        return finalGraphList;
        //removing duplicate element ends
    }

    public static CopyOnWriteArrayList<DataImplementationCls> resetListAndAddRoot(CopyOnWriteArrayList<DataImplementationCls> finalGraphList)
    {
        finalGraphList = new CopyOnWriteArrayList<DataImplementationCls>();
        DataImplementationCls currentInsertNodeObj = new DataImplementationCls(OwlSequentialParsing.topNode);
        System.out.println("root=" + currentInsertNodeObj);
        finalGraphList.add(currentInsertNodeObj);
        return finalGraphList;
    }


    public static void startThreadOnBasisOFParsingNumber(int threadsNumber,CopyOnWriteArrayList<DataImplementationCls> finalGraphList,OwlSequentialParsing parser,List<OWLClass> listTobeWorkedOn)
    {
        /*long startTime = System.currentTimeMillis();

        final int NUMBER_OF_THREADS = 10;
        //ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<OWLClass> items = OwlSequentialParsing.randomClassListBFS;
        final int NUMBER_OF_ITEMS = items.size();
        int minItemsPerThread = NUMBER_OF_ITEMS / NUMBER_OF_THREADS;
        int maxItemsPerThread = minItemsPerThread + 1;
        int threadsWithMaxItems = NUMBER_OF_ITEMS - NUMBER_OF_THREADS * minItemsPerThread;
        int start = 0;
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        GraphThread[] t = null;
       // System.out.println("NUMBER_OF_ITEMS"+NUMBER_OF_ITEMS);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int itemsCount = (i < threadsWithMaxItems ? maxItemsPerThread : minItemsPerThread);
            //System.out.println("Start:"+start);
            int end = start + itemsCount;
            //System.out.println("End:"+end);
            threads[i] = new Thread(new GraphThread(finalGraphList,parser,items.subList(start, end)));
            threads[i].start();
            start = end;
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
*/

        int size = listTobeWorkedOn.size();
        List<OWLClass> list;
        Thread th;
        Runnable proExec;
        int numberOfThreads = size/threadsNumber;
        int intialRange =0;
        if(threadsNumber <size)
        {
            while(numberOfThreads<size)
            {
                list= listTobeWorkedOn.subList(intialRange, numberOfThreads);
                proExec = new ThreadExecution(finalGraphList, parser, list);
                th = new Thread(proExec, "Thread-range-"+numberOfThreads+"-"+intialRange);
                th.start();
                if(numberOfThreads+numberOfThreads < size)
                {
                    intialRange = numberOfThreads;
                    numberOfThreads+=numberOfThreads;
                }else
                {
                    break;
                }
            }
            if(numberOfThreads<size)
            {
                list= listTobeWorkedOn.subList(numberOfThreads, size-1);
                proExec = new ThreadExecution(finalGraphList, parser, list);
                th = new Thread(proExec, "Thread-range-"+numberOfThreads+"-"+intialRange);
                th.start();
            }

        }
        long startTime = System.currentTimeMillis();
        //thread approach starts
        while (finalGraphList.size() != listTobeWorkedOn.size()) {
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        System.out.println("========================================Time consumption in wating for multithreading==================================" + duration);

    }

    public static void startForkOnBasisOFParsingNumber(int threadsNumber,CopyOnWriteArrayList<DataImplementationCls> finalGraphList,OwlSequentialParsing parser,List<OWLClass> listTobeWorkedOn)
    {
        int size = listTobeWorkedOn.size();
        int numberOfThreads = size/threadsNumber;
        int intialRange =0;
        MyRecursiveAction myRecursiveAction;
        List<MyRecursiveAction> myRecursiveActionList = new ArrayList<MyRecursiveAction>();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<OWLClass> list;
        if(threadsNumber < size) {
            while (numberOfThreads < size) {
                list= listTobeWorkedOn.subList(intialRange, numberOfThreads);
                myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, list);
                forkJoinPool.execute(myRecursiveAction);
                myRecursiveActionList.add(myRecursiveAction);
                if(numberOfThreads+numberOfThreads < size)
                {
                    intialRange = numberOfThreads;
                    numberOfThreads+=numberOfThreads;
                }else
                {
                    break;
                }
            }
            if(numberOfThreads<size)
            {
                list= listTobeWorkedOn.subList(numberOfThreads, size-1);
                myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, list);
                forkJoinPool.execute(myRecursiveAction);
                myRecursiveActionList.add(myRecursiveAction);
            }
        }
        int count=0;
        while (true) {
            if(count == myRecursiveActionList.size())break;
            count=0;
            for(MyRecursiveAction myRecursiveAction1:myRecursiveActionList)
            {
                if(myRecursiveAction1.isDone())
                {
                    count++;
                }
            }
            //printThreadDetailsFork(forkJoinPool);
        }
    }
    /*public void result(DataImplementationCls finalGraphList)
    {
        for (OWLClass element :finalGraphList.get(getElement()))

        {
            //randomClassListDFS.add(cls);
            if (OwlSequentialParsing.subClassSet.get(element.getElement()) == finalGraphList.) {
                randomClassListDFS.addAll(OwlUnreasoningClass.EquivalentClassMap.get(cls));
                for (OWLClass owlClass : OwlUnreasoningClass.EquivalentClassMap.get(cls)
                        ) {
                    randomClassListDFS.addAll(OwlUnreasoningClass.subClassMap.get(owlClass));
                }
            }
            if (subClasses != null && subClasses.size() > 0) {
                Iterator<OWLClass> it = subClasses.iterator();
                while (it.hasNext()) {
                    OWLClass owlclass = it.next();
                    recursiveDFS(owlclass);
                }
            }
        }
    }*/

}

