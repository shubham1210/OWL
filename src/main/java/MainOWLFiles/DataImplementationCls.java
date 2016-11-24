// Data implementation class use for final graph data structure
package MainOWLFiles;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

public class DataImplementationCls {

    public Set<OWLClass> predcessorElements;
    public Set<OWLClass> successorElements;
    public OWLClass element;
    public Set<OWLClass> isEquivalentList;


    public Set<OWLClass> getIsEquivalentList() {
        return isEquivalentList;
    }

    public void setIsEquivalentList(Set<OWLClass> isEquivalentList) {
        this.isEquivalentList = isEquivalentList;
    }

    public DataImplementationCls(OWLClass element) {
        this.element = element;
        predcessorElements = new HashSet<OWLClass>();
        successorElements = new HashSet<OWLClass>();
        this.isEquivalentList = new HashSet();

    }

    public Set<OWLClass> getPredcessorElements() {
        return predcessorElements;
    }

    public void setPredcessorElements(Set<OWLClass> predcessorElements) {
        this.predcessorElements = predcessorElements;
    }

    public Set<OWLClass> getSuccessorElements() {
        return successorElements;
    }

    public void setSuccessorElements(Set<OWLClass> successorElements) {
        this.successorElements = successorElements;
    }

    public OWLClass getElement() {
        return element;
    }

    public void setElement(OWLClass element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "DataImplementationCls [predcessorElements=" + predcessorElements + ", "
                + "successorElements=" + successorElements + ", "
                + "element=" + element + ", "
                + "isEquivalent=" + isEquivalentList + ""
                + "]";
    }

}
