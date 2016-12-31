//Class to retrieve the random nodes from loaded ontology file. All outputs are tested and correct
package MainOWLFiles;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OwlUnreasoningClass {
	static HashMap<OWLClass, Set<OWLClass>> subClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static HashMap<OWLClass, Set<OWLClass>> superClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static HashMap<OWLClass, Set<OWLClass>> EquivalentClassMap = new HashMap<OWLClass, Set<OWLClass>>();
	static Set<OWLClass> listOfEquivalentClassesTemp;
	static Set<OWLClass> listOfClassesWithNoParent=new HashSet<>();
	static Set<OWLClass> tempSet;
	static boolean OwlThingFound=false;

	/*Set<OWLClass> randomClassListGroup1 = new HashSet<OWLClass>();
	Set<OWLClass> randomClassListGroup2 = new HashSet<OWLClass>();
	Set<OWLClass> processedSubAndSuperClassInEchGrp1 = new HashSet<OWLClass>();
	Set<OWLClass> processedSubAndSuperClassInEchGrp2 = new HashSet<OWLClass>();
	List<OWLClass> nonAdded = new ArrayList<OWLClass>();
	HashMap temp;*/

	//this method is used to fetch all the subclasses, super classes and equivalent classes
	public static void subClassFetch() {// Loaded owlfile
		Set<OWLClass> listOfSubClasses = null;
		Set<OWLClass> listOfSuperClasses = null;
		Set<OWLClass> listOfEquivalentClasses = null;
		for (OWLClass oap : LauncherClass.ontology.getClassesInSignature()) {
			listOfSubClasses = new HashSet<OWLClass>();
			listOfSuperClasses = new HashSet<OWLClass>();
			listOfEquivalentClasses = new HashSet<OWLClass>();

			//System.out.println("oap="+oap);
			//System.out.println("oap1="+LauncherClass.ontology.getSubClassAxiomsForSuperClass(oap));
			//System.out.println("oap2="+LauncherClass.ontology.getSubClassAxiomsForSubClass(oap));

			// This is giving the subclasses of given concept
			if (oap.isOWLThing() == true) {
				listOfSubClasses.addAll(OwlSequentialParsing.OWLHerm.getSubClasses((OWLClass) oap, true).getFlattened());
				OwlThingFound=true;
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

			if(listOfSuperClasses.size()==0)
			{
				listOfClassesWithNoParent.add(oap);
			}
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
		}
		System.out.println("size of Ontology file................" + subClassMap.size());
	}
}
