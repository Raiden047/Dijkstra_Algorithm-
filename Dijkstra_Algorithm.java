import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.JOptionPane;

public class Dijkstra_Algorithm {
	public static void main(String[] args) throws IOException {

		int more = 0;
		// this loop is for allowing the user to continue using the program
		// until they wish to exit
		while (more == 0) {
			Map path = new Map();

			String input = JOptionPane.showInputDialog("Please enter file location: "
					+ "\n*Note: The file must have data format of\n" + "Node-ID Node-ID Distance");
			
			String fixInput = fixfileLoc(input);
			path.insertFile(fixInput);
			path.displayMap();
			DijkstraTable table = new DijkstraTable(path.map);

			boolean choose = false;
			while (choose == false) {
				String solveFor = JOptionPane.showInputDialog("Please enter the ID of the node you wish to start at: ");
				table.solveTable(solveFor);
				
				System.out.print(table.displayTable());
				
				String destination = JOptionPane.showInputDialog("Please enter the ID of the node you wish to go to: ");
				JOptionPane.showMessageDialog(null, table.showRoute(destination));

				more = JOptionPane.showConfirmDialog(null, "Do you want to go again?", 																
						"or log out.", JOptionPane.YES_NO_OPTION);

				if (more == 1){
					JOptionPane.showMessageDialog(null, "Have a great day!");
					choose = true;
				}

			}
		}

	}
	
	public static String fixfileLoc(String loc){
		String output = "";
		
		for(int i = 0; i < loc.length(); i++){
			if(loc.substring(i,i+1).equals("\\")){
				output += "\\\\";
			}
			else{
				output += loc.charAt(i);
			}
		}
		
		return output;
	}
}

class Link {
	String id;
	int length;
	Node next;

	public Link() {
		length = 0;
		next = null;
	}

	public Link(Node n, int d) {
		next = n;
		length = d;
	}

	public Node getNext() {
		return next;
	}

	public int getLength() {
		return length;
	}

}

class Node {
	String id;
	ArrayList<Link> linkList = new ArrayList<Link>();

	public Node() {
		id = "";
	}

	public Node(String i) {
		id = i;
	}

	public void addLink(Node n, int l) {
		linkList.add(new Link(n, l));
	}

	public Node getLink(String des) {
		for (int i = 0; i < linkList.size(); i++) {
			if (linkList.get(i).getNext().getId().equals(des))
				return linkList.get(i).getNext();
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		String output = "Node: " + this.getId() + "\n";

		for (int i = 0; i < linkList.size(); i++) {
			output += this.getId() + " --> " + linkList.get(i).getNext().getId() + " Length: "
					+ linkList.get(i).getLength() + "\n";
		}

		return output;
	}

}

class NodeNetwork {

	ArrayList<Node> nodeList = new ArrayList<Node>();
	int size;

	public NodeNetwork() {
		size = 0;
	}

	public void addConnection(String a, String b, int d) {
		Node one = new Node(a);
		Node two = new Node(b);

		if (nodePresent(a))
			getNode(a).addLink(two, d);
		else {
			one.addLink(two, d);
			nodeList.add(one);
			size++;
		}

		if (nodePresent(b))
			getNode(b).addLink(one, d);
		else {
			two.addLink(one, d);
			nodeList.add(two);
			size++;
		}

	}

	public boolean nodePresent(String n) {
		boolean present = false;

		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getId().equals(n))
				present = true;
		}

		return present;
	}

	public int getSize() {
		return size;
	}

	public Node getNode(String n) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getId().equals(n))
				return nodeList.get(i);
		}
		return null;
	}

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	public String toString() {
		String output = "";

		for (int i = 0; i < nodeList.size(); i++) {
			output += nodeList.get(i) + "\n";
		}

		return output;
	}

}

class DijkstraRow {
	Node first;
	Boolean visited = false;
	Boolean infinite = true;
	int distance = 0;
	Node from;

	public DijkstraRow(Node one) {
		first = one;
		from = null;
	}

	public void upDateDistance(int d) {
		distance = d;
	}

