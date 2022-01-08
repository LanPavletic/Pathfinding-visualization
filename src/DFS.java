
import java.util.ArrayList;
import java.util.Stack;

public class DFS {
	
	public static void search(int[][] graph, int startNode, ArrayList<Integer> endNodes)
	{
		boolean[] marked = new boolean[graph.length];
		int[] from = new int[graph.length];
		
		
		Stack<Integer> stack = new Stack<Integer>();
	
		from[startNode] = -1;
		marked[startNode] = true;
		stack.push(startNode);
		
		System.out.println("Polagam na sklad vozlisce " + startNode);
		
		while(!stack.isEmpty())
		{
			int curNode = stack.peek();

			if (endNodes.contains(curNode))
			{
				System.out.println("Resitev DFS v vozliscu " + curNode);
				System.out.print("Pot: " + curNode);
				
				while (true)
				{
					curNode = from[curNode];
					if (curNode != -1)
						System.out.print(" <-- " + curNode);
					else
						break;
				}
				
				return;
			}

			// najdi neobiskanega naslednjika
			boolean found = false;
			for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++)
			{
				if (graph[curNode][nextNode] == 1 && !marked[nextNode])
				{
						marked[nextNode] = true;
						from[nextNode] = curNode;
						stack.push(nextNode);
						
						System.out.println("Polagam na sklad vozlisce " + nextNode);
						
						found = true;
						break;
				}
			}
			
			if (!found)
			{
				stack.pop();
				System.out.println("Odstranjum s sklada vozlisce " + curNode);
			}
		}
	}
	
	public static void main(String[] args) 
	{
		int[][] graph = {
				{0,1,0,0,0,0,0,0},
				{0,0,1,0,0,0,0,1},
				{0,0,0,0,1,1,0,0},
				{0,1,0,0,0,0,0,1},
				{0,0,0,0,0,0,1,1},
				{0,0,0,1,0,0,1,0},
				{0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0}};
		
		int startNode = 0;
		ArrayList<Integer> endNodes = new ArrayList<Integer>();
		endNodes.add(6);
		endNodes.add(7);
		
		DFS.search(graph, startNode, endNodes);

	}

}
