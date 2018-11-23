package chitchat;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu{


	private static int newM = 0;
	private static int accessLevel;
	private String menuUserName;
	private String menuUserPassword;
	static int wrongP = 0;
	static boolean again = false;
	static Menu m;
	static int choice;
	Login l;
	//private Database db = new Database();



	public Menu () {

	}

	public Menu(String userName, String password) {
		menuUserName = userName;
		menuUserPassword = password;
	}

	public static void appStart() throws Exception {
		Login l =new Login();
		Menu m = new Menu(l.getLogUserName(),l.getLogUserPassword());
		m.startLogin();
	}
	
	public void startLogin() throws Exception  {
		try {
			do {
				again= false;
				Database.getInstance().connect("jdbc:mysql://localhost:3306/chitchat?useUnicode=true&characterEncoding=utf-8", "root", "password");
				if(!checkUserName(menuUserName)) {
					System.out.println("User not found");
					if(again()) {
						again = true;
						appStart();
						continue;
					}else {
						break;
					}
				}

				if (checkUserTempPassword(menuUserName).equals("temp")) {
					if(!(compareChar(menuUserPassword, (checkUserPassword(menuUserName))))) {
						wrongP++;

						System.out.println("Password does not match!");
						if(wrongP>1) {
							System.out.println("Press 1 for requesting a temporary password from the administrator");
							System.out.println("Anything else for quitting the app");
							Scanner sc = new Scanner(System.in);
							String s = sc.next();
							if (s.equals("1")) {
								System.out.println("You will be informed as soon a new temporary password is ready");
								String request = "User: " + menuUserName + " requested a new temporary password";
								Database.getInstance();
								Database.executeStatement("insert into messages (date_created,date_edited,sender,receiver,message_data)\r\n" + 
										"values ('"+currentDateTime()+"','"+currentDateTime()+"','"+menuUserName+"','admin','"+request+"');");
							}else {
								stop();
							}
							appStart();
							break;
						}
						System.out.println();
						if(again()) {
							again= true;
							appStart();
						}else {
							again=false;
						}
					}
					else {
						System.out.println();
						System.out.println("###### Succesfull Connection #######");
						setAccessLevel(checkAccess(menuUserName, menuUserPassword));
						accessLevel = getAccessLevel();
						if (accessLevel== 1) {
							adminMenu();
						}

						if((accessLevel==2 ||accessLevel==3||accessLevel==4|| choice==2)) {
							menuMessages(getAccessLevel());

						}
					}
				}else {
					enterPass();
					again = false;
				}

			}while(again);

		}catch (NoSuchElementException e)  { 
			System.out.println();
			System.out.println("Have a nice day!!!!");
		}
	}

	public static boolean again() {
		boolean again = false;
		System.out.println("Wanna try again?");
		System.out.println("Y for yes");
		System.out.println("Anything else for no");
		Scanner sc = new Scanner (System.in);
		String c = sc.nextLine();
		if (c.equalsIgnoreCase("y")) {
			again=true;
		}else {
			again= false;
		}

		return again;
	}

	public int adminMenu() throws Exception {
		boolean again = false;
		int choice = 0;
		do {
			again=false;
			System.out.println();
			System.out.println("Please select next field....");
			System.out.println();
			System.out.println("1 for User Database");
			System.out.println("2 for messages");
			System.out.println("3 Log out");
			Scanner sc = new Scanner(System.in);
			try {
				choice = sc.nextInt();
				if (choice != 1 && choice!=2 && choice !=3) {
					System.out.println("Please press 1, 2 or 3....");
					again= true;
				}
			}catch (InputMismatchException e) {
				System.out.println("Please press 1, 2 or 3....");
				again = true;
			}
			if (choice==1) {
				menuUsers();
				break;
			}else if(choice==2) {
				menuMessages(getAccessLevel());
			}else if(choice==3) {
				appStart();
			}
		}while(again);

		return choice;
	}

	public void menuUsers() throws Exception {
		boolean again = false;
		int choice = 0;
		do {
			again= false;
			System.out.println("What would you like to do?");
			System.out.println();
			System.out.println("1. Add a user");
			System.out.println("2. Edit a user");
			System.out.println("3. Delete a user");
			System.out.println("4. View all users");
			System.out.println("5. Go back");
			System.out.println("6. Quit");
			Scanner sc = new Scanner(System.in);
			try {
				choice = sc.nextInt();
				if (!(choice>=1) && !(choice<=4)) {
					System.out.println("Please enter a correct choice");
					again= true;
				}
			}catch (InputMismatchException e) {
				System.out.println("Please enter a valid choice (1-4)");
				again = true;
			}
			if(choice==1) {
				again= false;
				addUser();

			}else if(choice==2) {
				again= false;
				searchUser(2);
			}else if(choice==3) {
				again= false;
				viewAllUsers(2);
				searchUser(3);
			}else if(choice==4) {
				again= false;
				viewAllUsers(4);
			}else if(choice==5) {
				again= false;
				adminMenu();
			}else if(choice==6) {
				again= false;
				stop();
			}
		}while(again);
	}

	public void addUser() throws Exception {


		boolean again;

		String email;String password;String userName;String lname;String date;
		String fname;String gender;int aLevel;

		System.out.println("Please enter new user details");
		System.out.println();
		Scanner sc = new Scanner(System.in);
		do {
			again = false;

			fname = enterFname();
			lname = enterLname();
			date = enterDate();
			gender = enterGender();
			aLevel = enterAccess();
			userName = enterUserName();
			password = enterTempPass();
			email = createEmail(fname, lname, date);


		
			Database.executeStatement("insert into users (first_name,last_name,birthDate,gender,email_address,user_name,\r\n" + 
					"`tempPassword`,accessLevel) \r\n" + 
					"values ('" +fname+ "','" +lname+ "','" +date+ "','" +gender+ "','" +email+ "','" +userName+ "',\r\n" + 
					"				'" +password+ "','" +aLevel+ "');");


			boolean c;
			do {
				c = false;
				System.out.println();
				System.out.println("Press");
				System.out.println("1. for adding another user");
				System.out.println("2. for going back");
				try {
					String choice= sc.next();
					if (choice.equals("1")) {
						again = true;
					}else if (choice.equals("2")) {
						this.menuUsers();
					}
				}catch(InputMismatchException e) {
					System.out.println("Please enter 1 or 2....");
					c=true;
				}
			}while(c);
		}while(again);
	}

	public void deleteUser() {

	}
	
	public void searchUser(int x) throws Exception {
		boolean searchAgain = false;
		boolean chooseAgain= false;
		String search = null;
		String fname = null;
		String lname = null;
		String bdate = null;
		String gender = null;
		String email = null;
		String uname = null;
		String password = null;
		int aLevel = 0;
		int userId = 0;
		do {
			Scanner sc = new Scanner(System.in);
			searchAgain = false;
			if (x==3) {
				System.out.println("Enter users name to delete");
				search = sc.next();
			}else if (x==2){

				System.out.println("Enter user's first,last or username to search for");
				search = sc.next();
			}


			String query = "select * from users \r\n" + 
					"where user_name = '" +search+ "' or last_name = '" +search+ "' or first_name = '" +search+ "'\r\n" + 
					";";

			ResultSet rs = Database.resultSet(query);

			if(rs.next()==false) {
				System.out.println("User(s) not found!");
				System.out.println();
				System.out.println("Search again?  ");
				System.out.println("1 for Yes");
				System.out.println("2 for No");
				search = sc.next();
				if (search.equals("1")) {
					searchAgain = true;
				}else if (search.equals("2")) {
					searchAgain = false;
					this.menuUsers();

				}
			}else{
				rs = Database.resultSet(query);
				System.out.println();
				System.out.printf("%-12s %-12s %-12s %-12s %-20s %-12s %-12s", "First name", "Last Name", "Birth Date", "Gender", "Email", "Username", "Access Level");
				System.out.println();
				while(rs.next()) {
					userId = rs.getInt("user_id");
					fname = rs.getString("first_name");
					lname = rs.getString("last_name");
					bdate = rs.getString("birthDate");
					gender = rs.getString("gender");
					email = rs.getString("email_address");
					uname = rs.getString("user_name");
					aLevel = rs.getInt("accessLevel");
					System.out.println();
					System.out.printf("%-12s %-12s %-12s %-12s %-20s %-12s %-12s", fname, lname, bdate, gender, email, uname, aLevel);
					System.out.println();

				}
				if(x==2) {
					do {
						chooseAgain = false;
						System.out.println();
						System.out.println("What would you like to change?");
						System.out.println();
						System.out.println("1. First name");
						System.out.println("2. Last name");
						System.out.println("3. Birhdate");
						System.out.println("4. Gender");
						System.out.println("5. user_name");
						System.out.println("6. Access Level");
						System.out.println("7. Password");
						System.out.println("8. I changed my mind!");
						try {

							int choice = sc.nextInt();
							if (choice<1 || choice>8 ) {
								throw new outOfRangeException(choice);
							}
							if(choice==1) {
								fname = enterFname();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET first_name = '" +fname+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if (choice==2) {
								lname = enterLname();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET last_name = '" +lname+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if (choice == 3) {
								bdate = enterDate();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET birthDate = '" +bdate+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if (choice == 4) {
								gender = enterGender();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET gender = '" +gender+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if(choice==5) {
								uname = enterUserName();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET user_name = '" +uname+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if(choice==6) {
								aLevel = enterAccess();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET accessLevel = '" +aLevel+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
							}else if(choice==7) {
								password =enterTempPass();
								Database.executeStatement("UPDATE users \r\n" + 
										"SET tempPassword = '" +password+ "' \r\n" + 
										"WHERE user_id = '" +userId+ "';");
								Database.editPassword(uname);

							}else if(choice==8) {
								chooseAgain = false;
								continue;
							}

						}catch(InputMismatchException | outOfRangeException e) {
							System.out.println("Please enter a valid choice ..(1-8)");
							chooseAgain = true;
							continue;
						}
						System.out.println();
						System.out.println("User " + uname + " successfully updated!!");
						System.out.println();
						int i = yesno("Any more changes to the same user?");
						System.out.println();
						if(i==1) {
							chooseAgain = true;
						}
					}while(chooseAgain);
				}else if(x==3) {
					System.out.println();
					int i = yesno("Are you sure you want to delete user " + uname + " ?" );
					if(i==1) {
						Database.executeStatement("delete from users where user_id = '" +userId+ "';");
						System.out.println("");
						System.out.println("User successfully deleted!");
					}
				}
			}
			String m = null;
			if(x==2) {
				m = "edit";
			}else if (x==3) {
				m = "delete";
			}
			int i= yesno("Would you like to "+m+" another user?");

			if(i==1) {
				searchAgain = true;
			}else if(i==2) {
				this.menuUsers();
			}

		}while(searchAgain);
	}
	
	public void viewAllUsers(int x) throws Exception {
		String fname = null;
		String lname = null;
		String bdate = null;
		String gender = null;
		String email = null;
		String uname = null;
		String password = null;
		int aLevel = 0;
		int userId = 0;
		String query = "select * from users;";

		ResultSet rs = Database.resultSet(query);
		rs = Database.resultSet(query);
		System.out.println();
		System.out.printf("%-12s %-12s %-13s %-12s %-30s %-12s %-12s", "First name", "Last Name", "Birth Date", "Gender", "Email", "Username", "Access Level");
		System.out.println();
		System.out.println();
		while(rs.next()) {
			userId = rs.getInt("user_id");
			fname = rs.getString("first_name");
			lname = rs.getString("last_name");
			bdate = rs.getString("birthDate");
			gender = rs.getString("gender");
			email = rs.getString("email_address");
			uname = rs.getString("user_name");
			aLevel = rs.getInt("accessLevel");
			System.out.printf("%-12s %-12s %-13s %-12s %-30s %-12s %-12s", fname, lname, bdate, gender, email, uname, aLevel);
			System.out.println("\n");
		}
		if(x==3 || x==4) {
			this.menuUsers();
		}
	}

	public int menuMessages(int accessLevel) throws Exception {
		boolean alreadyExec = false;
		boolean again = true;
		int temp = 0;
		newM = 0;
		int[] newMes = checkNewmes();

		if (newMes[0]>newMes[1]) {
			newM = (newMes[0]-newMes[1]);
			System.out.println();
			System.out.println("You have " + newM + " new messages!");
		}
		do {
			if(!alreadyExec) {
				System.out.println();
				System.out.println("Select an action");
				System.out.println();
			}
			System.out.println();
			System.out.println("1 for send a new Message");
			System.out.println("2 for viewing your Messages");
			if (accessLevel==1 || accessLevel==2 || accessLevel==3) {
				System.out.println("3 for edit a Message");
			}
			if (accessLevel==1 || accessLevel==2) {
				System.out.println("4 for delete a Message");
			}
			if (accessLevel==1) {
				temp = 1;
				System.out.println("5 for going back");
			}else {
				temp = 2;
				System.out.println("4 for Log Out");
			}
			Scanner sc = new Scanner(System.in);
			String choice = sc.next();
			if(choice.equals("1")) {
				sendMessage();
				again = false;
			}else if (choice.equals("2")) {
				viewMessagesMenu();
				again = false;
			}else if (choice.equals("3")) {
				editMessage();
				again = false;
			}else if (choice.equals("4") && temp==1) {
				deleteMessage();
				again = false;
			}else if (choice.equals("5") && temp == 1) {
				adminMenu();
				again = false;
			}else if (choice.equals("4") && temp == 2) {
				appStart();
				again = false;
			}else {
				System.out.println("Please enter a valid choice");
				System.out.println();
				alreadyExec = true;
			}
		}while (again);
		return accessLevel;
	}

	public void sendMessage() throws Exception {
		boolean again = false;
		boolean bigmes = false;
		String mes;
		String userName;
		System.out.println("Write your message: ");
		Scanner sc = new Scanner(System.in);
		mes= sc.nextLine();
		mes = mes.replaceAll("'", "\"");
		if (mes.length()>250) {
			bigmes=true;
		}

		do {
			again = false;
			System.out.println();
			System.out.println("Enter username ");
			userName= sc.next();
			if(checkUserName(userName) && !bigmes) {
				Database.executeStatement("insert into messages (date_created,date_edited,sender,receiver,message_data)\r\n" + 
						"values ('"+currentDateTime()+"','"+currentDateTime()+"','"+getMenuUserName()+"','"+userName+"','"+mes+"');");
				System.out.println();
				System.out.println("Message sent to user " + userName + " at " + currentDateTime());
				System.out.println();
				splitBigMesToText(mes, 100, userName,"inbox");

			}else if(checkUserName(userName) && bigmes)  {
				System.out.println();
				System.out.println("Your message is above the character limit and will be split to more messages.");
				System.out.println("We apologise for the incovenience");
				System.out.println();
				splitBigMes(mes, 250, userName);
				splitBigMesToText(mes, 100, userName,"inbox");

			}


			else {
				System.out.println("Username not found!");
				System.out.println();
				if(yesno("Do you want to search again?")==1) {
					again = true;
				}else {
					if(yesno("Do you want to see the avaliable users?")==1) {
						viewAllUsers(1);
					}else {
						again=true;
					}
				}
			}
		}while(again);
		menuMessages(getAccessLevel());
	}

	public void viewMessagesMenu() throws Exception {
		boolean again = false;


		do { 
			again =false;

			System.out.println();
			System.out.println("1 for all messages");
			System.out.println("2 for new messages");
			System.out.println("3 for going back");
			Scanner sc = new Scanner(System.in);
			String choice = sc.next();
			if (choice.equals("1")) {
				viewAllMessages(1);

			}else if(choice.equals("2")) {

				viewAllMessages(2);
			}else {
				System.out.println("Please enter a valid choice");
				System.out.println();
			}
			menuMessages(getAccessLevel());
		}while(again);
	}

	public HashMap<Integer, Integer> viewAllMessages(int x) throws Exception {
		HashMap<Integer, Integer> messages = new HashMap<Integer, Integer>();
		String dateS;
		String dateE;
		String sender;
		String message;
		String query;
		ResultSet rs;
		int mesId;
		String m = String.valueOf(newM);
		int i =0;
		if (x==2 ) {
			rs = Database.resultSet("SELECT * FROM (SELECT message_id, message_data, date_created, date_edited,sender FROM messages where receiver = '"+menuUserName+"'\r\n" + 
					"    ORDER BY message_id DESC \r\n" + 
					") sub\r\n" + 
					"ORDER BY message_id desc LIMIT "+newM+";");


		}else {
			rs = Database.resultSet("select message_id,date_created,date_edited,sender,message_data from messages\r\n" + 
					"where receiver = '"+getMenuUserName()+"';");
		}
		//		ResultSet rs = db.resultSet(query);
		//		rs = db.resultSet(query);
		System.out.println();

		while(rs.next()) {
			i++;
			mesId = rs.getInt("message_id");
			dateS = rs.getString("date_created");
			dateE = rs.getString("date_edited");
			sender = rs.getString("sender");
			message = rs.getString("message_data");
			messages.put(i, mesId);

			System.out.println("\n");
			System.out.println("Message No " + i);
			System.out.println("***************");
			if (getMenuUserName().equals(sender)) {
				System.out.println("\"Note to my self\" at " + dateS);
			}else {
				System.out.println("From \"" + sender + "\" at " + dateS);
			}
			System.out.println();
			splitString(message, 45);
			System.out.println();
			Scanner sc = new Scanner(System.in);
			System.out.println("Press enter to continue.....");
			sc.nextLine();
			System.out.println("------------------------------------------------");
		}
		if(i==0 && x!=3) {
			System.out.println("Nobody is thinking about you!");
			System.out.println();
			System.out.println("Better luck next time!");
		}
		if(x!=3) {
			viewMessagesMenu();
		}

		return messages;
	}

	public void editMessage() throws Exception {
		boolean bigMes = false;
		boolean again = false;
		HashMap<Integer, Integer> messages = viewAllMessages(3);
		if(messages.isEmpty()) {
			System.out.println();
			System.out.println("Nothing to edit here. You have no messages....");
			System.out.println();
		}else {
			System.out.println("Please the number of the message you want to edit");
			Scanner sc = new Scanner(System.in);
			int mesNumber = sc.nextInt();
			sc.nextLine();
			int mesId = messages.get(mesNumber);

			do {
				again = false;
				System.out.println("Write the new edited message: ");
				String newMes = sc.nextLine();
				newMes = newMes.replaceAll("'", "\"");
				if (newMes.length()> 250) {
					bigMes = true;
				}
				if (!bigMes) {
					Database.executeStatement("UPDATE messages \r\n" + 
							"SET message_data = '"+newMes+"', \r\n" + 
							"date_edited = '"+currentDateTime()+"'\r\n" + 
							"WHERE message_id = '"+mesId+"';");
				}else if (bigMes) {
					System.out.println();
					System.out.println("The updated message cannot be more than 250 characters");
					System.out.println("You will have to send a new message");
					int  choice  = yesno("Edit the same text?");
					if(choice == 1) {
						again = true;
					}else {
						menuMessages(getAccessLevel());
					}
				}
				System.out.println();
				System.out.println("Message updated!   " + currentDateTime());
				System.out.println();
				menuMessages(getAccessLevel());
			}while(again);
		}
		menuMessages(getAccessLevel());

	}

	public void deleteMessage() throws Exception {
		HashMap<Integer, Integer> messages = viewAllMessages(3);
		System.out.println("Please the number of the message you want to delete");
		Scanner sc = new Scanner(System.in);
		int mesNumber = sc.nextInt();
		sc.nextLine();
		int mesId = messages.get(mesNumber);
		System.out.println();
		int choice = yesno("Are you sure you want to delete the mesaage?");
		if (choice==1) {
			Database.executeStatement("delete from messages where message_id = '"+mesId+"';");
			System.out.println();
			System.out.println("Message successfully deleted!");
			System.out.println();
		}
		menuMessages(getAccessLevel());
	}

	public int checkAccess(String userName, String userPassword) {
		int accessLevel = 0;
		String type = null;
		String query = "select accessLevel as al from users \r\n" + 
				"where `user_name` = '"+userName+"' and `password` = '"+userPassword+"';";
		try{
			ResultSet rs1 = Database.resultSet(query);
			while(rs1.next()) {
				accessLevel = rs1.getInt("al");
				if(accessLevel==1) {
					type="Administrator";
				}else if(accessLevel==2) {
					type="Moderator";
				}else if(accessLevel==3) {
					type="Editor";
				}if(accessLevel==4) {
					type="Viewer";
				}
				System.out.println();
				System.out.println("Access level : " + accessLevel + "         | " + type + " |");
				System.out.println();
			}

		}catch (SQLException | NullPointerException e) {
			System.out.println("Error in method Check Access");
			e.printStackTrace();
			return -1;
		}
		return accessLevel;
	}

	public static boolean checkDate(String date) {
		boolean correctDate = true;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			formatter.setLenient(false);
			formatter.parse(date);
		} catch (ParseException e) {

			System.out.println("wrong or invalid date");
			correctDate = false;
			return correctDate;
		}
		String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
		if (!(date.matches(datePattern))) {
			System.out.println("Incorrect date format. Expected date format: yyyy//MM//dd");
			correctDate = false;
			return correctDate;
		}


		return correctDate;
	}

	public boolean checkUserName(String name) throws SQLException{
		boolean check = false;
		String query = "SELECT user_name FROM users;";

		ResultSet rs = Database.resultSet(query);
		while(rs.next()) {
			if (name.equals(rs.getString("user_name"))) {
				check = true;
				return check;
			}
		}
		return check;
	}

	public boolean compareChar(String x, String y) {
		boolean equalHash = false;
		char[] m = x.toCharArray();
		char[] l = y.toCharArray();

		for (int i = 0; i<m.length; i++) {
			if(m[i]==l[i]) {
				equalHash=true;
			}else {
				equalHash=false;
			}
		}
		return equalHash;

	}

	public String checkUserPassword(String username) throws SQLException{
		String pass = "";
		String query = "select `password` from users \r\n" + 
				"where user_name = '" +username+ "';";

		ResultSet rs = Database.resultSet(query);
		while(rs.next()) {
			pass = rs.getString(1);
		}
		return pass;
	}

	public String checkUserTempPassword(String username) throws SQLException{
		String pass = "";
		String query = "select `tempPassword` from users \r\n" + 
				"where user_name = '" +username+ "';";

		ResultSet rs = Database.resultSet(query);
		while(rs.next()) {
			pass = rs.getString(1);
		}
		return pass;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getMenuUserName() {
		return menuUserName;
	}

	public String getMenuUserPassword() {
		return menuUserPassword;
	}

	public void setMenuUserName(String menuUserName) {
		this.menuUserName = menuUserName;
	}

	public void setMenuUserPassword(String menuUserPassword) {
		this.menuUserPassword = menuUserPassword;
	}

//	public Database getDb() {
//		return Database;
//	}

//	public void setDb(Database db) {
//		this.db = db;
//	}

	public String enterFname() {
		boolean wrongFname;
		String fname;
		System.out.print("Enter user's first name: ");
		Scanner sc = new Scanner(System.in);
		do {
			wrongFname = false;
			fname = sc.next().trim();
			try {
				if(fname.matches(".*\\W+.*")) {
					throw new invalidCharException(fname);
				}
			}catch (invalidCharException e) {
				System.out.println();
				System.out.println("First name should contain only letters");
				wrongFname = true;
			}
		}while(wrongFname);
		return fname;
	}

	public String enterLname() {
		boolean wrongLname;
		String lname;
		System.out.print("Enter user's last name: ");
		Scanner sc = new Scanner(System.in);
		do {
			wrongLname = false;
			lname = sc.next().trim();
			try {
				if(lname.matches(".*\\W+.*")) {
					throw new invalidCharException(lname);
				}
			}catch (invalidCharException e) {
				System.out.println("First name should contain only letters");
				wrongLname = true;
			}
		}while(wrongLname);
		return lname;
	}

	public String enterDate() {
		boolean wrongDate = false;
		Scanner sc = new Scanner(System.in);
		String date;
		System.out.print("Enter users's birthdate (yyyy-MM-dd): ");
		do {
			wrongDate = false;
			date = sc.next();
			if(!checkDate(date)) {
				System.out.println("Please enter a valid date and/or use the correct format: (yyyy-MM-dd)");
				wrongDate = true;
			}
		}while(wrongDate);
		return date;
	}

	public String enterGender() {
		boolean wrongGender = false;
		System.out.print("Enter users's gender: ");
		Scanner sc = new Scanner(System.in);
		String gender;
		do {
			wrongGender = false;
			gender = sc.next();
			if(!gender.matches(".*[m,f,u].*")) {
				System.out.println("Please enter a valid gender (m for male, f for female, u for unknown");
				wrongGender = true;
			}
		}while(wrongGender);
		return gender;
	}

	public int enterAccess() {
		boolean wrongAccessLevel = false;

		System.out.println("Enter users's Access Level");
		System.out.println();
		System.out.println("2 for moderator (write, view, edit and delete rights)");
		System.out.println("3 for editor (write, view and edit rights)");
		System.out.println("4 for viewer (write and view rights)");
		Scanner sc= new Scanner(System.in);
		int aLevel;
		do {
			wrongAccessLevel = false;
			aLevel = sc.nextInt();
			if(aLevel !=2 && aLevel !=3 && aLevel !=4) {
				System.out.println("Please enter a valid access Level: 2,3 or 4");
				wrongAccessLevel = true;
			}
		}while(wrongAccessLevel);
		return aLevel;
	}

	public String enterUserName() throws SQLException {
		boolean invalidUserName = false;
		System.out.print("Choose a unique user name for the user: ");
		Scanner sc = new Scanner(System.in);
		String userName;
		do {
			invalidUserName = false;
			userName = sc.next();
			if (checkUserName(userName)) {
				System.out.println("User name already exists. Please choose another one!");
				invalidUserName = true;
			}
		}while(invalidUserName);
		return userName;
	}

	public String enterTempPass() {
		boolean invalidPassword = false;
		System.out.print("Choose a temporary password for the user: ");
		Scanner sc = new Scanner(System.in);
		String password;
		do {
			invalidPassword = false;

			password = sc.next();
			if (password.length()<4) {
				System.out.println("You should try something bigger!!!");
				invalidPassword = true;
			}
		}while(invalidPassword);
		return password;
	}

	public String createEmail(String fname, String lname, String date) {
		String email = (fname + (lname.substring(0, 1)) + date.substring(2, 4) + "@chitchat.com");
		return email;
	}

	public String currentDateTime() {
		long currentTime = System.currentTimeMillis();
		Date time = new Date(currentTime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String cTime = sdf.format(time);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date today = Calendar.getInstance().getTime();
		String cDate = df.format(today);

		return cDate+" "+cTime;
	}

	public int yesno(String s) {
		boolean yesno = false;
		int choice = 0;
		Scanner sc =new Scanner(System.in);
		System.out.println(s);
		do {
			System.out.println("1. for yes");
			System.out.println("2. for no");
			try {
				choice = sc.nextInt();
				if(choice != 1 && choice != 2) {
					throw new outOfRangeException(choice);
				}
			}catch (InputMismatchException | outOfRangeException e) {
				System.out.println("please enter 1 or 2....");
				yesno=true;
			}
		}while(yesno);
		return choice;
	}

	public void enterPass() throws Exception {
		boolean hasLetter = false;
		boolean hasDigit = false;
		boolean hasSpace = false;
		boolean hasSpecialChar = false;
		boolean again = false;
		String pass;
		Scanner s  = new Scanner(System.in);
		System.out.println("Hi there! We are happy to welcome you for the first time in our app!");
		System.out.println();
		System.out.println("For your own security you have to set up your new password!!!");
		System.out.println();
		System.out.print("New Password: ");

		do {
			again = false;
			pass = s.next();
			if (pass.length() >= 8) {
				for (int i = 0; i < pass.length(); i++) {
					char x = pass.charAt(i);
					if (Character.isLetter(x)) {
						hasLetter = true;
					}
					else if (Character.isDigit(x)) {
						hasDigit = true;
					}else if(x==' ') {
						hasSpace = true;
					}else if((x=='@') || (x=='!') || (x=='#')) {
						hasSpecialChar = true;
					}

					if(hasLetter && hasDigit && hasSpecialChar && !hasSpace){
						again = false;
					}

				}
				if (!hasLetter || !hasDigit || !hasSpecialChar || hasSpace) {
					System.out.println("Your password should contain at least a number, a letter and a special character (@ /! /#)!");
					again = true;
				} else {
				}
			} else {
				System.out.println("Your password should be at least 8 characters");
				again = true;
			}
		}while(again);
		try {
			String password = Encrypt.byteArrayToHexString(Encrypt.computeHash(pass));
			Database.executeStatement("UPDATE users \r\n" + 
					"SET password = '" +password+ "' \r\n" + 
					",tempPassword = 'temp' \r\n" +
					"WHERE user_name = '" +getMenuUserName()+ "';");
		} catch (Exception e) {
			System.out.println("Error sending password to database....");
			e.printStackTrace();
		}
		appStart();

	}

	public void stop() {
		System.out.println();
		System.out.println("Thank you for using Chim Chat");
		System.out.println("");
		System.out.println("Have a nice day!!!");
		System.exit(0);
	}

	public void splitString(String msg, int lineSize) {
		List<String> res = new ArrayList<>();

		Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
		Matcher m = p.matcher(msg);

		while(m.find()) {
			System.out.println(m.group().trim());   // Debug
			res.add(m.group());
		}
	}

	public void splitBigMes(String msg, int lineSize, String userName) throws IOException {
		List<String> res = new ArrayList<>();
		int i = 0;
		if(!msg.contains(" ")) {
			res =splitEqually(msg, 250);

		}else {
			Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
			Matcher m = p.matcher(msg);

			while(m.find()) {
				res.add(m.group());
			}
		}
		Iterator<String> it = res.iterator();

		while(it.hasNext()) {
			i++;
			Database.executeStatement("insert into messages (date_created,date_edited,sender,receiver,message_data)\r\n" + 
					"values ('"+currentDateTime()+"','"+currentDateTime()+"','"+getMenuUserName()+"','"+userName+"','"+it.next().toString()+"');");

		}
		System.out.println(i + " Messages sent to user " + userName);
		System.out.println(currentDateTime());
		System.out.println();
	}

	public void splitBigMesToText(String msg, int lineSize, String userName, String type) throws IOException {
		List<String> res = new ArrayList<>();
		String currentUser = userName;
		int i = 0;
		if(!msg.contains(" ")) {
			res =splitEqually(msg, 100);
		}else {	
			Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
			Matcher m = p.matcher(msg);

			while(m.find()) {
				res.add(m.group());
			}
		}
		Iterator<String> it = res.iterator();
		FileEdit f = new FileEdit(type,userName);
		f.getW().append("");
		f.getW().newLine();
		f.getW().append("");
		if(currentUser.equals(getMenuUserName())) {
			f.getW().append("Note to myself    ");
		}
		else {
			f.getW().append("From " + getMenuUserName()+ "   ");
		}
		f.getW().append(currentDateTime());
		f.getW().newLine();
		f.getW().append("----------------------------------------------------------------------------------------------------");
		f.getW().newLine();
		f.getW().append("");
		f.getW().newLine();
		while(it.hasNext()) {
			i++;

			f.getW().append(it.next().toString());
			f.getW().newLine();

		}
		f.getW().close();
		System.out.println();
	}

	public static List<String> splitEqually(String text, int size) {
		List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

		for (int start = 0; start < text.length(); start += size) {
			ret.add(text.substring(start, Math.min(text.length(), start + size)));
		}
		return ret;
	}

	public int[] checkNewmes() throws SQLException {
		int[] mes = new int[2];

		ResultSet rs  = Database.resultSet("select count(receiver) as num from messages \r\n" + 
				"where receiver = '"+menuUserName+"'\r\n" + 
				"group by receiver");
		while(rs.next()) {
			mes[0] = rs.getInt(1);
		}

		rs = Database.resultSet("select message_count from users\r\n" + 
				"where user_name = '"+menuUserName+"';");
		while(rs.next()) {
			mes[1] = rs.getInt(1);
		}
		Database.executeStatement("update users \r\n" + 
				"set message_count = (select count(receiver) as num from messages \r\n" + 
				"where receiver = '"+menuUserName+"'\r\n" + 
				"group by receiver)\r\n" + 
				"where user_name = '"+menuUserName+"';");

		return mes;

	}

	class invalidCharException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String s;
		invalidCharException(String s){
			this.s = s;
		}
		public String toString(){
			return ("NoNumberException: " + s);
		}
	}

	class outOfRangeException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 */
		int i;
		outOfRangeException(int i){
			this.i = i;
		}
		public String toString(){
			return ("NoNumberException: " + i);
		}
	}

}
