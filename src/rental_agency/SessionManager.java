package rental_agency;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;


public class SessionManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4264379014286412482L;
	
	private Map<String, AgencyReservationSession> clientSessionLookup = new HashMap<String, AgencyReservationSession>();
	private ICarRentalAgency agency;
	
	public AgencyReservationSession create(String name, ICarRentalAgency agency) {
		this.agency = agency;
		AgencyReservationSession session = new AgencyReservationSession(name, agency);
		addSession(name, session);
		return session;
		
	}
	
	public AgencyManagerSession createManager(String name, ICarRentalAgency agency) throws Exception {
		this.agency = agency;
		return new AgencyManagerSession(name, agency);
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
	
	public ICarRentalAgency agency() {
		return agency;
	}
	
}
