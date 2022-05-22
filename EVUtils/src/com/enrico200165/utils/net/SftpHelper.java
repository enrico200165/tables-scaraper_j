package com.enrico200165.utils.net;

import com.jcraft.jsch.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SftpHelper {

	public boolean connect() {

		try {
			jsch = new JSch();

			String host = "transfer0.silverpop.com";
			String user = "enrico.viali@it.ibm.com";
			String password = "Ocir!?00si";
			int port = 22;

			session = jsch.getSession(user, host, port);

			// username and password will be given via UserInfo interface.
			UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.setPassword(password);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}

	public boolean upload(String src, String dest) {
		try {
			if (sftpChannel == null) connect();
			it().put(src, dest);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean download(String src, String dest) {
		try {
			if (sftpChannel == null) connect();

//			log.info(lsString("download"));
//			log.info(lsString(src));
//			it().get(src.replaceAll(" ", "\\ "),dest.replaceAll(" ", "\\ "));
			it().get(src,dest);

		} catch (SftpException e) {
			log.error("problema con get "+src+ " ->"+dest,e);
			return false;
		}
		return true;
	}

	public java.util.Vector ls(String dir) {
		java.util.Vector vv = new Vector<>();
		try {
			vv = it().ls(dir);
		} catch (SftpException e) {
			log.error("problema con: ls "+dir);
			return vv;
		}
		return vv;
	}

	
	public String lsString(String dir) {
		return output(ls(dir));
	}

	
	
	
	public ChannelSftp it() {
		return this.sftpChannel;
	}

	public void disconnect() {

		sftpChannel.quit();
		session.disconnect();

		jsch = null;
		session = null;
		sftpChannel = null;
	}

	public static String output(java.util.Vector vv) {
		String ret = "";
		if (vv == null) {
			return "vector is null";
		}
		for (int ii = 0; ii < vv.size(); ii++) {
			// out.println(vv.elementAt(ii).toString());
			ret +=  vv.elementAt(ii) +"\n";
			Object obj = vv.elementAt(ii);
			if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
				System.out.println(((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getLongname());
			}
		}
		return ret;
	}

	JSch jsch;
	Session session;
	ChannelSftp sftpChannel;

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
			//			JOptionPane.showMessageDialog(null, message);
			log.info(message);
		}

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		private Container panel;

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx = 0;
			panel.add(new JLabel(instruction), gbc);
			gbc.gridy++;

			gbc.gridwidth = GridBagConstraints.RELATIVE;

			JTextField[] texts = new JTextField[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridx = 0;
				gbc.weightx = 1;
				panel.add(new JLabel(prompt[i]), gbc);

				gbc.gridx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weighty = 1;
				if (echo[i]) {
					texts[i] = new JTextField(20);
				} else {
					texts[i] = new JPasswordField(20);
				}
				panel.add(texts[i], gbc);
				gbc.gridy++;
			}

			if (JOptionPane.showConfirmDialog(null, panel, destination + ": " + name, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
				String[] response = new String[prompt.length];
				for (int i = 0; i < prompt.length; i++) {
					response[i] = texts[i].getText();
				}
				return response;
			} else {
				return null; // cancel
			}
		}
	}

	public static class MyProgressMonitor implements SftpProgressMonitor {
		ProgressMonitor monitor;
		long count = 0;
		long max = 0;

		public void init(int op, String src, String dest, long max) {
			this.max = max;
			monitor = new ProgressMonitor(null, ((op == SftpProgressMonitor.PUT) ? "put" : "get") + ": " + src, "", 0, (int) max);
			count = 0;
			percent = -1;
			monitor.setProgress((int) this.count);
			monitor.setMillisToDecideToPopup(1000);
		}

		private long percent = -1;

		public boolean count(long count) {
			this.count += count;

			if (percent >= this.count * 100 / max) {
				return true;
			}
			percent = this.count * 100 / max;

			monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
			monitor.setProgress((int) this.count);

			return !(monitor.isCanceled());
		}

		public void end() {
			monitor.close();
		}
	}

	private static String help = "guarda il codice originale per l'help";

	private static Logger log = LogManager.getLogger(SftpHelper.class.getSimpleName());

}
