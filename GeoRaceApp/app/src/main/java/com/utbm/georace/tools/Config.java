package com.utbm.georace.tools;

/**
 * Created by jojo on 31/10/2014.
 */
public class Config {

    public class Url {
        //URL to the server
        public final static String url_server = "http://francks.eu/georace/webService/"; //with terminal "/"

        //URL to the different webservice
        public final static String url_get_login = "get_login.php";
        public final static String url_get_user = "get_user.php";
        public final static String url_get_team = "get_team.php";
        public final static String url_get_friend = "get_friendship.php";
        public final static String url_get_race = "get_race.php";
        public final static String url_get_data = "get_data.php";
        public final static String url_get_checks = "get_checks.php";
        public final static String url_get_track = "get_track.php";
        public final static String url_get_participation = "get_participation.php";
        public final static String url_get_checkpoint = "get_checkpoint.php";


        public final static String url_set_login = "set_login.php";
        public final static String url_set_user = "set_user.php";
        public final static String url_set_team = "set_team.php";
        public final static String url_set_friend = "set_friendship.php";
        public final static String url_set_checks = "set_checks.php";
        public final static String url_set_race = "set_race.php";
        public final static String url_set_data = "set_data.php";
        public final static String url_set_track = "set_track.php";
        public final static String url_set_participation = "set_participation.php";
        public final static String url_set_checkpoint = "set_checkpoint.php";
    }

    public class Service{

        public final static String service_get_login = Url.url_server+Url.url_get_login;
        public final static String service_get_user = Url.url_server+Url.url_get_user;
        public final static String service_get_race = Url.url_server+Url.url_get_race;
        public final static String service_get_participation = Url.url_server+Url.url_get_participation;
        public final static String service_get_checks = Url.url_server+Url.url_get_checks;
        public final static String service_get_track = Url.url_server+Url.url_get_track;
        public final static String service_get_checkpoints = Url.url_server+Url.url_get_checkpoint;
        public final static String service_get_friendship = Url.url_server+Url.url_get_friend;

        public final static String service_set_login = Url.url_server+Url.url_set_login;
        public final static String service_set_user = Url.url_server+Url.url_set_user;
        public final static String service_set_race = Url.url_server+Url.url_set_race;
        public final static String service_set_participation = Url.url_server+Url.url_set_participation;
        public final static String service_set_track = Url.url_server+Url.url_set_track;
        public final static String service_set_checkpoints = Url.url_server+Url.url_set_checkpoint;
        public final static String service_set_friendship = Url.url_server+Url.url_set_friend;



    }

    public class Http {
        //timeout of connection
        public final static int connectionTimeout = 3000;
        public final static int socketTimeout = 5000;
    }
}
