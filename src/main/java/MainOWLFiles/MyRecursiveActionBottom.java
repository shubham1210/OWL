package MainOWLFiles;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;

public class MyRecursiveActionBottom extends RecursiveAction {

    private long workLoad = 0;
    private final CopyOnWriteArrayList<DataImplementationCls> finalGraphList;
    private OwlSequentialParsing parser;
    private List<DataImplementationCls> randomClassList;

    public MyRecursiveActionBottom(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<DataImplementationCls> randomClassList) {
        this.workLoad = randomClassList.size();
        this.finalGraphList = finalGraphList;
        this.parser =parser;
        this.randomClassList = randomClassList;
    }

    @Override
    protected void compute() {
        //if work is above threshold, break tasks up into smaller tasks
       /* if(this.workLoad > 25) {
            System.out.println("Splitting workLoad : " + this.workLoad);
            List<MyRecursiveAction> subtasks =
                    new ArrayList<MyRecursiveAction>();

            subtasks.addAll(createSubtasks(finalGraphList,parser,randomClassList));

            for(RecursiveAction subtask : subtasks){
                subtask.fork();
            }
        } else {*/
            //System.out.println("Doing workLoad myself: " + this.workLoad);
           parser.graphPopulationBottom(finalGraphList,randomClassList);
        //}
    }

}
