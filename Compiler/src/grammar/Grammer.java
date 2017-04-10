package grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import lexer.Lexer;
import main.Stack;
import main.Tool;

public class Grammer {
	
	private static final String OUTPUT = "deduction.txt";

	public static void launch() {
		PATable paTable = new PATable();
		Stack<Sign> stack = new Stack<>();
		stack.push(Sign.ENDING_SIGN);
		stack.push(paTable.getStartSign());
		Sign sign = stack.getTop();
		File file = new File(Lexer.OUTPUT);
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = reader.readLine();
            while(!sign.equals(Sign.ENDING_SIGN)){
            	Sign tSign = geTerminalSign(tempString);
            	if(sign.equals(tSign)){
            		Tool.writeline(OUTPUT, "匹配 " + tSign);
            		stack.pop();
            		tempString = reader.readLine();
            	}else if(paTable.match(sign, tSign) != null){
            		Production production = paTable.match(sign, tSign);
            		Tool.writeline(OUTPUT, "输出 " + production);
            		List<Sign> signs = production.getRight();
            		stack.pop();
            		for (int i = signs.size() - 1; i >= 0; i--) {
						stack.push(signs.get(i));
					}
            	}
            	while(stack.getTop().equals(Sign.EMPTY_STRING)){
            		stack.pop();
            	}
            	sign = stack.getTop();
    		}
            Tool.writeline(OUTPUT, "-----------------------------");
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
	
	private static Sign geTerminalSign(String string){
		if(string == null)
			return Sign.ENDING_SIGN;
		if(string.equals("$"))
			return Sign.ENDING_SIGN;
		string = string.substring(1, string.length() - 1);
		String[] strings = string.split(",");
		if(strings[0].equals("ID")){
			return new Sign("ID");
		}else if(strings[0].equals("OPERATION")){
			return new Sign(strings[1]);
		}
		return null;
	}
	
}
