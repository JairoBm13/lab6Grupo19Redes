package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InterfazPrincipal extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1508134142552847275L;

	public static final String LOGIN = "login";
	public static final String REGISTRO = "registro";
	private JTextField txtUser;
	private JLabel user;
	private JLabel pwd;
	private JPasswordField txtPwd;
	private JButton btnLogin;
	private JButton btnRegistro;
	private ClienteTCP clienteTCP;
	private IntVideo video;

	public InterfazPrincipal(){
		super("login");
		clienteTCP = new ClienteTCP();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(6, 1, 0, 0));

		user = new JLabel("User:");
		add(user);

		txtUser= new JTextField();
		add(txtUser);

		pwd = new JLabel("Password:");
		add(pwd);

		txtPwd = new JPasswordField();
		add(txtPwd);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(this);
		btnLogin.setActionCommand(LOGIN);
		btnRegistro = new JButton("Registrarse");
		btnRegistro.addActionListener(this);
		btnRegistro.setActionCommand(REGISTRO);
		add(btnLogin);
		add(btnRegistro);
	}

	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if (LOGIN.equals(comando))
		{
			login(txtUser.getText(), new String(txtPwd.getPassword()));
		}
		if (REGISTRO.equals(comando))
		{
			registro(txtUser.getText(), new String(txtPwd.getPassword()));
		}
	}

	public void login(String text, String password) {
		ClienteTCP.login(text, password);
		this.setVisible(false);
		video = new IntVideo(clienteTCP);
		video.setVisible(true);
	}

	public void registro(String text, String password) {
		String result = ClienteTCP.register(text, password);
		if (result.equals("registered")){
			JOptionPane.showMessageDialog(this,"Usted ha sido registrado");
		}
	}

	public static void main(String[] args) {
		InterfazPrincipal ip = new InterfazPrincipal();
		ip.setVisible(true);
	}
}