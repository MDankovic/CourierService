package rs.etf.sab.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.DriveOperation;

public class dm180096_DriveOperationImpl implements DriveOperation {
	private Connection connection = DB.getInstance().getConnection();
	
	private boolean planPickupUtil(String courierUsername, int toDeliver) {
		boolean success = false;
		
		try(CallableStatement cstGetCar = connection.prepareCall("{? = call fGetCar(?)}");
				CallableStatement cstPlanDrive = connection.prepareCall("{? = call spPlanPickup(?, ?, ?)}");) {
			connection.setAutoCommit(false);
			
			cstGetCar.registerOutParameter(1, Types.VARCHAR);
			cstGetCar.setString(2, courierUsername);		
			cstGetCar.execute();
			
			String reg = cstGetCar.getString(1);
			
			cstPlanDrive.registerOutParameter(1, Types.INTEGER);
			cstPlanDrive.setString(2, courierUsername);
			cstPlanDrive.setString(3, reg);
			cstPlanDrive.setInt(4, toDeliver);
			cstPlanDrive.execute();
			
			success = cstPlanDrive.getInt(1) == 1 ? true : false;
			
			connection.commit();
		}catch(SQLException s) {
			try {
				connection.rollback();
				return false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			s.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return success;
	}
	
	private boolean planingDriveUtil(String courierUsername) {
		boolean success = false;
		
		try(CallableStatement cstPlanDrive = connection.prepareCall("{? = call spPlanDrive(?)}");) {
			
			cstPlanDrive.registerOutParameter(1, Types.INTEGER);
			cstPlanDrive.setString(2, courierUsername);
			cstPlanDrive.execute();
			
			success = cstPlanDrive.getInt(1) == 1 ? true : false;
			
		}catch(SQLException s) {
			s.printStackTrace();
		}

		return success;
	}

	@Override
	public List<Integer> getPackagesInVehicle(String courierUsername) {
		List<Integer> packages = new ArrayList<Integer>();

		try (PreparedStatement pstGetPackages = connection.prepareStatement(
				"select IdPaket from PLAN_VOZNJE where Kurir = ? and PrevoziSe = 1");) {
			pstGetPackages.setString(1, courierUsername);
			ResultSet rsPackages = pstGetPackages.executeQuery();

			while (rsPackages.next()) {
				packages.add(rsPackages.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return packages;
	}

	@Override
	public int nextStop(String courierUsername) {
		int res = 0;
		
		try(CallableStatement cstNextStop = connection.prepareCall("{? = call spGetNextStop(?, ?)}");
//				CallableStatement cstPlanDrive = connection.prepareCall("{call spPlanPickup2(?)}");
				CallableStatement cstProcessPackage = connection.prepareCall("{? = call spProcessPackage(?, ?)}");
				CallableStatement cstReturnToStockroom = connection.prepareCall("{call spReturnToStockroom(?)}");) {
			connection.setAutoCommit(false);
			
			cstNextStop.registerOutParameter(1, Types.INTEGER);
			cstNextStop.setString(2, courierUsername);		
			cstNextStop.registerOutParameter(3, Types.INTEGER);
			cstNextStop.execute();
			
			int nextPackage = cstNextStop.getInt(1);
			int newCity = cstNextStop.getInt(3);
			
			// if the next package is in the new city, see if there are packages you need to pick up from current city
//			if(newCity == 1) {
//				cstPlanDrive.setString(1, courierUsername);
//				cstPlanDrive.execute();
				
//				cstNextStop.registerOutParameter(1, Types.INTEGER);
//				cstNextStop.setString(2, courierUsername);		
//				cstNextStop.registerOutParameter(3, Types.INTEGER);
//				cstNextStop.execute();
//				nextPackage = cstNextStop.getInt(1);
//				System.out.println("********NEW CITY****** - " + nextPackage);
//			}
			
			// if there are more packages to process then do it, else return to stockroom
			if(nextPackage != -1) {
				cstProcessPackage.registerOutParameter(1, Types.INTEGER);
				cstProcessPackage.setString(2, courierUsername);		
				cstProcessPackage.setInt(3, nextPackage);		
				cstProcessPackage.execute();
				
				res = cstProcessPackage.getInt(1);
			} else {
				cstReturnToStockroom.setString(1, courierUsername);	
				cstReturnToStockroom.execute();
				System.out.println("************RETURN TO STOCKROOM************");
				res = -1;
			}
			
			connection.commit();
		}catch(SQLException s) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			s.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}

	@Override
	public boolean planingDrive(String courierUsername) {
//		return planPickupUtil(courierUsername, 1);
		return planingDriveUtil(courierUsername);
	}

}
