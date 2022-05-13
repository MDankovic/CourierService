package rs.etf.sab.student;

import java.util.Random;

import org.junit.Assert;

import rs.etf.sab.operations.AddressOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.DriveOperation;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.StockroomOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.tests.TestHandler;

public class TestClass {
	AddressOperations addressOperations = new dm180096_AddressOperationsImpl(); // Change to your
	// implementation.
	CityOperations cityOperations = new dm180096_CityOperationsImpl(); // Do it for all classes.
	CourierOperations courierOperations = new dm180096_CourierOperationsImpl(); // e.g. = new
	// MyDistrictOperations();
	CourierRequestOperation courierRequestOperation = new dm180096_CourierRequestOperationImpl();
	DriveOperation driveOperation = new dm180096_DriveOperationImpl();
	GeneralOperations generalOperations = new dm180096_GeneralOperationsImpl();
	PackageOperations packageOperations = new dm180096_PackageOperationsImpl();
	StockroomOperations stockroomOperations = new dm180096_StockroomOperationsImpl();
	UserOperations userOperations = new dm180096_UserOperationsImpl();
	VehicleOperations vehicleOperations = new dm180096_VehicleOperationsImpl();
	TestHandler testHandler;
	
	public void setUp() {
        this.generalOperations.eraseAll();
    }
    
    public void tearDown() {
        this.generalOperations.eraseAll();
    }

	int insertAddress() {
		final String street = "Bulevar kralja Aleksandra";
		final int number = 73;
		final int idCity = this.cityOperations.insertCity("Belgrade", "11000");
//		System.out.println((long) idCity);
		final int idAddress = this.addressOperations.insertAddress(street, number, idCity, 10, 10);
//		System.out.println((long) idAddress);
//		System.out.println((long) this.addressOperations.getAllAddresses().size());
		return idAddress;
	}

	public void insertUser_Good() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void insertUser_UniqueUsername() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void insertUser_BadFirstname() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void insertUser_BadLastName() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void insertUser_BadAddress() {
		final Random random = new Random();
		final int idAddress = random.nextInt();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void insertUser_BadPassword() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password1 = "test_123";
		final String password2 = "Test123";
		final String password3 = "Test_test";
		final String password4 = "TEST_123";
		final String password5 = "Test_1";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password1, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password2, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password3, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password4, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password5, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		System.out.println((long) this.userOperations.getAllUsers().size());
	}

	public void declareAdmin() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.declareAdmin(username));
	}

	public void declareAdmin_AlreadyAdmin() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.declareAdmin(username));
		System.out.println(this.userOperations.declareAdmin(username));
	}

	public void declareAdmin_NoSuchUser() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		final String username2 = "crno.dete.2";
		System.out.println(this.userOperations.declareAdmin(username2));
	}

	public void getSentPackages_userExisting() {
		final int idAddress = this.insertAddress();
		final String username = "crno.dete";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println((long) this.userOperations.getSentPackages(new String[] { username }));
	}

	public void getSentPackages_userNotExisting() {
		final String username = "crno.dete";
		System.out.println((long) this.userOperations.getSentPackages(new String[] { username }));
	}

	public void deleteUsers() {
		final int idAddress = this.insertAddress();
		final String username1 = "crno.dete1";
		final String username2 = "crno.dete2";
		final String username3 = "crno.dete3";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username1, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.insertUser(username2, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.insertUser(username3, firstName, lastName, password, idAddress));
		System.out.println((long) this.userOperations.deleteUsers(new String[] { username1, username2 }));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.getAllUsers().contains(username1));
		System.out.println(this.userOperations.getAllUsers().contains(username2));
		System.out.println(this.userOperations.getAllUsers().contains(username3));
	}

	public void getAllUsers() {
		final int idAddress = this.insertAddress();
		final String username1 = "crno.dete1";
		final String username2 = "crno.dete2";
		final String firstName = "Svetislav";
		final String lastName = "Kisprdilov";
		final String password = "Test_123";
		System.out.println(this.userOperations.insertUser(username1, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.insertUser(username2, firstName, lastName, password, idAddress));
		System.out.println((long) this.userOperations.getAllUsers().size());
		System.out.println(this.userOperations.getAllUsers().contains(username1));
		System.out.println(this.userOperations.getAllUsers().contains(username2));
	}

	public static void main(String[] args) {
		TestClass myTestClass = new TestClass();
		
		myTestClass.setUp();
		myTestClass.insertUser_Good();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.insertUser_UniqueUsername();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.insertUser_BadFirstname();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.insertUser_BadLastName();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.insertUser_BadAddress();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.insertUser_BadPassword();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.declareAdmin();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.declareAdmin_AlreadyAdmin();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.declareAdmin_NoSuchUser();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.getSentPackages_userExisting();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.getSentPackages_userNotExisting();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.deleteUsers();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.getAllUsers();
		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.tearDown();

	}
}