	public void setFrom(Node two) {
		from = two;
	}

	public void setVisited() {
		visited = true;
	}

	public boolean isVisited() {
		return visited;
	}

	public boolean isInfinite() {
		return infinite;
	}

	public void setNotInfinite() {
		infinite = false;
	}

	public String toString() {
		String output = "";
		if (first != null && from != null) {
			output = first.getId() + "\t" + this.distance + "\t" + from.getId();
		} else {
			if (first != null)
				output = first.getId() + "\t" + this.distance + "\tNull";
		}

		return output;
	}

}

class DijkstraTable {

	ArrayList<DijkstraRow> table = new ArrayList<DijkstraRow>();
	Node start;

	public DijkstraTable(NodeNetwork map) {
		for (int i = 0; i < map.getSize(); i++) {

			DijkstraRow row = new DijkstraRow(map.getNodeList().get(i));
			table.add(row);

		}
	}

	public void solveTable(String str) {
		start = this.getNode(str);
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).first.getId().equals(start.getId())) {
				getRow(start).setNotInfinite();
				transverse(table.get(i).first);
			}
		}
	}

	public void transverse(Node one) {
		if (one != null) {
			for (int i = 0; i < one.linkList.size(); i++) {
				Node next = one.linkList.get(i).getNext();
				if (!getRow(next).isVisited()) {
					int oneDisToNext = getRow(one).distance + one.linkList.get(i).getLength();
					int nextDis = getRow(next).distance;
					if (getRow(next).isInfinite()) {
						getRow(next).upDateDistance(oneDisToNext);
						getRow(next).setNotInfinite();
						getRow(next).setFrom(one);
					} else {
						if (oneDisToNext < nextDis) {
							getRow(next).upDateDistance(oneDisToNext);
							getRow(next).setNotInfinite();
							getRow(next).setFrom(one);
						}
					}
				}
			}

			getRow(one).setVisited();

			int min = 9999;
			Node smallest = null;
			for (int i = 0; i < table.size(); i++) {
				if (!table.get(i).isInfinite() && table.get(i).distance != 0 && !table.get(i).isVisited()) {
					if (table.get(i).distance < min) {
						min = table.get(i).distance;
						smallest = table.get(i).first;
					}
				}
			}

			transverse(smallest);
		}
	}

	public String showRoute(String str) {
		String output = "";
		Node first = this.getNode(str);
		Node next = getRow(this.getNode(str)).from;
		// System.out.println("Total Distance: " + getRow(first).distance);
		output = "Total Distance: " + getRow(first).distance + "\n";

		while (getRow(first).from != null) {
			// System.out.print(first.getId() + " --> ");
			output += first.getId() + " --> ";
			first = getRow(first).from;
		}
		output += start.getId();
		// System.out.print(start.getId());

		return output;
	}

	public DijkstraRow getRow(Node get) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).first.getId().equals(get.getId())) {
				return table.get(i);
			}
		}
		return null;
	}

	public Node getNode(String str) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).first.getId().equals(str)) {
				return table.get(i).first;
			}
		}
		return null;
	}

	public String displayTable() {
		String output = "";
		
		//System.out.println("Start\tLength\tPrevious");
		output += "Start\tLength\tPrevious\n";
		//System.out.println(this);
		output += this.toString();
		
		return output;
	}

	public String toString() {
		String output = "";

		for (int i = 0; i < table.size(); i++) {
			output += table.get(i) + "\n";
		}

		return output;
	}

}

class Map {

	NodeNetwork map = new NodeNetwork();

	public Map() throws IOException {

	}

	public void insertFile(String fileLoc) throws IOException {
		// "C:\\Users\\Asad\\Documents\\Workspace\\Dijkstra_Algorithm\\src\\file.txt"
		Scanner input = new Scanner(new File(fileLoc));

		while (input.hasNext()) {

			map.addConnection(input.next(), input.next(), input.nextInt());

		}
	}

	public void displayMap() {
		System.out.println("Map size: " + map.getSize() + "\n");
		System.out.println(map);

	}

}