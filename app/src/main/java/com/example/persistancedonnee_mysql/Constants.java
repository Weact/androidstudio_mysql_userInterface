package com.example.persistancedonnee_mysql;

public class Constants {
    //TO MAKE THIS PROJECT WORKS, PLEASE CLONE THE REPOSITORY https://github.com/Weact/php_mysql_for_androidstudio
    //IF YOU DO NOT HAVE ACCESS TO THE REPOSITORY, PLEASE ASK ACCESS TO https://github.com/Weact
    //CHANGE THE ROUTE_URL TO YOUR IPADRESS (ipV4)
    // FOR EXEMPLE :
    // ROUTE_URL = http://your_ip_adress/wamp_project_folder/php_mysql_for_androidstudio/v1/";
    // YOU MUST LEAVE /php_mysql_for_androidstudio/v1/ OR IT WON'T WORK !

    private static final String ROUTE_URL = "http://192.168.19.209/projets/androidphpmysql/v1/";
    public static final String URL_REGISTER = ROUTE_URL + "userRegister.php";
    public static final String URL_LOGIN = ROUTE_URL + "userLogin.php";
    public static final String URL_USERLIST = ROUTE_URL + "userList.php";
    public static final String URL_USERUPDATE = ROUTE_URL + "userUpdate.php";
    public static final String URL_USERDELETE = ROUTE_URL + "userRemove.php";
}
