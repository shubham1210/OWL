//http://www.programcreek.com/java-api-examples/index.php?api=org.semanticweb.owlapi.model.OWLOntologyManager
package org.test1.com.OwlTest1;

//import static org.semanticweb.owlapi.search.Searcher.annotations;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

import java.util.*;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
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



public class OwlExample1 {/*
	//OWLOntology is an interface, modelling a set of logical and nonlogical OWLAxioms.
	private static OWLOntology ontology;	
	//OwlDataFactory is an interface for creating entities, axioms
	private static OWLDataFactory df;
  
  
  public static void main(String[] args) throws OWLException,
  InstantiationException, IllegalAccessException,ClassNotFoundException {
  	
	  String x= "http://protege.stanford.edu/ontologies/pizza/pizza.owl";
	  IRI documentIRI = IRI.create(x);
		
	  //OWLOntology are created by OWLOntologyManager
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
	  df= manager.getOWLDataFactory();
	  //OwlExample1 exm = new OwlExample1();
	  
		System.out.println(ontology);
		System.out.println(ontology.getAxiomCount());
		System.out.println(ontology.getLogicalAxiomCount());
		
		//Fetch all ABoxaxioms
		Set<OWLAxiom> setAxioms = new HashSet <OWLAxiom>();
		setAxioms = ontology.getABoxAxioms(null);
		for (OWLAxiom oa: setAxioms){
			System.out.println(oa);
		} 
		
		//Very important getAxioms list
		Set<OWLAxiom> setAxiomsa = new HashSet <OWLAxiom>();
		setAxiomsa = ontology.getAxioms();
		for (OWLAxiom oab: setAxiomsa){
			//System.out.println(oab.getClassesInSignature());
			
			//System.out.println(oab);
			//System.out.println("----------------------------------------------------");
		} 
		
		OWLClass clsA = df.getOWLClass(documentIRI);
		//System.out.println(clsA);
		
		OWLClass clsB = df.getOWLThing();
		System.out.println(clsB);
		
		}
 */

}
