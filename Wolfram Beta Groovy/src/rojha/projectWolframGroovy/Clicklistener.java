package rojha.projectWolframGroovy;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import java.util.ArrayList;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
class Clicklistener implements ActionListener{
	static int improperComputionCheck = 0;
	JButton button;
	public Clicklistener(JButton button8) {
		button = button8;
	}
	public void actionPerformed(ActionEvent e) {
		if (button.getText().equals("Enter")) {
			answer();
		}
		else {
			answer(button);
		}
	}
	public static void answer() {
		Main.amendAnswer(preliminaryTests(Main.text()));
	}
	public void answer(JButton buttonts) {
		String addString = buttonts.getText();
		if (addString=="^") {
			addString = "pow(b,p)";
		}
		else if (addString=="logBASEn") {
			addString = "logn(";
		}
		else if (addString=="sqrt"||addString=="cbrt"||addString=="floor") {
			addString += "(";
		}
		else if (addString=="ceiling") {
			addString = "ceil(";
		}
		Main.amendQuestion(addString);
	}
	public static String preliminaryTests(String input) {
		long startTime = System.currentTimeMillis();
		Main.updatePhoto("Clear");
		String selectedItem = (String) Main.methodField.getSelectedItem();
		String addendum = "";
		input = input.replace("sec", "1/cos");
		input = input.replace("csc", "1/sin");
		input = input.replace("cot", "1/tan");
		input = input.replace("pi","3.1415926535");
		input = input.replace("e","2.71828");
		input = input.replace("%", "/100");
		input = input.replace("c2.71828il", "ceil");
		if (input.contains("log(")){
			addendum +="\nAssumed log base e";
		}
		input = input.replace("ln","log");
		if (selectedItem == "Indefinite Integral") {
			String saveInput = input;
			input = indefInt(input);
			input = input.replace("loge", "ln");
			if (saveInput == input||input.contains("k")) {
				input = "We could not calculate this integral";
			}
			improperComputionCheck = 0;
			if (input.contains("We could not")) {
				input = "We could not calculate this integral";
			}
			if (input.charAt(0)!='W') {
				input+=" + constant";
			}
			long endTime = System.currentTimeMillis();
			addendum += "\n\n\nResults calculated in " + (endTime-startTime)/(double) 1000 + " seconds";
			return input + addendum;
		}
		else if (selectedItem == "Func Derivative") {
			String saveInput = input;
			input = funcDeriv(input);
			System.out.println(input);
			input = input.replace("loge", "ln");
			if (saveInput == input||input.contains("k")) {
				input = "We could not calculate this derivative";
			}
			improperComputionCheck = 0;
			if (input.contains("We could not")) {
				input = "We could not calculate this derivative";
			}
			long endTime = System.currentTimeMillis();
			addendum += "\n\n\nResults calculated in " + (endTime-startTime)/(double) 1000 + " seconds";
			return input + addendum;
			
		}
		input = input.replace(")(",")*(");
		input = input.replace("pow(", "Math.pow(");
		input = input.replace("sqrt(","Math.sqrt(");
		input = input.replace("cbrt(", "Math.cbrt(");
		input = input.replace("arcsin", "Math.asin");
		input = input.replace("arccos", "Math.acos");
		input = input.replace("arctan", "Math.atan");
		input = input.replace("floor", "Math.floor");
		input = input.replace("ceil", "Math.ceil");
		for (int d = 0; d < input.length()-4; d++) {
			if (input.substring(d,d+3).equals("sin")){
				if (d==0||(input.charAt(d-1)!='a'&&input.charAt(d+2)!='h')) {
					input = input.substring(0,d) + "Math.sin" + input.substring(d+3);
					d+=5;
					System.out.println(input);
				}
			}
			else if (input.substring(d,d+3).equals("cos")){
				if (d==0||(input.charAt(d-1)!='a'&&input.charAt(d+2)!='h')) {
					input = input.substring(0,d) + "Math.cos" + input.substring(d+3);
					d+=5;
					System.out.println(input);
				}
			}
			else if (input.substring(d,d+3).equals("tan")){
				if (d==0||(input.charAt(d-1)!='a'&&input.charAt(d+2)!='h')) {
					input = input.substring(0,d) + "Math.tan" + input.substring(d+3);
					d+=5;
					System.out.println(input);
				}
			}
		}
		if (input.contains("tanh")&&!input.contains("Math.")||input.contains("abs")&&!input.contains("Math.")){
			addendum+="\nRemember to use the java.lang.Math syntax for function not seen in one of the buttons.";
		}
		input = input.replace("y=", "");
		String base = "";
		int placeLogger = 0;
		int placeEnd = 0;
		int numParens = 1;
		String logger = "";
		for (int i = 0; i < input.length()-5; i++) {
			if (input.substring(i,i+3).equals("log")) {
				for (int j = i+3;j<input.length();j++) {
					if (input.charAt(j)=='(') {	
						placeLogger = j+1;
						break;
					}
					base += input.charAt(j);
				}
				for (int j = placeLogger;j<input.length();j++) {
					if (input.charAt(j)=='(') {
						numParens++;
					}
					else if (input.charAt(j)==')') {	
						numParens--;
					}
					if (numParens==0) {
						placeEnd = j;
						break;
					}
					logger += input.charAt(j);
				}
				if (base == "") {
					base = "2.71828";
				}
				input = input.substring(0,i) + "Math.log(" + logger + ")/Math.log(" + base + input.substring(placeEnd);
				i = i+23+logger.length()+base.length();
			}
		}
		if (input.contains("^")) {
			addendum +="\nMake sure to use the format pow(base,power) instead of '^'";
		}
		System.out.println(input);
		if (selectedItem == "Derivative") {
			input = derivative(input);
			if (input.equals("20480.0")) {
				input = "Infinity";
			}
		}
		else if (selectedItem == "Evaluate Expression") {
			input = evaluate(input);
		}
		else if (selectedItem == "Sum") {
			input = sum(input);
		}
		else if (selectedItem == "Definite Integral") {
			input = defInt(input);
		}
		else if (selectedItem == "Limit as x approaches n") {
			input = limit(input);
		}
		else if (selectedItem == "Extrema for a < x < b") {
			input = extrema(input);
		}
		else if (selectedItem == "Zeroes") {
			input = zeroes(input);
		}
		else if (selectedItem == "Asymptotes") {
			input = asymptotes(input);
		}
		else if (selectedItem == "Evaluate at x=n") {
			input = evaluateX(input);
		}
		else if (selectedItem == "Evaluate at y=n for a < n < b") {
			input = evaluateY(input);
		}
		else if (selectedItem == "Download Graph") {
			URI url = null;
			try {
				input = input.replace("/", "%2F");
				url = new URI("https://graphsketch.com/render.php?eqn1_color=1&eqn1_eqn=" + input + "&eqn2_color=2&eqn2_eqn=&eqn3_color=3&eqn3_eqn=&eqn4_color=4&eqn4_eqn=&eqn5_color=5&eqn5_eqn=&eqn6_color=6&eqn6_eqn=&x_min=-10&x_max=10&y_min=-10.5&y_max=10.5&x_tick=1&y_tick=1&x_label_freq=5&y_label_freq=5&do_grid=0&do_grid=1&bold_labeled_lines=0&bold_labeled_lines=1&line_width=4&image_w=850&image_h=525&download");
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(url);
				SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd'_'HHmmss");
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = new Date(System.currentTimeMillis());
				String file = "graph_" + formatter.format(date) + ".png";
				input = "Graph Succesfully downloaded. Name is " + file;
			} 
			catch (Exception e) {System.out.println(e);input = "Graph failed to download";}
		}
		long endTime = System.currentTimeMillis();
		addendum += "\n\n\nResults calculated in " + (endTime-startTime)/(double) 1000 + " seconds";
		return input + addendum;
	}
	public static String evaluate(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		try {
			input = input.replace("=","==");
			input = input.replace("====", "==");
			return String.valueOf(shell.evaluate(input));
		}
		catch(Exception e){
			return "Please enter an expression to evaluate";
		}
	}
	public static String sum(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		double sum = 0;
		try {
			String[] splitted = input.split(":");
			double a = 0;
			double b = 0;
			a = computeInput(a, splitted[1]);
			b = computeInput(b, splitted[2]);
			if (a > b) {
				double saver = a;
				a = b;
				b = saver;
			}
			if (b==Double.POSITIVE_INFINITY&&a==Double.NEGATIVE_INFINITY) {
				String aSum = sum(splitted[0] + ":0:" + b);
				String bSum = sum(splitted[0] + ":" + a + ":-1");
				try {
					return String.valueOf(Double.parseDouble(aSum)+Double.parseDouble(bSum));
				}
				catch(Exception e) {
					System.out.println("We could not calculate this sum");
				}
			}
			if (b == Double.POSITIVE_INFINITY) {
				return sumFinity(a, splitted[0]);
			}
			for (double i = a; i <= b; i++) {
				binding.setVariable("x", i);
				sum += (double) shell.evaluate(splitted[0]);
				System.out.println(sum);
			}
		}
		catch(Exception e) {
			return "Please enter a sum of the form: \"Function:IntInitial:IntFinal\"";
		}
		return cleanUp(String.valueOf(sum));
	}
	private static String sumFinity(double a, String func) {
		if (!limit(func + ":" + "Infinity").equals("0.0")) {
			System.out.println(limit(func + ":" + "Infinity"));
			return "Diverges.";
		}
		ArrayList<String> sumList = new ArrayList<>();
		ArrayList<Boolean> growthList = new ArrayList<>();
		for (double i = 100; i < 100000; i*=10) {
			sumList.add(sum(func + ":" + a + ":" + String.valueOf(i)));
			System.out.println(sumList);
			if (sumList.size()>1) {
				if (Double.parseDouble(sumList.get(sumList.size()-1))-Double.parseDouble(sumList.get(sumList.size()-2))<0.0001) {
					return cleanUp(sumList.get(sumList.size()-1));
				}
				if (Double.parseDouble(sumList.get(sumList.size()-1))==Double.POSITIVE_INFINITY) {
					return "Diverges to Infinity";
				}
				if (Double.parseDouble(sumList.get(sumList.size()-1))==Double.NEGATIVE_INFINITY) {
					return "Diverges to -Infinity";
				}
				if (Double.parseDouble(sumList.get(sumList.size()-1))>Double.parseDouble(sumList.get(sumList.size()-2))) {
					growthList.add(false);
				}
				else if (Double.parseDouble(sumList.get(sumList.size()-1))<Double.parseDouble(sumList.get(sumList.size()-2))) {
					growthList.add(true);
				}
			}
		}
		boolean monotonic = true;
		for (int i = 0; i < growthList.size()-1; i++) {
			if (growthList.get(i)!=growthList.get(i+1)) {
				monotonic = false;
			}
		}
		if (monotonic) {
			return "Diverges";
		}
		else {
			return "Diverges or Oscillates";
		}
	}
	public static String derivative(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		String[] splitted = input.split(":");
		try {
			double a = 0;
			a = computeInput(a, splitted[1]);
			if (a==Double.POSITIVE_INFINITY) {
				return improperDeriv(true, splitted[0]);
			}
			else if (a==Double.NEGATIVE_INFINITY) {
				return improperDeriv(false, splitted[0]);
			}
			binding.setVariable("x", a);
			double fAtA = (double) shell.evaluate(splitted[0]);
			if(fAtA==Double.POSITIVE_INFINITY) {
				return "Infinity";
			}
			if (fAtA==Double.NEGATIVE_INFINITY) {
				return "-Infinity";
			}
			if (Double.isNaN(fAtA)) {
				try {
					fAtA=Double.parseDouble(limit(splitted[0] + ":" + a));
				}
				catch(Exception e){}
			}
			String toLimit ="(" + splitted[0] + "-" + String.valueOf(fAtA) + ")/(x-" + a + "):" + a;
			toLimit = toLimit.replace("--", "+");
			input = limit(toLimit);
			
		}
		catch(Exception e){
			System.out.println(e +"Caught in derivative");
			return "Please input expression of form \"Function:Double\"";
		}
		if (input.charAt(0)=='P') {
			input = "This derivative doesn't exist";
		}
		if (input.charAt(0)=='L') {
			input = '\n' + input;
		}
		return input;
	}
	public static String improperDeriv(boolean pos, String input) {
		System.out.println("Improper!");
		if (pos) {
			double x = 1000;
			double oscillator = 0;
			double j;
			double k;
			while (true) {
				oscillator++;
				x*=10;
				try {
					j= Double.parseDouble(derivative(input + ":" + x));
					k= Double.parseDouble(derivative(input + ":" + x+1));
				}
				catch(Exception e) {
					j = 0;
					k = 1;
				}
				if (Math.abs(j-k)<0.0000001) {
					return String.valueOf(j);
				}
				if (k>1E20) {
					return "Infinity";
				}
				if (k<-1E20) {
					return "-Infinity";
				}
				if (oscillator>10) {
					return "Derivative does not Exist";
				}
			}
		}
		else {
			double oscillator = 0;
			double x = -1000;
			double j;
			double k;
			while (true) {
				x*=10;
				oscillator++;
				try {
					j= Double.parseDouble(derivative(input + ":" + x));
					k= Double.parseDouble(derivative(input + ":" + x+1));
				}
				catch(Exception e) {
					j = 0;
					k = 1;
				}
				if (Math.abs(j-k)<0.0000001) {
					return String.valueOf(j);
				}
				if (k>1E20) {
					return "Infinity";
				}
				if (k<-1E20) {
					return "-Infinity";
				}
				if (oscillator>100) {
					return "Derivative does not Exist";
				}
			}
		}
	}
	public static String limit(String input1) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		String[] splitted = input1.split(":");
		String input2 = input1;
		boolean infinity = false;
		boolean lhospitals = false;
		String plusOrMinus = "";
		try {
			double n = 0;
			if (splitted[1].charAt(splitted[1].length()-1)=='-') {
				splitted[1] = splitted[1].substring(0, splitted[1].length()-1);
				plusOrMinus = "-";
			}
			else if (splitted[1].charAt(splitted[1].length()-1)=='+') {
				splitted[1] = splitted[1].substring(0, splitted[1].length()-1);
				plusOrMinus = "+";
			}
			n = computeInput(n, splitted[1]);
			if (n==Double.POSITIVE_INFINITY||n==Double.NEGATIVE_INFINITY) {
				infinity = true;
			}
			if (!infinity) {
				for (double i = n-0.1; i <=n; i = n - ((n-i)/11)) {
					System.out.println(i + "))");
					binding.setVariable("x", i);
					System.out.println(shell.evaluate(splitted[0]));
					if ((double) shell.evaluate(splitted[0])==Double.POSITIVE_INFINITY) {
						input1 = String.valueOf(Double.POSITIVE_INFINITY);
						break;
					}
					else if((double) shell.evaluate(splitted[0])==Double.NEGATIVE_INFINITY) {
						input1 = String.valueOf(Double.NEGATIVE_INFINITY);
						break;
					}
					else if (Double.isNaN((double) shell.evaluate(splitted[0]))) {
						binding.setVariable("x", (n+((n-i)*10)));
						System.out.println(n+((n-i)*10) + ";");
						input1 = String.valueOf(shell.evaluate(splitted[0]));
						break;
					}
					else if (n-i<.000001) {
						System.out.println("HI");
						input1 = String.valueOf(shell.evaluate(splitted[0]));
						break;
					}
				}
			}
			if (!infinity) {
				for (double i = n+0.1; i >=n; i = n + ((i-n)/11)) {
					binding.setVariable("x", i);
					System.out.println(shell.evaluate(splitted[0]));
					if ((double) shell.evaluate(splitted[0])==Double.POSITIVE_INFINITY) {
						input2 = String.valueOf(Double.POSITIVE_INFINITY);
						break;
					}
					else if((double) shell.evaluate(splitted[0])==Double.NEGATIVE_INFINITY) {
						input2 = String.valueOf(Double.NEGATIVE_INFINITY);
						break;
					}
					else if (Double.isNaN((double) shell.evaluate(splitted[0]))) {
						binding.setVariable("x", (n-((i-n)*10)));
						input2 = String.valueOf(shell.evaluate(splitted[0]));
						break;
						
					}
					else if (i-n<.000001) {
						input2 = String.valueOf(shell.evaluate(splitted[0]));
						break;
					}
				}
			}
			for (int i = 0; i < splitted[0].length(); i++) {
				if (splitted[0].charAt(i)=='/') {
					boolean left = isFunc(splitted[0].substring(0,i));
					boolean right = isFunc(splitted[0].substring(i+1, splitted[0].length()));
					System.out.println(left + " " + right);
						if (left&&right) {
						binding.setVariable("x", n);
						double leftX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(0,i))));
						double rightX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(i+1,splitted[0].length()))));
						leftX = cleanUpDouble(leftX);
						rightX = cleanUpDouble(rightX);
						System.out.println(leftX + " " + rightX);
						if (leftX==0&&rightX==0||leftX==Double.POSITIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.POSITIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY) {
							lhospitals = true;
						}
					}
				}
				if (splitted[0].charAt(i)=='*') {
					boolean left = isFunc(splitted[0].substring(0,i));
					boolean right = isFunc(splitted[0].substring(i+1, splitted[0].length()));
					System.out.println(left + " " + right);
					if (left&&right) {
						binding.setVariable("x", n);
						double leftX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(0,i))));
						double rightX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(i+1,splitted[0].length()))));
						leftX = cleanUpDouble(leftX);
						rightX = cleanUpDouble(rightX);
						System.out.println(leftX + " " + rightX);
						if (leftX==0&&rightX==Double.POSITIVE_INFINITY||leftX==0&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.POSITIVE_INFINITY&&rightX==0||leftX==Double.NEGATIVE_INFINITY&&rightX==0||leftX==Double.POSITIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY) {
							lhospitals = true;
						}
					}
				}
				if (splitted[0].charAt(i)=='+') {
					boolean left = isFunc(splitted[0].substring(0,i));
					boolean right = isFunc(splitted[0].substring(i+1, splitted[0].length()));
					System.out.println(left + " " + right);
					if (left&&right) {
						binding.setVariable("x", n);
						double leftX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(0,i))));
						double rightX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(i+1,splitted[0].length()))));
						leftX = cleanUpDouble(leftX);
						rightX = cleanUpDouble(rightX);
						System.out.println(leftX + " " + rightX);
						if (leftX==Double.POSITIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.POSITIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY) {
							lhospitals = true;
						}
					}
				}
				if (splitted[0].charAt(i)=='-') {
					System.out.println("-");
					boolean left = isFunc(splitted[0].substring(0,i));
					boolean right = isFunc(splitted[0].substring(i+1, splitted[0].length()));
					System.out.println(left + " " + right);
					if (left&&right) {
						binding.setVariable("x", n);
						double leftX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(0,i))));
						double rightX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(i+1,splitted[0].length()))));
						leftX = cleanUpDouble(leftX);
						rightX = cleanUpDouble(rightX);
						System.out.println(splitted[0].substring(0,i));
						System.out.println(leftX + " " + rightX);
						if (leftX==Double.POSITIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.POSITIVE_INFINITY&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.NEGATIVE_INFINITY&&rightX==Double.POSITIVE_INFINITY) {
							lhospitals = true;
						}
					}
				}
				if (splitted[0].charAt(i)=='p') {
					int comma = 0;
					int parens = 0;
					int endParen = 0;
					for (int ii = i+3; ii<splitted[0].length(); ii++) {
						System.out.println(ii + ";");
						if (parens==1&&splitted[0].charAt(ii)==',') {
							comma = ii;
						}
						if (splitted[0].charAt(ii)=='(') {
							System.out.println("Paren++");
							parens++;
						}
						if (splitted[0].charAt(ii)==')') {
							parens--;
						}
						if (parens==0) {
							endParen = ii;
							break;
						}
					}
					boolean left = isFunc(splitted[0].substring(i+4,comma));
					boolean right = isFunc(splitted[0].substring(comma+1, endParen));
					System.out.println(left + " " + right);
					if (left&&right) {
						binding.setVariable("x", n);
						double leftX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(i+4,comma))));
						double rightX = Double.parseDouble(String.valueOf(shell.evaluate(splitted[0].substring(comma+1, endParen))));
						System.out.println(leftX + " " + rightX);
						if (leftX==0&&rightX==Double.POSITIVE_INFINITY||leftX==0&&rightX==Double.NEGATIVE_INFINITY||leftX==Double.POSITIVE_INFINITY&&rightX==0||leftX==Double.NEGATIVE_INFINITY&&rightX==0||leftX==1.0&&rightX==Double.POSITIVE_INFINITY||leftX==1.0&&rightX==Double.NEGATIVE_INFINITY) {
							lhospitals = true;
						}
					}
				}
			}
			if (infinity) {
				input1 = infiLimit(n, splitted[0], binding, shell);
				input2 = input1;
			}
		}
		catch(Exception e){
			System.out.println(e);
			return "Please input expression of form lim (x->n) f(x) as \"Function:Double n\"";
		}
		try {
			input1 = cleanUp(input1);
			input2 = cleanUp(input2);
			if (input1.equals("NaN")&&!input2.equals("NaN")) {
				input1 = input2;
			}
			else if (!input1.equals("NaN")&&input2.equals("NaN")) {
				input2 = input1;
			}
			double m = Double.parseDouble(input1);
			double n = Double.parseDouble(input2);
			if (Math.abs(m-n)<.001||(m==Double.POSITIVE_INFINITY&&n==Double.POSITIVE_INFINITY)||(m==Double.NEGATIVE_INFINITY&&n==Double.NEGATIVE_INFINITY)) {
				if (lhospitals) {
					if (input1.equals("Infinity")||input1.equals("-Infinity")) {
						Main.updatePhoto("Infinitus");
					}
					else if (input1.equals("0")||input1.equals("0.0")) {
						Main.updatePhoto("Captain Zero");
					}
					else {
						Main.updatePhoto("Constants");
					}
				}
				return input1;
			}
			else {
				if (plusOrMinus.equals("+")) {
					if (lhospitals) {
						if (input2.equals("Infinity")||input2.equals("-Infinity")) {
							Main.updatePhoto("Infinitus");
						}
						else if (input2.equals("0")||input2.equals("0.0")) {
							Main.updatePhoto("Captain Zero");
						}
						else {
							Main.updatePhoto("Constants");
						}
					}
					return input2;
				}
				else if (plusOrMinus.equals("-")) {
					if (lhospitals) {
						if (input1.equals("Infinity")||input1.equals("-Infinity")) {
							Main.updatePhoto("Infinitus");
						}
						else if (input1.equals("0")||input1.equals("0.0")) {
							Main.updatePhoto("Captain Zero");
						}
						else {
							Main.updatePhoto("Constants");
						}
					}
					return input1;
				}
				if (lhospitals) {
					Main.updatePhoto("DNE");
				}
				return "Limit from left is: " + input1 + "\nLimit from right is: " + input2 + "\nLimit does not exist";
			}
		}
		catch(Exception e) {
			return input1;
		}
	}
	private static String infiLimit(double n, String func, Binding binding, GroovyShell shell) {
		if (n==Double.POSITIVE_INFINITY) {
			double i = 100;
			double center = 0;
			double left = 0;
			while (i < Double.MAX_VALUE) {
				binding.setVariable("x", i);
				center = (double) shell.evaluate(func);
				if (center>1E15) {
					return "Infinity";
				}
				if (center<-1E15) {
					return "-Infinity";
				}
				binding.setVariable("x", i+1);
				left = (double) shell.evaluate(func);
				System.out.println(left + " " + center + " " + i);
				if (Math.abs(center-left)<.00000000001) {
					return String.valueOf(center);
				}
				i*=10;
			}
			if (func.contains("sin")||func.contains("cos")||func.contains("tan")) {
				return "Probably oscillates, small chance it diverges";
			}
			if (center-left<0) {
				return "-Infinity";
			}
			else if (center-left>=0) {
				return "Infinity";
			}
		}
		if (n==Double.NEGATIVE_INFINITY) {
			double i = -100;
			double center = 0;
			double left = 0;
			while (i < Double.MIN_VALUE) {
				binding.setVariable("x", i);
				center = (double) shell.evaluate(func);
				binding.setVariable("x", i-1);
				left = (double) shell.evaluate(func);
				System.out.println(left + " " + center);
				if (Math.abs(center-left)<.00000000001) {
					return String.valueOf(center);
				}
				i*=10;
			}
			if (func.contains("sin")||func.contains("cos")||func.contains("tan")) {
				return "Probably oscillates, small chance it diverges";
			}
			if (center-left<0) {
				return "-Infinity";
			}
			else if (center-left>=0) {
				return "Infinity";
			}
		}
		return "We could not calculate this limit";
	}
	public static String extrema(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		String[] splitted = input.split(":");
		double[] max = new double[2];
		double[] min = new double[2];
		ArrayList<String> relMinima = new ArrayList<>();
		ArrayList<String> relMaxima = new ArrayList<>();
		try {
			double a = 0;
			double b = 0;
			a = computeInput(a, splitted[1]);
			b = computeInput(b, splitted[2]);
			if (a > b) {
				double transfer = a;
				a = b;
				b = transfer;
			}
			binding.setVariable("x",a);
			double initialAbsolute = (double) shell.evaluate(splitted[0]);
			max[0] = a;
			max[1] = initialAbsolute;
			min[0] = a;
			min[0] = initialAbsolute;
			double step = 0.01;
			System.out.println(a + ";");
			System.out.println(b + ";");
			for (double i = a; i <= b ; i+=step) {
				binding.setVariable("x", i);
				double center = (double) shell.evaluate(splitted[0]);
				if (max[1]<center) {
					max[1] = cleanUpDouble(center);
					max[0] = cleanUpDouble(i);
				}
				if (min[1]>center) {
					min[1] = cleanUpDouble(center);
					min[0] = cleanUpDouble(i);
				}
				binding.setVariable("x", i-.001);
				binding.setVariable("x", i+.001);
				String derivL = derivative(splitted[0]+":"+String.valueOf(i-step));
				String derivR = derivative(splitted[0]+":"+String.valueOf(i+step));
				try {
					if (Double.parseDouble(derivL)>0&&Double.parseDouble(derivR)<0) {
						relMaxima.add(i + ", " + center);
					}
					else if (Double.parseDouble(derivL)<0&&Double.parseDouble(derivR)>0) {
						relMinima.add(i + ", " + center);
					}
				}
				catch (Exception e) {
					System.out.println(e);
				}
				System.out.println(center);
			}
		}
		catch(Exception e){
			return "Please input expression of form \"Function:DoubleLower:DoubleUpper\"";
		}
		return "Max: " + Arrays.toString(max) + "\nMin: " + Arrays.toString(min) + "\nRelMax: " + relMaxima + "\nRelMin: " + relMinima;
	}
	public static String zeroes(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		ArrayList<String> zeroes = new ArrayList<>();
		String[] splitted = input.split(":");
		try {
			double a = 0;
			double b = 0;
			a = computeInput(a, splitted[1]);
			b = computeInput(b, splitted[2]);
			binding.setVariable("x", a);
			if ((double) shell.evaluate(splitted[0])==0){
				zeroes.add(String.valueOf(cleanUpDouble(a)));
			}
			for (double i = a; i <=b; i+=.15) {
				binding.setVariable("x", i);
				double center = (double) shell.evaluate(splitted[0]);
				binding.setVariable("x", i+.15);
				double right = (double) shell.evaluate(splitted[0]);
				System.out.println(i);
				System.out.println(center + " " + right);
				if (((center>=0&&right<=0)||(center<=0&&right>=0))&&((center<100)&&(center>=0)||(center>-100)&&(center<0))) {
					double w = zeroesHelper(splitted[0],i, binding, shell);
					if (w!=0.02929) {
						zeroes.add(cleanUp(String.valueOf(w)));
					}
				}
			}
			binding.setVariable("x", b);
			if ((double) shell.evaluate(splitted[0])==0) {
				zeroes.add(String.valueOf(cleanUpDouble(b)));
			}
		}
		catch(Exception e){
			return "Please input expression in the form \"Function:DoubleLower:DoubleUpper\"";
		}
		return "Zeroes: " + zeroes;
	}
	private static double zeroesHelper(String function, double x, Binding binding, GroovyShell shell) {
		for (double i = x; i <=x+.15; i+=.001) {
			binding.setVariable("x", i);
			double center = (double) shell.evaluate(function);
			binding.setVariable("x", i+.001);
			double right = (double) shell.evaluate(function);
			if (((center>=0&&right<=0)||(center<=0&&right>=0))&&((center<100&&center>=0)||(center>-100&&center<0))) {
				System.out.println("         " + i + " " + center + " " + right);
				return i;
			}
		}
		return 0.02929;
	}
	public static String asymptotes(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		ArrayList<String> asymptotes = new ArrayList<>();
		String[] splitted = input.split(":");
		try {
			double a = 0;
			double b = 0;
			a = computeInput(a, splitted[1]);
			b = computeInput(b, splitted[2]);
			for (double i = a; i <=b; i+=.5) {
				binding.setVariable("x", i);
				double center = (double) shell.evaluate(splitted[0]);
				binding.setVariable("x", i+.5);
				double right = (double) shell.evaluate(splitted[0]);
				System.out.println(center + " " + right);
				if (center == Double.POSITIVE_INFINITY||right == Double.NEGATIVE_INFINITY){
					asymptotes.add(String.valueOf(cleanUpDouble(center)));
				}
				if ((center>=0&&right<=0)||(center<=0&&right>=0)) {
					System.out.println("HI");
					double j = asymptotesHelper(splitted[0],i, binding, shell);
					if (j!=0.02929) {
						asymptotes.add(cleanUp(String.valueOf(j)));
					}
				}
			}
			binding.setVariable("x", b);
			if ((double) shell.evaluate(splitted[0])==0) {
				asymptotes.add(String.valueOf(cleanUpDouble(b)));
			}
		}
		catch(Exception e){
			return "Please input expression in the form \"Function:DoubleLower:DoubleUpper\"";
		}
		return "Asymptotes: " + asymptotes;
	}
	private static double asymptotesHelper(String function, double x, Binding binding, GroovyShell shell) {
			for (double i = x; i <=x+.5; i+=.001) {
				binding.setVariable("x", i);
				double center = (double) shell.evaluate(function);
				binding.setVariable("x", i+.001);
				double right = (double) shell.evaluate(function);
				if (((center>=0&&right<=0)||(center<=0&&right>=0))&&(center>100||center<-100)) {
					return i;
				}
			}
			return 0.02929;
		}
	public static String defInt(String input) {  
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		String[] splitted = input.split(":");
		String coeff = "";
		double approx = 0;
		try {
			double a = 0;
			double b = 0;
			a = computeInput(a, splitted[1]);
			b = computeInput(b, splitted[2]);
			if (a > b) {
				double transfer = a;
				a = b;
				b = transfer;
				coeff = "-";
			}
			if (a == b*-1) {
				double m;
				double n;
				boolean odd = true;
				for (double i = a; i<=b+2; i+=(b-a)/10) {
					binding.setVariable("x", i);
					m = (double) shell.evaluate(splitted[0]);
					binding.setVariable("x", i*-1);
					n = (double) shell.evaluate(splitted[0]);
					n = n*-1;
					if (n!=m) {
						odd = false;
						break;
					}
				}
				if (odd) {
					return "Odd functions from -n to n are 0";
				}
			}
			for (double i = a + 0.000001; i < b; i+=((b-a)/10000)) {
				binding.setVariable("x", i);
				Double height = (Double) shell.evaluate(splitted[0]);
				System.out.println(height + " " + approx);
				approx += (height*(b-a)/10);
			}
			if (String.valueOf(approx/1000).length()>5) {
				input = String.valueOf(approx/1000).substring(0,5);
			}
			else {
				input = String.valueOf(approx/1000);
			}
		}
		catch(Exception e){
			return "Please input expression of form \"Function:DoubleLower:DoubleUpper\"";
		}
		return "Estimation using Left Hand Sum with n = 10000: " + coeff + input;
	}
	public static String indefInt(String input) {
		improperComputionCheck++;
		input = input.replace(" ","");
		boolean startInt = false;
		int numLong = 0;
		ArrayList<String> constants = new ArrayList<>();
		ArrayList<Integer> placeConstants = new ArrayList<>();
		try {
			if (input.charAt(0)=='('&&input.charAt(input.length()-1)==')') {
				return indefInt(input.substring(1,input.length()-1));
			}
			input = input.replace("/pow(x,", "*pow(x,-");
			input = input.replace("--", "+");
			input = input.replace(",+", ",");
			input = input.replace("pow(x,1)","x");
			for (int i = 0; i < input.length(); i++) {
				if (Character.isDigit(input.charAt(i))||input.charAt(i)=='.') {
					if (!startInt) {
						constants.add(String.valueOf(input.charAt(i)));
						placeConstants.add(i-numLong);
						startInt = true;
					}
					else {
						constants.set(constants.size()-1, String.valueOf(constants.get(constants.size()-1)) + String.valueOf(input.charAt(i)));
						numLong++;
					}
				}
				else {
					if (startInt) {
						startInt = false;
					}
				}
			}
			String funcWithConstants = input;
			for (int i = 0; i < constants.size(); i++) {
				System.out.println(String.valueOf(placeConstants));
				int length = constants.get(i).length();
				int place = placeConstants.get(i);
				System.out.println(input.substring(0,place) + "k" + input.substring(place+length,input.length()));
				input = input.substring(0,place) + "k" + input.substring(place+length,input.length());
			}
			for (int i = 0; i < placeConstants.size(); i++) {
				if (placeConstants.get(i)>0) {
					if (input.charAt(placeConstants.get(i)-1)=='-') {
						input = input.substring(0,placeConstants.get(i)-1) + '+' + input.substring(placeConstants.get(i),input.length());
						System.out.println(input);
						constants.set(i, String.valueOf(Double.parseDouble(constants.get(i))*-1));
						if (input.charAt(0)=='+') {
							input = input.substring(1);
						}
					}
				}
			}
			input = input.replace(",+",",");
			input = input.replace("(+","(");
			input = input.replace("x^k", "pow(x,k)");
			input = input.replace("k^x", "pow(k,x)");
			System.out.println(input);
			if (input.equals("k")) {
				return constants.get(0)+"*x";
			}
			for (int i = 0; i < funcWithConstants.length(); i++) {
				if (funcWithConstants.charAt(i)=='+') {
					boolean left = isFunc(funcWithConstants.substring(0,i));
					boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
					if (left&&right) {
						return indefInt(funcWithConstants.substring(0,i)) + "+" + indefInt(funcWithConstants.substring(i+1,funcWithConstants.length()));
					}
				}
				else if (funcWithConstants.charAt(i)=='-') {
					boolean left = isFunc(funcWithConstants.substring(0,i));
					boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
					if (left&&right) {
						return indefInt(funcWithConstants.substring(0,i)) + "-" + indefInt(funcWithConstants.substring(i+1,funcWithConstants.length()));
					}
				}
			}
			if (input.charAt(0)=='k'&&input.charAt(1)=='*'&&isFunc(input.substring(2))) {
				return constants.get(0) + "*(" + indefInt(funcWithConstants.substring(constants.get(0).length()+1)) + ")";
			}
			if (input.charAt(0)=='k'&&input.charAt(1)=='/'&&isFunc(input.substring(2))&&!constants.get(0).equals("1")) {
				return constants.get(0) + "*(" + indefInt("1/" + funcWithConstants.substring(constants.get(0).length()+1)) + ")";
			}
			if (input.equals("x")) {
				input = "0.5*pow(x,2)";
			}
			if (input.equals("k/x")||(input.equals("pow(x,k)")&&constants.get(0).equals("-1"))) {
				input = "loge(abs(x))";
			}
			if (input.equals("pow(x,k)")) {
				String power = String.valueOf(Double.parseDouble(constants.get(0))+1);
				input = "(" + 1/Double.parseDouble(power) + ")*pow(x," + power + ")";
			}
			if (input.equals("pow(x+k,k)")) {
				String power = String.valueOf(Double.parseDouble(constants.get(1))+1);
				input = "pow(x+" + constants.get(0) + "," + power + ")/" + power;
			}
			if (input.equals("k/(kx+k)")){
				double coeff = Double.parseDouble(constants.get(0))/Double.parseDouble(constants.get(1));
				input = coeff + "*loge(abs(" + constants.get(1) + "x+" + constants.get(2) + "))";
			}
			if (input.equals("k/pow(x+k,k)")) {
				if (constants.get(2).equals(String.valueOf(2))) {
					input = "-" + constants.get(0)+ "/(x+" + constants.get(1) + ")";
				}
			}
			if (input.equals("x*pow(x+k,k)")) {
				double plusOne = Double.parseDouble(constants.get(1))+1;
				double plusTwo = plusOne + 1;
				input = "pow(x+" + constants.get(0) + "," + plusOne + ")*(x*" + plusOne + "-" + constants.get(0) + ")/" + String.valueOf(plusOne*plusTwo);
			}
			if (input.equals("k/(k+pow(x,k))")) {
				if (constants.get(0).equals("1")&&constants.get(2).equals("2")) {
					double coeff = 1/Math.sqrt(Double.parseDouble(constants.get(1)));
					input = String.valueOf(coeff) + "*arctan(" + String.valueOf(coeff) + "*x)";
				}
			}
			if (input.equals("x/(k+pow(x,k))")) {
				if (constants.get(1).equals("2")) {
					input = "0.5*loge(abs(" + constants.get(0) + "+pow(x,2)))";
				}
			}
			if (input.equals("pow(x,k)/(k+pow(x,k))")) {
				if (constants.get(0).equals("2")&&constants.get(2).equals("2")) {
					double coeff = 1/Math.sqrt(Double.parseDouble(constants.get(1)));
					input = "x-" + String.valueOf(Math.sqrt(Double.parseDouble(constants.get(1)))) + "*arctan(" + coeff + "*x)";
				}
				else if (constants.get(0).equals("3")&&constants.get(2).equals("2")) {
					input = "0.5*pow(x,2)-0.5*" + constants.get(1) + "*loge(abs(" + constants.get(1) + "+pow(x,2)))";
				}
			}
			if (input.equals("k/(k*pow(x,k)+k*x+k)")) {
				if (constants.get(2).equals("2")) {
					double coeff = 2/Math.sqrt((4*Double.parseDouble(constants.get(1))*Double.parseDouble(constants.get(4)))-Math.pow(Double.parseDouble(constants.get(3)),2));
					if (!Double.isNaN(coeff)&&coeff!=Double.POSITIVE_INFINITY) {
						double halfB = Double.parseDouble(constants.get(3))/2;
						input = String.valueOf(coeff) + "*arctan(" + String.valueOf(coeff) + "*(" + String.valueOf(constants.get(1)) + "*x+" + String.valueOf(halfB) + "))";
					}
					if (constants.get(1).equals("1")&&Math.pow(Double.parseDouble(constants.get(3))/2,2)==Double.parseDouble(constants.get(4))) {
						input = "-1/(x+" + String.valueOf(Double.parseDouble(constants.get(3))/2) + ")";
					}
				}
			}
			if (input.equals("k/((x+k)(x+k))")) {
				double coeff = 1/(Double.parseDouble(constants.get(2))-Double.parseDouble(constants.get(1)));
				input = coeff + "*loge((" + constants.get(1) + "+x)/(" + constants.get(2) + "+x))";
			}
			if (input.equals("x/pow(x+k,k)")) {
				if (constants.get(1).equals("2")) {
					input = constants.get(0) + "/(x+" + constants.get(0) + ")" + "+loge(abs(" + constants.get(0) + "+x))";
				}
			}
			if (input.equals("x/(k*pow(x,k)+k*x+k)")) {
				if (constants.get(1).equals("2")) {
					double a = Double.parseDouble(constants.get(0));
					double b = Double.parseDouble(constants.get(2));
					double c = Double.parseDouble(constants.get(3));
					double coeff = 1/(2*a);
					double coeff2 = b/(a*Math.sqrt(4*a*c-Math.pow(b,2)));
					double coeff3 = 1/Math.sqrt(4*a*c-Math.pow(b,2));
					input = coeff + "*loge(abs(" + a + "*pow(x,2)+" + b + "*x+" + c + "))-" + coeff2 + "*arctan(" + coeff3 + "*(" + 2*a + "*x+" + b + "))";
				}
			}
			if (input.equals("sqrt(x+k)")) {
				input = "(0.66666666)*pow(x+" + constants.get(0) + ",1.5)";
			}
			if (input.equals("k/sqrt(x+k)")||input.equals("k/sqrt(k+x)")) {
				if (constants.get(0).equals("1")) {
					input = "2*sqrt(x+" + constants.get(1) + ")";
				}
			}
			if (input.contains("k")) {
				for (int i = 0; i < funcWithConstants.length(); i++) {
					if (funcWithConstants.charAt(i)=='*') {
						boolean left = isFunc(funcWithConstants.substring(0,i));
						boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
						if (left&&right&&!funcWithConstants.substring(0,i).equals(funcWithConstants.substring(i+1,funcWithConstants.length()))&&improperComputionCheck<4) {
							System.out.println(funcWithConstants.substring(0,i) + "*" + funcWithConstants.substring(i+1,funcWithConstants.length()));
							String reverse = indefInt(funcWithConstants.substring(i+1,funcWithConstants.length()) + "*" + funcWithConstants.substring(0,i));
							improperComputionCheck = 0;
							if (!(reverse.contains("k")||reverse.contains("We could not"))) {
								return reverse;
							}
						}
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
			return "We could not calculate this integral";
		}
		input = input.replace("+-", "-");
		input = input.replace("--", "+");
		
		return input;
	}
	public static String funcDeriv(String input) {
		if (input.length()==0) {
			return "";
		}
		improperComputionCheck++;
		input = input.replace(" ","");
		boolean startInt = false;
		int numLong = 0;
		ArrayList<String> constants = new ArrayList<>();
		ArrayList<Integer> placeConstants = new ArrayList<>();
		try {
			if (input.charAt(0)=='('&&input.charAt(input.length()-1)==')') {
				return funcDeriv(input.substring(1,input.length()-1));
			}
			input = input.replace("/pow(x,", "*pow(x,-");
			input = input.replace("--", "+");
			input = input.replace(",+", ",");
			input = input.replace("pow(x,1)","x");
			for (int i = 0; i < input.length(); i++) {
				if (Character.isDigit(input.charAt(i))||input.charAt(i)=='.') {
					if (!startInt) {
						constants.add(String.valueOf(input.charAt(i)));
						placeConstants.add(i-numLong);
						startInt = true;
					}
					else {
						constants.set(constants.size()-1, String.valueOf(constants.get(constants.size()-1)) + String.valueOf(input.charAt(i)));
						numLong++;
					}
				}
				else {
					if (startInt) {
						startInt = false;
					}
				}
			}
			String funcWithConstants = input;
			for (int i = 0; i < constants.size(); i++) {
				System.out.println(String.valueOf(placeConstants));
				int length = constants.get(i).length();
				int place = placeConstants.get(i);
				System.out.println(input.substring(0,place) + "k" + input.substring(place+length,input.length()));
				input = input.substring(0,place) + "k" + input.substring(place+length,input.length());
			}
			for (int i = 0; i < placeConstants.size(); i++) {
				if (placeConstants.get(i)>0) {
					if (input.charAt(placeConstants.get(i)-1)=='-') {
						input = input.substring(0,placeConstants.get(i)-1) + '+' + input.substring(placeConstants.get(i),input.length());
						System.out.println(input);
						constants.set(i, String.valueOf(Double.parseDouble(constants.get(i))*-1));
						if (input.charAt(0)=='+') {
							input = input.substring(1);
						}
					}
				}
			}
			input = input.replace(",+",",");
			input = input.replace("(+","(");
			input = input.replace("x^k", "pow(x,k)");
			input = input.replace("k^x", "pow(k,x)");
			System.out.println(input);
			if (input.equals("k")) {
				return "0";
			}
			for (int i = 0; i < funcWithConstants.length(); i++) {
				if (funcWithConstants.charAt(i)=='+') {
					boolean left = isFunc(funcWithConstants.substring(0,i));
					boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
					if (left&&right) {
						System.out.println("sum");
						input = funcDeriv(funcWithConstants.substring(0,i)) + "+" + funcDeriv(funcWithConstants.substring(i+1,funcWithConstants.length()));
						return input.replace("+-", "-");
					}
				}
				else if (funcWithConstants.charAt(i)=='-') {
					boolean left = isFunc(funcWithConstants.substring(0,i));
					boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
					if (left&&right) {
						System.out.println("difference");
						input = (funcDeriv(funcWithConstants.substring(0,i)) + "-" + funcDeriv(funcWithConstants.substring(i+1,funcWithConstants.length()))).replace("--", "+");
						if (input.charAt(0)=='+') {
							input = input.substring(1);
						}
						if (input.charAt(input.length()-1)=='0') {
							input = input.substring(0, input.length()-2);
						}
						return input;
					}
				}
			}
			if (input.charAt(0)=='k'&&input.charAt(1)=='*'&&isFunc(input.substring(2))) {
				return constants.get(0) + "*(" + funcDeriv(funcWithConstants.substring(constants.get(0).length()+1)) + ")";
			}
			if (input.charAt(0)=='k'&&input.charAt(1)=='/'&&isFunc(input.substring(2))&&!constants.get(0).equals("1")) {
				return constants.get(0) + "*(" + funcDeriv("1/" + funcWithConstants.substring(constants.get(0).length()+1)) + ")";
			}
			if (input.equals("x")) {
				input = "1";
			}
			if (input.equals("k/x")) {
				input = "-pow(x,-2)";
			}
			if (input.equals("pow(x,k)")) {
				String power = String.valueOf(Double.parseDouble(constants.get(0))-1);
				input = constants.get(0) + "*pow(x," + power + ")";
			}
			if (input.equals("pow(k,x)")) {
				input = "ln(" + constants.get(0) + ")*pow(" + constants.get(0) + ",x)";
			}
			if (input.equals("log(x)")||input.equals("logk(x)")&&constants.get(0).equals("2.71828")){
				input = "1/x";
			}
			if (input.equals("logk(x)")){
				String coeff = String.valueOf(1/Math.log(Double.parseDouble(constants.get(0))));
				input = coeff + "/x";
			}
			if (input.equals("cos(x)")){
				return "-sin(x)";
			}
			if (input.equals("sin(x)")){
				return "cos(x)";
			}
			if (input.equals("tan(x)")) {
				return "1/pow(cos(x),2)";
			}
			if (input.equals("cosh(x)")){
				return "sinh(x)";
			}
			if (input.equals("sinh(x)")){
				return "cosh(x)";
			}
			if (input.equals("tanh(x)")) {
				return "1/pow(cosh(x),2)";
			}
			if (input.equals("floor(x)")) {
				return "0/(floor(x)-x)";
			}
			if (input.equals("ceil(x)")){
				return "0/(ceil(x)-x)";
			}
			if (input.equals("sqrt(x)")) {
				return "0.5*pow(x,-0.5)";
			}
			if (input.equals("cbrt(x)")) {
				return "(1/3)*pow(x,-2/3)";
			}
			if (input.equals("arcsin(x)")){
				return "1/sqrt(1-pow(x,2))";
			}
			if (input.equals("arccos(x)")){
				return "-1/sqrt(1-pow(x,2))";
			}
			if (input.equals("arctan(x)")){
				return "1/(1+pow(x,2))";
			}
			input = input.replace("pow(x,1.0)", "x");
			if (input.contains("k")||funcWithConstants.equals(input)) {
				for (int i = 0; i < funcWithConstants.length(); i++) {
					if (funcWithConstants.charAt(i)=='*') {
						boolean left = isFunc(funcWithConstants.substring(0,i));
						boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
						if (left&&right&&improperComputionCheck<4) {
							System.out.println("product");
							String reverse = "(" + funcWithConstants.substring(0,i) + ")*(" + funcDeriv(funcWithConstants.substring(i+1,funcWithConstants.length())) + ")+(" + funcWithConstants.substring(i+1,funcWithConstants.length()) + ")*(" + funcDeriv(funcWithConstants.substring(0,i)) + ")";
							improperComputionCheck = 0;
							if (!(reverse.contains("k")||reverse.contains("We could not"))) {
								return reverse;
							}
						}
					}
					if (funcWithConstants.charAt(i)=='/') {
						boolean left = isFunc(funcWithConstants.substring(0,i));
						boolean right = isFunc(funcWithConstants.substring(i+1,funcWithConstants.length()));
						System.out.println(left + " " + right);
						if (left&&right&&improperComputionCheck<4) {
							System.out.println("quotient");
							String reverse = "(("
									+ "" + funcWithConstants.substring(i+1,funcWithConstants.length()) + ")*(" + funcDeriv(funcWithConstants.substring(0,i)) + ")-(" + funcWithConstants.substring(0,i) + ")*(" + funcDeriv(funcWithConstants.substring(i+1,funcWithConstants.length())) + "))/pow(" + funcWithConstants.substring(i+1,funcWithConstants.length()) + ",2)";
							improperComputionCheck = 0;
							System.out.println(reverse);
							if (!(reverse.contains("k")||reverse.contains("We could not"))) {
								return reverse;
							}
						}
					}
				}
				int par = 0;
				String chain = "";
				String chain0 = "";
				for (int i = 0; i < funcWithConstants.length(); i++) {
					if (funcWithConstants.charAt(i)==')') {
						par--;
					}
					if (par > 0) {
						if (funcWithConstants.charAt(i)==','&&par==1) {
							chain0 = chain;
							chain = "";
						}
						else {
							chain += funcWithConstants.charAt(i);
						}
					}
					if (funcWithConstants.charAt(i)=='(') {
						par++;
					}
					
				}
				System.out.println(chain + "?");
				System.out.println(chain0 + ";");
				if (chain0!=""&&improperComputionCheck<5) {
					boolean left = chain0.contains("x");
					boolean right = chain.contains("x");
					if (left&&!right) {
						System.out.println("x^k");
						funcWithConstants = funcWithConstants.replace(chain0, "x");
						String outer = funcDeriv(funcWithConstants);
						String inner = funcDeriv(chain0);
						outer = outer.replace("x", "(" + chain0 + ")");
						return "(" + outer + ")*(" + inner + ")";
					}
					if (!left&&right) {
						System.out.println("k^x");
						funcWithConstants = funcWithConstants.replace(chain, "x");
						String outer = funcDeriv(funcWithConstants);
						String inner = funcDeriv(chain);
						outer = outer.replace("x", "(" + chain + ")");
						return "(" + outer + ")*(" + inner + ")";
					}
					if (left&&right) {
						System.out.println("x^x");
						String answer = funcDeriv("pow(2.71828," + chain + "*ln(" + chain0 + "))");
						return answer;
					}
				}
				if (chain!="x"&&chain!=""&&improperComputionCheck<5) {
					System.out.println("chain");
					funcWithConstants = funcWithConstants.replace(chain, "x");
					String outer = funcDeriv(funcWithConstants);
					String inner = funcDeriv(chain);
					outer = outer.replace("x", "(" + chain + ")");
					return "(" + outer + ")*(" + inner + ")";
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
			return "We could not calculate this derivative";
		}
		input = input.replace("+-", "-");
		input = input.replace("--", "+");
		
		return input;
	}
	private static boolean isFunc(String func) {
		int paren = 0;
		for (int i = 0; i < func.length(); i ++) {
			if (func.charAt(i)=='(') {
				paren++;
			}
			else if (func.charAt(i)==')') {
				paren--;
			}
		}
		if (paren==0) {
			return true;
		}
		return false;
	}
	public static String evaluateX(String input) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		String[] splitted = input.split(":");
		try {
			double n = 0;
			n = computeInput(n, splitted[1]);
			binding.setVariable("x", n);
			input = String.valueOf(cleanUpDouble((double) shell.evaluate(splitted[0])));
		}
		catch(Exception e) {
			return "Enter your expression in the form \"Function:DoubleXVal\"";
		}
		return cleanUp(input);
	}
	public static String evaluateY(String input) {
		String[] splitted = input.split(":");
		try {
			double n = 0;
			double a = 0;
			double b = 0;
			n = computeInput(n, splitted[1]);
			a = computeInput(a, splitted[2]);
			b = computeInput(b, splitted[3]);
			input = zeroes(splitted[0]+"-"+String.valueOf(n)+":"+String.valueOf(a)+":"+String.valueOf(b));
		}
		catch(Exception e){
			return "Enter your expression in the form \"Function:DoubleYVal:DoubleLower:DoubleUpper\"";
		}
		return "X-values: " + input.substring(7);
	}
	public static String cleanUp(String input) {
		try {
			double output = Double.parseDouble(input);
			output = cleanUpDouble(output);
			input = String.valueOf(output);
			return input;
		}
		catch (Exception e) {
			return input;
		}
	}
	public static double cleanUpDouble(double input) {
		if ((input<1E-3&&input>0)||(input>-1E-3&&input<0)) {
			input = 0;
		}
		if (input>=1E8) {
			input = Double.POSITIVE_INFINITY;
		}
		if (input<=-1E8) {
			input = Double.NEGATIVE_INFINITY;
		}
		if (Math.abs(((int) input)-input)<0.001) {
			input = (int) input;
		}
		else if (Math.abs(((int) input)-input+1)<0.001) {
			input = (int) input+1;
		}
		return input;
	}
	public static double computeInput(double a, String input) {
		GroovyShell shell = new GroovyShell();
		try {
			a = Double.parseDouble(input);
		}
		catch(java.lang.NumberFormatException e) {
			try {
				BigDecimal po = (BigDecimal) shell.evaluate(input);
				a = po.doubleValue();
			}
			catch(java.lang.ClassCastException e1) {
				try {
					Integer po = (int) shell.evaluate(input);
					a = po.doubleValue();
				}
				catch(java.lang.ClassCastException e2) {
					a = (double) shell.evaluate(input);
				}
			}
		}
		return a;
	}
}