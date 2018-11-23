package chitchat;

import java.io.Console;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Login {
	
	private String logUserName;
	private String logUserPassword;
	static Console console = System.console();
	static boolean again;
	
	public Login() throws Exception {
		System.out.println();
		printWithDelays("******* Welcome to ChitChat *******", TimeUnit.MILLISECONDS, 50);
		do {
			System.out.println();
		System.out.println("Please enter username and password");
		System.out.println();
		System.out.print("Username: ");
		Scanner sc = new Scanner(System.in);
		String name = sc.next();
		setLogUserName(name);		
		}while(again == true);
		
		if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
		 char passwordArray[] = console.readPassword("Password: ");
		 
		 System.out.println();
		String pass = new String(passwordArray);
		setLogUserPassword(Encrypt.byteArrayToHexString(Encrypt.computeHash(pass)));
	}
	
	public static void printWithDelays(String data, TimeUnit unit, long delay)
	        throws InterruptedException {
	    for (char ch:data.toCharArray()) {
	        System.out.print(ch);
	        unit.sleep(delay);
	        
	    }
	    System.out.println();
	}
	

	
	public  String getLogUserName() {
		return logUserName;
	}

	public String getLogUserPassword() {
		return logUserPassword;
	}

	public void setLogUserName(String userName) {
		logUserName = userName;
	}
	
	public void setLogUserPassword(String userPassword) {
		logUserPassword = userPassword;
	}

}