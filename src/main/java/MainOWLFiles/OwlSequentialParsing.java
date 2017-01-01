//http://stackoverflow.com/questions/3364588/how-do-i-read-owl-files-in-java-and-display-its-contents
//http://stackoverflow.com/questions/3483594/how-to-read-out-specific-values-from-an-owl-ontology-using-java
//Class that implements reasoner to implement class hierarchy
package MainOWLFiles;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.Node;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwlSequentialParsing {

    static Reasoner OWLHerm;
    static HashMap<OWLClass, Set<OWLClass>> subClassHashMap = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> superClassMap = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> equiClassMap = new HashMap<OWLClass, Set<OWLClass>>();

    static List<OWLClass> randomClassList = new ArrayList<>();
    static Set<OWLClass> randomClassListDFS = new LinkedHashSet<>();
    static Set<OWLClass> randomClassListBFS = new LinkedHashSet<>();
    static List<OWLClass> randomClassListTemp = new ArrayList<>();
    static Set<OWLClass> nonAddedElelemntInRecursion = new HashSet<>();
    static boolean recursion = true;
    static HashSet<DataImplementationCls> bottomUp = new HashSet<>();
    static HashSet<DataImplementationCls> TopDown = new HashSet<>();
    static int countNumberOfTest = 0;
    static OWLClass topNode;
    static OWLClass bottomNode;
    static int rootElementIndex = 0;
    //public static Boolean arrayMat[][];
    public static boolean removeDuplicateCheck = true;

    public OwlSequentialParsing(Reasoner OWLHermit) {
        this.OWLHerm = OWLHermit;
    }//Constructor

    //this method is giving 100% correct result. tested
    public void ontologyClassList() {
        for (OWLClass oap : LauncherClass.ontology.getClassesInSignature()) {

            // This is list of superclass of given concept given by Hermit reasoner
            Set<OWLClass> superClasses = this.OWLHerm.getSuperClasses((OWLClass) oap, true).getFlattened();

            //System.out.println(count++ + "  The super classes for ---" + oap + "  is====" + superClasses);
            superClassMap.put(oap, superClasses);

            // This is list of equivalent classes of given concept given by Hermit reasoner
            Node<OWLClass> equiClass = this.OWLHerm.getEquivalentClasses(oap);
            Set temp;
            for (OWLClass class1 : equiClass.getEntities()) {
                temp = new HashSet(equiClass.getEntities());
                temp.remove(class1);
                equiClassMap.put(class1, temp);
            }

            // This is giving the subclass of given concept fetch by Hermit reasoner
            Set<OWLClass> subClasses = this.OWLHerm.getSubClasses((OWLClass) oap, true).getFlattened();
            //System.out.println(count++ + "  The sub classes for ---" + oap + "is====" + subClasses);
            subClassHashMap.put(oap, subClasses);

            randomClassList.add(oap);

            if (oap.isOWLThing()) {
                topNode = oap;
            }
            if (oap.isOWLNothing()) {
                bottomNode = oap;
            }
        }
        //System.out.println(this.OWLHerm.getTopClassNode());
        //System.out.println("equiClassMap == "+equiClassMap);
        removeDuplicateCheck = false;
        if (topNode == null) {
            removeDuplicateCheck = true;
            for (Iterator<OWLClass> it = this.OWLHerm.getTopClassNode().getEntities().iterator();
                 it.hasNext(); ) {
                topNode = it.next();
                break;
            }
        }
        if(topNode == null)
        {
            topNode = new OwlThing(IRI.create(new File("c:")));
            randomClassList.add(topNode);
            subClassHashMap.put(topNode, new HashSet<>());
            superClassMap.put(topNode, new HashSet<>());
        }

    }

    // Method to simply parse the top down class hierarchy from reasoner file
    public void topDownParsing(String Search) throws OWLOntologyCreationException {

        if (Search.equals("DFS") || Search.equals("ALL")) {
            for (OWLClass cls : OWLHerm.getTopClassNode()) {
                if (cls.isOWLThing()) {
                    recursiveDFS(cls);
                }
            }
        }
        if (Search.equals("BFS") || Search.equals("ALL")) {
            for (OWLClass cls : OWLHerm.getTopClassNode()) {
                if (cls.isOWLThing()) {
                    recursiveBFS(cls);
                }
            }
        }
    }

    // recursive method for DFS approach
    private void recursiveDFS(OWLClass cls) {
        Set<OWLClass> subClasses = OwlUnreasoningClass.subClassMap.get(cls);
        if(OwlUnreasoningClass.OwlThingFound == false)
        {
            if(subClasses==null)
            {
                subClasses=new HashSet<OWLClass>();
            }
            subClasses.addAll(OwlUnreasoningClass.listOfClassesWithNoParent);
            System.out.println("adding classes that has no parent == "+subClasses);
            OwlUnreasoningClass.OwlThingFound=true;
        }
        randomClassListDFS.add(cls);
        if (OwlUnreasoningClass.EquivalentClassMap.get(cls) != null) {
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
        //System.out.println(cls+"..........."+subClasses);
    }

    // recursive method for BFS approach
    private void recursiveBFS(OWLClass cls) {
        Queue<OWLClass> queue = new LinkedList<OWLClass>();
        if (cls == null)
            return;
        queue.clear();
        queue.add(cls);
        randomClassListBFS.add(cls);
        while (!queue.isEmpty()) {
            OWLClass node = queue.remove();
            Set<OWLClass> subClasses = OwlUnreasoningClass.subClassMap.get(node); //this.OWLHerm.getSubClasses(node, true).getFlattened();//
            if(OwlUnreasoningClass.OwlThingFound == false)
            {
                if(subClasses==null)
                {
                    subClasses=new HashSet<OWLClass>();
                }
                subClasses.addAll(OwlUnreasoningClass.listOfClassesWithNoParent);
                OwlUnreasoningClass.OwlThingFound=true;
                System.out.println("adding classes that has no parent == "+subClasses);
            }
            if (OwlUnreasoningClass.EquivalentClassMap.get(node) != null) {
                randomClassListBFS.addAll(OwlUnreasoningClass.EquivalentClassMap.get(node));
                for (OWLClass owlClass : OwlUnreasoningClass.EquivalentClassMap.get(node)
                        ) {
                    randomClassListBFS.addAll(OwlUnreasoningClass.subClassMap.get(owlClass));
                }
            }
            if (subClasses != null && subClasses.size() > 0) {
                for (OWLClass oc : subClasses) {
                    queue.add(oc);
                    if (randomClassListBFS.contains(oc) == false) randomClassListBFS.add(oc);
                }
            }
        }
    }

    // This method is used to implement the algorithm to construct final graph
    public void graphPopulation(CopyOnWriteArrayList<DataImplementationCls> clsList, List<OWLClass> randomClassList) {
        List<DataImplementationCls> currentInsertNodeObjList = new ArrayList<>();
        for (OWLClass currentInsertNode : randomClassList) {
            graphPopulationRecursive(currentInsertNode, clsList,currentInsertNodeObjList);
        }
        System.out.println("======="+clsList.size());
        //while(clsList.size()!=LauncherClass.sizeOfList){}
        for (DataImplementationCls currentInsertNode : currentInsertNodeObjList) {
            bottomUpSearch(clsList, currentInsertNode,1);
        }

    }

    private void graphPopulationRecursive(OWLClass currentInsertNode, CopyOnWriteArrayList<DataImplementationCls> clsList,List<DataImplementationCls> currentInsertNodeObjList) {
        DataImplementationCls currentInsertNodeObj;
        boolean nodePSFlag = false;
        // System.out.println(Thread.currentThread().getName());
        // current node is the child of any node // top down approach
        currentInsertNodeObj = new DataImplementationCls(currentInsertNode);
        nodePSFlag = topDownSearch(clsList, currentInsertNodeObj, 1);
        // if current dataElement is not inferred by any processed
        // dataElement
        if (nodePSFlag == false) {
            // System.out.println("currentInsertNode............"+currentInsertNode);
            // adding Successor of the root dataElement root
            clsList.get(0).getSuccessorDataSet().add(currentInsertNode);
            // adding father as root of current dataElement
            currentInsertNodeObj.getPredcessorDataSet().add(clsList.get(0).getDataElement());
        }
        // Bottom Down Traversal
        // wo kisi ka parent hai ke nai
        //System.out.println("////////////////"+currentInsertNode);
        if(OwlSequentialParsing.recursion==true)
        {
            clsList.add(currentInsertNodeObj);
            currentInsertNodeObjList.add(currentInsertNodeObj);
            if (currentInsertNodeObj.getDataElement() == topNode)
                rootElementIndex = clsList.indexOf(currentInsertNodeObj);
        }
    }

    private boolean bottomUpSearch(CopyOnWriteArrayList<DataImplementationCls> clsList, DataImplementationCls currentInsertNodeObj, int numberOfRerun) {
        boolean flag = false;
        for (int i = clsList.size() - 1; i >= 0; i--) {
            int countNodeProcessesByIndividulaThread = clsList.get(i).getPredcessorDataSet().size();
            int countNodeProcessesByIndividulaThreadS = clsList.get(i).getSuccessorDataSet().size();

            countNumberOfTest++;
            if (superClassMap.get(clsList.get(i).getDataElement()) != null
                    && superClassMap.get(clsList.get(i).getDataElement()).contains(currentInsertNodeObj.getDataElement())) {
                if (recursion && (clsList.get(i).getPredcessorDataSet().size() > countNodeProcessesByIndividulaThread)
                        && numberOfRerun < LauncherClass.numberOfRerun) {//|| clsList.get(i).getSuccessorDataSet().size() > countNodeProcessesByIndividulaThreadS
                    System.out.println("Rerunning bottom search node..........."+clsList.get(i).getPredcessorDataSet().size() +"====="+countNodeProcessesByIndividulaThread);
                    bottomUpSearch(clsList, currentInsertNodeObj, numberOfRerun++);
                } else if (numberOfRerun < LauncherClass.numberOfRerun) {
                    synchronized (clsList.get(i).getPredcessorDataSet()) {
                       // if (recursion == false) System.out.println("adding...........");
                        clsList.get(i).setEquivalentDataSet(equiClassMap.get(clsList.get(i).getDataElement()));
                        currentInsertNodeObj.setEquivalentDataSet(equiClassMap.get(currentInsertNodeObj.getDataElement()));
                        currentInsertNodeObj.getSuccessorDataSet().add(clsList.get(i).getDataElement());
                        clsList.get(i).getPredcessorDataSet().remove(clsList.get(0).getDataElement());
                        clsList.get(i).getPredcessorDataSet().add(currentInsertNodeObj.getDataElement());
                        clsList.get(0).getSuccessorDataSet().remove(currentInsertNodeObj.getDataElement());
                        countNodeProcessesByIndividulaThread++;
                        flag = true;
                    }
                }
                else {
                    System.out.println("adding non added element from BOTTOM UP");
                    nonAddedElelemntInRecursion.add(currentInsertNodeObj.getDataElement());
                }
            }
        }
        return flag;
    }

    private boolean topDownSearch(CopyOnWriteArrayList<DataImplementationCls> clsList, DataImplementationCls currentInsertNodeObj, int numberOfRerun) {

        boolean flag = false;
        for (DataImplementationCls processedNode : clsList) {
            int countNodeProcessesByIndividulaThread = processedNode.getSuccessorDataSet().size();
            int countNodeProcessesByIndividulaThreadP = processedNode.getPredcessorDataSet().size();
            countNumberOfTest++;
            if (subClassHashMap.get(processedNode.getDataElement()) != null
                    && subClassHashMap.get(processedNode.getDataElement()).contains(currentInsertNodeObj.getDataElement())) {
                if (recursion && (processedNode.getSuccessorDataSet().size() > countNodeProcessesByIndividulaThread
                        )
                        && numberOfRerun < LauncherClass.numberOfRerun)//|| processedNode.getPredcessorDataSet().size() > countNodeProcessesByIndividulaThreadP
                {
                    System.out.println("Rerunning top node search...........");
                    topDownSearch(clsList, currentInsertNodeObj, numberOfRerun++);
                } else if (numberOfRerun < LauncherClass.numberOfRerun) {
                    //if (recursion == false) System.out.println("adding...........");
                    synchronized (processedNode.getSuccessorDataSet())
                    {
                        processedNode.setEquivalentDataSet(equiClassMap.get(processedNode.getDataElement()));
                        currentInsertNodeObj.setEquivalentDataSet(equiClassMap.get(currentInsertNodeObj.getDataElement()));
                        currentInsertNodeObj.getPredcessorDataSet().add(processedNode.getDataElement());
                        processedNode.getSuccessorDataSet().add(currentInsertNodeObj.getDataElement());
                        clsList.get(0).getSuccessorDataSet().remove(currentInsertNodeObj.getDataElement());
                        flag = true;
                    }
                } else {
                    System.out.println("adding non added element from TOP UP");
                    nonAddedElelemntInRecursion.add(currentInsertNodeObj.getDataElement());
                }
            }
        }

        return flag;
    }
}
