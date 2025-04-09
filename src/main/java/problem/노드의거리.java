package problem;

import java.util.*;
import java.io.*;


public class 노드의거리 {
    static int T;
    static int V, E;
    static int startVertex, destVertex;
    static Map<Integer, List<Integer>> graphMap;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    public static class Node {
    	int curVertex;
    	int depth;
    	
    	public Node(int curVertex, int depth) {
    		this.curVertex = curVertex;
    		this.depth = depth;
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	T = Integer.parseInt(st.nextToken());
    	
    	for (int i = 0; i < T; i++) {
    		init();
    		int answer = solution();
    		System.out.println("#" + (i + 1) + " " + answer);
    	}
    	
    }
    
    public static int solution() {
    	int[] visited = new int[V];
    	
    	Deque<Node> queue = new ArrayDeque<>();
    	queue.add(new Node(startVertex, 0));
    	
    	while (!queue.isEmpty()) {
    		Node node = queue.pollFirst();
    		if (visited[node.curVertex] == 1) { continue; }
    		visited[node.curVertex] = 1;
    		
    		if (node.curVertex == destVertex) {
    			return node.depth;
    		}
    		
    		for (int nearVertex : graphMap.get(node.curVertex)) {
    			if (visited[nearVertex] == 1) { continue; }
    			
    			queue.add(new Node(nearVertex, node.depth + 1));
    		}
    	}
    	
    	return 0;
    }
    
    public static void init() throws IOException {
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	V = Integer.parseInt(st.nextToken());
    	E = Integer.parseInt(st.nextToken());
    	
    	graphMap = new HashMap<>();
    	for (int i = 0; i < V; i++) {
    		graphMap.put(i, new ArrayList<>());
    	}
    	
    	for (int i = 0; i < E; i++) {
    		st = new StringTokenizer(br.readLine());
    		int vertex1 = Integer.parseInt(st.nextToken()) - 1;
    		int vertex2 = Integer.parseInt(st.nextToken()) - 1;
    		
    		graphMap.get(vertex1).add(vertex2);
    		graphMap.get(vertex2).add(vertex1);
    	}
    	
    	st = new StringTokenizer(br.readLine());
    	startVertex = Integer.parseInt(st.nextToken()) - 1;
    	destVertex = Integer.parseInt(st.nextToken()) - 1;
     }
}