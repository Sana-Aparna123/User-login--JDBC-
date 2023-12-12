import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
//User Login program using JDBC to perform CRUD Operations.

public class UserLogin {
	public static Connection giveConnection() {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			System.out.println("Driver loaded");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userlogin", "root", "root");
			System.out.println("connection established");
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("1.new user");
		System.out.println("2.existing user");
		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			register();
			break;
		case 2:
			login();
			break;
		default:
			System.out.println("Invalid input");
			break;

		}
	}

	private static void register() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("enter your name");
		String name = sc.next();
		System.out.println("enter your username:");
		String username = sc.next();
		String s = "select * from user";
		Connection con = giveConnection();
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(s);

		while (res.next()) {
			String tempUserName = res.getString(2);
			if (tempUserName.equals(username)) {
				System.out.println("username is already exists");
				System.out.println("enter your username:");
				username = sc.next();
			}
		}

		System.out.println("enter your password:");
		String password = sc.next();
		System.out.println("Re-enter your password:");
		String confirmPassword = sc.next();
		while (!password.equals(confirmPassword)) {
			System.out.println("Passwords do not match");
			System.out.println("enter your password:");
			password = sc.next();
			System.out.println("Re-enter your  password:");
			confirmPassword = sc.next();
		}
		System.out.println("enter email:");
		String email = sc.next();

		String s1 = "insert into user values(?,?,?,?)";

		PreparedStatement pstmt = con.prepareStatement(s1);
		pstmt.setString(1, name);
		pstmt.setString(2, username);
		pstmt.setString(3, password);
		pstmt.setString(4, email);
		int rows = pstmt.executeUpdate();
		if (rows == 0) {
			System.out.println("user acoount is not created");
		} else {
			System.out.println("user account is created successfully");
		}

	}

	private static void login() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("enter username");
		String username = sc.next();
		System.out.println("enter password");
		String password = sc.next();
		Connection con = giveConnection();
		String s = "select * from user where username=?";
		PreparedStatement pstmt = con.prepareStatement(s);
		pstmt.setString(1, username);
		ResultSet res = pstmt.executeQuery();
		if (res.next()) {
			if (password.equals(res.getString(3))) {
				System.out.println("you have succesfully logged in");
				loginFeatures(username);
			} else {
				System.out.println("invalid password");
			}
		} else {
			System.out.println("invalid username");
		}

	}

	private static void loginFeatures(String username) throws SQLException {
		System.out.println("1.change password");
		System.out.println("2.change email");
		System.out.println("3.delete account");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch (choice) {
		case 1:// change password
			System.out.println("enter old password");
			String oldPassword = sc.next();
			System.out.println("enter new password");
			String newPassword = sc.next();
			Connection con = giveConnection();
			String s = "select * from user where username=?";
			PreparedStatement pstmt = con.prepareStatement(s);
			pstmt.setString(1, username);
			ResultSet res = pstmt.executeQuery();
			if (res.next()) {
				if (oldPassword.equals(res.getString(3))) {
					String s1 = "update user set password=? where username= ?";
					PreparedStatement pstmt1 = con.prepareStatement(s1);
					pstmt1.setString(1, newPassword);
					pstmt1.setString(2, username);
					pstmt1.executeUpdate();
				} else {
					System.out.println("Incorrect Password");
				}
			}
			break;
		case 2:// change email
			System.out.println("Enter the new email");
			String email = sc.next();
			Connection con1 = giveConnection();
			String s1 = "update user set email=? where username=?";
			PreparedStatement pstmt1 = con1.prepareStatement(s1);
			pstmt1.setString(1, email);
			pstmt1.setString(2, username);
			pstmt1.executeUpdate();
			System.out.println("email updated succesfully");
			break;
		case 3:// delete account
			Connection con2 = giveConnection();
			String s2 = "delete from user where username=?";
			PreparedStatement pstmt2 = con2.prepareStatement(s2);
			pstmt2.setString(2, username);
			pstmt2.executeUpdate();
			System.out.println("account is deleted succesfully");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice");
			break;
		}

	}

}
