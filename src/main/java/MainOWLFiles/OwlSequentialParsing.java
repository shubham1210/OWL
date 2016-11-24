//http://stackoverflow.com/questions/3364588/how-do-i-read-owl-files-in-java-and-display-its-contents
//http://stackoverflow.com/questions/3483594/how-to-read-out-specific-values-from-an-owl-ontology-using-java
//Class that implements reasoner to implement class hierarchy
package MainOWLFiles;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OwlSequentialParsing {

    static Reasoner OWLHerm;
    static HashMap<OWLClass, Set<OWLClass>> subClassSet = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> superClassSet = new HashMap<OWLClass, Set<OWLClass>>();
    static HashMap<OWLClass, Set<OWLClass>> equiClassMap = new HashMap<OWLClass, Set<OWLClass>>();
    
    static List<OWLClass> randomClassList = new ArrayList<OWLClass>();
    static List<OWLClass> randomClassListDFS = new ArrayList<OWLClass>();
    static List<OWLClass> randomClassListBFS = new ArrayList<>();
    

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
        int count =1;
        for (OWLClass oap : LauncherClass.ontology.getClassesInSignature()) {

            // This is list of superclass of given concept given by Hermit reasoner
            Set<OWLClass> superClasses = this.OWLHerm.getSuperClasses((OWLClass) oap, true).getFlattened();

            //System.out.println(count++ + "  The super classes for ---" + oap + "  is====" + superClasses);
            superClassSet.put(oap, superClasses);

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
            subClassSet.put(oap, subClasses);

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
    public void topDownParsing() throws OWLOntologyCreationException {

        for (OWLClass cls : OWLHerm.getTopClassNode()) {
            recursiveDFS(cls);
        }
        for (OWLClass cls : OWLHerm.getTopClassNode()) {
            recursiveBFS(cls);
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
         DataImplementationCls currentInsertNodeObj;
        boolean nodePSFlag;
        boolean isEquivalent;
        for (OWLClass currentInsertNode : randomClassList) {

            {
                // System.out.println(Thread.currentThread().getName());
                // current node is the child of any node // top down approach
                currentInsertNodeObj = new DataImplementationCls(currentInsertNode);
                nodePSFlag = false;

                for (DataImplementationCls processedNode : clsList) {
                	countNumberOfTest++;
                    if (subClassSet.get(processedNode.getElement()) != null
                            && subClassSet.get(processedNode.getElement()).contains(currentInsertNodeObj.getElement())) {
                        processedNode.setIsEquivalentList(equiClassMap.get(processedNode.getElement()));
                        currentInsertNodeObj.setIsEquivalentList(equiClassMap.get(currentInsertNodeObj.getElement()));
                        currentInsertNodeObj.getPredcessorElements().add(processedNode.getElement());
                        processedNode.getSuccessorElements().add(currentInsertNodeObj.getElement());
                        clsList.get(0).getSuccessorElements().remove(currentInsertNodeObj.getElement());
                        nodePSFlag = true;
                    }
                }
                // if current element is not inferred by any processed
                // element
                if (nodePSFlag == false) {
                    // adding Successor of the root element root
                    clsList.get(0).getSuccessorElements().add(currentInsertNode);
                    // adding father as root of current element
                    currentInsertNodeObj.getPredcessorElements().add(clsList.get(0).getElement());
                }
                clsList.add(currentInsertNodeObj);
                if (currentInsertNodeObj.getElement() == topNode)
                    rootElementIndex = clsList.indexOf(currentInsertNodeObj);

            }

            // Bottom Down Traversal
            // wo kisi ka parent hai ke nai
           for (int i = clsList.size() - 1; i >= 0; i--) {
            	countNumberOfTest++;
                if (superClassSet.get(clsList.get(i).getElement()) != null
                        && superClassSet.get(clsList.get(i).getElement()).contains(currentInsertNodeObj.getElement())) {
                    clsList.get(i).setIsEquivalentList(equiClassMap.get(clsList.get(i).getElement()));
                    currentInsertNodeObj.setIsEquivalentList(equiClassMap.get(currentInsertNodeObj.getElement()));
                    currentInsertNodeObj.getSuccessorElements().add(clsList.get(i).getElement());
                    clsList.get(i).getPredcessorElements().remove(clsList.get(0).getElement());
                    clsList.get(i).getPredcessorElements().add(currentInsertNodeObj.getElement());
                    clsList.get(0).getSuccessorElements().remove(currentInsertNodeObj.getElement());
                    nodePSFlag = true;
                }
            }
        }
    }
}
