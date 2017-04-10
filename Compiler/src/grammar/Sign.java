package grammar;

public class Sign {
	
	public static final Sign EMPTY_STRING = new Sign("");

	public static final Sign ENDING_SIGN = new Sign("$");
	
	protected String string;
	
	protected int order = -1;
	
	public Sign(String string) {
		this.string = string;
	}
	
	public Sign(String string, int order) {
		this.string = string;
		this.order = order;
	}
	
	public String getString() {
		return string;
	}

	public void setOrder(int num) {
		this.order = num;
	}
	
	@Override
	public boolean equals(Object obj) {
		return string.equals(((Sign)obj).string);
	}
	
	@Override
	public String toString() {
		return string;
	}
	
	public int getOrder() {
		return order;
	}
	
}
