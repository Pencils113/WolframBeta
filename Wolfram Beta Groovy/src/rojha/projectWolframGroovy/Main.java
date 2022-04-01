package rojha.projectWolframGroovy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Main extends JFrame{
	static GraphicsConfiguration gc;
	static boolean enter;
	static boolean start = true;
	Font titleFont = new Font("Helvetica", Font.PLAIN, 65);
	int titleLength = getFontMetrics(titleFont).stringWidth("Wolfram Beta");
	public static JTextField textfield = new JTextField("Enter Function", 10);
	static JTextArea answerField = new JTextArea("Answer Appears Here\nUse Buttons for Common Functions\nFor other functions, use the java.lang.Math syntax \n(not to be used in the indefinite integrals or funcn derivatives)\ne.g. Math.abs(x)\nnote: No Implied Multiplication", 10, 10);
	static String[] methods = {"Evaluate Expression","Evaluate at x=n","Evaluate at y=n for a < n < b","Zeroes","Asymptotes","Extrema for a < x < b","Sum","Limit as x approaches n","Derivative", "Func Derivative", "Definite Integral","Indefinite Integral","Download Graph"};
	public static JComboBox methodField = new JComboBox(methods);
	static JLabel constants = new JLabel(new ImageIcon("/Users/rohanojha/Downloads/Constants.png"));
	static JLabel captainZero = new JLabel(new ImageIcon("/Users/rohanojha/Downloads/CaptainZero.png"));
	static JLabel infinitus = new JLabel(new ImageIcon("/Users/rohanojha/Downloads/CaptainnZero.png"));
	static JLabel dne = new JLabel(new ImageIcon("/Users/rohanojha/Downloads/dne.png"));
	JPanel rectangle = new JPanel();
	JLabel title = new JLabel("Wolfram");
	JLabel title2 = new JLabel("Beta");
	static JButton button1 = new JButton("sqrt");
	static JButton button2 = new JButton("cbrt");
	static JButton button3 = new JButton("logBASEn");
	static JButton button4 = new JButton("(");
	static JButton button5 = new JButton(")");
	static JButton button6 = new JButton("^");
	static JButton button7 = new JButton("Infinity");
	static JButton button8 = new JButton("sin");
	static JButton button9 = new JButton("cos");
	static JButton button10 = new JButton("tan");
	static JButton button11 = new JButton("pi");
	static JButton button12 = new JButton("e");
	static JButton button13 = new JButton("arcsin");
	static JButton button14 = new JButton("arccos");
	static JButton button15 = new JButton("arctan");
	static JButton button16 = new JButton("ceiling");
	static JButton button17 = new JButton("floor");
	static JButton button18 = new JButton("sinh");
	static JButton button19 = new JButton("cosh");
	static JButton button20 = new JButton("tanh");
	static JButton button21 = new JButton("1");
	static JButton button22 = new JButton("2");
	static JButton button23 = new JButton("3");
	static JButton button24 = new JButton("4");
	static JButton button25 = new JButton("+");
	static JButton button26 = new JButton("5");
	static JButton button27 = new JButton("6");
	static JButton button28 = new JButton("7");
	static JButton button29 = new JButton("8");
	static JButton button30 = new JButton("-");
	static JButton button31 = new JButton("9");
	static JButton button32 = new JButton("0");
	static JButton button33 = new JButton("*");
	static JButton button34 = new JButton("/");
	static JButton button35 = new JButton(".");
	static JButton deleteButton = new JButton("Delete");
	static JButton enterButton = new JButton("Enter");
	static JButton[] buttonArray = {button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20, button21, button22, button23, button24, button25, button26, button27, button28, button29, button30, button31, button32, button33, button34, button35, deleteButton, enterButton};
	static Clicklistener click1= new Clicklistener(button1);
	static Clicklistener click2 = new Clicklistener(button2);
	static Clicklistener click3 = new Clicklistener(button3);
	static Clicklistener click4 = new Clicklistener(button4);
	static Clicklistener click5 = new Clicklistener(button5);
	static Clicklistener click6 = new Clicklistener(button6);
	static Clicklistener click7 = new Clicklistener(button7);
	static Clicklistener click8 = new Clicklistener(button8);
	static Clicklistener click9 = new Clicklistener(button9);
	static Clicklistener click10 = new Clicklistener(button10);
	static Clicklistener click11 = new Clicklistener(button11);
	static Clicklistener click12 = new Clicklistener(button12);
	static Clicklistener click13 = new Clicklistener(button13);
	static Clicklistener click14 = new Clicklistener(button14);
	static Clicklistener click15 = new Clicklistener(button15);
	static Clicklistener click17 = new Clicklistener(button16);
	static Clicklistener click18 = new Clicklistener(button17);
	static Clicklistener click19 = new Clicklistener(button18);
	static Clicklistener click20 = new Clicklistener(button19);
	static Clicklistener click21 = new Clicklistener(button20);
	static Clicklistener click22 = new Clicklistener(button21);
	static Clicklistener click23 = new Clicklistener(button22);
	static Clicklistener click24 = new Clicklistener(button23);
	static Clicklistener click25 = new Clicklistener(button24);
	static Clicklistener click26 = new Clicklistener(button25);
	static Clicklistener click27 = new Clicklistener(button26);
	static Clicklistener click28 = new Clicklistener(button27);
	static Clicklistener click29 = new Clicklistener(button28);
	static Clicklistener click30 = new Clicklistener(button29);
	static Clicklistener click31 = new Clicklistener(button30);
	static Clicklistener click32 = new Clicklistener(button31);
	static Clicklistener click33 = new Clicklistener(button32);
	static Clicklistener click34 = new Clicklistener(button33);
	static Clicklistener click35 = new Clicklistener(button34);
	static Clicklistener click36 = new Clicklistener(button35);
	static Clicklistener click37 = new Clicklistener(deleteButton);
	static Clicklistener click38 = new Clicklistener(enterButton);
	static Keylistener listen = new Keylistener();
	public static void main(String[] args) {
		button1.addActionListener(click1);
		button2.addActionListener(click2);
		button3.addActionListener(click3);
		button4.addActionListener(click4);
		button5.addActionListener(click5);
		button6.addActionListener(click6);
		button7.addActionListener(click7);
		button8.addActionListener(click8);
		button9.addActionListener(click9);
		button10.addActionListener(click10);
		button11.addActionListener(click11);
		button12.addActionListener(click12);
		button13.addActionListener(click13);
		button14.addActionListener(click14);
		button15.addActionListener(click15);
		button16.addActionListener(click17);
		button17.addActionListener(click18);
		button18.addActionListener(click19);
		button19.addActionListener(click20);
		button20.addActionListener(click21);
		button21.addActionListener(click22);
		button22.addActionListener(click23);
		button23.addActionListener(click24);
		button24.addActionListener(click25);
		button25.addActionListener(click26);
		button26.addActionListener(click27);
		button27.addActionListener(click28);
		button28.addActionListener(click29);
		button29.addActionListener(click30);
		button30.addActionListener(click31);
		button31.addActionListener(click32);
		button32.addActionListener(click33);
		button33.addActionListener(click34);
		button34.addActionListener(click35);
		button35.addActionListener(click36);
		deleteButton.addActionListener(click37);
		enterButton.addActionListener(click38);
		textfield.addKeyListener(listen);
		Main a = new Main();
		a.setVisible(true);
		a.repaint();
	}
	private Main() {
		super("Wolfram Beta");
		setSize(1500, 600);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void paint(Graphics g) {
		System.out.println("Start");
		int width = getWidth();
		frameAdd(width);
	}
	public static String text() {
		return textfield.getText();
	}
	public void frameAdd(int width) {
		title.setForeground(new Color(222, 16, 1));
		title.setFont(titleFont);
		title.setBounds(540, 10, 300,100);
		add(title);
		title2.setForeground(new Color(255, 125, 0));
		title2.setFont(titleFont);
		title2.setBounds(785, 10, 200, 100);
		add(title2);
		textfield.setBounds(20, 100, width-290, 25);
		add(textfield);
		answerField.setBounds(10,180,width-820,300);
		answerField.setEditable(false);
		add(answerField);
		methodField.setBounds(width-270,103,270,20);
		add(methodField);
		constants.setSize(500,250);
		constants.setBounds(width - 420, getHeight()-400, 420, 300);
		captainZero.setSize(500,250);
		captainZero.setBounds(width-420, getHeight()-400, 420, 300);
		infinitus.setSize(500,250);
		infinitus.setBounds(width-420, getHeight()-400, 420, 300);
		dne.setSize(500,250);
		dne.setBounds(width-420, getHeight()-400, 420, 300);
		add(captainZero);
		add(constants);
		add(infinitus);
		add(dne);
		captainZero.setVisible(false);
		constants.setVisible(false);
		infinitus.setVisible(false);
		dne.setVisible(false);
		int n = 0;
		int y = 0;
		for (JButton thisButton : buttonArray) {
			thisButton.setBounds(700+n, 177 + y, (width-20)/20, 45);
			add(thisButton);
			thisButton.setSelected(true);
			thisButton.setSelected(false);
			if (n > (width-20)/6) {
				n = -((width-20)/20);
				y+=50;
			}
			n+=((width-20)/20);
		}
		rectangle.setBounds(0,0,width, 200);
		rectangle.setBackground(Color.ORANGE);
		add(rectangle);
		methodField.updateUI();
	}
	public static void updatePhoto(String name) {
		captainZero.setVisible(false);
		constants.setVisible(false);
		infinitus.setVisible(false);
		dne.setVisible(false);
		//graph.setVisible(false);
		if (name.equals("Infinitus")) {
			infinitus.setVisible(true);
		}
		else if (name.equals("Captain Zero")) {
			captainZero.setVisible(true);
		}
		else if (name.equals("Clear")) {}
		else if (name.equals("DNE")) {
			dne.setVisible(true);
		}
		else {
			constants.setVisible(true);
		}
	}
	public static void amendQuestion(String addText) {
		if (textfield.getText().equals("Enter Function")) {
			textfield.setText("");
		}
		int n = textfield.getCaretPosition();
		if (n<1) {
			if (addText.equals("Delete")) {
				addText = "";
			}
			textfield.setText(addText+textfield.getText());
		}
		else {
			if (addText.equals("Delete")) {
				textfield.setText(textfield.getText().substring(0,n-1)+textfield.getText().substring(n, textfield.getText().length()));
			}
			else {
				textfield.setText(textfield.getText().substring(0,n)+addText+textfield.getText().substring(n,textfield.getText().length()));
			}
		}
	}
	public static void amendAnswer(String output) {
		answerField.setText(output);
	}
}