package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.AddressOperations;

public class dm180096_AddressOperationsImpl implements AddressOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public int deleteAddresses(String name, int number) {
		int cnt = 0;

		try (PreparedStatement pstDeleteAddress = connection
				.prepareStatement("delete from ADRESA where Ulica = ? and Broj = ?");) {
			pstDeleteAddress.setString(1, name);
			pstDeleteAddress.setInt(2, number);
			cnt = pstDeleteAddress.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return cnt;
	}

	@Override
	public boolean deleteAdress(int idAddress) {
		int cnt = 0;
		
		try (PreparedStatement pstDeleteAddress = connection.prepareStatement("delete from ADRESA where Id = ?");) {
			pstDeleteAddress.setInt(1, idAddress);
			cnt = pstDeleteAddress.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public int deleteAllAddressesFromCity(int idCity) {
		int cnt = 0;

		try (PreparedStatement pstDeleteAddress = connection.prepareStatement("delete from ADRESA where IdGrad = ?");) {
			pstDeleteAddress.setInt(1, idCity);
			cnt = pstDeleteAddress.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return cnt;
	}

	@Override
	public List<Integer> getAllAddresses() {
		List<Integer> addresses = new ArrayList<Integer>();

		try (Statement stGetAddressess = connection.createStatement();) {
			ResultSet rsAddresses = stGetAddressess.executeQuery("select Id from ADRESA");

			while (rsAddresses.next()) {
				addresses.add(rsAddresses.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return addresses;
	}

	@Override
	public List<Integer> getAllAddressesFromCity(int idCity) {
		List<Integer> addresses = new ArrayList<Integer>();

		try (PreparedStatement pstGetAddressess = connection.prepareStatement("select Id from ADRESA where IdGrad = ?");) {
			pstGetAddressess.setInt(1, idCity);
			ResultSet rsAddresses = pstGetAddressess.executeQuery();

			while (rsAddresses.next()) {
				addresses.add(rsAddresses.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
			addresses = null;
		}

		return addresses.size() != 0 ? addresses : null;
	}

	@Override
	public int insertAddress(String street, int number, int cityId, int xCord, int yCord) {
		int id = -1;
		try (PreparedStatement pstInsertAddress = connection.prepareStatement(
				"insert into ADRESA (Ulica, Broj, IdGrad, X, Y) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
			pstInsertAddress.setString(1, street);
			pstInsertAddress.setInt(2, number);
			pstInsertAddress.setInt(3, cityId);
			pstInsertAddress.setInt(4, xCord);
			pstInsertAddress.setInt(5, yCord);
			pstInsertAddress.executeUpdate();

			ResultSet rsKeys = pstInsertAddress.getGeneratedKeys();
			if (rsKeys.next()) {
				id = rsKeys.getInt(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return id;
	}

}
