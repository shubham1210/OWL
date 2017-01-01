//Main class with main method to execute all the methods of other classes
package MainOWLFiles;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class LauncherClass {
    // Pizza Ontology file use for processing
    //private static final String pizzalink = "http://www.cs.ox.ac.uk/isg/ontologies/lib/Phenoscape/http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252FBFO_0000053_some_http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252Fpato/2012-07-07/00762.owl";
    //private static final File pizzalink = new File("/Users/shubhamsharma/Downloads/00762.owl.xml");
    //private static final File pizzalink = new File("C:\\Users\\User\\Desktop\\owl files\\Sawada_1982.xml.owl.xml");
    static OWLOntology ontology;
    static OWLDataFactory df;
    static int numberOfRerun = 5;
    static int sizeOfList =0;
    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the input OWL file you want to classify:");
        String pizzalink = reader.nextLine();
        System.out.println("Enter the file where to pic:local , host");
        String fromWhere = reader.nextLine();
        //String pizzalink = pizzalinktemp + " ";
        System.out.println("Enter approach TO start with : single,multi,fork,all");
        String approachName = reader.nextLine();
        System.out.println("For Which Algo it should run:BFS,DFS");
        String sortingName = reader.nextLine();
        System.out.println("Enter No of thread to start with if you have chosen apart from single: ");
        int noThread = reader.nextInt();
        System.out.println("Enter No of rerun: ");
        numberOfRerun = reader.nextInt();

        CopyOnWriteArrayList<DataImplementationCls> finalGraphList = new CopyOnWriteArrayList<DataImplementationCls>();
        System.out.println("===================Onltology file object Created START=====================");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        if(fromWhere.toLowerCase().equals("local"))
        {
            ontology = manager.loadOntologyFromOntologyDocument(IRI.create(new File(pizzalink)));////Load the ontology file

        }else
            ontology = manager.loadOntologyFromOntologyDocument(IRI.create(pizzalink.trim()));////Load the ontology file
        System.out.println("===================Onltology file object Created END=====================");

        System.out.println("===================FIle read START=====================");

        //Reasoner to parse the owl parse
        Reasoner herm = new Reasoner(ontology);
        df = manager.getOWLDataFactory();

        System.out.println("===================FIle read END=====================");

        //calling Graph creation class
        OwlSequentialParsing parser = new OwlSequentialParsing(herm);
        System.out.println("===================SUBCLASS/Super loader FETCH START=====================");
        OwlUnreasoningClass.subClassFetch();
        System.out.println("===================SUBCLASS/Super loader FETCH END=====================");
        System.gc();
        System.out.println("===================Hermit Super/Sub class FETCH START=====================");
        parser.ontologyClassList();
        System.out.println("===================Hermit Super/sub class FETCH END=====================");
        System.gc();
        System.out.println("===================BFS/DFS LIST  START=====================");
        parser.topDownParsing(sortingName.toUpperCase());
        System.out.println("===================topDownParsing FETCH END=====================");
        System.gc();
        List sortedList = sortingName.toUpperCase().equals("BFS")?new ArrayList(OwlSequentialParsing.randomClassListBFS):new ArrayList(OwlSequentialParsing.randomClassListDFS);
        /*List BFSlist = new ArrayList(OwlSequentialParsing.randomClassListBFS);
        List DFSlist = new ArrayList(OwlSequentialParsing.randomClassListDFS);*/

        //System.out.println(OwlSequentialParsing.randomClassListBFS);
        sizeOfList = sortedList.size();
        System.out.println("number of random element after BFS/DFS==="+sizeOfList);
        //==================SINGLE THREAD EXECUTION START================
        if(approachName!=null && (approachName.toLowerCase().equals("single") || approachName.toLowerCase().equals("all")))
            startSingleThread(finalGraphList, parser, sortedList);

        //==================SINGLE THREAD EXECUTION END================


        //==================MULTI THREAD EXECUTION START================
        if(approachName!=null && (approachName.toLowerCase().equals("multi") || approachName.toLowerCase().equals("all")))
            startMultipleThread(finalGraphList,parser,sortedList,noThread);

        //==================MULTI THREAD EXECUTION END================

        //==================FORK AND JOIN EXECUTION START================
        if(approachName!=null && (approachName.toLowerCase().equals("fork") || approachName.toLowerCase().equals("all")))
            startForkThread(finalGraphList,parser,sortedList,noThread);
        //==================FORK AND JOIN EXECUTION END================


        System.out.println("Test performed == " + parser.countNumberOfTest);

    }

    public static void printThreadDetailsFork(ForkJoinPool forkJoinPool) {
        System.out.printf("******************************************\n");
        System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
        System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
        System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
        System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
        System.out.printf("******************************************\n");
    }

    public static void startSingleThread(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List list) {

        long startTime, endTime;
        float duration;
        //adding top node to tree starts
        finalGraphList = resetListAndAddRoot(finalGraphList);
        //adding top node to tree ends
        startTime = System.currentTimeMillis();
        parser.graphPopulation(finalGraphList, list);
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);

        if (OwlSequentialParsing.removeDuplicateCheck == false) {
            //removing duplicate dataElement starts

            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate dataElement ends
        }
        //System.out.println("randomclassListBFS---"+OwlSequentialParsing.randomClassListBFS.size());
        //System.out.println("randomclassListDFS---"+OwlSequentialParsing.randomClassListDFS);
        //System.out.println("randomclassList---"+OwlSequentialParsing.randomClassList.size());
        System.out.println("\n\n========================================SINGLE Threading Framework STARTS==================== ");
        System.out.println("========================================Time consumption ==================================" + duration);
        //System.out.println(finalGraphList);
        resultComparator(finalGraphList,true);
        System.out.println("========================================SINGLE Threading Framework ENDS==================== ");
        //==================SINGLE THREAD EXECUTION END================

    }

    public static void startMultipleThread(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List list,int noThread) {
        long startTime, endTime;
        float duration;
        //adding top node to tree starts
        finalGraphList = resetListAndAddRoot(finalGraphList);
        //adding top node to tree ends
        startTime = System.currentTimeMillis();

        startThreadOnBasisOFParsingNumber(noThread, finalGraphList, parser, list);
        //resultComparator(finalGraphList,false);
        System.out.println("\n\n========================================NON added ==================== "+OwlSequentialParsing.nonAddedElelemntInRecursion.size());
        List<OWLClass> temp = new ArrayList<>(OwlSequentialParsing.nonAddedElelemntInRecursion);
        OwlSequentialParsing.recursion =false;
        startThreadOnBasisOFParsingNumber(2, finalGraphList, parser, temp);
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        if (OwlSequentialParsing.removeDuplicateCheck == false) {
            //removing duplicate dataElement starts
            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate dataElement ends
        }

        //thread approach starts
        System.out.println("\n\n========================================MULTIPLE Threading Framework STARTS==================== "+finalGraphList.size());
        System.out.println("========================================Time consumption ==================================" + duration);
        //System.out.println(finalGraphList);
        resultComparator(finalGraphList,true);
        System.out.println("========================================MULTIPLE Threading Framework ENDS==================== ");
    }

    public static void startForkThread(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List list,int noThread) {
        long startTime, endTime;
        float duration;
        //fork and join
        //adding top node to tree starts
        finalGraphList = resetListAndAddRoot(finalGraphList);

        startTime = System.currentTimeMillis();
        startForkOnBasisOFParsingNumber(noThread, finalGraphList, parser, list);
        //resultComparator(finalGraphList,false);
        System.out.println("\n\n========================================NON added ==================== "+OwlSequentialParsing.nonAddedElelemntInRecursion.size());
        List<OWLClass> temp = new ArrayList<>(OwlSequentialParsing.nonAddedElelemntInRecursion);
        OwlSequentialParsing.recursion =false;
        startForkOnBasisOFParsingNumber(noThread, finalGraphList, parser, temp);
        endTime = System.currentTimeMillis();

        if (OwlSequentialParsing.removeDuplicateCheck == false) {
            //removing duplicate dataElement starts\
            finalGraphList = removeDuplicateElement(finalGraphList);
            //removing duplicate dataElement ends
        }
        //finalGraphList = resetListAndAddRoot(finalGraphList);
        duration = (endTime - startTime);
        System.out.println("\n\n========================================FORK Threading Framework STARTS==================== ");
        System.out.println("========================================Time consumption ==================================" + duration);
        //System.out.println(finalGraphList);
        resultComparator(finalGraphList,true);
        System.out.println("========================================FORK Threading Framework ENDS==================== ");
    }

    public static CopyOnWriteArrayList<DataImplementationCls> removeDuplicateElement(CopyOnWriteArrayList<DataImplementationCls> finalGraphList) {
        //removing duplicate dataElement starts
        Set<OWLClass> sucess2 = finalGraphList.get(OwlSequentialParsing.rootElementIndex).getSuccessorDataSet();
        //if (sucess2 != null) return finalGraphList;
        DataImplementationCls currentInsertNodeObj = finalGraphList.get(0);
        currentInsertNodeObj.setSuccessorDataSet(sucess2);
        if (sucess2 != null) {
            finalGraphList.remove(OwlSequentialParsing.rootElementIndex);
        }
        return finalGraphList;
        //removing duplicate dataElement ends
    }

    public static CopyOnWriteArrayList<DataImplementationCls> resetListAndAddRoot(CopyOnWriteArrayList<DataImplementationCls> finalGraphList) {
        finalGraphList = new CopyOnWriteArrayList<DataImplementationCls>();
        DataImplementationCls currentInsertNodeObj = new DataImplementationCls(OwlSequentialParsing.topNode);
        //System.out.println("root=" + currentInsertNodeObj);
        finalGraphList.add(currentInsertNodeObj);
        return finalGraphList;
    }


    public static void startThreadOnBasisOFParsingNumber(int threadsNumber, CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<OWLClass> listTobeWorkedOn) {
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);//creating a pool of 5 threads
        int size = listTobeWorkedOn.size();
        List<OWLClass> list;
        List<Thread> threads = new ArrayList<Thread>();
        Thread th;
        Runnable proExec;
        int numberOfThreads = size / threadsNumber;
        int intialRange = 0;
        if (threadsNumber < size) {
            while (numberOfThreads < size) {
                list = listTobeWorkedOn.subList(intialRange, numberOfThreads);
                proExec = new ThreadExecution(finalGraphList, parser, list);
                Runnable worker = proExec;
                executor.execute(worker);//calling execute method of ExecutorService
                if (numberOfThreads + (size / threadsNumber) < size) {
                    intialRange = numberOfThreads;
                    numberOfThreads += size / threadsNumber;
                } else {
                    break;
                }
            }
            if (numberOfThreads < size) {
                list = listTobeWorkedOn.subList(numberOfThreads, size);
                proExec = new ThreadExecution(finalGraphList, parser, list);
                Runnable worker = proExec;
                executor.execute(worker);//calling execute method of ExecutorService
            }
            if(threadsNumber == 1)
            {
                proExec = new ThreadExecution(finalGraphList, parser, listTobeWorkedOn);
                Runnable worker = proExec;
                executor.execute(worker);//calling execute method of ExecutorService
            }

        }
        else
        {
            proExec = new ThreadExecution(finalGraphList, parser, listTobeWorkedOn);
            Runnable worker = proExec;
            executor.execute(worker);//calling execute method of ExecutorService
        }
        executor.shutdown();
        while (!executor.isTerminated()) {   }
    }

    public static void startForkOnBasisOFParsingNumber(int numOfThreads, CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<OWLClass> listTobeWorkedOn) {
        int numOfNodes = listTobeWorkedOn.size();
        int nodesPerThread = numOfNodes / numOfThreads;
        int intialRange = 0;
        MyRecursiveAction myRecursiveAction;
        List<MyRecursiveAction> myRecursiveActionList = new ArrayList<MyRecursiveAction>();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<OWLClass> list;
        if (numOfThreads < numOfNodes) {
            while (nodesPerThread < numOfNodes) {
                list = listTobeWorkedOn.subList(intialRange, nodesPerThread);
                //System.out.println("Sublist Size in fork............"+list.size());

                myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, list);
                forkJoinPool.execute(myRecursiveAction);
                myRecursiveActionList.add(myRecursiveAction);
                if (nodesPerThread + (numOfNodes / numOfThreads) < numOfNodes) {
                    intialRange = nodesPerThread;
                    nodesPerThread += numOfNodes / numOfThreads;
                } else {
                    break;
                }
            }
            if (nodesPerThread < numOfNodes) {
                list = listTobeWorkedOn.subList(nodesPerThread, numOfNodes);
                //System.out.println("Sublist Size in fork"+list.size());
                myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, list);
                forkJoinPool.execute(myRecursiveAction);
                myRecursiveActionList.add(myRecursiveAction);
            }
            else if(numOfThreads == 1)
            {
                myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, listTobeWorkedOn);
                forkJoinPool.execute(myRecursiveAction);
                myRecursiveActionList.add(myRecursiveAction);
            }
        }else
        {
            myRecursiveAction = new MyRecursiveAction(finalGraphList, parser, listTobeWorkedOn);
            forkJoinPool.execute(myRecursiveAction);
            myRecursiveActionList.add(myRecursiveAction);
        }
        int count = 0;
        while (true) {
            if (count == myRecursiveActionList.size()) break;
            count = 0;
            for (MyRecursiveAction myRecursiveAction1 : myRecursiveActionList) {
                if (myRecursiveAction1.isDone()) {
                    count++;
                }
            }
            //printThreadDetailsFork(forkJoinPool);
        }
    }

    public static void resultComparator(CopyOnWriteArrayList<DataImplementationCls> finalGraphList,boolean logging) {
        int count = 1;
        int succCount=1;
        int predecessorCount =1;
        int equiCount = 1;
        for (DataImplementationCls dataImplementationCls : finalGraphList) {

            OWLClass element = dataImplementationCls.getDataElement();
            Set<OWLClass> successorSet = OwlSequentialParsing.subClassHashMap.get(element);
            Set<OWLClass> predecessorSet = OwlSequentialParsing.superClassMap.get(element);
            Set<OWLClass> equivalentSet = OwlSequentialParsing.equiClassMap.get(element);

            if (successorSet != null && successorSet.size() > 0) {

                for (OWLClass currentObj : dataImplementationCls.getSuccessorDataSet()) {
                    //iterate all successors of DataImplementationClass and check value in HashMap SuccessorSet
                    if (!successorSet.contains(currentObj)) {
                        // TODO: print some message
                        //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                        if(logging)
                        System.out.println( "Missing Successor nodes in Reasoner: ======= "+currentObj +"is not present in child list of.." +element );
                    }
                }
                //System.out.println(succCount++ +"Successors successful 1");

                for (OWLClass succesorElement : successorSet) {
                    // visa versa
                    if (!succesorElement.isOWLNothing()&&!dataImplementationCls.getSuccessorDataSet().contains(succesorElement) ) {
                        // TODO: print some message
                        //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                        if(logging)
                        System.out.println("Missing Successor nodes in fina graph"+succesorElement +"is not present in child list of final graph.." + element);
                    }

                }
                //System.out.println(succCount++ +"Successors successful 2");
            }
            //System.out.println(succCount++ +"Successors successful-- "+dataElement);

                //-------------------------------------------------------//
                //iterate all Predecessor of DataImplementationClass in HashMap PredecessorSet
                if (predecessorSet != null && predecessorSet.size() > 0) {


                    for (OWLClass currentObj : dataImplementationCls.getPredcessorDataSet()) {
                        //iterate all successors of DataImplementationClass and check value in HashMap SuccessorSet
                        if (!predecessorSet.contains(currentObj)) {
                            // TODO: print some message
                            //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                            if(logging)
                            System.out.println("Missing predecessor nodes in reasoner"+currentObj +"is not present in child list of reasoner.." + element);
                        }
                        //System.out.println("successful 1"+currentObj);
                    }
                    //System.out.println(predecessorCount++ +"Predecessor successful 1");

                    for (OWLClass predecessorElement : predecessorSet) {
                        // visa versa
                        if (!dataImplementationCls.getPredcessorDataSet().contains(predecessorElement) ) {
                            // TODO: print some message
                            //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                            if(logging)
                            System.out.println("Missing predecessor nodes in final graph"+predecessorElement +"is not present in child list of final graph.." + element);
                        }

                    }
                    //System.out.println(predecessorCount++ +"Predecessor successful 2");
                }
            //System.out.println(predecessorCount++ +"Predecessor successful---"+ dataElement);

                    //-------------------------------------------------------//
                    //iterate all Equivalent of DataImplementationClass in HashMap EquivalentSet
                    if (equivalentSet != null && equivalentSet.size() > 0) {

                        for (OWLClass currentObj : dataImplementationCls.getEquivalentDataSet()) {
                            //iterate all successors of DataImplementationClass and check value in HashMap SuccessorSet
                            if (!equivalentSet.contains(currentObj)) {
                                // TODO: print some message
                                //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                                if(logging)
                                System.out.println("equivalentSet FAILURE 1 !!!" + currentObj);
                            }
                            //System.out.println("successful 1"+currentObj);
                        }
                        //System.out.println(equiCount++ +"Equivalent successful 1");
                        for (OWLClass equiElement : equivalentSet) {
                            // visa versa
                            if (!dataImplementationCls.getEquivalentDataSet().contains(equiElement)) {
                                // TODO: print some message
                                //OwlSequentialParsing.nonAddedElelemntInRecursion.add(element);
                                if(logging)
                                System.out.println("equivalentSet FAILURE 2 !!!" + equiElement);
                            }
                        }
                        //System.out.println(equiCount++ +"Equivalent successful 2");
                    }
            //System.out.println(equiCount++ +"Equivalent successful---"+ dataElement);
            //System.out.println(count++ +"successful Testing Done");
                }
            }
        }



