package lexer.re;

public class Operand {

	private String string;

	public Operand(String string) {
		super();
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
