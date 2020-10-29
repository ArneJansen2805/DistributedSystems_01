package rental_agency;

import java.util.HashMap;
import java.util.Map;


public class SessionManager {
	
	private Map<String, AgencyReservationSession> clientSessionLookup = new HashMap<String, AgencyReservationSession>();
	private rentalAgency agency;
	
	public AgencyReservationSession create(String name, rentalAgency agency) {
		this.agency = agency;
		AgencyReservationSession session = new AgencyReservationSession(name, agency);
		addSession(name, session);
		return session;
		
	}
	
	public AgencyManagerSession createManager(String name) {
		return new AgencyManagerSession(name);
	}
	
	private void addSession(String name, AgencyReservationSession session) {
		 clientSessionLookup.put(name, session);
	}
	
	public AgencyReservationSession get(String name) {
		return clientSessionLookup.get(name);
		
	}
	
	private void removeSession(String name) {
		AgencyReservationSession session = get(name);
		session.clear();
		clientSessionLookup.remove(name);
		
	}
	
	public rentalAgency agency() {
		return agency;
	}
	
	
}
