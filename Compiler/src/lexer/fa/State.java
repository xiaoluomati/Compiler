package lexer.fa;

import java.util.List;
import java.util.Vector;

public class State{

	private String identity;
	
	private Vector<Edge> edgesStart;
	
	private Vector<Edge> edgesEnd;
	
	private boolean isEndState;

	public State(String identity) {
		this(identity, false);
	}

	public static void main(String[] args) {
		State state0 = new State(0+"");
		State state1 = new State(1+"", true);
		State state2 = new State(2+"", true);
		new Edge("a", state0, state1);
		new Edge("b", state0, state2);
		new Edge("a", state1, state1);
		new Edge("b", state1, state2);
		new Edge("a", state2, state1);
		new Edge("b", state2, state2);
		List<State> states = new Vector<>();
		states.add(state0);
		states.add(state1);
		states.add(state2);
//		State state = State.mergeAll(states);
//		state.printAboutEdge();
	}
	
	public State(String identity, boolean isEndState) {
		super();
		this.identity = identity;
		this.isEndState = isEndState;
		this.edgesStart = new Vector<>();
		this.edgesEnd = new Vector<>();
	}

	public List<Edge> getEdgesStart() {
		return edgesStart;
	}

	public List<Edge> getEdgesEnd() {
		return edgesEnd;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public void addStartEdge(Edge edge){
		if(!edgesStart.contains(edge))
			edgesStart.addElement(edge);
	}
	
	public void addEndEdge(Edge edge) {
		if(!edgesEnd.contains(edge))
			edgesEnd.addElement(edge);
	}
	
	public void delStartEdge(Edge edge){
		edgesStart.remove(edge);
	}
	
	public void delEndEdge(Edge edge) {
		edgesEnd.remove(edge);
	}
	
	
	
	public List<State> getStatesByOneInput(String input){
		List<State> states = new Vector<>();
		for (Edge edge : edgesStart) {
			if(edge.getInput().equals(input)){
				states.add(edge.getEnd());
			}
		}
		return states;
	}
	
	public List<String> getInputs(){
		List<String> inputs = new Vector<>();
		for (Edge edge : edgesStart) {
			if(!inputs.contains(edge.getInput())){
				inputs.add(edge.getInput());
			}
		}
		return inputs;
	}

	public boolean isEndState() {
		return isEndState;
	}

	public void setEndState(boolean isEndState) {
		this.isEndState = isEndState;
	}
	
	protected List<Edge> getAboutEdge() {
		List<Edge> edges = new Vector<>();
		for (Edge edge : edgesStart) {
			edges.add(edge);
		}
		for (Edge edge : edgesEnd) {
			if(!edges.contains(edge))
				edges.add(edge);
		}
		return edges;
	}
	
//	public State merge(State state) {
//		
//		State result = new State(identity);
//		for (Edge edge : state.edgesEnd) {
//			State s = edge.getStart();
//			if(s.equals(this))
//				new Edge(edge.getInput(), result, result);
//			else if(!s.equals(state))
//				new Edge(edge.getInput(), s, result);
//		}
//		for (Edge edge : edgesEnd) {
//			State s = edge.getStart();
//			if(s.equals(this))
//				new Edge(edge.getInput(), result, result);
//			else if(!s.equals(state))
//				new Edge(edge.getInput(), s, result);
//		}
//		for (String string : this.getInputs()) {
//			State s = this.getStatesByOneInput(string).get(0);		
//			if(s.equals(this))
//				new Edge(string, result, result);
//			else if(!s.equals(state))
//				new Edge(string, result, s);
//		}
//		return result;
//	}
	
	public void printAboutEdge() {
		System.out.println(identity);
		for (Edge edge : getAboutEdge()) {
			System.out.println(edge);
		}
		System.out.println();
	}
	
	@Override
	public String toString() {
		return identity;
	}
	
}
