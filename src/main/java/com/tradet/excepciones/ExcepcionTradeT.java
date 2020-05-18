/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.excepciones;

/**
 *
 * @author ricar
 */
public class ExcepcionTradeT extends Exception{
    
    private String mensajeUsuario;
    private String mensajeAdministrador;
    private Integer codigoError;
    private String metodoError;

    public ExcepcionTradeT() {
    }
    
    public ExcepcionTradeT(String mensajeUsuario, String mensajeAdministrador, Integer codigoError, String metodoError) {
        this.mensajeUsuario = mensajeUsuario;
        this.mensajeAdministrador = mensajeAdministrador;
        this.codigoError = codigoError;
        this.metodoError = metodoError;
    }
    
    public String getMensajeUsuario() {
        return mensajeUsuario;
    }

    public void setMensajeUsuario(String mensajeUsuario) {
        this.mensajeUsuario = mensajeUsuario;
    }

    public String getMensajeAdministrador() {
        return mensajeAdministrador;
    }

    public void setMensajeAdministrador(String mensajeAdministrador) {
        this.mensajeAdministrador = mensajeAdministrador;
    }

    public Integer getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(Integer codigoError) {
        this.codigoError = codigoError;
    }

    public String getMetodoError() {
        return metodoError;
    }

    public void setMetodoError(String metodoError) {
        this.metodoError = metodoError;
    }
  
    @Override
    public String toString() {
        return "ExcepcionTradeT{" + "mensajeUsuario=" + mensajeUsuario + ", mensajeAdministrador=" + mensajeAdministrador + ", codigoError=" + codigoError + ", metodoError=" + metodoError + '}';
    }
    
    
    
}
