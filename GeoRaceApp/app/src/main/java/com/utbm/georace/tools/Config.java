package com.utbm.georace.tools;

/**
 * Created by jojo on 31/10/2014.
 */
public class Config {

    public class Url {
        //URL to the server
        public final static String url_server = "http://172.18.0.36/georace/";

        //URL to the different webservice
        public final static String url_login = "get_login.php";
        public final static String url_data = "get_data.php";

    }

    public class Service{

        public final static String service_login = Url.url_server+Url.url_login;
        public final static String service_data = Url.url_server+Url.url_data;

    }

    public class Http {
        //timeout of connection
        public final static int connectionTimeout = 3000;
        public final static int socketTimeout = 5000;
    }
}
