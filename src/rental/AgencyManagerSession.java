package rental;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AgencyManagerSession {
	String agencyManagerName = "";
	Map<String, CarRentalCompany> carRentalCompanies;

	public AgencyManagerSession(String name) {
		agencyManagerName = name;
		carRentalCompanies = new HashMap<String, CarRentalCompany>();
	}

	public void Register(CarRentalCompany rentalCompany) {
		carRentalCompanies.put(rentalCompany.getName(), rentalCompany);
	}

	public void UnRegister(String rentalCompanyName) {
		carRentalCompanies.remove(rentalCompanyName);
	}

	public Collection<CarRentalCompany> getRegisteredCompanies() {
		return carRentalCompanies.values();
	}

	public Collection<CarType> getRegisteredCompanyCarTypes(String companyName) {
		CarRentalCompany company = getCarRentalCompany(companyName);
		return company.getAllCarTypes();
	}

	public int getNumberOfCompanyReservationsPerCarType(String companyName, String carType) throws Exception {
		try {
			CarRentalCompany company = getCarRentalCompany(companyName);
			return company.getNumberOfReservationsForCarType(carType);
		} catch (Exception e) {
			throw new Exception("Error retrieving number of company reservations for company: " + companyName
					+ " and car type: " + carType + "\nException message: " + e.getMessage());
		}
	}
	
	public int getNumberOfReservationsByRenter(String clientName) {
		int numberOfReservations = 0;
		for (CarRentalCompany crc : carRentalCompanies.values()) {
			try {
				numberOfReservations += crc.getReservationsByRenter(clientName).size();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return numberOfReservations;
	} 

	public Set<String> getBestClients() {
		List<String> bestCustomers = new ArrayList<String>();
		
		for (CarRentalCompany company : carRentalCompanies.values()) {
			List<String> companyBest =  company.getBestCustomers();
			bestCustomers.addAll(companyBest);			
		}
		
		// To ensure only unique best customers are returned
		return new HashSet<String>(bestCustomers);

	}
	
	
	public CarType getMostPopularCarType(String companyName, int year) {
		CarRentalCompany company = getCarRentalCompany(companyName);
		return company.getMostPopularCarTypePerYear(year);
	}
	

	private CarRentalCompany getCarRentalCompany(String companyName) throws IllegalArgumentException {
		if (!carRentalCompanies.containsKey(companyName))
			throw new IllegalArgumentException("No registered company named: " + companyName + "found.");

		CarRentalCompany company = carRentalCompanies.get(companyName);
		return company;
	}
}
