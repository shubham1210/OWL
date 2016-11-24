package MainOWLFiles;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by shsharma on 8/1/2016.
 */
public class ThreadExecution implements Runnable {


        private final CopyOnWriteArrayList<DataImplementationCls> finalGraphList;
        private OwlSequentialParsing parser;
        private List<OWLClass> randomClassList;

        public ThreadExecution(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser,List<OWLClass> randomClassList) {
            this.finalGraphList = finalGraphList;
            this.parser =parser;
            this.randomClassList = randomClassList;
        }
        public void run() {
            parser.graphPopulation(finalGraphList,randomClassList);
        }
}
