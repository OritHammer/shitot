package control;

public class Globals {
	
private static String userName;

public static String getuserName(){
	return(userName);
}
public static void setuserName(String userNameToChange) {
	userName=userNameToChange;
}

}
