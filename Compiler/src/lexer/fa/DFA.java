package lexer.fa;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class DFA
extends FA{

	public DFA(State startState) {
		super(startState);
	}
	
	public void mergeStates(List<State> states) {
		if(states.isEmpty())
			return;
		
		Vector<Edge> linkOther = new Vector<>();
		Vector<Edge> linkSelf = new Vector<>();
		State newState = new State(states.get(0).getIdentity());
 		for (State state : states) {
 			if(state.isEndState())
 				newState.setEndState(true);
 			if(this.startState.equals(state))
 				this.setStartState(newState);
			for (Edge edge : state.getAboutEdge()) {
				if(!states.contains(edge.getStart()) || !states.contains(edge.getEnd())){
					linkOther.add(edge);
				}else{
					linkSelf.add(edge);
				}
				this.edges.remove(edge);
			}
		}
 		
 		Vector<String> stringsIn = new Vector<>();
 		Vector<String> stringsOut = new Vector<>();
		for (Edge edge : linkOther) {
			String input = edge.getInput();
//			System.out.println(edge);
			if(!stringsIn.contains(input) && states.contains(edge.getEnd())){
				new Edge(input, edge.getStart(), newState);
				stringsIn.add(input);
			}else if(!stringsOut.contains(input) &&states.contains(edge.getStart())){
				new Edge(input, newState, edge.getEnd());
				stringsOut.add(input);
			}
//			System.out.println(newState.getAboutEdge());
//			Tool.printline();
		}
		Vector<String> strings = new Vector<>();
		for (Edge edge : linkSelf) {
			String input = edge.getInput();
			if(!strings.contains(input)){
				new Edge(input,newState, newState);
				strings.addElement(input);
			}
		}
//		int size = states.size();
//		for (int i = 0; i < size; i++) {
//			this.deleteState(states.get(0));
//		}
		while (!states.isEmpty()) {
			int x = states.size();
			this.deleteState(states.get(0));
			if(x == states.size()){
				states.remove(0);
			}
		}
		
		this.addState(newState);
	}
	
	public DFA minStates(){
		Vector<StateGroup> groups = new Vector<>();
		groups.addElement(new StateGroup(this.endState));
		StateGroup nF = new StateGroup();
		for (State state : this.states) {
			if(!state.isEndState()){
				nF.add(state);
			}
		}
		groups.addElement(nF);
		
		while (isSeparable(groups)) {
			StateGroup stateGroup = this.getNextSeparable(groups);
			
			for (String input : this.getInputs()) {
				if(isSeparable(groups, stateGroup, input)){
					int[] arr = StateGroup.getStatePos(groups, stateGroup.trans(input));
					HashMap<Integer, StateGroup> map = new HashMap<>();
					for (int i = 0; i < arr.length; i++) {
						if(!map.keySet().contains(arr[i])){
							map.put(arr[i], new StateGroup());
						}
						map.get(arr[i]).add(stateGroup.getStates().get(i));
					}
					groups.remove(stateGroup);
					for (StateGroup stateGroup2 : map.values()) {
						groups.add(stateGroup2);
					}
					break;
				}
			}
		}
		
		for (StateGroup stateGroup : groups) {
			List<State> states = stateGroup.getStates();
			if(states.size() > 1){
				this.mergeStates(states);
			}
		}
		return this;
	}
	
	private boolean isSeparable(List<StateGroup> stateGroups){
		for (StateGroup stateGroup : stateGroups) {
			if(this.isSeparable(stateGroups, stateGroup))
				return true;
		}
		return false;
	}
	
	
	private boolean isSeparable(List<StateGroup> stateGroups,StateGroup stateGroup){
		for (String input : this.getInputs()) {
			if(isSeparable(stateGroups, stateGroup, input))
				return true;
		}
		return false;
	}
	
	private boolean isSeparable(List<StateGroup> stateGroups,StateGroup stateGroup,String input){
		if(stateGroup.getStates().size() == 1)
			return false;
		int[] arr = StateGroup.getStatePos(stateGroups, stateGroup.trans(input));
		for (int i = 0; i < arr.length; i++) {
			if(arr[0] != arr[i])
				return true;
		}
		return false;
	}
	
	private StateGroup getNextSeparable(List<StateGroup> stateGroups){
		for (StateGroup stateGroup : stateGroups) {
			if (isSeparable(stateGroups, stateGroup)) {
				return stateGroup;
			}
		}
		return null;
	}
}
