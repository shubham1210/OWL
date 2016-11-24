package org;

import MainOWLFiles.DataImplementationCls;
import MainOWLFiles.OwlSequentialParsing;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by User on 2016-11-01.
 */
public class GraphThread extends Thread{

    private CopyOnWriteArrayList<DataImplementationCls> finalGraphList;
    private OwlSequentialParsing parser;
    private List<OWLClass> randomBfs;

    public GraphThread(CopyOnWriteArrayList<DataImplementationCls> finalGraphList, OwlSequentialParsing parser,List<OWLClass> randomBfs){
        this.finalGraphList=finalGraphList;
        this.parser=parser;
        this.randomBfs=randomBfs;
    }

    @Override
    public void run(){
        parser.graphPopulation(finalGraphList,randomBfs);
    }
}
