package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.CourierOperations;

public class dm180096_CourierOperationsImpl implements CourierOperations {
	private Connection connection = DB.getInstance().getConnection();
	
	@Override
	public boolean deleteCourier(String courierUserName) {
		int cnt = 0;
		
		try (PreparedStatement pstDeleteUsers = connection
				.prepareStatement("delete from KURIR where KorisnickoIme = ?");) {
			pstDeleteUsers.setString(1, courierUserName);

			cnt = pstDeleteUsers.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0? true : false;
	}

	@Override
	public List<String> getAllCouriers() {
		List<String> couriers = new ArrayList<String>();

		try (Statement stGetCouriers = connection.createStatement();) {
			ResultSet rsCouriers = stGetCouriers.executeQuery("select KorisnickoIme from KURIR");

			while (rsCouriers.next()) {
				couriers.add(rsCouriers.getString(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return couriers;
	}

	@Override
	public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
		BigDecimal avgProfit = new BigDecimal(0);

		try (PreparedStatement pstGetCouriersProfit = connection
				.prepareStatement("select coalesce(avg(Profit), 0) from KURIR where KURIR.BrojPaketa = ?");) {
			pstGetCouriersProfit.setInt(1, numberOfDeliveries);
			ResultSet rsCouriers = pstGetCouriersProfit.executeQuery();

			if (rsCouriers.next()) {
				avgProfit = rsCouriers.getBigDecimal(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return avgProfit;
	}

	@Override
	public List<String> getCouriersWithStatus(int status) {
		List<String> couriers = new ArrayList<String>();

		try (PreparedStatement pstGetCouriers = connection.prepareStatement(
				"select KorisnickoIme from KURIR where Status = ?");) {
			pstGetCouriers.setInt(1, status);
			ResultSet rsCouriers = pstGetCouriers.executeQuery();

			while (rsCouriers.next()) {
				couriers.add(rsCouriers.getString(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return couriers;
	}

	@Override
	public boolean insertCourier(String userName, String driverLicenceNumber) {
		try (PreparedStatement pstInsertCourier = connection.prepareStatement(
				"insert into KURIR (KorisnickoIme, BrojVozackeDozvole, Status, Profit, Distanca, BrojPaketa) VALUES(?, ?, 0, 0, 0, 0)");) {
			pstInsertCourier.setString(1, userName);			
			pstInsertCourier.setString(2, driverLicenceNumber);
			pstInsertCourier.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

}
