package rental_agency;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rental.ICarRentalCompany;

public class CentralNamingService implements ICentralNamingService {
	
	private ArrayList<ICarRentalCompany> companies = new ArrayList<>();
	
	@Override
	public void registerCRC(ICarRentalCompany crc) throws RemoteException {
		companies.add(crc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ICarRentalCompany> getCompanies() throws RemoteException {
		return  (ArrayList<ICarRentalCompany>) companies.clone();
	}
	
	@Override
	public ICarRentalCompany getCompany(String name) throws RemoteException {

		for (ICarRentalCompany c : companies) {
			if (c.getName() == name) {
				return c;
			}
		}
		throw new IllegalArgumentException("company doesnt exist");
	}

}
