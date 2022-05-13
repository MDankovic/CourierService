package rs.etf.sab.student;

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
import rs.etf.sab.tests.TestRunner;

public class StudentMain {

	public static void main(String[] args) {
		AddressOperations addressOperations = new dm180096_AddressOperationsImpl();
		CityOperations cityOperations = new dm180096_CityOperationsImpl();
		CourierOperations courierOperations = new dm180096_CourierOperationsImpl();
		CourierRequestOperation courierRequestOperation = new dm180096_CourierRequestOperationImpl();
		DriveOperation driveOperation = new dm180096_DriveOperationImpl();
		GeneralOperations generalOperations = new dm180096_GeneralOperationsImpl();
		PackageOperations packageOperations = new dm180096_PackageOperationsImpl();
		StockroomOperations stockroomOperations = new dm180096_StockroomOperationsImpl();
		UserOperations userOperations = new dm180096_UserOperationsImpl();
		VehicleOperations vehicleOperations = new dm180096_VehicleOperationsImpl();

		TestHandler.createInstance(
			addressOperations, 
			cityOperations, 
			courierOperations, 
			courierRequestOperation,
			driveOperation, 
			generalOperations, 
			packageOperations, 
			stockroomOperations, 
			userOperations,
			vehicleOperations
		);

		TestRunner.runTests();
	}
}

//System.out.println(cityOperations.insertCity("Nis", "18000"));
//System.out.println(cityOperations.insertCity("Beograd", "11000"));
//System.out.println(cityOperations.insertCity("Novi Sad", "21000"));
//System.out.println(cityOperations.insertCity("Leskovac", "16000"));

//List<Integer> cities = cityOperations.getAllCities();
//for (Integer city : cities) {
//	System.out.print("City: ");
//	System.out.println(city);
//}
//
//cityOperations.deleteCity(5);
//cityOperations.deleteCity("Novi Sad");
//
//cities = cityOperations.getAllCities();
//for (Integer city : cities) {
//	System.out.print("City: ");
//	System.out.println(city);
//}

//final String name1 = "Tokyo";
//final String name2 = "Beijing";
//final String postalCode = "100";
//final String postalCode2 = "1001";
//final int rowIdValid = cityOperations.insertCity(name1, postalCode);
//final int rowIdInvalid = cityOperations.insertCity(name1, postalCode);
//System.out.println(rowIdInvalid);
//System.out.println(cityOperations.getAllCities().size());
//System.out.println(cityOperations.getAllCities().contains(rowIdValid));
//
//final String streetOne = "Bulevar kralja Aleksandra";
//final int numberOne = 73;
//final int idCity = cityOperations.insertCity("Belgrade", "11000");
//System.out.println((long)idCity);
//final int idAddressOne = addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
//final int idAddressTwo = addressOperations.insertAddress(streetOne, numberOne, idCity, 100, 100);
//System.out.println(idAddressOne);
//System.out.println(idAddressTwo);
//System.out.println("-------------------------------");
//System.out.println((long)addressOperations.getAllAddresses().size());
//System.out.println((long)addressOperations.deleteAddresses(streetOne, numberOne));
//System.out.println((long)addressOperations.getAllAddresses().size());

//final String street = "Bulevar kralja Aleksandra";
//final int number = 73;
//final int idCity = cityOperations.insertCity("Belgrade", "11000");
//final int idAddress = addressOperations.insertAddress(street, number, idCity, 10, 10);
//
//final String street1 = "Vojvode Stepe";
//final int number1 = 73;
//final int idCity1 = cityOperations.insertCity("Nis", "700000");
//final int idAddress1 = addressOperations.insertAddress(street, number, idCity, 100, 100);
//
//
//int rowId = stockroomOperations.insertStockroom(idAddress);
//System.out.println((long) rowId);
//System.out.println((long) stockroomOperations.getAllStockrooms().size());
//System.out.println(stockroomOperations.getAllStockrooms().contains(rowId));
//System.out.println(stockroomOperations.getAllStockrooms().size());
//
//System.out.println("-------------------------------");
//for (int id : stockroomOperations.getAllStockrooms()) {
//	System.out.println(id);
//}