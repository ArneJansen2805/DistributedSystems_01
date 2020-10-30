package rental_agency;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;

public class AgencyManagerSession {
	String agencyManagerName = "manager";
	HashMap<String, ICarRentalCompany> carRentalCompanies;
	private ICarRentalAgency agency;

	public AgencyManagerSession(String name, ICarRentalAgency agency) throws Exception {
		agencyManagerName = name;
		this.agency = agency;
		carRentalCompanies = new HashMap<String, ICarRentalCompany>();
		localizeCompanies();
	
	}

	private void localizeCompanies() throws Exception{
		

		for (ICarRentalCompany c : agency.getCompanies()) {
			carRentalCompanies.put(c.getName(), c);
	
		}
	}
	public void Register(CarRentalCompany rentalCompany) throws RemoteException {
		carRentalCompanies.put(rentalCompany.getName(), rentalCompany);
		agency.registerCompany(rentalCompany);
	}

	public void UnRegister(String rentalCompanyName) throws RemoteException {
		ICarRentalCompany c = carRentalCompanies.get(rentalCompanyName);
		carRentalCompanies.remove(c);
		agency.unregisterCompany(c);
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
		HashMap<String, Integer> totals = new HashMap<String, Integer>();
		
		for (ICarRentalCompany company : carRentalCompanies.values()) {
			HashMap<String, Integer>companyBest =  (HashMap<String, Integer>) company.getBestCustomers();
			for(String name : companyBest.keySet())		{
				if (totals.containsKey(name)) {
					totals.put(name, totals.get(name) + companyBest.get(name));
				}
				else {
					totals.put(name, companyBest.get(name));
				}
			}
		}
		
		Entry<String, Integer> max = null;
		for (Entry<String, Integer> val : totals.entrySet()) {
			if(max == null || val.getValue().compareTo(max.getValue()) >0 ) {
				max = val;
			}
			if (val.getValue().compareTo(max.getValue()) == 0) {
				bestCustomers.add(val.getKey());
			}
		
			
		}
		bestCustomers.add(max.getKey());
		
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
