package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import rs.etf.sab.operations.PackageOperations;

public class dm180096_PackageOperationsImpl implements PackageOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public boolean acceptAnOffer(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeType(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeWeight(int arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePackage(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date getAcceptanceTime(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAllPackages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAllPackagesCurrentlyAtCity(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAllPackagesWithSpecificType(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAllUndeliveredPackages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getAllUndeliveredPackagesFromCity(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentLocationOfPackage(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDeliveryStatus(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getPriceOfDelivery(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertPackage(int arg0, int arg1, String arg2, int arg3, BigDecimal arg4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean rejectAnOffer(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
