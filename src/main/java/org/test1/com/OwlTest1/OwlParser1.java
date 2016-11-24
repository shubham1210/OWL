//http://stackoverflow.com/questions/3364588/how-do-i-read-owl-files-in-java-and-display-its-contents
//http://stackoverflow.com/questions/3483594/how-to-read-out-specific-values-from-an-owl-ontology-using-java
//package NewOwl;
package org.test1.com.OwlTest1;

//import static org.semanticweb.owlapi.search.Searcher.annotations;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

import java.util.*;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
//import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
//import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class OwlParser1 {

	/*private static final HashMap<String, Object> classMap = new HashMap<String, Object>();

	private final OWLOntology ontology;
	private OWLDataFactory df;
	private OWLReasoner reasoner;
	boolean direct;

	public OwlParser1(OWLOntology ontology, OWLDataFactory df, OWLReasoner hermit) {
		this.ontology = ontology;
		this.df = df;
		this.reasoner = hermit;
	}

	public void parseOntology() throws OWLOntologyCreationException {

		for (OWLClass cls : reasoner.getTopClassNode()) {

			//classMap.put(label, "value");
			// System.out.println(label + " [" + label + "]");
			recursive(cls);
		}

	}

	private void recursive(OWLClass cls)
	{
		String id = cls.getIRI().toString();
		String label = get(cls, RDFS_LABEL.toString()).size() > 0 ? get(cls, RDFS_LABEL.toString()).get(0)
				: get(cls, RDFS_LABEL.toString()).toString();
		System.out.println(label + " [" + id + "]");
		NodeSet<OWLClass> subClasses = this.reasoner.getSubClasses(cls, true);
		System.out.println("Nodes in "+id+" classs =="+subClasses.getNodes());
		Iterator<Node<OWLClass>> it = subClasses.iterator();
		while(it.hasNext())
		{
			Node<OWLClass> owlclass =it.next();
			Iterator<OWLClass> it2 = owlclass.iterator();
			recursive(it2.next());
		}
	}
	private List<String> get(OWLClass clazz, String property) {
		List<String> ret = new ArrayList();

		final OWLAnnotationProperty owlProperty = df.getOWLAnnotationProperty(IRI.create(property));
		for (OWLOntology o : ontology.getImportsClosure()) {
			for (OWLAnnotation annotation : annotations(o.getAnnotationAssertionAxioms(clazz.getIRI()), owlProperty)) {
				if (annotation.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) annotation.getValue();
					ret.add(val.getLiteral());
				}
			}
		}
		return ret;
	}

	public static void main(String[] args)
			throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Set<String> OWLIndividual= new HashSet();

		// public java.util.Set<OWLIndividual>
		// getObjectPropertyValues(OWLObjectPropertyExpression
		// property,OWLOntology ontology);
		// String x =
		// "http://ontology.neuinfo.org/NIF/Dysfunction/NIF-Dysfunction.owl";
		String x = "http://protege.stanford.edu/ontologies/pizza/pizza.owl";

		IRI documentIRI = IRI.create(x);
		// System.out.println("Document:"+documentIRI);

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// System.out.println("OWLOntologyManager:"+manager);

		// OWLDataFactory dataFactory = manager.getOWLDataFactory();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(documentIRI);

		/* Get the object property and the individual you're interested in */
		/* return the value */
		// OWLObjectProperty o_p_responsible_for =
		// dataFactory.getOWLObjectProperty(IRI.create(documentIRI +
		// "#"+"Responsiblefor"));
		// OWLIndividual ind_Wayne =
		// dataFactory.getOWLNamedIndividual(IRI.create(documentIRI +
		// "#"+"Wayne_Smith"));
		// Set<OWLIndividual> responsibilities_of_Wayne=
		// ind_Wayne.getObjectPropertyValues(o_p_responsible_for, ontology);
	/*
		OWLReasoner reasoner=new StructuralReasonerFactory().createReasoner(ontology);
		OwlParser1 parser = new OwlParser1(ontology, manager.getOWLDataFactory(), reasoner);
		// System.out.println("OWLOntologyManager:"+manager);
		// System.out.println("Document:"+documentIRI);
		parser.parseOntology();
		System.out.println("class Map:" + classMap);
		System.out.println("class Map size:" + classMap.size());

	}*/

}
