/**
 * AMEAutoshudown.java
 * Adobe Media Encoder Auto Shutdown
 * 20.04.2017 LsHallo
 */

package tk.lshallo.ameautoshutdown;

import java.io.IOException;
import java.io.PrintWriter;

public class AMEAutoshutdown {
	static boolean isNotShuttingDown = true;
	static int shutdownDelay = 60;

	public static void main(String[] args) {
		GUI frame = new GUI("AME Auto Shutdown");

		for (;;) {
			if (frame.checkForFile() && isNotShuttingDown) {
				System.out.println("File exists!\nShutting down!");
				isNotShuttingDown = false;
				//Code credit to Pepe
				//http://stackoverflow.com/questions/4157303/how-to-execute-cmd-commands-via-java
				try {
					String[] command = {"cmd"};
					Process p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());
					if(isWindows()) {
						stdin.println("shutdown -s -t " + shutdownDelay);	
					}
					if(isMac() || isUnix()) {
						stdin.println("shutdown -h " + shutdownDelay);
					}
					stdin.println("exit");
					stdin.close();
					
					//Wait for shutdown
					Thread.sleep((int)(shutdownDelay*2 * 1000));
					//Shutdown did not happen properly
					isNotShuttingDown = true;
				} catch (IOException | InterruptedException e) {
					System.out.println("Shutdown failed...");
				}
			} else {
				if (isNotShuttingDown) {
					System.out.println("No file found...");
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
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}
}
