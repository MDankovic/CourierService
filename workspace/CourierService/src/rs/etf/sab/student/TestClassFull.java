package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

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
import rs.etf.sab.tests.Pair;
import rs.etf.sab.tests.Util;

public class TestClassFull {
	AddressOperations addressOperations = new dm180096_AddressOperationsImpl(); 
	CityOperations cityOperations = new dm180096_CityOperationsImpl(); 
	CourierOperations courierOperation = new dm180096_CourierOperationsImpl();
	CourierRequestOperation courierRequestOperation = new dm180096_CourierRequestOperationImpl();
	DriveOperation driveOperation = new dm180096_DriveOperationImpl();
	GeneralOperations generalOperations = new dm180096_GeneralOperationsImpl();
	PackageOperations packageOperations = new dm180096_PackageOperationsImpl();
	StockroomOperations stockroomOperations = new dm180096_StockroomOperationsImpl();
	UserOperations userOperations = new dm180096_UserOperationsImpl();
	VehicleOperations vehicleOperations = new dm180096_VehicleOperationsImpl();

	Map<Integer, Pair<Integer, Integer>> addressesCoords = new HashMap<Integer, Pair<Integer,Integer>>();
	Map<Integer, BigDecimal> packagePrice = new HashMap<Integer, BigDecimal>();

	public void setUp() {
		this.generalOperations.eraseAll();
	}

	public void tearDown() {
		this.generalOperations.eraseAll();
	}

	public void tearUp() {
		this.generalOperations.eraseAll();
	}
	
	double euclidean(final int x1, final int y1, final int x2, final int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
	
	BigDecimal getPackagePrice(final int type, final BigDecimal weight, final double distance) {
        switch (type) {
            case 0: {
                return new BigDecimal(115.0 * distance);
            }
            case 1: {
                return new BigDecimal((175.0 + weight.doubleValue() * 100.0) * distance);
            }
            case 2: {
                return new BigDecimal((250.0 + weight.doubleValue() * 100.0) * distance);
            }
            case 3: {
                return new BigDecimal((350.0 + weight.doubleValue() * 500.0) * distance);
            }
            default: {
                return null;
            }
        }
    }
	
	double getDistance(final Pair<Integer, Integer>... addresses) {
        double distance = 0.0;
        for (int i = 1; i < addresses.length; ++i) {
            distance += euclidean((int)addresses[i - 1].getKey(), (int)addresses[i - 1].getValue(), (int)addresses[i].getKey(), (int)addresses[i].getValue());
        }
        return distance;
    }

	int insertCity(final String name, final String postalCode) {
		final int idCity = this.cityOperations.insertCity(name, postalCode);
		System.out.println((long) idCity);
		System.out.println(this.cityOperations.getAllCities().contains(idCity));
		return idCity;
	}

	int insertAddress(final String street, final int number, final int idCity, final int x, final int y) {
		final int idAddress = this.addressOperations.insertAddress(street, number, idCity, x, y);
		System.out.println((long) idAddress);
		System.out.println(this.addressOperations.getAllAddresses().contains(idAddress));
		this.addressesCoords.put(idAddress, (Pair<Integer, Integer>) new Pair((Object) x, (Object) y));
		return idAddress;
	}

	String insertUser(final String username, final String firstName, final String lastName, final String password,
			final int idAddress) {
		System.out.println(this.userOperations.insertUser(username, firstName, lastName, password, idAddress));
		System.out.println(this.userOperations.getAllUsers().contains(username));
		return username;
	}

	String insertCourier(final String username, final String firstName, final String lastName, final String password,
			final int idAddress, final String driverLicenceNumber) {
		this.insertUser(username, firstName, lastName, password, idAddress);
		System.out.println(this.courierOperation.insertCourier(username, driverLicenceNumber));
		return username;
	}

	public void insertAndParkVehicle(final String licencePlateNumber, final BigDecimal fuelConsumption,
			final BigDecimal capacity, final int fuelType, final int idStockroom) {
		System.out.println(
				this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption, capacity));
		System.out.println(this.vehicleOperations.getAllVehichles().contains(licencePlateNumber));
		System.out.println(this.vehicleOperations.parkVehicle(licencePlateNumber, idStockroom));
	}

	public int insertStockroom(final int idAddress) {
		final int stockroomId = this.stockroomOperations.insertStockroom(idAddress);
		System.out.println((long) stockroomId);
		System.out.println(this.stockroomOperations.getAllStockrooms().contains(stockroomId));
		return stockroomId;
	}

