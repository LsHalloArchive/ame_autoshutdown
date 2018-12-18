/**
 * AMEAutoshudown.java
 * Adobe Media Encoder Auto Shutdown
 * 20.04.2017 LsHallo
 */

package tk.lshallo.ameautoshutdown;

import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class AMEAutoshutdown {
	static boolean isNotShuttingDown = true;
	static int shutdownDelay = 60;
	private static String sudoPasswd = "";

	public static void main(String[] args) {
		GUI frame = new GUI("AME Auto Shutdown");

		//Initial permission check on unix based OS
		/*
		if (isMac() || isUnix()) {
			System.out.println("Trying access for commands...\n");
			try {
				System.out.print("Using ProcessBuilder: 'sudo shutdown -h " + shutdownDelay + "'");
				ProcessBuilder pb = new ProcessBuilder("/sbin/shutdown -h " + shutdownDelay);
				pb.redirectErrorStream(true);
				Process p = pb.start();
				p.waitFor();
				p.destroy();

				pb = new ProcessBuilder("sudo shutdown -c ");
				pb.redirectErrorStream(true);
				p = pb.start();
				p.waitFor();
				p.destroy();
				System.out.println(" > Success!\n\n\n");
			} catch (IOException | InterruptedException e) {
				System.out.println(" > ProcessBuilder failed!\n\n\n");
				try {
					System.out.println("Using 'shutdown -h " + shutdownDelay + "'");
					String command = "/bin/bash";
					Process p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					stdin.println("sudo shutdown -h " + shutdownDelay);
					stdin.println("sudo shutdown -c");
					stdin.println("exit");
					stdin.close();
					System.out.println(" > Success!\n\n");
				} catch (IOException ex) {
					System.out.println(" > Runtime failed!\n\n");
					JOptionPane.showMessageDialog(null, "Could not successfully send shutdown command.\n"
							+ "The program will not be working!\n" + "Exiting now...", "Error!", 3);
					System.exit(-1);
				}
			}
		}*/
		
		if(isMac() || isUnix()) {
			System.out.println("Checking privileges for shutdown.");
			if(!shutdownNormalPrivileges(true)) {
				System.out.println("Normal privileges failed.");
				
				sudoPasswd = JOptionPane.showInputDialog("Normal user privileges seem to not be enough. Please input sudo password.");

				if(!shutdownAdminPrivileges(true)) {
					JOptionPane.showMessageDialog(null, "Could not successfully send shutdown command.\nThe program will not be working!\nExiting now...", "Error!", 3);
					System.exit(-1);
				}
			}
		}

		//Endless check if project finished
		for (;;) {
			if (frame.checkForFile() && isNotShuttingDown) {
				System.out.println("File exists!\nShutting down!");
				isNotShuttingDown = false;

				if(!shutdownNormalPrivileges(false)) {
					if(!shutdownAdminPrivileges(false)) {
						isNotShuttingDown = true;
					}
				}

				// Wait for shutdown
				try {
					for (int i = (int) (shutdownDelay * 1.5); i >= 0; i--) {
						System.out.println("Waiting for shutdown... (" + i + ")");
						if (i == shutdownDelay * 1.5 - shutdownDelay) {
							System.out.println("Shutdown should occur NOW!");
						}
						Thread.sleep((int) (1000));
					}
				} catch (InterruptedException e) {}
				
				// Shutdown did not happen properly
				System.out.println("Shutdown failed!");
				isNotShuttingDown = true;
			} else {
				if (isNotShuttingDown) {
					System.out.println("File not found...");
				} else {
					System.out.println("Already shutting down...");
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
		}
	}

	private final static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
	
	static boolean shutdownNormalPrivileges(boolean test) {
		if(isWindows()) {
			try {
				System.out.println("Using 'shutdown -s -t " + shutdownDelay + "' (Windows)");
				String command = "cmd";
				Process p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
				new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
				PrintWriter stdin = new PrintWriter(p.getOutputStream());
				stdin.println("shutdown -s -t " + shutdownDelay);
				if(test) {
					stdin.println("shutdown -a");
				}
				stdin.println("exit");
				stdin.close();
				return true;
			} catch (IOException e) {
				System.out.println("Shutdown failed...");
				return false;
			}
		} else if(isMac() || isUnix()) {
			System.out.println("Using normal privileges to shut down.");
			try {
				System.out.println("Using 'shutdown -h " + (shutdownDelay / 60) + "'");
				String command = "/bin/bash";
				Process p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
				new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
				PrintWriter stdin = new PrintWriter(p.getOutputStream());
				stdin.println("shutdown -h " + (shutdownDelay / 60));
				if(test) {
					stdin.println("shutdown -c");
				}
				stdin.println("exit");
				stdin.close();
				System.out.println("Normal privileges success!");
				return true;
			} catch (IOException ex) {
				System.out.println("Normal privileges failed!");
				return false;
			}
		} else {
			System.out.println("Operating system not recognized. Debug info: " + OS);
			return false;
		}
	}
	
	static boolean shutdownAdminPrivileges(boolean test) {
		if(isWindows()) {
			try {
				System.out.println("Using 'shutdown -s -t " + shutdownDelay + "' (Windows)");
				String command = "cmd";
				Process p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
				new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
				PrintWriter stdin = new PrintWriter(p.getOutputStream());
				stdin.println("shutdown -s -t " + shutdownDelay);
				if(test) {
					stdin.println("shutdown -a");
				}
				stdin.println("exit");
				stdin.close();
				return true;
			} catch (IOException e) {
				System.out.println("Shutdown failed...");
				return false;
			}
		} else if(isMac() || isUnix()) {
			System.out.println("Using admin privileges to shut down.");
			try {
				System.out.println("Using 'shutdown -h " + (shutdownDelay / 60) + "'");
				String command = "/bin/bash";
				Process p = Runtime.getRuntime().exec(command);
				new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
				new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
				PrintWriter stdin = new PrintWriter(p.getOutputStream());
				stdin.println("sudo -S shutdown -h " + (shutdownDelay / 60));
				stdin.println(sudoPasswd);
				if(test) {
					stdin.println("sudo -S shutdown -c");
				}
				stdin.println("exit");
				stdin.close();
				System.out.println("Admin privileges success!");
				return true;
			} catch (IOException ex) {
				System.out.println("Admin privileges failed!");
				return false;
			}
		} else {
			System.out.println("Operating system not recognized. Debug info: " + OS);
			return false;
		}
	}
}
