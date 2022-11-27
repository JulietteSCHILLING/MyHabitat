package outils;
import android.util.Log;
import habitat.Habitat;
import habitat.Ouverture;
import habitat.Piece;

import java.util.*;

public class GrapheHabitat {

    // A class to store a graph edge
    class Edge
    {
        int source, dest, weight;

        public Edge(int source, int dest, int weight)
        {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }
    }

    // A class to store a heap node
    class Node
    {
        int vertex, weight;

        public Node(int vertex, int weight)
        {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    // A class to represent a graph object
    class Graph
    {
        // A list of lists to represent an adjacency list
        List<List<Edge>> adjList = null;

        // Constructor
        Graph(List<Edge> edges, int n)
        {
            adjList = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                adjList.add(new ArrayList<>());
            }

            // add edges to the directed graph
            for (Edge edge: edges) {
                adjList.get(edge.source).add(edge);
            }
        }
    }

    private Habitat habitat;
    private HashMap<Integer, Piece> hmap;
    private HashMap<Piece, Integer> hmapGet;
    //private HashMap<Piece, List<Piece>> res;
    private HashMap<Piece, HashMap<Piece, ArrayList<Piece>>> res;

    public GrapheHabitat(Habitat habitat) {
        this.habitat = habitat;
        res = new HashMap<>();
        //On initialise le num correspondant aux pieces
        hmap = new HashMap<>();
        hmapGet = new HashMap<>();
        int i = 0;
        for(Piece piece : habitat.getPieces()){
            hmap.put(i, piece);
            hmapGet.put(piece, i);
            Log.i("testGraphe", "je mets " + piece.getNom());
            i++;

            res.put(piece, new HashMap<>());
        }

        //Initialisation des resultats
        for(Piece piece : habitat.getPieces()){
            for(Piece pieceArrivee : habitat.getPieces()){
                if(!pieceArrivee.equals(piece)){
                    res.get(piece).put(pieceArrivee, new ArrayList<>());
                }
            }
        }


        //On créé la list à partir des ouvertures
        List<Edge> edges = new ArrayList<>();
        for(Ouverture ouverture : habitat.getOuvertures()){
            Log.i("testGraphe", "je veux " + ouverture.getMurDepart().getPiece() + " et " + ouverture.getMurArrivee().getPiece());
            edges.add(new Edge(hmapGet.get(ouverture.getMurDepart().getPiece()), hmapGet.get(ouverture.getMurArrivee().getPiece()), 1));
            edges.add(new Edge(hmapGet.get(ouverture.getMurArrivee().getPiece()), hmapGet.get(ouverture.getMurDepart().getPiece()), 1));
        }


        // total number of nodes in the graph (labelled from 0 to 4)
        int n = habitat.getOuvertures().size()*2;

        // construct graph
        Graph graph = new Graph(edges, n);


        // run the Dijkstra’s algorithm from every node
        for (int source = 0; source < n; source++) {
            findShortestPaths(graph, source, n);
        }

        //For test
        /*

        for(Piece p1 : habitat.getPieces()){
            for(Piece p2 : habitat.getPieces()){
                if(!p1.equals(p2)){
                    getPlusCourtChemin(p1, p2);
                }
            }
        }

         */
    }

    private void getRoute(int[] prev, int i, List<Piece> route)
    {
        if (i >= 0)
        {
            getRoute(prev, prev[i], route);
            route.add(hmap.get(i));
        }
    }

    // Run Dijkstra’s algorithm on a given graph
    public void findShortestPaths(Graph graph, int source, int n)
    {
        // create a min-heap and push source node having distance 0
        PriorityQueue<Node> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Node(source, 0));

        // set initial distance from the source to `v` as infinity
        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));

        // distance from the source to itself is zero
        dist.set(source, 0);

        // boolean array to track vertices for which minimum
        // cost is already found
        boolean[] done = new boolean[n];
        done[source] = true;

        // stores predecessor of a vertex (to a print path)
        int[] prev = new int[n];
        prev[source] = -1;

        // run till min-heap is empty
        while (!minHeap.isEmpty())
        {
            // Remove and return the best vertex
            Node node = minHeap.poll();

            // get the vertex number
            int u = node.vertex;

            // do for each neighbor `v` of `u`
            for (Edge edge: graph.adjList.get(u))
            {
                int v = edge.dest;
                int weight = edge.weight;

                // Relaxation step
                if (!done[v] && (dist.get(u) + weight) < dist.get(v))
                {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new Node(v, dist.get(v)));
                }
            }

            // mark vertex `u` as done so it will not get picked up again
            done[u] = true;
        }

        List<Piece> routePiece = new ArrayList<>();



        for (int i = 0; i < n; i++)
        {
            if (i != source && dist.get(i) != Integer.MAX_VALUE)
            {
                getRoute(prev, i, routePiece);
                res.get(hmap.get(source)).get(hmap.get(i)).addAll(routePiece);
                routePiece.clear();
            }
        }


    }


    public ArrayList<Piece> getPlusCourtChemin(Piece pieceDepart, Piece pieceArrivee){
        Log.i("testGraphe", "Path (" + pieceDepart.getNom() + " -> " + pieceArrivee.getNom() + ", Route = " + res.get(pieceDepart).get(pieceArrivee));
        return res.get(pieceDepart).get(pieceArrivee);
    }

}
