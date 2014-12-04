package com.utbm.georace.tools;

/**
 * Created by jojo on 31/10/2014.
 */
public class Config {

    public class Url {
        //URL to the server
        public final static String url_server = "http://192.168.0.15/georace/"; //with terminal "/"

        //URL to the different webservice
        public final static String url_login = "get_login.php";
        public final static String url_user = "get_user.php";
        public final static String url_team = "get_team.php";
        public final static String url_race = "get_race.php";
        public final static String url_data = "get_data.php";
        public final static String url_track = "get_track.php";
        public final static String url_participation = "get_participation.php";
        public final static String url_checkpoint = "get_checkpoint.php";

    }

    public class Service{

        public final static String service_login = Url.url_server+Url.url_login;
        public final static String service_user = Url.url_server+Url.url_user;
        public final static String service_race = Url.url_server+Url.url_race;
        public final static String service_participation = Url.url_server+Url.url_participation;
        public final static String service_track = Url.url_server+Url.url_track;
        public final static String service_checkpoints = Url.url_server+Url.url_checkpoint;


    }

    public class Http {
        //timeout of connection
        public final static int connectionTimeout = 3000;
        public final static int socketTimeout = 5000;
    }
}
