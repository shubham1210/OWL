package MainOWLFiles;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadExecutionBottom implements Runnable {


        private final CopyOnWriteArrayList<DataImplementationCls> finalGraphList;
        private OwlSequentialParsing parser;
        private List<DataImplementationCls> randomClassList;

        public ThreadExecutionBottom(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<DataImplementationCls> randomClassList) {
            this.finalGraphList = finalGraphList;
            this.parser =parser;
            this.randomClassList = randomClassList;
        }
        public void run() {
            parser.graphPopulationBottom(finalGraphList,randomClassList);
            //parser.graphPopulation(finalGraphList,randomClassList);

        }
}
