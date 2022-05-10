package com;

import com.server.*;
import com.server.entity.ServerConnection;
import com.server.protocol.*;
import com.server.web.*;

import com.db.crud.*;
import java.lang.reflect.Field;





public class Main {

    public static Launcher launcher;
    public static HTTP http_protocol;

    public static void main(String[] args) {

        launcher = new Launcher(); //create a launcher to run the backend
        launcher.DEBUG_SERVER_LEVEL = 0; //debug
        launcher.addDatabaseManager(); //loads standard database manager thread
        launcher.addLoginHandler(); //loads standard login handler thread
        launcher.addCareTaker(1800000); //caretaker for checking server health
        CRUDHandler.DEBUG_CRUD = false;


        //define http
        http_protocol = new HTTP("res/front/",80) {
            public byte[] processGET(ServerConnection c, String uri, String resource, String[] fields, String[] values) {
                if (uri.contains("/tickets") && fields.length>0) {
                    //return ticketManager.processGET(this,c,uri,resource,fields,values);
                }
                return null;
            }
        };
        Server http = new Server(http_protocol,2048);

        WebPackets wp = new WebPackets() {
            public void processPOST(ServerConnection c, String uri, int packetID, String[] fields, String[] values) {
                if (packetID >= 500 && packetID <= 600) {
                   // ticketManager.processPOST(http_protocol,c,uri,packetID,fields,values);
                }
            }
        };
        Server tcp = new Server(new TCP(43594),1024);
        tcp.setWebPacketHandler(wp);

        launcher.loadThread(http,"Web Server");
        launcher.loadThread(tcp,"TCP Server");
        // your custom app threads go here


        // end custom app threads
        launcher.startThreads();
    }



}