package rental_agency;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import rental.ICarRentalCompany;

public interface ICentralNamingService extends Remote {

	void registerCRC(ICarRentalCompany crc) throws RemoteException ;

	ArrayList<ICarRentalCompany> getCompanies() throws RemoteException ;

	ICarRentalCompany getCompany(String name) throws RemoteException ;

}