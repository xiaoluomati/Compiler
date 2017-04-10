package lexer.re;

import java.util.List;
import java.util.Vector;

import lexer.fa.NFA;
import main.Stack;

public class REConverter {
	
	public static void main(String[] args) {
		NFA a = REtoNFA("(a|b)(a|b|c)*");
		a.print();
		System.out.println(a.getStartState());
		System.out.println(a.getEndState());
	}
	
	public static NFA REtoNFA(String RE) {
		List<Object> input = infixToSuffix(RE);
		Stack<NFA> nfaStack = new Stack<>();
		for (Object object : input) {
			if(object instanceof Character){
				nfaStack.push(NFA.getNFAOfSingleChar(((Character) object).charValue()));
			}else{
				Operator o = (Operator) object;
				if(o.equals(Operator.link)){
					NFA a = nfaStack.pop();
					NFA b = nfaStack.pop();
					nfaStack.push(b.link(a));
				}else if(o.equals(Operator.or)){
					nfaStack.push(nfaStack.pop().or(nfaStack.pop()));
				}else if(o.equals(Operator.closure)){
					nfaStack.push(nfaStack.pop().closure());
				}
			}
		}
		return nfaStack.pop();
	}
	
	private static List<Object> infixToSuffix(String infixRe){
		Stack<Operator> s = new Stack<>();
		List<Object> list = new Vector<>();
		String specialChars = "*|()\0";
		
		String infixWithLink = "";
		for (int i = 0; i < infixRe.length()-1; i++){
			infixWithLink += infixRe.charAt(i);
			char c1 = infixRe.charAt(i);
			char c2 = infixRe.charAt(i+1);
			if(c1 != '|' && c1 !='(' && c2 != '|' && c2 != ')' && c2 != '*'){
				infixWithLink += '\0';
			}
		}
		infixWithLink+=infixRe.charAt(infixRe.length()-1);
		
		for (int i = 0; i < infixWithLink.length(); i++) {
			char c = infixWithLink.charAt(i);
			if(!specialChars.contains(c+"")){
				list.add(new Character(c));
			}else{
				switch (c) {
				case '*':
					dealOperator(s, Operator.closure, list);
					break;
				case '|':	
					dealOperator(s, Operator.or, list);	
					break;
				case '\0':
					dealOperator(s, Operator.link, list);	
					break;
				case '(':
					s.push(Operator.left_parentheses);
					break;
				case ')':
					while (!s.isEmpty() && s.getTop().getCharacter() != '(') {
						list.add(s.pop());
					}
					s.pop();
					break;
				}
			}
		}
		while (!s.isEmpty()) {
			list.add(s.pop());
		}
		return list;
	}
	
	private static void dealOperator(Stack<Operator> s, Operator operator, List<Object> output) {
		if(s.isEmpty() || s.getTop().getPriority() < operator.getPriority())
			s.push(operator);
		else {
			while (!s.isEmpty() && s.getTop().getCharacter() != '(' && s.getTop().getPriority() > operator.getPriority()) {
				output.add(s.pop());
			}
			s.push(operator);
		}
	}
}
