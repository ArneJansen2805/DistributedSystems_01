package client;

import java.util.HashMap;
import java.util.Map;


public class CentralNamingService {
	private static Map<String, Client> clientNameLookup = new HashMap<String, Client>();
	
	public Client getClientForCompany(String companyName) {
		if (!clientNameLookup.containsKey(companyName)) {
			throw new IllegalArgumentException("No company with name: " + companyName + "exists.");
		}
		
		return clientNameLookup.get(companyName);
	}
	
	public void addClient(String companyName, Client client) {
		clientNameLookup.put(companyName, client);
	}
}
