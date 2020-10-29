package rental_agency;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
	
	private Map<String, AgencyReservationSession> clientSessionLookup = new HashMap<String, AgencyReservationSession>();
	
	public AgencyReservationSession addSession(String id, String name) {
		AgencyReservationSession session = new AgencyReservationSession(name, id);
		clientSessionLookup.put(id, session);
		return session;
	}
	
	public synchronized String createClientUUID() {
		return UUID.randomUUID().toString();
	}
	
	
	public String bindClient() {		
		String clientId = createClientUUID();
		//TODO: make new session and connect to client
		
		
		return clientId;
	}
}
