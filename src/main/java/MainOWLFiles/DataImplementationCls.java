// Data implementation class use for final graph data structure
package MainOWLFiles;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

public class DataImplementationCls {

    public OWLClass dataElement;
    public Set<OWLClass> predcessorDataSet;
    public Set<OWLClass> successorDataSet;
    public Set<OWLClass> equivalentDataSet;

    public DataImplementationCls(OWLClass dataElement) {
        this.dataElement = dataElement;
        predcessorDataSet = new HashSet<OWLClass>();
        successorDataSet = new HashSet<OWLClass>();
        this.equivalentDataSet = new HashSet();
    }

    public Set<OWLClass> getEquivalentDataSet() {
        return equivalentDataSet;
    }

    public void setEquivalentDataSet(Set<OWLClass> equivalentDataSet) {
        this.equivalentDataSet = equivalentDataSet;
    }

    public Set<OWLClass> getPredcessorDataSet() {
        return predcessorDataSet;
    }

    public void setPredcessorDataSet(Set<OWLClass> predcessorDataSet) {
        this.predcessorDataSet = predcessorDataSet;
    }

    public Set<OWLClass> getSuccessorDataSet() {
        return successorDataSet;
    }

    public void setSuccessorDataSet(Set<OWLClass> successorDataSet) {
        this.successorDataSet = successorDataSet;
    }

    public OWLClass getDataElement() {
        return dataElement;
    }

    public void setDataElement(OWLClass dataElement) {
        this.dataElement = dataElement;
    }
    @Override
    public String toString() {
        return "DataImplementationCls [dataElement=" + dataElement + ", "
                + "predcessorDataSet=" + predcessorDataSet + ", "
                + "successorDataSet=" + successorDataSet + ", "
                + "isEquivalent=" + equivalentDataSet + ""
                + "]";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataImplementationCls that = (DataImplementationCls) o;

        return getDataElement().equals(that.getDataElement());

    }

    @Override
    public int hashCode() {
        return getDataElement().hashCode();
    }
    /*public boolean equals(DataImplementationCls other) {
        // compare the features of the tow instances and return true or false
        if (!this.getPredcessorDataSet().equals(other.getPredcessorDataSet())) {
            return false;
        } else if (!this.getSuccessorDataSet().equals(other.getSuccessorDataSet())) {
            return false;
        } else if (!dataElement.equals(other)) {
            return false;
        } else if (!this.getEquivalentDataSet().equals(other.getEquivalentDataSet())) {
            return false;
        }
        return true; // all the features are equal
    }*/

}
