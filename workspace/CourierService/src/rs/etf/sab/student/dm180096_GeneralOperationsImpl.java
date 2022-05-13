package rs.etf.sab.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import rs.etf.sab.operations.GeneralOperations;

public class dm180096_GeneralOperationsImpl implements GeneralOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public void eraseAll() {
		try (CallableStatement stEraseAll = connection.prepareCall("{ call spDeleteAll() }");) {
			stEraseAll.execute();
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

}
