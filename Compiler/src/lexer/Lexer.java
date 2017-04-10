package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import lexer.fa.DFA;
import lexer.fa.NFAConverter;
import lexer.fa.State;
import lexer.re.REConverter;

public class Lexer {
	
	private static final String LEXER_L = "lexer.l";
	
	private static final String INPUT = "input.txt";
	
	public static final String OUTPUT = "lex_output.txt";

	public static void launch() {
		HashMap<String, String> map1 = getSimplifiedREs();
		HashMap<String, DFA> dfas = new HashMap<>();
		HashMap<String, String> map2 = getNameOfTokens();
		for (String key : map2.keySet()) {
			String re = map1.get(map2.get(key));
			dfas.put(key, new NFAConverter(REConverter.REtoNFA(re)).toDFA().minStates());
		}
		writeOutput(dfas);
	}
	
	private static List<String> readInputs() {
		Vector<String> inputs = new Vector<>();
		File file = new File(INPUT);
		Reader reader = null;
	    try {
	        reader = new InputStreamReader(new FileInputStream(file));
	        int tempchar;
	        String string = "";
	        while ((tempchar = reader.read()) != -1) {
	        	char c = (char) tempchar;
	        	if(c != ' ' && c != '\n'){
	        		string += c == '\r' ? "" : c;
	        	}else{
	        		inputs.add(string);
	        		string = "";
	        	}
	        }
	        inputs.add(string);
	        reader.close();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
		 return inputs;
	}
	
	private static void writeOutput(HashMap<String, DFA> dfas) {
		List<String> inputs = readInputs();
		List<String> keywords = getKeywords();
		List<String> toWrites = new Vector<>();
		HashMap<String, String> map = getNameOfTokens();
		for (String string : inputs) {
			String toWrite = "";
			Vector<String> strings = new Vector<>();
			for (String key : dfas.keySet()) {
				if(isMatch(string, dfas.get(key))){
					strings.add(key);
				}
			}
			String token = null;
			if(strings.size() == 1){
				token = strings.get(0);
			}else if(strings.size() > 1){
				
				for (String string2 : strings) {
					if(keywords.contains(map.get(string2))){
						token = string2;
						break;
					}
				}
			}
			toWrite += ("<"+token+","+string+">");
			toWrites.add(toWrite);
		}
		 try {
            FileWriter writer = new FileWriter(OUTPUT);
            for (String string : toWrites) {
            	writer.write(string + "\n");
			}
            writer.close();
		 }catch (IOException e) {
            e.printStackTrace();
		 }
	}
	
	public static boolean isMatch(String input, DFA dfa) {
		State state = dfa.getStartState();
		for (int i = 0; i < input.length(); i++) {
			List<State> states = state.getStatesByOneInput(input.charAt(i)+"");
			if(!states.isEmpty())
				state = states.get(0);
			else 
				return false;
		}
		if(dfa.getEndState().contains(state)){
			return true;
		}
		else {
			return false;
		}
			
	}
	
	public static HashMap<String, String> getSimplifiedREs() {
		HashMap<String, String> map = getREs();
		for (String string : map.keySet()) {
			String re = map.get(string);
			if(re.contains("[") && re.contains("]")){
				while(re.contains("-")){
					int i = re.indexOf("-");
					String temp = "";
					for (int j = re.charAt(i-1); j <= re.charAt(i+1); j++) {
						temp += ((char)j) + "|";
					}
					re = re.replace(re.charAt(i-1)+"-"+re.charAt(i+1), temp);
				}
				re = re.replace("[", "(");
				re = re.replace("|]", ")");
				map.put(string, re);
			}
		}
		for (String string : map.keySet()) {
			String re = map.get(string);
			while (containString(map.keySet(), re)) {
				for (String string2 : map.keySet()) {
					if(re.contains(string2)){
						re = re.replace(string2, map.get(string2));
					}
				}
			}
			map.put(string, re);
		}
		return map;
	}
	
	private static boolean containString(Set<String> strings, String re){
		for (String string : strings) {
			if(re.contains(string))
				return true;
		}
		return false;
	}
	
	public static HashMap<String, String> getNameOfTokens() {
		File file = new File(LEXER_L);
		BufferedReader reader = null;
		HashMap<String, String> map = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while (!(tempString = reader.readLine()).equals("%%")) {
            	String[] strings = tempString.split(",");
            	for (String string : strings) {
					String[] strings2 = string.split(":");
					if (strings2.length > 1) {
						map.put(strings2[0], strings2[1]);
					}
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
		return map;
	}
	
	public static HashMap<String, String> getREs() {
		File file = new File(LEXER_L);
		BufferedReader reader = null;
		HashMap<String, String> map = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while (!(tempString = reader.readLine()).equals("%%")) {}
            while (!(tempString = reader.readLine()).equals("%%")) {
				if(tempString.contains("->")){
					String[] strings = tempString.split("->");
					map.put(delEndSpace(strings[0]), delStartSpace(strings[1]));
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
		return map;
	}
	
	public static List<String> getKeywords() {
		File file = new File(LEXER_L);
		BufferedReader reader = null;
		Vector<String> keywords = new Vector<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while (!(tempString = reader.readLine()).equals("%%")) {}
            while (!(tempString = reader.readLine()).equals("%%")) {}
            while ((tempString = reader.readLine()) != null) {
				if(tempString.contains(",")){
					String[] strings = tempString.split(",");
					for (String string : strings) {
						keywords.addElement(string);
					}
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
		return keywords;
	}
	
	private static String delEndSpace(String string) {
		while(string.charAt(string.length()-1)==' '){
			string = string.substring(0, string.length()-1);
		}
		return string;
	}
	
	private static String delStartSpace(String string) {
		while(string.charAt(0)==' '){
			string = string.substring(1);
		}
		return string;
	}
	
}
