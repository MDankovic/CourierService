package rs.etf.sab.student;

import java.sql.Connection;
import java.util.List;

import rs.etf.sab.operations.DriveOperation;

public class dm180096_DriveOperationImpl implements DriveOperation {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public List<Integer> getPackagesInVehicle(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nextStop(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean planingDrive(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
