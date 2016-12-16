//http://stackoverflow.com/questions/3364588/how-do-i-read-owl-files-in-java-and-display-its-contents
//http://stackoverflow.com/questions/3483594/how-to-read-out-specific-values-from-an-owl-ontology-using-java
//Class that implements reasoner to implement class hierarchy
package MainOWLFiles;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwlSequentialParsing {

    static Reasoner OWLHerm;
    static HashMap<OWLClass, Set<OWLClass>> subClassHashMap = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> superClassMap = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> equiClassMap = new HashMap<OWLClass, Set<OWLClass>>();

    static List<OWLClass>  randomClassList =  new ArrayList<>();
    static Set<OWLClass> randomClassListDFS = new LinkedHashSet<>();
    static Set<OWLClass>  randomClassListBFS =  new LinkedHashSet<>();
    static List<OWLClass>  randomClassListTemp =  new ArrayList<>();



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
            Set temp ;
            for(OWLClass class1 : equiClass.getEntities())
            {
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
        removeDuplicateCheck =false;
        if(topNode ==null)
        {
            removeDuplicateCheck =true;
            for (Iterator<OWLClass> it = this.OWLHerm.getTopClassNode().getEntities().iterator();
                 it.hasNext();) {
                topNode = it.next();
                break;
            }
        }

    }

    // Method to simply parse the top down class hierarchy from reasoner file
    public void topDownParsing(String Search) throws OWLOntologyCreationException {

        if(Search.equals("DFS") || Search.equals("ALL"))
        {
            for (OWLClass cls : OWLHerm.getTopClassNode()) {
                if (cls.isOWLThing())
                {
                    recursiveDFS(cls);
                }
            }
        }
        if(Search.equals("BFS") || Search.equals("ALL")){
            for (OWLClass cls : OWLHerm.getTopClassNode()) {
                if (cls.isOWLThing())
                {
                    recursiveBFS(cls);
                }
            }
        }
    }

    // recursive method for DFS approach
    private void recursiveDFS(OWLClass cls) {
        Set<OWLClass> subClasses = OwlUnreasoningClass.subClassMap.get(cls);
        randomClassListDFS.add(cls);
        if (OwlUnreasoningClass.EquivalentClassMap.get(cls) != null) {
            randomClassListDFS.addAll(OwlUnreasoningClass.EquivalentClassMap.get(cls));
            for (OWLClass owlClass:OwlUnreasoningClass.EquivalentClassMap.get(cls)
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
        for (OWLClass currentInsertNode : randomClassList) {
            graphPopulationRecursive(currentInsertNode,clsList);
        }
    }

    private void graphPopulationRecursive(OWLClass currentInsertNode,CopyOnWriteArrayList<DataImplementationCls> clsList) {
        DataImplementationCls currentInsertNodeObj;
        boolean nodePSFlag=false;
        // System.out.println(Thread.currentThread().getName());
        // current node is the child of any node // top down approach
        currentInsertNodeObj = new DataImplementationCls(currentInsertNode);
        nodePSFlag = topDownSearch(clsList, currentInsertNodeObj);
        // if current dataElement is not inferred by any processed
        // dataElement
        if (nodePSFlag == false) {
            // System.out.println("currentInsertNode............"+currentInsertNode);
            // adding Successor of the root dataElement root
            clsList.get(0).getSuccessorDataSet().add(currentInsertNode);
            // adding father as root of current dataElement
            currentInsertNodeObj.getPredcessorDataSet().add(clsList.get(0).getDataElement());
        }
        //System.out.println("////////////////"+currentInsertNode);
        clsList.add(currentInsertNodeObj);

        if (currentInsertNodeObj.getDataElement() == topNode)
            rootElementIndex = clsList.indexOf(currentInsertNodeObj);

        // Bottom Down Traversal
        // wo kisi ka parent hai ke nai
        nodePSFlag = bottomUpSearch(clsList, currentInsertNodeObj);

    }
    private boolean bottomUpSearch(CopyOnWriteArrayList<DataImplementationCls> clsList,DataImplementationCls currentInsertNodeObj)
    {
        boolean flag= false;
        int countNodeProcessesByIndividulaThread=clsList.size();
        for (int i = clsList.size() - 1; i >= 0; i--) {
            countNumberOfTest++;
            if (superClassMap.get(clsList.get(i).getDataElement()) != null
                    && superClassMap.get(clsList.get(i).getDataElement()).contains(currentInsertNodeObj.getDataElement())) {
                if(clsList.size() > countNodeProcessesByIndividulaThread)
                {
                    System.out.println("Rerunning bottom search node...........");
                    bottomUpSearch(clsList,currentInsertNodeObj);
                }
                clsList.get(i).setEquivalentDataSet(equiClassMap.get(clsList.get(i).getDataElement()));
                currentInsertNodeObj.setEquivalentDataSet(equiClassMap.get(currentInsertNodeObj.getDataElement()));
                currentInsertNodeObj.getSuccessorDataSet().add(clsList.get(i).getDataElement());
                clsList.get(i).getPredcessorDataSet().remove(clsList.get(0).getDataElement());
                clsList.get(i).getPredcessorDataSet().add(currentInsertNodeObj.getDataElement());
                clsList.get(0).getSuccessorDataSet().remove(currentInsertNodeObj.getDataElement());
                flag = true;
            }
        }
        return flag;
    }

    private boolean topDownSearch(CopyOnWriteArrayList<DataImplementationCls> clsList,DataImplementationCls currentInsertNodeObj)
    {

        boolean flag= false;
        int countNodeProcessesByIndividulaThread=clsList.size();
        for (DataImplementationCls processedNode : clsList) {
            countNumberOfTest++;
           // countNodeProcessesByIndividulaThread+= processedNode.getSuccessorDataSet().size();
            if (subClassHashMap.get(processedNode.getDataElement()) != null
                    && subClassHashMap.get(processedNode.getDataElement()).contains(currentInsertNodeObj.getDataElement())) {
                if(clsList.size() > countNodeProcessesByIndividulaThread)
                {
                    System.out.println("Rerunning top node search...........");
                    System.out.println("clsList.size()"+clsList.size()+"countNodeProcessesByIndividulaThread"+countNodeProcessesByIndividulaThread);
                    topDownSearch(clsList,currentInsertNodeObj);
                }
                processedNode.setEquivalentDataSet(equiClassMap.get(processedNode.getDataElement()));
                currentInsertNodeObj.setEquivalentDataSet(equiClassMap.get(currentInsertNodeObj.getDataElement()));
                currentInsertNodeObj.getPredcessorDataSet().add(processedNode.getDataElement());
                processedNode.getSuccessorDataSet().add(currentInsertNodeObj.getDataElement());
                clsList.get(0).getSuccessorDataSet().remove(currentInsertNodeObj.getDataElement());
                flag= true;
            }
        }

        return flag;
    }
}
