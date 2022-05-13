package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.etf.sab.operations.UserOperations;

public class dm180096_UserOperationsImpl implements UserOperations {
	private Connection connection = DB.getInstance().getConnection();
	
	private boolean checkFirstName(String firstName) {
		return Character.isUpperCase(firstName.charAt(0));
	}
	
	private boolean checkLastName(String lastName) {
		return Character.isUpperCase(lastName.charAt(0));
	}

	private boolean checkPassword(String password) {
		Pattern smallLetter = Pattern.compile("[a-z]");
		Pattern bigLetter = Pattern.compile("[A-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        
        Matcher hasSmallLetter = smallLetter.matcher(password);
        Matcher hasBigLetter = bigLetter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);
        
		return hasSmallLetter.find() && hasBigLetter.find() && hasDigit.find() && hasSpecial.find() && password.length() >= 8;
	}
	
	private int userExists(String userName) {
		int id = -1;
		
		try (PreparedStatement pstGetUserByUsername = connection
				.prepareStatement("select Id from KORISNIK where KorisnickoIme = ?");) {
			pstGetUserByUsername.setString(1, userName);
			ResultSet rsUsers = pstGetUserByUsername.executeQuery();
			
			if(rsUsers.next()) {
				id = rsUsers.getInt(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
			return id;
		}
		
		return id;
	}

	@Override
	public boolean declareAdmin(String userName) {
		if(userExists(userName) == -1) {
			return false;
		}
		
		try (PreparedStatement pstDeclareAdmin = connection
				.prepareStatement("insert into ADMINISTRATOR (Id) values ((select Id from KORISNIK where KorisnickoIme = ?))");) {
			pstDeclareAdmin.setString(1, userName);
			pstDeclareAdmin.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public int deleteUsers(String... userNames) {
		int cnt = 0;
		try (PreparedStatement pstDeleteUsers = connection
				.prepareStatement("delete from KORISNIK where KorisnickoIme = ?");) {
			for (String userName : userNames) {
				pstDeleteUsers.setString(1, userName);
				try {
					cnt += pstDeleteUsers.executeUpdate();
				} catch (SQLException s) {
					s.printStackTrace();
				}
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return cnt;
	}

	@Override
	public List<String> getAllUsers() {
		List<String> users = new ArrayList<String>();

		try (Statement stGetUsers = connection.createStatement();) {
			ResultSet rsUsers = stGetUsers.executeQuery("select KorisnickoIme from KORISNIK");

			while (rsUsers.next()) {
				users.add(rsUsers.getString(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return users;
	}

	@Override
	public int getSentPackages(String... arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean insertUser(String userName, String firstName, String lastName, String password, int idAddress) {
		if(!checkFirstName(firstName) || !checkLastName(lastName) || !checkPassword(password)) {
			return false;
		}
		
		try (PreparedStatement pstInsertUser = connection.prepareStatement(
				"insert into KORISNIK (KorisnickoIme, Ime, Prezime, Sifra, IdAdresa) VALUES(?, ?, ?, ?, ?)");) {
			pstInsertUser.setString(1, userName);
			pstInsertUser.setString(2, firstName);
			pstInsertUser.setString(3, lastName);
			pstInsertUser.setString(4, password);
			pstInsertUser.setInt(5, idAddress);
			pstInsertUser.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

}
