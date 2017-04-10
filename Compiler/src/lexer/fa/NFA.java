package lexer.fa;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class NFA extends FA{

	public NFA(State startState) {
		super(startState);
	}

	public static NFA getNFAOfSingleChar(char c) {
		State s = new State(0+"");
		State e = new State(1+"", true);
		new Edge(c+"", s, e);
		return new NFA(s);
	}
	
	public NFA or(NFA nfa) {
		State s = new State(0+"");
		int i = 1;
		for (State state : this.getStates()) {
			state.setIdentity(i+"");
			i++;
		}
		for (State state : nfa.getStates()) {
			state.setIdentity(i+"");
			i++;
		}
		State e = new State(i+"", true);
		new Edge(s, this.getStartState());
		new Edge(s, nfa.getStartState());
		for (State state : this.getEndState()) {
			state.setEndState(false);
			new Edge(state, e);
		}
		for (State state : nfa.getEndState()) {
			state.setEndState(false);
			new Edge(state, e);
		}
		return new NFA(s);
	}
	
	public NFA link(NFA nfa) {
		int i = this.getNumberOfStates();
		//获得第二个NFA的开始状态为起点的边
		List<Edge> edges1 = nfa.getStartState().getEdgesStart();
		HashMap<State,String> map1 = new HashMap<>();
		for (Edge edge : edges1) {
			map1.put(edge.getEnd(),edge.getInput());
		}
		//获得第二个NFA的开始状态为终点的边
		List<Edge> edges2 = nfa.getStartState().getEdgesEnd();
		HashMap<State,String> map2 = new HashMap<>();
		for (Edge edge : edges2) {
			map2.put(edge.getStart(),edge.getInput());
		}
				
		nfa.deleteState(nfa.getStartState());
		State state = this.getEndState().get(0);
		state.setEndState(false);
		for (State s : map1.keySet()) {
			new Edge(map1.get(s), state, s);
		}
		for (State s : map2.keySet()) {
			new Edge(map2.get(s), s, state);
		}
		for (int j = 0; j < nfa.getNumberOfStates(); j++) {
			nfa.getStates().get(j).setIdentity((i+j)+"");
		}
		return new NFA(this.getStartState());
	}
	
	public NFA closure() {
		State s = new State(0+"");
		State e = new State((1+this.getNumberOfStates())+"", true);
		int i = 1;
		for (State state : states) {
			state.setIdentity(i + "");
			i++;
		}
		State e0 = this.getEndState().get(0);
		e0.setEndState(false);
		new Edge(s, e);
		new Edge(s, this.getStartState());
		new Edge(e0, e);
		new Edge(e0, this.getStartState());
		
		return new NFA(s);
	}
	
	public static NFA combine(List<NFA> nfas) {
		State s = new State(0+"");
		int i = 1;
		for (NFA nfa : nfas) {
			for (State state : nfa.states) {
				state.setIdentity(i+"");
				i++;
			}
			new Edge(s, nfa.getStartState());
		}
		return  new NFA(s);
	}
	
	public static NFA combine(NFA...nfas) {
		List<NFA> nfas2 = new Vector<NFA>();
		for (NFA nfa : nfas) {
			nfas2.add(nfa);
		}
		return combine(nfas2);
	}
	
	public State getNFAEndState() {
		return endState.get(0);
	}
	
//	public static void main(String[] args) {
//		NFA a = getNFAOfSingleChar('a');
////		a.print();
////		NFA b = a.closure();
////		b.print();
//		NFA c = getNFAOfSingleChar('c');
////		NFA d = b.link(c);
////		d.print();
//		NFA e = getNFAOfSingleChar('e');
////		NFA f = d.or(e);
////		f.print();
//		combine(a,c,e).print();
//		for (State state : combine(a,c,e).endState) {
//			System.out.println(state);
//		}
//	}
	
}
