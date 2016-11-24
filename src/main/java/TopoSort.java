// a Java program to print topological sorting of a DAG
//http://www.sanfoundry.com/java-program-topological-sorting-graphs
//http://stackoverflow.com/questions/2739392/sample-directed-graph-and-topological-sort-code
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by User on 2016-10-13.
 */
/*public class TopoSort {
    int concept;
    List<Integer> adjList[];

    TopoSort(int con) {
        concept = con;
        adjList = new ArrayList[con];
        for (int i = 0; i < con; i++) {
            adjList[i] = new ArrayList();
        }
    }
    //function to add an edge into graph
        void addEdge(int u, int v){
            adjList[u].add(v);
    }

    // recursive function used by topological sort
    void toplogicalSort(int con, boolean visited[], Stack st){

        //mark the current node as visited
        visited[con] = true;
        Integer i;

        //call all the vertices adjacent to this vertex

    }
    }*/

import java.util.*;

public class TopoSort {

    static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
        used[u] = true;
        for (int v : graph[u])
            if (!used[v])
                dfs(graph, used, res, v);
        res.add(u);
    }

    public static List<Integer> TopoSort(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (!used[i])
                dfs(graph, used, res, i);
        Collections.reverse(res);
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 3;
        List<Integer>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        g[2].add(0);
        g[2].add(1);
        g[0].add(1);

        List<Integer> res = TopoSort(g);
        System.out.println(res);
    }
}


