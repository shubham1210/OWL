
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

import org.semanticweb.owlapi.model.OWLClass;

import MainOWLFiles.DataImplementationCls;

public class Graph1 {

    Hashtable<Node, ArrayList<Node>> adjList = new Hashtable<Node, ArrayList<Node>>();
    ArrayList<Node> nodes = new ArrayList<Node>();
    LinkedList<Node> topoSorted;

    public Graph1() {}

    public void add(Node node) { 
        if (adjList.contains(node)) { 
            return;
        } else { 
            adjList.put(node, new ArrayList<Node>());
            nodes.add(node);
        }
    }

    public void addNeighbor(Node from, ArrayList<Node> list) { 
        for (Node to: list) { 
            addNeighbor(from, to);
        }
    }

    public void addNeighbor(Node from, Node to) { 
        if (!adjList.containsKey(from)) { 
            add(from);
        }
        if (!adjList.containsKey(to)) { 
            add(to);
        }
        adjList.get(from).add(to);
        to.inDegree++;
        to.inNodes.add(from);
    }

    public void remove(Node node) { 
        for (Node n: nodes) { 
            for (Node x: adjList.get(n)) { 
                if (x.equals(node)) removeNeighbor(n, x);
            }
        }
        adjList.remove(node);
        nodes.remove(node);
    }

    public void removeNeighbor(Node from, Node to) { 
        adjList.get(from).remove(to);
        to.inDegree--;
        to.inNodes.remove(from);
    }

    public void resetVisited() { 
        for (Node node: nodes) { 
            node.visited = false;
        }
    }

    public boolean hasEdge(Node from, Node to) { 
        return adjList.get(from).contains(to) ? true : false;
    }

    /**
     * for DAGS only
     * @throws Exception
     */
    public void topologicalSort() throws Exception { 
        /* L <-- Empty list that will contain the sorted elements */
        topoSorted = new LinkedList<Node>();

        /* Use set to keep track of permanently visited nodes
         * in constant time. Does have pointer overhead */
        HashSet<Node> visited = new HashSet<Node>();

        /* while there are unmarked nodes do */
        for (Node n: nodes) { 

            /* select an unmarked node n
             * visit(n)
             */
            if (!visited.contains(n)) visit(n, visited);
        }
    }

    /* function: visit(node n) */
    public void visit(Node node, HashSet<Node> set) throws Exception { 
        /* if n has a temporary mark then stop (not a DAG) */
        if (node.visited) { 
            throw new Exception("Graph1 cyclic");

        /* if n is not marked (i.e. has not been visited) then... */
        } else { 

            /* mark n temporarily [using boolean field in node]*/
            node.visited = true;

            /* for each node m with an edge n to m do... */
            for (Node m: adjList.get(node)) { 

                /* visit(m) */
                if (!set.contains(m)) visit(m, set);            
            }

            /* mark n permanently */
            set.add(node);

            /* unmark n temporarily */
            node.visited = false;

            /* add n to head of L */
            topoSorted.addFirst(node);
        }
    }

    public void printGraph1() { 
        for (Node node: nodes) { 
            System.out.print("from: " + node.value + " |  to: ");
            for (Node m: adjList.get(node)) { 
                System.out.print(m.value + " ");
            }
            System.out.println();
        }
    }

    public void instantiateGraph1() { 
//        Node seven = new Node("7");
//        Node five = new Node("5");
//        Node three = new Node("3");
//        Node eleven = new Node("11");
//        Node eight = new Node("8");
//        Node two = new Node("2");
//        Node nine = new Node("9");
//        Node ten = new Node("10");

//        addNeighbor(seven, eleven);
//        addNeighbor(seven, eight);
//        addNeighbor(five, eleven);
//        addNeighbor(three, eight);
//        addNeighbor(three, ten);
//        addNeighbor(eleven, two);
//        addNeighbor(eleven, nine);
//        addNeighbor(eleven, ten);
//        addNeighbor(eight, nine);

        try {
            topologicalSort();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Node node: topoSorted) { 
            System.out.print(node.value + " ");
        }   
    }

    public static class Node { 
    	DataImplementationCls value; 
        boolean visited = false;
        int inDegree = 0;
        ArrayList<Node> inNodes = new ArrayList<Node>();


        public Node (DataImplementationCls value) { 
            this.value = value;
        }
    }

//    public static void main(String[] args) { 
//        Graph1 g = new Graph1();
//        g.instantiateGraph1();
//    }
}
