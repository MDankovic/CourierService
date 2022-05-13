package rs.etf.sab.student;

import java.sql.Connection;
import java.util.List;

import rs.etf.sab.operations.CourierRequestOperation;

public class dm180096_CourierRequestOperationImpl implements CourierRequestOperation {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public boolean changeDriverLicenceNumberInCourierRequest(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCourierRequest(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getAllCourierRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean grantRequest(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertCourierRequest(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
