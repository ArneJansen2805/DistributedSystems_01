package rental_agency;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;

public class AgencyManagerSession {
	String agencyManagerName = "manager";
	HashMap<String, ICarRentalCompany> carRentalCompanies;
	private ICarRentalAgency agency;

	public AgencyManagerSession(String name, ICarRentalAgency agency) throws RemoteException {
		agencyManagerName = name;
		this.agency = agency;
		carRentalCompanies = new HashMap<String, ICarRentalCompany>();
		for (ICarRentalCompany c : agency.getCompanies()) {
			carRentalCompanies.put(c.getName(), c);
		}
	
	}

	public void Register(CarRentalCompany rentalCompany) {
		carRentalCompanies.put(rentalCompany.getName(), rentalCompany);
	}

	public void UnRegister(String rentalCompanyName) {
		carRentalCompanies.remove(rentalCompanyName);
	}

	public Collection<ICarRentalCompany> getRegisteredCompanies() {
		return carRentalCompanies.values();
	}

	public Collection<CarType> getRegisteredCompanyCarTypes(String companyName) {
		ICarRentalCompany company = getCarRentalCompany(companyName);
		return company.getAllCarTypes();
	}

	public int getNumberOfCompanyReservationsPerCarType(String companyName, String carType) throws Exception {
		try {
			ICarRentalCompany company = getCarRentalCompany(companyName);
			return company.getNumberOfReservationsForCarType(carType);
		} catch (Exception e) {
			throw new Exception("Error retrieving number of company reservations for company: " + companyName
					+ " and car type: " + carType + "\nException message: " + e.getMessage());
		}
	}
	
	public int getNumberOfReservationsByRenter(String clientName) {
		int numberOfReservations = 0;
		for (ICarRentalCompany crc : carRentalCompanies.values()) {
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
		
		for (ICarRentalCompany company : carRentalCompanies.values()) {
			List<String> companyBest =  company.getBestCustomers();
			bestCustomers.addAll(companyBest);			
		}
		
		// To ensure only unique best customers are returned
		return new HashSet<String>(bestCustomers);

	}
	
	
	public CarType getMostPopularCarType(String companyName, int year) {
		ICarRentalCompany company = getCarRentalCompany(companyName);
		return company.getMostPopularCarTypePerYear(year);
	}
	

	private ICarRentalCompany getCarRentalCompany(String companyName) throws IllegalArgumentException {
		if (!carRentalCompanies.containsKey(companyName))
			throw new IllegalArgumentException("No registered company named: " + companyName + "found.");

		ICarRentalCompany company = carRentalCompanies.get(companyName);
		return company;
	}
}
