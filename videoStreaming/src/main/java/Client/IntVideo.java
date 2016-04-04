package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.uniandes.streaming.client.LoginClient;


public class IntVideo extends JFrame implements ActionListener,ListSelectionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7341671746498005886L;
	
	private final static String SETUP = "SETUP";
	private final static String PLAY = "PLAY";
	private final static String PAUSE = "PAUSE";
	private final static String UPLOAD = "UPLOAD";

	private JFrame f = new JFrame("Client");
	private JButton setupButton = new JButton("Setup");
	private JButton playButton = new JButton("Play");
	private JButton pauseButton = new JButton("Pause");
	private JButton tearButton = new JButton("Upload");
	private JPanel mainPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JLabel iconLabel = new JLabel();
	private ClienteUDP client;
	private JList<Canal> list;
	
	public IntVideo(){
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		mainPanel.setLayout(new BorderLayout(0, 0));

		//Buttons
		buttonPanel.setLayout(new GridLayout(1,0));
		buttonPanel.add(setupButton);
		buttonPanel.add(playButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(tearButton);
		setupButton.addActionListener(this);
		playButton.addActionListener(this);
		pauseButton.addActionListener(this);
		tearButton.addActionListener(this);


		//Image display label
		iconLabel.setIcon(null);
		mainPanel.add(iconLabel,BorderLayout.CENTER);
		mainPanel.add(buttonPanel,BorderLayout.SOUTH);

		f.getContentPane().add(mainPanel, BorderLayout.CENTER);
		f.setSize(new Dimension(600,370));
		f.setVisible(true);

		Timer time = new Timer(1,this);
		Timer time2 = new Timer(5000, this);

		client = new ClienteUDP(time);
		time2.start();

		Canal[] channelList = client.getChannels();
		list = new JList<Canal>(channelList); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(100, 500));
		mainPanel.add(listScroller, BorderLayout.EAST);
	}
	
	public void createJFileChooser()
	 {
		  JFileChooser fileChooser=new JFileChooser();
		   fileChooser.showOpenDialog(null);
		   File open=fileChooser.getSelectedFile();
		   if (open != null) {
			   String file= open.getAbsolutePath();
			   String name= open.getName();
			   String result = loginClient.sendVideo(file,name);
			   if (result.equals(LoginClient.UDPLOADING))
			   {
				   JOptionPane.showMessageDialog(jp,"el cliente esta ocupado subiendo un archivo");
			   }
			   else if (result.equals(LoginClient.FREE)){
				   JOptionPane.showMessageDialog(jp,"se ha terminado de subir el archivo");
			   }
		   }
		   
	 }

	public void actionPerformed(ActionEvent arg0) {
		String comando = arg0.getActionCommand();
		if(SETUP.equals(comando)){
			client.setup();
		}else if(PLAY.equals(comando)){
			client.play();
		}else if(PAUSE.equals(comando)){
			client.pause();
		}else if(UPLOAD.equals(comando)){
			createJFileChooser();
		}
		
	}

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}