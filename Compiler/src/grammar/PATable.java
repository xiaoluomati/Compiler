package grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class PATable {

	private Vector<Sign> nSigns;
	
	private Vector<Sign> tSigns;
	
	private Production[][] table;
	
	private static final String PATH = "PATable.txt";
	
	public static void main(String[] args) {
		new PATable();
	}
	
	public PATable() {
		nSigns = new Vector<>();
		tSigns = new Vector<>();
		read();
	}
	
	public Sign getStartSign(){
		for (Sign nonterminalSign : nSigns) {
			if(nonterminalSign.getOrder() == 0)
				return nonterminalSign;
		}
		return null;
	}
	
	public void print() {
		for (Production[] productions : table) {
			for (Production production : productions) {
				System.out.print((production == null ? "X" : production) + " ");
			}
			System.out.println();
		}
	}

	private void read(){
		File file = new File(PATH);
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = reader.readLine();
            String[] strings = tempString.split(",");
            for (int i = 0;i < strings.length;i++) {
				tSigns.add(new Sign(strings[i],i));
			}
            tempString = reader.readLine();
            String[] strings2 = tempString.split(",");
            for (int i = 0; i < strings2.length; i++) {
				nSigns.add(new Sign(strings2[i], i));
			}
            table = new Production[strings2.length][strings.length];
            while ((tempString = reader.readLine()) != null) {
            	String[] proList = tempString.split(",");
            	int line = -1;
            	for (Sign nSign : nSigns) {
					if(nSign.getString().equals(proList[0])){
						line = nSign.getOrder();
					}
				}
            	for (int i = 1; i < proList.length; i++) {
            		String string = proList[i];
            		if(!string.equals("$"))
            			table[line][i-1] = strToProduction(string);
            		else
            			table[line][i-1] = null;
				}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	private Production strToProduction(String string){
		String[] strings = string.split("->");
		Sign nonterminalSign = new Sign(strings[0]);
		Vector<Sign> list = new Vector<>();
		if(strings.length == 1){
			list.add(Sign.EMPTY_STRING);
		}else{
			String right = strings[1];
			String str = "";
			for (int i = 0; i < right.length(); i++) {
				str += right.charAt(i);
				boolean found = false;
				for (Sign sign : tSigns) {
					if(sign.getString().equals(str)){
						found = true;
						list.add(sign);
					}
				}
				for (Sign sign : nSigns) {
					if(sign.getString().equals(str)){
						found = true;
						list.add(sign);
					}
				}
				if(found){
					str = "";
				}
			}
		}
		return new Production(nonterminalSign, list);
	}
	
	public Production match(Sign nSign, Sign tSign) {
		int x = -1;
		for (Sign nonterminalSign : nSigns) {
			if(nonterminalSign.equals(nSign)){
				x = nonterminalSign.getOrder();
			}
		}
		int y = -1;
		for (Sign terminalSign : tSigns) {
			if(terminalSign.equals(tSign)){
				y = terminalSign.getOrder();
			}
		}
		return x == -1 || y == -1 ? null : table[x][y];
	}
	
}
