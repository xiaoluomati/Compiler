package lexer.fa;

import java.util.List;
import java.util.Vector;

import lexer.re.REConverter;

public class NFAConverter {
	
	private NFA nfa;
	
	public NFAConverter(NFA nfa) {
		this.nfa = nfa;
	}
	
	public static void main(String[] args) {
		NFA nfa = REConverter.REtoNFA("()");
//		nfa.print();
//		System.out.println(nfa.endState);
		NFAConverter nfaConverter = new NFAConverter(nfa);
		DFA dfa = nfaConverter.toDFA();
		dfa.print();
		System.out.println(dfa.endState);
		dfa.minStates().print();
		System.out.println(dfa.endState);
	}

	public DFA toDFA() {
		Vector<DState> dStates = new Vector<>();
		int i = 0;
		State s =  new State((i++)+"");
		dStates.addElement(new DState(s, esClosure(nfa.startState)));
		List<String> inputs = nfa.getInputs();
		while(existNotMarked(dStates)){
			DState dState = findFirstNotMarked(dStates);
			dState.isMarked = true;
			for (String input : inputs) {
				if(input.equals(Edge.EMPTY_STRING)){
					continue;
				}
				StateGroup set = esClosure(move(dState.set, input));
				
				if(!isInDstates(dStates, set)){
					dStates.addElement(new DState(new State(i+""), set));
					i++;
				}
				new Edge(input, dState.state, findDState(dStates, set).state);
			}
		}
		for (DState dState1 : dStates) {
			if(dState1.set.getStates().contains(nfa.getNFAEndState())){
				dState1.state.setEndState(true);
			}
		}
		DFA dfa = new DFA(s);
		Vector<State> states = new Vector<>();
		for (State state : dfa.states) {
			boolean x = true;
			for (String input : inputs) {
				if(input.equals(Edge.EMPTY_STRING)){
					continue;
				}
				if(!state.getStatesByOneInput(input).isEmpty()){
					x = x && state.getStatesByOneInput(input).get(0).equals(state);
				}
			}
			if(x){
				states.add(state);
			}
		}
		for (State state : states) {
			dfa.deleteState(state);
		}
		return dfa;
	}
	
	private DState findDState(List<DState> dList, StateGroup set){
		DState result = null;
		for (DState dState : dList) {
			if(dState.set.equals(set)){
				result = dState;
				break;
			}
		}
		return result;
	}
	
	private boolean isInDstates(List<DState> dList, StateGroup set){
		for (DState dState1 : dList) {
			if(dState1.set.equals(set))
				return true;
		}
		return false;
	}
	
	private boolean existNotMarked(List<DState> dList){
		for (DState dState : dList) {
			if(!dState.isMarked)
				return true;
		}
		return false;
	}
	
	private DState findFirstNotMarked(List<DState> dList){
		if(!existNotMarked(dList))
			return null;
		DState dS = null;
		for (DState dState : dList) {
			if(!dState.isMarked){
				dS = dState;
				break;
			}
		}
		return dS;
	}
	
	private StateGroup move(StateGroup set, String input){
		if(input == null || input.equals(Edge.EMPTY_STRING))
			return null;
		StateGroup StateGroup = new StateGroup();
		for (State state : set) {
			StateGroup.add(new StateGroup(state.getStatesByOneInput(input)));
		}
		return StateGroup;	
	}
	
	private StateGroup esClosure(StateGroup set){
		StateGroup StateGroup = new StateGroup();
		for (State state : set) {
			StateGroup.add(esClosure(state));
		}
		return StateGroup;
	}
	
	private StateGroup esClosure(State state){
		StateGroup StateGroup = new StateGroup();
		StateGroup.add(state);
		while(!isComplete(StateGroup)){
			StateGroup set = new StateGroup();
			for (State state2 : StateGroup) {
				set.add(new StateGroup(state2.getStatesByOneInput(Edge.EMPTY_STRING)));
			}
			StateGroup.add(set);
		}
		return StateGroup;
	}
	
	private boolean isComplete(StateGroup set){
		for (State state : set) {
			StateGroup set2 = new StateGroup(state.getStatesByOneInput(Edge.EMPTY_STRING));
			if(!set2.isSubSetOf(set))
				return false;
		}
		return true;
	}
	
	class DState{

		State state;
		
		boolean isMarked;
		
		StateGroup set;
		
		DState(State state, StateGroup set) {
			this.state = state;
			this.set = set;
			this.isMarked = false;
		}
		
	}

	
}
