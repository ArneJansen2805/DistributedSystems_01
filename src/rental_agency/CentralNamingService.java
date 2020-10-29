package rental_agency;

import java.rmi.Remote;
import java.util.ArrayList;

import rental.ICarRentalCompany;

public class CentralNamingService implements Remote {
	
	private ArrayList<ICarRentalCompany> companies = new ArrayList<>();
	
	public void registerCRC(ICarRentalCompany crc) {
		companies.add(crc);
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getCompanies() {
		return  (ArrayList) companies.clone();
	}
	
	public ICarRentalCompany getCompany(String name) {

		for (ICarRentalCompany c : companies) {
			if (c.getName() == name) {
				return c;
			}
		}
		throw new IllegalArgumentException("company doesnt exist");
	}

}
