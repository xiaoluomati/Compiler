package lexer.fa;

public class Edge{
	
	protected static final String EMPTY_STRING = "";

	private String input;
	
	private State start;
	
	private State end;

	public Edge(State start, State end) {
		this(EMPTY_STRING, start, end);
	}

	public Edge(String input, State start, State end) {
		super();
		this.input = input;
		this.start = start;
		this.end = end;
		this.start.addStartEdge(this);
		this.end.addEndEdge(this);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public State getStart() {
		return start;
	}

	public void setStart(State start) {
		this.start = start;
	}

	public State getEnd() {
		return end;
	}

	public void setEnd(State end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return start.getIdentity() + "->" + input + end.getIdentity();
	}
	
}
