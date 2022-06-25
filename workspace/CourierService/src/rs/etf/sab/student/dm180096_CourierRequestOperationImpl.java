package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.CourierRequestOperation;

public class dm180096_CourierRequestOperationImpl implements CourierRequestOperation {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public boolean changeDriverLicenceNumberInCourierRequest(String userName, String licencePlateNumber) {
		int cnt = 0;

		try (PreparedStatement pstChangeLicence = connection
				.prepareStatement("update ZAHTEV set BrojVozackeDozvole = ? where KorisnickoIme = ?");) {
			pstChangeLicence.setString(1, licencePlateNumber);
			pstChangeLicence.setString(2, userName);
			cnt = pstChangeLicence.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public boolean deleteCourierRequest(String userName) {
		int cnt = 0;

		try (PreparedStatement pstDeleteRequest = connection
				.prepareStatement("delete from ZAHTEV where KorisnickoIme = ?");) {
			pstDeleteRequest.setString(1, userName);
			cnt = pstDeleteRequest.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public List<String> getAllCourierRequests() {
		List<String> requests = new ArrayList<String>();

		try (Statement stGetRequests = connection.createStatement();) {
			ResultSet rsRequests = stGetRequests.executeQuery("select KorisnickoIme from ZAHTEV");

			while (rsRequests.next()) {
				requests.add(rsRequests.getString(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return requests;
	}

	@Override
	public boolean grantRequest(String username) {
		try (PreparedStatement pstGetDriversLicence = connection
				.prepareStatement("select BrojVozackeDozvole from ZAHTEV where KorisnickoIme= ?");) {
			dm180096_CourierOperationsImpl courierDBOperation = new dm180096_CourierOperationsImpl();
			connection.setAutoCommit(false);
			pstGetDriversLicence.setString(1, username);
			ResultSet rsDriverNumber = pstGetDriversLicence.executeQuery();
			String driverLicenceNumber;

			if (rsDriverNumber.next()) {
				driverLicenceNumber = rsDriverNumber.getString(1);
			} else {
				throw new SQLException();
			}

			this.deleteCourierRequest(username);
			courierDBOperation.insertCourier(username, driverLicenceNumber);
			
			connection.commit();
			return true;
		} catch (SQLException s) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public boolean insertCourierRequest(String userName, String driverLicenceNumber) {
		try (PreparedStatement pstInsertRequest = connection
				.prepareStatement("insert into ZAHTEV (KorisnickoIme, BrojVozackeDozvole) VALUES(?, ?)");) {
			pstInsertRequest.setString(1, userName);
			pstInsertRequest.setString(2, driverLicenceNumber);
			pstInsertRequest.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

}
