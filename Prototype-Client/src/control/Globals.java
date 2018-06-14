package control;

import entity.User;

public class Globals {
private static User myUser ; 
// 	ARABIC STYLE !!!! 
private static String requestId;

public static void setRequestId(String id) {
	requestId=id;
}
public static String getRequestId() {
return requestId;
}
public static void setUser (User userFromS) {
	myUser = userFromS ;
}
public static User getUser () {
	return myUser ; 
}
}
