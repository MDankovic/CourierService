package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.PackageOperations;

public class dm180096_PackageOperationsImpl implements PackageOperations {
	private Connection connection = DB.getInstance().getConnection();

	@Override
	public boolean acceptAnOffer(int packageId) {
		try (PreparedStatement pstAcceptAnOffer = connection.prepareStatement("update PAKET set VremePrihvatanja = GETDATE(), Status = 1  where Id = ?");) {
			pstAcceptAnOffer.setInt(1, packageId);
			pstAcceptAnOffer.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean changeType(int packageId, int newType) {
		try (PreparedStatement pstChangeType = connection.prepareStatement("update PAKET set Tip = ? where Id = ?");) {
			pstChangeType.setInt(1, newType);
			pstChangeType.setInt(2, packageId);
			pstChangeType.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean changeWeight(int packageId, BigDecimal newWeight) {
		try (PreparedStatement pstChangeWeight = connection.prepareStatement("update PAKET set Tezina = ? where Id = ?");) {
			pstChangeWeight.setBigDecimal(1, newWeight);
			pstChangeWeight.setInt(2, packageId);
			pstChangeWeight.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean deletePackage(int packageId) {
		try (PreparedStatement pstDeletePackage = connection.prepareStatement("delete from PAKET where Id = ?");) {
			pstDeletePackage.setInt(1, packageId);
			pstDeletePackage.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public Date getAcceptanceTime(int packageId) {
		Date acceptanceTime = null;
		
		try (PreparedStatement pstGetAcceptanceTime = connection.prepareStatement("select VremePrihvatanja from PAKET where Id = ?");) {
			pstGetAcceptanceTime.setInt(1, packageId);
			ResultSet rsTime = pstGetAcceptanceTime.executeQuery();
			
			if(rsTime.next()) {
				acceptanceTime = rsTime.getDate(1);
			}
			
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return acceptanceTime;
	}

	@Override
	public List<Integer> getAllPackages() {
		List<Integer> packages = new ArrayList<Integer>();

		try (Statement stGetPackages = connection.createStatement();) {
			ResultSet rsPackages = stGetPackages.executeQuery("select Id from PAKET");

			while (rsPackages.next()) {
				packages.add(rsPackages.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return packages;
	}

	@Override
	public List<Integer> getAllPackagesCurrentlyAtCity(int cityId) {
		List<Integer> packages = new ArrayList<Integer>();

		try (PreparedStatement pstGetPackages = connection
				.prepareStatement("select Id from PAKET P1 where (select IdGrad from ADRESA where Id = (select TrenutnaLokacija from PAKET P2 where P1.Id = P2.Id)) = ?");) {
			pstGetPackages.setInt(1, cityId);
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
	public List<Integer> getAllPackagesWithSpecificType(int type) {
		List<Integer> packages = new ArrayList<Integer>();

		try (PreparedStatement pstGetPackages = connection
				.prepareStatement("select Id from PAKET where Tip = ?");) {
			pstGetPackages.setInt(1, type);
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
	public List<Integer> getAllUndeliveredPackages() {
		List<Integer> packages = new ArrayList<Integer>();

		try (Statement stGetPackages = connection.createStatement();) {
			ResultSet rsPackages = stGetPackages.executeQuery("select Id from PAKET where Status != 3");

			while (rsPackages.next()) {
				packages.add(rsPackages.getInt(1));
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return packages;
	}

	@Override
	public List<Integer> getAllUndeliveredPackagesFromCity(int cityId) {
		List<Integer> packages = new ArrayList<Integer>();

		try (PreparedStatement pstGetPackages = connection
				.prepareStatement("select Id from PAKET where (select IdGrad from ADRESA where Id = Paket.Id) = ? and Status != 3");) {
			pstGetPackages.setInt(1, cityId);
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
	public int getCurrentLocationOfPackage(int packageId) {
		int location = -1;
		
		try (PreparedStatement pstGetDeliveryStatus = connection.prepareStatement("select coalesce(TrenutnaLokacija, -1) from PAKET where Id = ?");) {
			pstGetDeliveryStatus.setInt(1, packageId);
			ResultSet rsStatus = pstGetDeliveryStatus.executeQuery();
			
			if(rsStatus.next()) {
				location = rsStatus.getInt(1);
			}
			
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return location;
	}

	@Override
	public int getDeliveryStatus(int packageId) {
		int status = -1;
		
		try (PreparedStatement pstGetDeliveryStatus = connection.prepareStatement("select Status from PAKET where Id = ?");) {
			pstGetDeliveryStatus.setInt(1, packageId);
			ResultSet rsStatus = pstGetDeliveryStatus.executeQuery();
			
			if(rsStatus.next()) {
				status = rsStatus.getInt(1);
			}
			
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return status;
	}

	@Override
	public BigDecimal getPriceOfDelivery(int packageId) {
		BigDecimal price = new BigDecimal(-1);
		
		try (PreparedStatement pstGetDeliveryPrice = connection.prepareStatement("select Cena from PAKET where Id = ?");) {
			pstGetDeliveryPrice.setInt(1, packageId);
			ResultSet rsPrice = pstGetDeliveryPrice.executeQuery();

			if(rsPrice.next()) {
				price = rsPrice.getBigDecimal(1);
			}
			
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return price;
	}

	@Override
	public int insertPackage(int addressFrom, int addressTo, String userName, int packageType, BigDecimal weight) {
		int id = -1;

		try (PreparedStatement pstInsertPackage = connection.prepareStatement(
				"insert into PAKET (AdresaOd, AdresaDo, KorisnickoIme, Tip, Tezina, Status, VremeKreiranja) VALUES(?, ?, ?, ?, ?, 0, GETDATE())", Statement.RETURN_GENERATED_KEYS);) {
			pstInsertPackage.setInt(1, addressFrom);
			pstInsertPackage.setInt(2, addressTo);
			pstInsertPackage.setString(3, userName);
			pstInsertPackage.setInt(4, packageType);
			pstInsertPackage.setBigDecimal(5, weight);
			pstInsertPackage.executeUpdate();

			ResultSet rsKeys = pstInsertPackage.getGeneratedKeys();
			if (rsKeys.next()) {
				id = rsKeys.getInt(1);
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return id;
	}

	@Override
	public boolean rejectAnOffer(int packageId) {
		try (PreparedStatement pstAcceptAnOffer = connection.prepareStatement("update PAKET set Status = 4 where Id = ?");) {
			pstAcceptAnOffer.setInt(1, packageId);
			pstAcceptAnOffer.executeUpdate();
		} catch (SQLException s) {
			s.printStackTrace();
			return false;
		}

		return true;
	}

}
