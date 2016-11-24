package MainOWLFiles;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;

public class MyRecursiveAction extends RecursiveAction {

    private long workLoad = 0;
    private final CopyOnWriteArrayList<DataImplementationCls> finalGraphList;
    private OwlSequentialParsing parser;
    private List<OWLClass> randomClassList;

    public MyRecursiveAction(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<OWLClass> randomClassList) {
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
           parser.graphPopulation(finalGraphList,randomClassList);
        //}
    }

    private List<MyRecursiveAction> createSubtasks(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser, List<OWLClass> randomClassList) {
        List<MyRecursiveAction> subtasks =
                new ArrayList<MyRecursiveAction>();

        MyRecursiveAction subtask1 = new MyRecursiveAction(finalGraphList,parser,randomClassList.subList(0,OwlSequentialParsing.randomClassList.size()/2));
        MyRecursiveAction subtask2 = new MyRecursiveAction(finalGraphList,parser,randomClassList.subList((OwlSequentialParsing.randomClassList.size()/2)+1,OwlSequentialParsing.randomClassList.size()-1));

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }

}
