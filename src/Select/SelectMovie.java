
package Select;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


public class SelectMovie {

	
	public SelectMovie() throws Exception {
		
		JPanel JP1 = new JPanel();	
		JLabel JL1 = new JLabel("��Ӱ��������");
		JL1.setFont(new Font("΢���ź�", Font.BOLD, 15));
		JP1.add(JL1, BorderLayout.CENTER);
		
		JPanel JP2 = new JPanel();
		JLabel JL2 = new JLabel("��Ӱ����");
		JL2.setFont(new Font("΢���ź�", Font.BOLD, 12));
		final JTextField Field1 = new JTextField(15);
		JP2.add(JL2);
		JP2.add(Field1);
		
		JPanel PointJP = new JPanel();
		final JLabel PointJL = new JLabel("");
		PointJL.setFont(new Font("΢���ź�", Font.BOLD, 10));
		PointJL.setForeground(Color.red);
		PointJP.add(PointJL);	
		
		JPanel JP3 = new JPanel();
		JLabel JL3 = new JLabel("������������");
		JL3.setFont(new Font("΢���ź�", Font.BOLD, 13));
		JP3.add(JL3);
		
		JPanel JP4 = new JPanel();
		final Choice Choice1 = new Choice();
		JLabel JL4 = new JLabel("����");
		JL4.setFont(new Font("΢���ź�", Font.BOLD, 12));
		
		Choice1.addItem("����");
		List<String> Types = FileUtils.readLines(new File("TypeList.txt"));
		for ( String Type:Types ) {
			Choice1.addItem(Type);
		}
		JLabel JL5 = new JLabel("  ����");
		JL5.setFont(new Font("΢���ź�", Font.BOLD, 12));
		final Choice Choice2 = new Choice();
		Choice2.addItem("����");
		Choice2.addItem("720p");
		Choice2.addItem("1080p");
		Choice2.addItem("����ԭ��");
		
		JP4.add(JL4);
		JP4.add(Choice1);
		JP4.add(JL5);
		JP4.add(Choice2);
		
		JPanel JP5 = new JPanel();
		JLabel JL6 = new JLabel("����");
		JL6.setFont(new Font("΢���ź�", Font.BOLD, 12));
		final Choice Choice3 = new Choice();
		List<String> Areas = FileUtils.readLines(new File("AreaList.txt"));
		for ( String Area:Areas ) {
			Choice3.addItem(Area);
		}
		JLabel JL7 = new JLabel("  ���");
		JL7.setFont(new Font("΢���ź�", Font.BOLD, 12));
		final Choice Choice4 = new Choice();
		List<String> Years = FileUtils.readLines(new File("YearList.txt"));
		for ( String Year:Years ) {
			Choice4.addItem(Year);
		}
		JP5.add(JL6);
		JP5.add(Choice3);
		JP5.add(JL7);
		JP5.add(Choice4);
		
		JPanel JP6 = new JPanel();
		JButton Select = new JButton("��һ��");
		JP6.add(Select);
		
		JFrame JF = new JFrame("��Ӱ��Ϣ��ѯϵͳ");
		Container CP = JF.getContentPane();
		GridBagLayout GBL = new GridBagLayout();
		CP.setLayout(GBL);
		GridBagConstraints GBC = new GridBagConstraints();
		
		GBC.gridx = 0;
		GBC.gridy = 0;
		CP.add(JP1);
		GBL.setConstraints(JP1, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 1;
		CP.add(JP2);
		GBL.setConstraints(JP2, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 2;
		CP.add(PointJP);
		GBL.setConstraints(PointJP, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 3;
		CP.add(JP3);
		GBL.setConstraints(JP3, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 4;
		CP.add(JP4);
		GBL.setConstraints(JP4, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 5;
		CP.add(JP5);
		GBL.setConstraints(JP5, GBC);
		
		GBC.gridx = 0;
		GBC.gridy = 6;
		CP.add(JP6);
		GBL.setConstraints(JP6, GBC);
		
		Dimension Dim = Toolkit.getDefaultToolkit().getScreenSize();
		Point P = new Point((Dim.width-300)/2, (Dim.height-300)/2);

		JF.setBounds(0, 0, 300, 300);
		JF.setLocation(P);
		JF.setLocationRelativeTo(null);
		JF.setResizable(false);
		JF.setVisible(true);
		JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String MovieName = Field1.getText();
				String MovieType = Choice1.getSelectedItem();
				String Definition = Choice2.getSelectedItem();
				String Area = Choice3.getSelectedItem();
				String Year = Choice4.getSelectedItem();
				
				PointJL.setText(" ");
				MyDatabase MDB = new MyDatabase();
				Connection Conn = MDB.ConnectionDB("MovieSpider");
				ArrayList<MovieInfo> MovieInfos = null;
				if ( MovieName.length() > 0 ) {
					try {
						MovieInfos = MDB.SelectMovie(Conn, MovieName, MovieType, Definition, Area, Year);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					if ( MovieInfos.size() > 0 ) {
						MakeTable( MovieInfos );
					}
					else {
						PointJL.setText("û���ҵ������Ϣ");
					}
				}
				else {
					if ( MovieType.compareTo("����") != 0 ) {
						try {
							MovieInfos = MDB.SelectType(Conn, MovieType, Definition, Area, Year);
							MakeTable(MovieInfos);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					else {
						PointJL.setText("�������Ӱ���ƻ�����������");
					}
				}
			}
		});
	}
   
	public void MakeTable( ArrayList<MovieInfo> MovieInfos ) {
		JFrame ANS = new JFrame("�������");
		Container Cont = ANS.getContentPane();
		Cont.setLayout(new BorderLayout());
		final JTable JT = new JTable();
		
		Comparator<MovieInfo> comparator = new Comparator<MovieInfo>(){
			public int compare(MovieInfo s1, MovieInfo s2) {
				return s2.getScore().compareTo(s1.getScore());
			}
		};
		Collections.sort(MovieInfos, comparator);
		
		String[] Header = {"����","����","����","����","����","���","����/���","����","��ϸ��Ϣ"};
		DefaultTableModel DTM = new DefaultTableModel(Header,0);
		JScrollPane JSP = new JScrollPane(JT);
		JT.setModel(DTM);
		
		for ( MovieInfo Info:MovieInfos  ) {
			Vector<String> V = new Vector<String>();
			V.addElement(Info.getMovieName());
			V.addElement(Info.getScore());
			V.addElement(Info.getDefinition());
			V.addElement(Info.getType());
			V.addElement(Info.getArea());
			V.addElement(Info.getYear());
			V.addElement(Info.getDirector_Screenwriter());
			V.addElement(Info.getStarring());
			V.addElement("����鿴");
			DTM.addRow(V);
		}
		
		JT.getColumnModel().getColumn(0).setPreferredWidth(100);
		JT.getColumnModel().getColumn(1).setPreferredWidth(10);
		JT.getColumnModel().getColumn(2).setPreferredWidth(20);
		JT.getColumnModel().getColumn(3).setPreferredWidth(100);
		JT.getColumnModel().getColumn(4).setPreferredWidth(30);
		JT.getColumnModel().getColumn(5).setPreferredWidth(10);
		JT.getColumnModel().getColumn(6).setPreferredWidth(20);
		JT.getColumnModel().getColumn(7).setPreferredWidth(30);
		JT.getColumnModel().getColumn(8).setPreferredWidth(10);
		
		Dimension Dim = Toolkit.getDefaultToolkit().getScreenSize();
		Point P = new Point((Dim.width-300)/2, (Dim.height-300)/2);
		
		Cont.add(JSP);	
		ANS.setBounds(0,0,800,500);
		ANS.setLocation(P);
		ANS.setLocationRelativeTo(null);
		ANS.setResizable(false);
		ANS.setVisible(true);
		
		JT.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ( JT.getValueAt(JT.getSelectedRow(), 0)!=null ) {
					String MovieName = (String) JT.getValueAt(JT.getSelectedRow(), 0);
					String Definition = (String) JT.getValueAt(JT.getSelectedRow(), 2);
					String MovieType = (String) JT.getValueAt(JT.getSelectedRow(), 3);
					String[] MovieTypes = MovieType.split("/ ");
					MovieName += "_"+Definition;
					
					for ( String Type:MovieTypes ) {
						File MovieFile = new File("E:/���/JAVA/workspace/Spider_Movie_6th/Movie_13189/"+Type+"/"+MovieName+"/"+MovieName+".htm");
						if ( MovieFile.exists() ) {
							MovieType = Type;
							break;
						}
					}
					try {
						Runtime.getRuntime().exec("cmd /c start E:/���/JAVA/workspace/Spider_Movie_6th/Movie_13189/"+MovieType+"/"+MovieName+"/"+MovieName+".htm");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
    public static void main(String args[]) throws Exception{
    	new SelectMovie();
    }
}

