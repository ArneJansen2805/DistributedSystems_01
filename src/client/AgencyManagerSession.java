package client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rental.CarRentalCompany;
import rental.CarType;


public class AgencyManagerSession {
	Map<String, CarRentalCompany> CarRentalCompanies; 
	
	public AgencyManagerSession() {
		this.CarRentalCompanies = new HashMap<String, CarRentalCompany>();
	} 
	
	public void Register(CarRentalCompany rentalCompany) {
		this.CarRentalCompanies.put(rentalCompany.getName(), rentalCompany);
	}
	
	public void UnRegister(String rentalCompanyName) {
		this.CarRentalCompanies.remove(rentalCompanyName);
	}
	
	public Collection<CarRentalCompany> getRegisteredCompanies() {		
		return CarRentalCompanies.values();
	} 
	
	public Collection<CarType> getRegisteredCompanyCarTypes(String companyName) 
			throws IllegalArgumentException {
		CarRentalCompany company = CarRentalCompanies.get(companyName);
		
		if (company == null)
			throw new IllegalArgumentException("No registered company named: " + companyName + "found.");
		
		return company.getAllCarTypes();
	}
}
