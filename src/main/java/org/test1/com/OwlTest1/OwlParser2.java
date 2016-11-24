//http://www.programcreek.com/java-api-examples/index.php?api=org.semanticweb.owlapi.model.OWLOntologyManager
package org.test1.com.OwlTest1;

//import static org.semanticweb.owlapi.search.Searcher.annotations;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

import java.util.*;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
//import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
//import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;



public class OwlParser2 {/*
	
	//Hashmap to save all the class names as ids and value as their subclasses
	private static final HashMap<String ,Object> classMap =  new HashMap<String,Object>();
	
	//OWLOntology is an interface, modelling a set of logical and nonlogical OWLAxioms.
	private final OWLOntology ontology;
	
	//OwlDataFactory is an interface for creating entities, axioms
	private OWLDataFactory df;
	
	//parametrized constructor for class passing OWLOntology and OwlDataFactory
  public OwlParser2(OWLOntology ontology, OWLDataFactory df) {
      this.ontology = ontology;
      this.df = df;
  }

  //Parsing method
  public void parseOntology()throws OWLOntologyCreationException {

      for (OWLClass cls : ontology.getClassesInSignature()) {
    	  //NodeSet<OWLClass> subClasses = this.reasoner.getSubClasses(cls, direct);
      	 //String id = cls.getIRI().toString();
          String label = get(cls, RDFS_LABEL.toString()).size()>0 
          		? get(cls, RDFS_LABEL.toString()).get(0):get(cls, RDFS_LABEL.toString()).toString();
         
          System.out.println(label);
          classMap.put(label, cls.getNestedClassExpressions());
      }		
  }
  
  /*private List<String> get(OWLClass clazz, String property) {
      List<String>ret = new ArrayList();

      final OWLAnnotationProperty owlProperty = df.getOWLAnnotationProperty(IRI.create(property));
      for (OWLOntology o : ontology.getImportsClosure()) {
          for (OWLAnnotation annotation : annotations(
                  o.getAnnotationAssertionAxioms(clazz.getIRI()), owlProperty)) {
              if (annotation.getValue() instanceof OWLLiteral) {
                  OWLLiteral val = (OWLLiteral) annotation.getValue();
                  ret.add(val.getLiteral());
              }
          }
      }
      return ret;
  }*/
  
  
  /*public static void main(String[] args) throws OWLException,
  InstantiationException, IllegalAccessException,ClassNotFoundException {
  	//Set<String> OWLIndividual= new HashSet();
  	
  	//public java.util.Set<OWLIndividual> getObjectPropertyValues(OWLObjectPropertyExpression property,OWLOntology ontology);
  	//String x = "http://ontology.neuinfo.org/NIF/Dysfunction/NIF-Dysfunction.owl";
  	String x= "http://protege.stanford.edu/ontologies/pizza/pizza.owl";
  	 
  	List<HashMap> maps = new ArrayList<HashMap>();

		IRI documentIRI = IRI.create(x);
		//System.out.println("Document:"+documentIRI);
		
		//OWLOntology are created by OWLOntologyManager
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//System.out.println("OWLOntologyManager:"+manager);
		
		//OWLDataFactory dataFactory = manager.getOWLDataFactory();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
		
		//OWLReasoner r = reasonerFactory.createReasoner(ontology);
		
		/*Get the object property and the individual you're interested in*/ 
		/*return the value*/
		//OWLObjectProperty o_p_responsible_for = dataFactory.getOWLObjectProperty(IRI.create(documentIRI + "#"+"Responsiblefor"));
		//OWLIndividual ind_Wayne = dataFactory.getOWLNamedIndividual(IRI.create(documentIRI + "#"+"Wayne_Smith"));
		//Set<OWLIndividual> responsibilities_of_Wayne= ind_Wayne.getObjectPropertyValues(o_p_responsible_for, ontology);
		//OwlParser2 parser = new OwlParser2(ontology, manager.getOWLDataFactory());
		//System.out.println("OWLOntologyManager:"+manager);
		//System.out.println("Document:"+documentIRI);
		/*parser.parseOntology();
		System.out.println("class Map:"+classMap);
		System.out.println("class Map size:"+classMap.size());
		maps = PartitionMap.partionMapBySize(classMap, 4);
		
		System.out.println(PartitionMap.partionMapBySize(classMap, 4));
		System.out.println("Size of Partition1: "+maps.get(0).size());
		System.out.println("Size of Partition2: "+maps.get(1).size());
		System.out.println("Size of Partition3: "+maps.get(2).size());
		System.out.println("Size of Partition4: "+maps.get(3).size());


		System.out.println(ontology);
		}
 */

}
