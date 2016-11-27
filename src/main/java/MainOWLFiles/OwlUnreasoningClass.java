//Class to retrieve the random nodes from loaded ontology file. All outputs are tested and correct
package MainOWLFiles;

import java.util.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OwlUnreasoningClass {
	static HashMap<Integer, Object> innerMap = new HashMap();
	static HashMap superSubMap = new HashMap();
	static HashMap<OWLClass, Set<OWLClass>> subClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static HashMap<OWLClass, Set<OWLClass>> superClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static HashMap<OWLClass, Set<OWLClass>> EquivalentClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static Set<OWLClass> listOfEquivalentClassesTemp;
	/*static Set<OWLClass> listOfSuperClasses = new HashSet<OWLClass>();
	static Set<OWLClass> listOfSubClasses = new HashSet<OWLClass>();
	*/static Set<OWLClass> tempSet;

	/*Set<OWLClass> randomClassListGroup1 = new HashSet<OWLClass>();
	Set<OWLClass> randomClassListGroup2 = new HashSet<OWLClass>();
	Set<OWLClass> processedSubAndSuperClassInEchGrp1 = new HashSet<OWLClass>();
	Set<OWLClass> processedSubAndSuperClassInEchGrp2 = new HashSet<OWLClass>();
	List<OWLClass> nonAdded = new ArrayList<OWLClass>();
	HashMap temp;*/

	//this method is used to fetch all the subclasses, super classes and equivalent classes
	public static void subClassFetch() {// Loaded owlfile
		for (OWLClass oap : LauncherClass.ontology.getClassesInSignature()) {
			Set<OWLClass> listOfSubClasses = new HashSet<OWLClass>();
			Set<OWLClass> listOfSuperClasses = new HashSet<OWLClass>();
			Set<OWLClass> listOfEquivalentClasses = new HashSet<>();

			//System.out.println("oap="+oap);
			//System.out.println("oap1="+LauncherClass.ontology.getSubClassAxiomsForSuperClass(oap));
			//System.out.println("oap2="+LauncherClass.ontology.getSubClassAxiomsForSubClass(oap));

			// This is giving the subclasses of given concept
			if (oap.isOWLThing() == true) {
				listOfSubClasses.addAll(OwlSequentialParsing.OWLHerm.getSubClasses((OWLClass) oap, true).getFlattened());
			}
			for (OWLSubClassOfAxiom axiom : LauncherClass.ontology.getSubClassAxiomsForSuperClass(oap)) {
				tempSet = axiom.getClassesInSignature();
				//System.out.println(tempSet);
				tempSet.remove(oap);
				listOfSubClasses.addAll(tempSet);
			}
			subClassMap.put(oap, listOfSubClasses);

			// This is giving the superclasses of given concept
			for (OWLSubClassOfAxiom axiom : LauncherClass.ontology.getSubClassAxiomsForSubClass(oap)) {
				tempSet = axiom.getClassesInSignature();
				tempSet.remove(oap);
				listOfSuperClasses.addAll(tempSet);
			}
			superClassMap.put(oap, listOfSuperClasses);

			for (OWLEquivalentClassesAxiom axiom : LauncherClass.ontology.getEquivalentClassesAxioms(oap)) {
				tempSet = axiom.getClassesInSignature();

				listOfEquivalentClasses.addAll(tempSet);
			}

			for (OWLClass owlClass:listOfEquivalentClasses)
			{
				listOfEquivalentClassesTemp = new HashSet<>(listOfEquivalentClasses);
				listOfEquivalentClassesTemp.remove(owlClass);
				if(EquivalentClassMap.get(owlClass)!=null)
					listOfEquivalentClassesTemp.addAll(EquivalentClassMap.get(owlClass));
				EquivalentClassMap.put(owlClass, listOfEquivalentClassesTemp);
			}
			if(EquivalentClassMap.containsKey(oap)==false)
			{
				if(listOfEquivalentClasses!=null && EquivalentClassMap.get(oap)!=null)
					listOfEquivalentClasses.addAll(EquivalentClassMap.get(oap));
				EquivalentClassMap.put(oap, listOfEquivalentClasses);
			}


           /*innerMap.put(0, listOfSubClasses);
           innerMap.put(1, listOfSuperClasses);
           innerMap.put(2, listOfSubClasses.size() + listOfSuperClasses.size());
           superSubMap.put(oap, innerMap);
           innerMap = new HashMap();
           //listOfSuperClasses.clear();*/
		}
		System.out.println("size of Ontology file................" + subClassMap.size());
		int count = 1;
		for (Object key : subClassMap.keySet()) {
			Object value = subClassMap.get(key);
			//System.out.println(count++ + ".. The class Label: " + key + "--" + value);
		}
		/*for (Object key : EquivalentClassMap.keySet()) {
			Object value = EquivalentClassMap.get(key);
			//System.out.println(count++ + ".. The class Label: " + key + "--" + value);
		}*/

	}

	/*public void dividegroup1(boolean flag) {
		if (flag == true) {
			for (OWLClass currentInsertNode : OwlSequentialParsing.randomClassList) {
				temp = (HashMap) OwlUnreasoningClass.superSubMap.get(currentInsertNode);
				if (randomClassListGroup1.size() == 0) {
					randomClassListGroup1.add(currentInsertNode);
					processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(0));
					processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(1));
				} else {
					if (processedSubAndSuperClassInEchGrp1.contains(currentInsertNode)
							&& randomClassListGroup1.size() < 50) {
						randomClassListGroup1.add(currentInsertNode);
						processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(0));
						processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(1));
					} else if (processedSubAndSuperClassInEchGrp2.contains(currentInsertNode)
							|| randomClassListGroup1.size() == 0) {
						randomClassListGroup2.add(currentInsertNode);
						processedSubAndSuperClassInEchGrp2.addAll((Set) temp.get(0));
						processedSubAndSuperClassInEchGrp2.addAll((Set) temp.get(1));
					} else {
						nonAdded.add(currentInsertNode);
					}
				}
			}
		} else {
			List<OWLClass> nonAddedTemp = new ArrayList<OWLClass>(nonAdded);
			for (OWLClass currentInsertNode : nonAddedTemp) {
				temp = (HashMap) OwlUnreasoningClass.superSubMap.get(currentInsertNode);
				if (randomClassListGroup1.size() == 0) {
					randomClassListGroup1.add(currentInsertNode);
					processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(0));
					processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(1));
				} else {
					if (processedSubAndSuperClassInEchGrp1.contains(currentInsertNode)
							|| randomClassListGroup1.size() < 50) {
						randomClassListGroup1.add(currentInsertNode);
						temp = (HashMap) OwlUnreasoningClass.superSubMap.get(currentInsertNode);
						processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(0));
						processedSubAndSuperClassInEchGrp1.addAll((Set) temp.get(1));
					} else if (processedSubAndSuperClassInEchGrp2.contains(currentInsertNode)) {
						randomClassListGroup2.add(currentInsertNode);
						processedSubAndSuperClassInEchGrp2.addAll((Set) temp.get(0));
						processedSubAndSuperClassInEchGrp2.addAll((Set) temp.get(1));
					} else {
						nonAdded.add(currentInsertNode);
					}
				}
			}
		}
		//System.out.println(randomClassListGroup1);
		//System.out.println(randomClassListGroup2);

	}
*/
}
