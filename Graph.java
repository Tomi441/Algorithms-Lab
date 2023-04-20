import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Graph {
    public ArrayList<Vertex> vlist;

    public Graph() {
        this.vlist = new ArrayList<Vertex>();
    }

    public void addVertex(String name) {
        Vertex v = new Vertex(name);
        vlist.add(v);
    }

    public Vertex getVertex(String name) {
        for (Vertex v : vlist) {
            if (v.name.equals(name)) {
                return v;
            }
        }
        return null;
    }

    public void addEdge(String from, String to, int weight) {
        Vertex v1 = getVertex(from);
        Vertex v2 = getVertex(to);
        Edge e = new Edge(v1, v2, weight);
        v1.adjlist.add(e);
        if (v1 != v2) { // check for self-loops
            Edge e2 = new Edge(v2, v1, weight);
            v2.adjlist.add(e2);
        }
    }

    public Edge getEdge(String from, String to) {
        Vertex v1 = getVertex(from);
        Vertex v2 = getVertex(to);
        for (Edge e : v1.adjlist) {
            if (e.to == v2) {
                return e;
            }
        }
        return null;
    }

    public Graph MST() {
        Graph mst = new Graph();
        HashSet<Vertex> visited = new HashSet<Vertex>();
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>((e1, e2) -> e1.weight - e2.weight);

        Vertex start = vlist.get(0);
        visited.add(start);
        pq.addAll(start.adjlist);

        while (visited.size() < vlist.size()) {
            Edge e = pq.poll();
            Vertex to = e.to;
            if (!visited.contains(to)) {
                visited.add(to);
                mst.addVertex(to.name);
                mst.addEdge(e.from.name, to.name, e.weight);
                pq.addAll(to.adjlist);
            }
        }

        return mst;
    }

    public int MSTCost() {
        int cost = 0;
        for (Vertex v : vlist) {
            for (Edge e : v.adjlist) {
                if (MST().getEdge(e.from.name, e.to.name) != null) {
                    cost += e.weight;
                }
            }
        }
        return cost / 2; // divide by 2 since each edge is counted twice
    }

    public Graph SP(String from, String to) {
        Graph sp = new Graph();
        HashMap<Vertex, Integer> dist = new HashMap<Vertex, Integer>();
        HashMap<Vertex, Vertex> prev = new HashMap<Vertex, Vertex>();
        PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>((v1, v2) -> dist.get(v1) - dist.get(v2));

        Vertex start = getVertex(from);
        Vertex end = getVertex(to);
        dist.put(start, 0);
        prev.put(start, null);
        pq.add(start);

        while (!pq.isEmpty()) {
            Vertex curr = pq.poll();
            if (curr == end) {
                break;
            }
            for (Edge e : curr.adjlist) {
                Vertex neighbor = e.to;
                int altDist = dist.get(curr) + e.weight;
                if (!dist.containsKey(neighbor) || altDist < dist.get(neighbor)) {
                    dist.put(neighbor, altDist);
                    prev.put(neighbor, curr);
                    pq.remove(neighbor); // update priority queue
                    pq.add(neighbor);
                }
            }
        }

        return sp;
    }
    }