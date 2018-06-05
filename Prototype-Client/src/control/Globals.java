package control;

public class Globals {
	
private static String userName;
private static String fullName;
private static String requestId;
public static String getuserName(){
	return(userName);
}
public static void setuserName(String userNameToChange) {
	userName=userNameToChange;
}
public static String getFullName() {
	return fullName;
}
public static void setFullName(String fullNameToChange) {
	fullName = fullNameToChange;
}
public static void setRequestId(String id) {
	requestId=id;
}
public static String getRequestId() {
return requestId;
}
}
