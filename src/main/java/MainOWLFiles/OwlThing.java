package MainOWLFiles;

import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

/**
 * Created by shsharma on 12/31/2016.
 */
public class OwlThing extends OWLClassImpl {
    public OwlThing(IRI iri) {
        super(iri);
    }

    @Override
    public boolean isOWLThing() {
        return true;
    }


}
