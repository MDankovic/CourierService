package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.CityOperations;

public class dm180096_CityOperationsImpl implements CityOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public int deleteCity(String... cityNames) {
		int cnt = 0;
		try (PreparedStatement pstDeleteCities = connection.prepareStatement("delete from GRAD where Naziv = ?");) {
			for (String name : cityNames) {
				pstDeleteCities.setString(1, name);
				try {
					cnt += pstDeleteCities.executeUpdate();
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
	public boolean deleteCity(int id) {
		int cnt = 0;
		
		try (PreparedStatement pstDeleteCity = connection.prepareStatement("delete from GRAD where Id = ?");) {
			pstDeleteCity.setInt(1, id);
			cnt = pstDeleteCity.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}
		
		return cnt != 0 ? true : false;
	}

	@Override
	public List<Integer> getAllCities() {
		List<Integer> cities = new ArrayList<Integer>();

		try (Statement stGetCities = connection.createStatement();) {
			ResultSet rsCities = stGetCities.executeQuery("select Id from GRAD");

			while (rsCities.next()) {
				cities.add(rsCities.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return cities;
	}

	@Override
	public int insertCity(String name, String postalCode) {
		int id = -1;

		try (PreparedStatement pstInsertCity = connection.prepareStatement(
				"insert into GRAD (Naziv, PostanskiBroj) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);) {
			pstInsertCity.setString(1, name);
			pstInsertCity.setString(2, postalCode);
			pstInsertCity.executeUpdate();

			ResultSet rsKeys = pstInsertCity.getGeneratedKeys();
			if (rsKeys.next()) {
				id = rsKeys.getInt(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return id;
	}

}
