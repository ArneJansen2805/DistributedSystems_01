package rental_agency;

import java.rmi.Remote;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rental.AgencyReservationSession;
import rental.ICarRentalCompany;


public class CentralNamingService implements Remote {
	private Map<String, String> clientIdLookup = new HashMap<String, String>();
	private Map<String, AgencyReservationSession> clientSessionLookup = new HashMap<String, AgencyReservationSession>();
	private ICarRentalCompany rentalCompany;
	
	public AgencyReservationSession getReservationSessionForClient(String clientName) {
		if (!clientIdLookup.containsKey(clientName)) {
			throw new IllegalArgumentException("No company with name: " + clientName + "exists.");
		}
		
		String clientID = clientIdLookup.get(clientName);
		return clientSessionLookup.get(clientName);
	}
	
	public void addClient(String clientName, AgencyReservationSession client) {		
		String clientId = createClientUUID();
		clientSessionLookup.put(clientId, new AgencyReservationSession(clientId));
	}
	
	public synchronized String createClientUUID() {
		return UUID.randomUUID().toString();
	}

	
	public void getAvailableCarTypes(Date start, Date end) {
	}
}
