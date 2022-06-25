package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.StockroomOperations;

public class dm180096_StockroomOperationsImpl implements StockroomOperations {
	private Connection connection = DB.getInstance().getConnection();
	
	private boolean cityHasStockroom(int idCity) {
		try (PreparedStatement pstGetStockrooms = connection.prepareStatement(
				"select IdAdresa from ADRESA JOIN MAGACIN on ADRESA.Id = MAGACIN.IdAdresa where IdGrad = ?");) {
			pstGetStockrooms.setInt(1, idCity);

			ResultSet rsStockrooms = pstGetStockrooms.executeQuery();

			if (rsStockrooms.next()) {
				return true;
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
		
		return false;
	}
	
	private int getCityForAddress(int idAddress) {
		int idCity = -1;
		try (PreparedStatement pstGetStockrooms = connection.prepareStatement(
				"select IdGrad from ADRESA where Id = ?");) {
			pstGetStockrooms.setInt(1, idAddress);

			ResultSet rsStockrooms = pstGetStockrooms.executeQuery();

			if (rsStockrooms.next()) {
				idCity = rsStockrooms.getInt(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
		
		return idCity;
	}
	
	@Override
	public boolean deleteStockroom(int idStockroom) {
		int cnt = 0;

		try (PreparedStatement pstDeleteStockroom = connection
				.prepareStatement("delete from MAGACIN where IdAdresa = ?");) {
			pstDeleteStockroom.setInt(1, idStockroom);
			cnt = pstDeleteStockroom.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return cnt != 0 ? true : false;
	}

	@Override
	public int deleteStockroomFromCity(int idCity) {
		int id = -1;

		try (PreparedStatement pstGetStockrooms = connection.prepareStatement(
				"select IdAdresa from MAGACIN where IdAdresa in (select Id from ADRESA where IdGrad = ?)");) {
			pstGetStockrooms.setInt(1, idCity);

			ResultSet rsStockrooms = pstGetStockrooms.executeQuery();

			if (rsStockrooms.next()) {
				id = rsStockrooms.getInt(1);
				if (this.deleteStockroom(id)) {
					return id;
				}
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return -1;
	}

	@Override
	public List<Integer> getAllStockrooms() {
		List<Integer> stockrooms = new ArrayList<Integer>();

		try (Statement stGetStockrooms = connection.createStatement();) {
			ResultSet rsStockrooms = stGetStockrooms.executeQuery("select IdAdresa from MAGACIN");

			while (rsStockrooms.next()) {
				stockrooms.add(rsStockrooms.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return stockrooms;
	}

	@Override
	public int insertStockroom(int address) {
		try (PreparedStatement pstInsertStockroom = connection.prepareStatement("insert into MAGACIN (IdAdresa) VALUES(?)");) {
			pstInsertStockroom.setInt(1, address);
			pstInsertStockroom.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return -1;
		}

		return address;
	}

}
