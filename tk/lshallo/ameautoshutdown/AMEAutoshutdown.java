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

	public static void main(String[] args) {
		GUI frame = new GUI("AME Auto Shutdown");

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
		}

		for (;;) {
			if (frame.checkForFile() && isNotShuttingDown) {
				System.out.println("File exists!\nShutting down!");
				isNotShuttingDown = false;
				// Code credit to Pepe
				// http://stackoverflow.com/questions/4157303/how-to-execute-cmd-commands-via-java
				try {

					if (isWindows()) {
						System.out.println("Using 'shutdown -s -t " + shutdownDelay + "'");
						String command = "cmd";
						Process p = Runtime.getRuntime().exec(command);
						new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
						new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
						PrintWriter stdin = new PrintWriter(p.getOutputStream());
						stdin.println("shutdown -s -t " + shutdownDelay);
						stdin.println("exit");
						stdin.close();
					}
				} catch (IOException e) {
					System.out.println("Shutdown failed...");
				}

				if (isMac() || isUnix()) {
					try {
						System.out.println("Using 'shutdown -h " + shutdownDelay/60 + "'");
						String command = "/bin/bash";
						Process p = Runtime.getRuntime().exec(command);
						new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
						new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
						PrintWriter stdin = new PrintWriter(p.getOutputStream());
						stdin.println("sudo shutdown -h " + (int)(shutdownDelay/60.0+0.5));
						stdin.println("exit");
						stdin.close();
					} catch(IOException e) {
						
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
				} catch (InterruptedException e) {

				}
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
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
}
