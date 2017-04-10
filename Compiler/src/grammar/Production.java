package grammar;

import java.util.List;

public class Production {

	private Sign left;
	
	private List<Sign> right;

	public Production(Sign left, List<Sign> right) {
		super();
		this.left = left;
		this.right = right;
	}

	public Sign getLeft() {
		return left;
	}

	public void setLeft(Sign left) {
		this.left = left;
	}

	public List<Sign> getRight() {
		return right;
	}

	public void setRight(List<Sign> right) {
		this.right = right;
	}
	
	@Override
	public String toString() {
		String string = "";
		string += left.getString();
		string += "->";
		for (Sign sign : right) {
			string += sign.getString() + " ";
		}
		return string;
	}
}