	int insertAndAcceptPackage(final int addressFrom, final int addressTo, final String userName, final int packageType,
			final BigDecimal weight) {
		final int idPackage = this.packageOperations.insertPackage(addressFrom, addressTo, userName, packageType,
				weight);
		System.out.println((long) idPackage);
		System.out.println(this.packageOperations.acceptAnOffer(idPackage));
		System.out.println(this.packageOperations.getAllPackages().contains(idPackage));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage));
		final BigDecimal price = getPackagePrice(packageType, weight, getDistance(
				new Pair[] { this.addressesCoords.get(addressFrom), this.addressesCoords.get(addressTo) }));
//		System.out.println(this.packageOperations.getPriceOfDelivery(idPackage)
//				.compareTo(price.multiply(new BigDecimal(1.05))) < 0);
//		System.out.println(this.packageOperations.getPriceOfDelivery(idPackage)
//				.compareTo(price.multiply(new BigDecimal(0.95))) > 0);
		System.out.println("PRICE MY: " + this.packageOperations.getPriceOfDelivery(idPackage));
		System.out.println("PRICE EXP: " + price);
		this.packagePrice.put(idPackage, price);
		return idPackage;
	}

	@Test
	public void publicOne() {
		final int BG = this.insertCity("Belgrade", "11000");
		final int KG = this.insertCity("Kragujevac", "550000");
		final int VA = this.insertCity("Valjevo", "14000");
		final int CA = this.insertCity("Cacak", "32000");
		System.out.println("-----------------------------------------------------------------------------");
		final int idAddressBG1 = this.insertAddress("Kraljice Natalije", 37, BG, 11, 15);
		final int idAddressBG2 = this.insertAddress("Bulevar kralja Aleksandra", 73, BG, 10, 10);
		final int idAddressBG3 = this.insertAddress("Vojvode Stepe", 39, BG, 1, -1);
		final int idAddressBG4 = this.insertAddress("Takovska", 7, BG, 11, 12);
		final int idAddressBG5 = this.insertAddress("Bulevar kralja Aleksandra", 37, BG, 12, 12);
		final int idAddressKG1 = this.insertAddress("Daniciceva", 1, KG, 4, 310);
		final int idAddressKG2 = this.insertAddress("Dure Pucara Starog", 2, KG, 11, 320);
		final int idAddressVA1 = this.insertAddress("Cika Ljubina", 8, VA, 102, 101);
		final int idAddressVA2 = this.insertAddress("Karadjordjeva", 122, VA, 104, 103);
		final int idAddressVA3 = this.insertAddress("Milovana Glisica", 45, VA, 101, 101);
		final int idAddressCA1 = this.insertAddress("Zupana Stracimira", 1, CA, 110, 309);
		final int idAddressCA2 = this.insertAddress("Bulevar Vuka Karadzica", 1, CA, 111, 315);
		System.out.println("-----------------------------------------------------------------------------");
		final int idStockroomBG = this.insertStockroom(idAddressBG1);
		final int idStockroomVA = this.insertStockroom(idAddressVA1);
		System.out.println("-----------------------------------------------------------------------------");
		this.insertAndParkVehicle("BG1675DA", new BigDecimal(6.3), new BigDecimal(1000.5), 2, idStockroomBG);
		this.insertAndParkVehicle("VA1675DA", new BigDecimal(7.3), new BigDecimal(500.5), 1, idStockroomVA);
		System.out.println("-----------------------------------------------------------------------------");
		final String username = "crno.dete";
		this.insertUser(username, "Svetislav", "Kisprdilov", "Test_123", idAddressBG1);
		System.out.println("-----------------------------------------------------------------------------");
		final String courierUsernameBG = "postarBG";
		this.insertCourier(courierUsernameBG, "Pera", "Peric", "Postar_73", idAddressBG2, "654321");
		final String courierUsernameVA = "postarVA";
		this.insertCourier(courierUsernameVA, "Pera", "Peric", "Postar_73", idAddressBG2, "123456");
		System.out.println("-----------------------------------------------------------------------------COURIER");
		final int type1 = 0;
		final BigDecimal weight1 = new BigDecimal(2);
		final int idPackage1 = this.insertAndAcceptPackage(idAddressBG2, idAddressCA1, username, type1, weight1);
		final int type2 = 1;
		final BigDecimal weight2 = new BigDecimal(4);
		final int idPackage2 = this.insertAndAcceptPackage(idAddressBG3, idAddressVA1, username, type2, weight2);
		final int type3 = 2;
		final BigDecimal weight3 = new BigDecimal(5);
		final int idPackage3 = this.insertAndAcceptPackage(idAddressBG4, idAddressKG1, username, type3, weight3);
		System.out.println("-----------------------------------------------------------------------------FIRST 3 PACKAGES ADDED");
		System.out.println((long) this.courierOperation.getCouriersWithStatus(1).size());
		this.driveOperation.planingDrive(courierUsernameBG);
		System.out.println(this.courierOperation.getCouriersWithStatus(1).contains(courierUsernameBG));
		System.out.println("-----------------------------------------------------------------------------DRIVE PLAN MADE");
		final int type4 = 3;
		final BigDecimal weight4 = new BigDecimal(2);
		final int idPackage4 = this.insertAndAcceptPackage(idAddressBG2, idAddressKG2, username, type4, weight4);
		System.out.println("-----------------------------------------------------------------------------LAST INSERT AND ACCEPT");
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2)));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3)));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2)); // OVDE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3)));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2)));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(VA).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println(this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1)));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2)));
		System.out.println((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(CA).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println("---STATUS START");
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage1)));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage2)));
		System.out.println(((long) this.packageOperations.getCurrentLocationOfPackage(idPackage3)));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(KG).size());
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage4));
		System.out.println((long) this.packageOperations.getAllUndeliveredPackages().size());
		System.out.println(this.packageOperations.getAllUndeliveredPackages().contains(idPackage4));
		System.out.println((long) this.courierOperation.getCouriersWithStatus(0).size());
		System.out.println("-----------------------------------------------------------------------------");
		final double distance = getDistance(
				new Pair[] { this.addressesCoords.get(idAddressBG1), this.addressesCoords.get(idAddressBG2),
						this.addressesCoords.get(idAddressBG3), this.addressesCoords.get(idAddressBG4),
						this.addressesCoords.get(idAddressVA1), this.addressesCoords.get(idAddressCA1),
						this.addressesCoords.get(idAddressKG1), this.addressesCoords.get(idAddressBG1) });
		System.out.println("-----------------------------------------------------------------------------DISTANCE");
		BigDecimal profit = this.packagePrice.get(idPackage1).add(this.packagePrice.get(idPackage2))
				.add(this.packagePrice.get(idPackage3));
		profit = profit.subtract(new BigDecimal(36).multiply(new BigDecimal(6.3)).multiply(new BigDecimal(distance)));
		System.out.println("EXPECTED PROFIT: " + profit);
		System.out.println("GOT: " + this.courierOperation.getAverageCourierProfit(6));
		System.out.println(
				this.courierOperation.getAverageCourierProfit(3).compareTo(profit.multiply(new BigDecimal(1.05))) < 0);
		System.out.println(
				this.courierOperation.getAverageCourierProfit(3).compareTo(profit.multiply(new BigDecimal(0.95))) > 0);
		System.out.println("-----------------------------------------------------------------------------PROFIT");
	}

	@Test
	public void publicTwo() {
		final int BG = this.insertCity("Belgrade", "11000");
		final int KG = this.insertCity("Kragujevac", "550000");
		final int VA = this.insertCity("Valjevo", "14000");
		final int CA = this.insertCity("Cacak", "32000");
		final int idAddressBG1 = this.insertAddress("Kraljice Natalije", 37, BG, 11, 15);
		final int idAddressBG2 = this.insertAddress("Bulevar kralja Aleksandra", 73, BG, 10, 10);
		final int idAddressBG3 = this.insertAddress("Vojvode Stepe", 39, BG, 1, -1);
		final int idAddressBG4 = this.insertAddress("Takovska", 7, BG, 11, 12);
		final int idAddressBG5 = this.insertAddress("Bulevar kralja Aleksandra", 37, BG, 12, 12);
		final int idAddressKG1 = this.insertAddress("Daniciceva", 1, KG, 4, 310);
		final int idAddressKG2 = this.insertAddress("Dure Pucara Starog", 2, KG, 11, 320);
		final int idAddressVA1 = this.insertAddress("Cika Ljubina", 8, VA, 102, 101);
		final int idAddressVA2 = this.insertAddress("Karadjordjeva", 122, VA, 104, 103);
		final int idAddressVA3 = this.insertAddress("Milovana Glisica", 45, VA, 101, 101);
		final int idAddressCA1 = this.insertAddress("Zupana Stracimira", 1, CA, 110, 309);
		final int idAddressCA2 = this.insertAddress("Bulevar Vuka Karadzica", 1, CA, 111, 315);
		final int idStockroomBG = this.insertStockroom(idAddressBG1);
		final int idStockroomVA = this.insertStockroom(idAddressVA1);
		this.insertAndParkVehicle("BG1675DA", new BigDecimal(6.3), new BigDecimal(1000.5), 2, idStockroomBG);
		this.insertAndParkVehicle("VA1675DA", new BigDecimal(7.3), new BigDecimal(500.5), 1, idStockroomVA);
		final String username = "crno.dete";
		this.insertUser(username, "Svetislav", "Kisprdilov", "Test_123", idAddressBG1);
		final String courierUsernameBG = "postarBG";
		this.insertCourier(courierUsernameBG, "Pera", "Peric", "Postar_73", idAddressBG2, "654321");
		final String courierUsernameVA = "postarVA";
		this.insertCourier(courierUsernameVA, "Pera", "Peric", "Postar_73", idAddressVA2, "123456");
		final int type = 1;
		final BigDecimal weight = new BigDecimal(4);
		final int idPackage1 = this.insertAndAcceptPackage(idAddressBG2, idAddressKG1, username, type, weight);
		final int idPackage2 = this.insertAndAcceptPackage(idAddressKG2, idAddressBG4, username, type, weight);
		final int idPackage3 = this.insertAndAcceptPackage(idAddressVA2, idAddressCA1, username, type, weight);
		final int idPackage4 = this.insertAndAcceptPackage(idAddressCA2, idAddressBG4, username, type, weight);
		System.out.println((long) this.courierOperation.getCouriersWithStatus(1).size());
		System.out.println("--------------------------------------------------------------------------------------");
		this.driveOperation.planingDrive(courierUsernameBG);
		this.driveOperation.planingDrive(courierUsernameVA);
		System.out.println((long) this.courierOperation.getCouriersWithStatus(1).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage1));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameVA));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameVA));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage3));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameVA));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage4));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(KG).contains(idPackage1));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameVA));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage4));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(VA).contains(idPackage4));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(CA).contains(idPackage3));
		System.out.println((long) this.courierOperation.getCouriersWithStatus(1).size());
		System.out.println("--------------------------------------------------------------------------------------");
		final int idPackage5 = this.insertAndAcceptPackage(idAddressVA2, idAddressCA1, username, type, weight);
		final int idPackage6 = this.insertAndAcceptPackage(idAddressBG3, idAddressVA3, username, type, weight);
		System.out.println((long) this.courierOperation.getCouriersWithStatus(1).size());
		System.out.println("--------------------------------------------------------------------------------------");
		this.driveOperation.planingDrive(courierUsernameBG);
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage6));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage6));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage2));
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage6));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage2));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage6));
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage5));
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage4));
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage4));
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(VA).size());
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(VA).contains(idPackage6));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getAllUndeliveredPackagesFromCity(BG).size());
		System.out.println((long) this.packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage4));
		System.out.println(this.packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage5));
		this.driveOperation.planingDrive(courierUsernameBG);
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.driveOperation.getPackagesInVehicle(courierUsernameBG).size());
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage4));
		System.out.println(this.driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage4));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getDeliveryStatus(idPackage5));
		System.out.println((long) this.driveOperation.nextStop(courierUsernameBG));
		System.out.println((long) this.packageOperations.getAllUndeliveredPackages().size());
		System.out.println((long) this.courierOperation.getCouriersWithStatus(0).size());
		System.out.println(this.courierOperation.getAverageCourierProfit(1).compareTo(new BigDecimal(0)) > 0);
		System.out.println(this.courierOperation.getAverageCourierProfit(5).compareTo(new BigDecimal(0)) > 0);
	}
	
	public static void main(String[] args) {
		TestClassFull myTestClass = new TestClassFull();
		
//		myTestClass.setUp();
//		myTestClass.publicOne();
//		myTestClass.tearDown();
		System.out.println("------------------------------------------------");
		myTestClass.setUp();
		myTestClass.publicTwo();
//		myTestClass.tearDown();
		System.out.println("------------------------------------------------");

	}
}
