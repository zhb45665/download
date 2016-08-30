package com.zhb.Ftp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sun.net.ftp.FtpClient;

import com.zhb.tools.ExecuteSql;
import com.zhb.tools.FTPUtil;
import org.apache.log4j.Logger;

public class DownLoad extends JFrame {

	private static final long serialVersionUID = 0x607904c422b5d922L;
	private JPanel ftpclient;
	private JTextField tf_localpath;
	private JTextArea ta_Info;
	private FtpClient ftp;
	private JTextField tf_tableName;
	private String regex;
	private JTextField tf_DbIP;
	private JTextField tf_DbName;

	private static Logger logger = Logger.getLogger(DownLoad.class);

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					DownLoad frame = new DownLoad();
					frame.setVisible(true);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

		});
	}

	public DownLoad() {
		regex = "/";
		setTitle("FtpClient");
		setDefaultCloseOperation(3);
		setBounds(100, 100, 741, 465);
		ftpclient = new JPanel();
		ftpclient.setToolTipText("FtpClient");
		setContentPane(ftpclient);
		ftpclient.setLayout(null);
		JLabel lblLocalpath = new JLabel("LocalPath");
		lblLocalpath.setBounds(27, 187, 61, 17);
		ftpclient.add(lblLocalpath);
		tf_localpath = new JTextField();
		tf_localpath.setBounds(125, 185, 255, 27);
		ftpclient.add(tf_localpath);
		tf_localpath.setColumns(10);
		JButton bt_select = new JButton("select");
		bt_select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(2);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					System.out.println((new StringBuilder("文件夹:")).append(
							file.getAbsolutePath()).toString());
					tf_localpath.setText(file.getAbsolutePath());
				} else if (file.isFile())
					System.out.println((new StringBuilder("文件:")).append(
							file.getAbsolutePath()).toString());
				System.out.println(jfc.getSelectedFile().getName());
			}
		});
		bt_select.setBounds(397, 187, 107, 27);
		ftpclient.add(bt_select);
		JButton bt_login = new JButton("LogIn");
		bt_login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ip = "10.1.253.152";
				int port = 21;
				String username = "tone";
				String password = "sxdxtone";
				ftp = FTPUtil.connectFTP(ip, port, username, password);
				ta_Info.append(ftp.getWelcomeMsg());
			}

		});
		bt_login.setBounds(397, 46, 107, 27);
		ftpclient.add(bt_login);
		ta_Info = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(ta_Info);
		scrollPane.setBounds(37, 234, 657, 180);
		ftpclient.add(scrollPane);
		JLabel lb_tableName = new JLabel("TableName");
		lb_tableName.setBounds(27, 98, 84, 17);
		ftpclient.add(lb_tableName);
		tf_tableName = new JTextField();
		tf_tableName.setBounds(158, 93, 151, 27);
		ftpclient.add(tf_tableName);
		tf_tableName.setColumns(10);
		JButton bt_DownLoad = new JButton("DownLoad");
		bt_DownLoad.addActionListener(new ActionListener() {

			private String tableName;
			private String quesstion;
			private String area;
			private String phone;
			private String DbIP;
			private String DbName;

			private String counts;
			private java.util.List list;

			public void actionPerformed(ActionEvent e) {
				if (tf_localpath.getText().equals(null)
						|| tf_localpath.equals("")) {
					ta_Info.append("LocalPath is null");
				} else {
					if (ftp != null){
					FTPUtil.changeDirectory(ftp, regex);
					}else{
						String ip = "10.1.253.152";
						int port = 21;
						String username = "tone";
						String password = "sxdxtone";
						ftp = FTPUtil.connectFTP(ip, port, username, password);
						FTPUtil.changeDirectory(ftp, regex);
					}
					ExecuteSql es = new ExecuteSql();
					tableName = tf_tableName.getText();
					DbIP = tf_DbIP.getText();
					DbName = tf_DbName.getText();
					try {
						System.out.println((new StringBuilder("total:"))
								.append(counts).toString());
						list = es.getpath(tableName, DbIP, DbName);
						for (int i = 0; i < list.size(); i++) {
							String filepath = (String) list.get(i);
							System.out.println(filepath);

							String ph[] = filepath.split(regex);
//							ftp文件路径pah
							String pah = (new StringBuilder(String
									.valueOf(ph[5]))).append(regex)
									.append(ph[6]).append(regex).append(ph[7])
									.append(regex).append(ph[8]).append(regex)
									.append(ph[9]).append(regex).append(ph[10])
									.toString();
//							目录名dirpah
							String dirpah = (new StringBuilder(String
									.valueOf(ph[3]))).append(regex)
									.append(ph[4]).append(regex).append(pah)
									.toString();
//							ftp文件名local
							String local = ph[11];
//							问卷名quesstion
							quesstion= ph[2];
//							城市名area
							area=ph[1];
//							电话号码
							phone=ph[0];
//							String localdir = (new StringBuilder(String
//									.valueOf(tf_localpath.getText())))
////									.append(regex).append(quesstion).append(regex).append(area).append(regex)
//									.append(regex).append(quesstion).append(regex)
//									.toString();
							String localdir =  (new StringBuilder(String.valueOf(tf_localpath.getText()))).append(regex).append(dirpah).toString();
							if (ftp != null){
							FTPUtil.changeDirectory(ftp, dirpah);
							FTPUtil.download(
									(new StringBuilder(String.valueOf(localdir)))
//											.append(phone+"-"+area+".wav").toString(), local,
									.append(regex).append(local).toString(), local,
//									.append(phone+"-"+local).toString(),local,
									ftp);
							 logger.info(ph[0]+"------"+local);  
							FTPUtil.changeDirectory(ftp, regex);
							}else{
								String ip = "10.1.253.152";
								int port = 21;
								String username = "tone";
								String password = "sxdxtone";
								ftp = FTPUtil.connectFTP(ip, port, username, password);
								FTPUtil.changeDirectory(ftp, dirpah);
								FTPUtil.download(
										(new StringBuilder(String.valueOf(localdir)))
//												.append(phone+"-"+area+".wav").toString(), local,
										.append(regex).append(local).toString(), local,
//										.append(phone+"-"+local).toString(),local,
										ftp);
								 logger.info(ph[0]+"------"+local);  
								FTPUtil.changeDirectory(ftp, regex);
							}
						}
							

						System.out.println((new StringBuilder("total:"))
								.append(counts).toString());
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						
					}
					
				}
			}
		});
		bt_DownLoad.setBounds(397, 121, 107, 27);
		ftpclient.add(bt_DownLoad);
		JLabel lb_Dbhostip = new JLabel("DB_HostIP");
		lb_Dbhostip.setBounds(27, 51, 84, 17);
		ftpclient.add(lb_Dbhostip);
		tf_DbIP = new JTextField();
		tf_DbIP.setBounds(158, 46, 150, 27);
		ftpclient.add(tf_DbIP);
		tf_DbIP.setColumns(10);
		JLabel lblDbname = new JLabel("DB_Name");
		lblDbname.setBounds(27, 146, 61, 17);
		ftpclient.add(lblDbname);
		tf_DbName = new JTextField();
		tf_DbName.setBounds(125, 146, 255, 27);
		ftpclient.add(tf_DbName);
		tf_DbName.setColumns(10);
	}

}