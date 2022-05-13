package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.VehicleOperations;

public class dm180096_VehicleOperationsImpl implements VehicleOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public boolean changeCapacity(String licensePlateNumber, BigDecimal capacity) {
		int cnt = 0;

		try (PreparedStatement pstChangeCapacity = connection.prepareStatement(
				"update VOZILO set Nosivost = ? where RegistracioniBroj = ? and IdAdresa is not NULL");) {
			pstChangeCapacity.setBigDecimal(1, capacity);
			pstChangeCapacity.setString(2, licensePlateNumber);
			cnt = pstChangeCapacity.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
		int cnt = 0;

		try (PreparedStatement pstChangeConsumption = connection.prepareStatement(
				"update VOZILO set Potrosnja = ? where RegistracioniBroj = ? and IdAdresa is not NULL");) {
			pstChangeConsumption.setBigDecimal(1, fuelConsumption);
			pstChangeConsumption.setString(2, licensePlateNumber);
			cnt = pstChangeConsumption.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public boolean changeFuelType(String licensePlateNumber, int fuelType) {
		int cnt = 0;

		try (PreparedStatement pstChangeFuelType = connection.prepareStatement(
				"update VOZILO set TipGoriva = ? where RegistracioniBroj = ? and IdAdresa is not NULL");) {
			pstChangeFuelType.setInt(1, fuelType);
			pstChangeFuelType.setString(2, licensePlateNumber);
			cnt = pstChangeFuelType.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public int deleteVehicles(String... licencePlateNumbers) {
		int cnt = 0;
		try (PreparedStatement pstDeleteVehicles = connection
				.prepareStatement("delete from VOZILO where RegistracioniBroj = ?");) {
			for (String licencePlateNumber : licencePlateNumbers) {
				pstDeleteVehicles.setString(1, licencePlateNumber);
				try {
					cnt += pstDeleteVehicles.executeUpdate();
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
	public List<String> getAllVehichles() {
		List<String> vehicles = new ArrayList<String>();

		try (Statement stGetVehicles = connection.createStatement();) {
			ResultSet rsCities = stGetVehicles.executeQuery("select RegistracioniBroj from VOZILO");

			while (rsCities.next()) {
				vehicles.add(rsCities.getString(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return vehicles;
	}

	@Override
	public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumtion,
			BigDecimal capacity) {
		try (PreparedStatement pstInsertVehicle = connection.prepareStatement(
				"insert into VOZILO (RegistracioniBroj, TipGoriva, Potrosnja, Nosivost) VALUES(?, ?, ?, ?)");) {
			pstInsertVehicle.setString(1, licencePlateNumber);
			pstInsertVehicle.setInt(2, fuelType);
			pstInsertVehicle.setBigDecimal(3, fuelConsumtion);
			pstInsertVehicle.setBigDecimal(4, capacity);
			pstInsertVehicle.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean parkVehicle(String licencePlateNumbers, int idStockroom) {
		int cnt = 0;
		
		try (PreparedStatement pstParkVehicle = connection
				.prepareStatement("update VOZILO set IdAdresa = ? where RegistracioniBroj = ?");) {
			pstParkVehicle.setInt(1, idStockroom);
			pstParkVehicle.setString(2, licencePlateNumbers);
			cnt = pstParkVehicle.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

}
