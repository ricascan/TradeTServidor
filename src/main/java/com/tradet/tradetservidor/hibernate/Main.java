/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.tradetservidor.hibernate;

import com.tinify.Tinify;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricar
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket socketServidor = new ServerSocket(5557);
            while (true) {
                Tinify.setKey("7xbRQ4xNSmqfmW26P2T2s03fLV7DBXjc");
                Socket cliente = socketServidor.accept();
                Runnable atenderPeticion = new HiloServidor(cliente);
                Thread hiloServidor = new Thread(atenderPeticion);
                hiloServidor.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
