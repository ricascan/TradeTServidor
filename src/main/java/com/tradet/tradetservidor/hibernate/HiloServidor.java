/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.tradetservidor.hibernate;

import com.tradet.excepciones.ExcepcionTradeT;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author ricar
 */
public class HiloServidor implements Runnable {

    private TradeTComponenteAD componente;
    private Socket cliente;
    private String ubicacionXampp = "E:\\xampp";

    public HiloServidor() {
    }

    public HiloServidor(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        componente = new TradeTComponenteAD();

        try {
            ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());

            HashMap peticion = (HashMap) flujoEntrada.readObject();
            HashMap mapProducto;
            HashMap mapUsuario;
            HashMap mapCategoria;
            HashMap mapValoracion;
            HashMap mapSorteo;

            Producto producto;
            Usuario usuario;
            Categoria categoria;
            Valoracion valoracion;
            Sorteo sorteo;

            List<Producto> listaProductos;
            List<Usuario> listaUsuarios;
            List<Valoracion> listaValoraciones;

            Integer productoId;
            Integer usuarioId;
            Integer categoriaId;
            HashMap valoracionId;
            Integer sorteoId;

            switch ((String) peticion.get("peticion")) {
                //PRODUCTO
                case "insertar producto":
                    mapProducto = (HashMap) peticion.get("argumento");
                    producto = new Producto(mapProducto);

                    try {
                        componente.insertarProducto(producto);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;

                case "eliminar producto":
                    productoId = (Integer) peticion.get("argumento");
                    producto = new Producto(productoId);
                    try {
                        componente.eliminarProducto(producto);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;

                case "modificar producto":
                    mapProducto = (HashMap) peticion.get("argumento");
                    producto = new Producto(mapProducto);
                    try {
                        componente.modificarProducto(producto);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;

                case "leer producto":
                    productoId = (Integer) peticion.get("argumento");
                    try {
                        producto = componente.leerProducto(productoId);
                        HashMap map = producto.toHash();
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(map);
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;

                case "leer productos":               
                    try {
                    listaProductos = componente.leerProductos("");
                    ArrayList<HashMap> listaHashProductos = new ArrayList();

                    for (Producto p : listaProductos) {
                        HashMap map = p.toHash();
                        listaHashProductos.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashProductos);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;

                case "leer productos filtro":
                    try {
                    listaProductos = componente.leerProductos((String) peticion.get("argumento"));
                    ArrayList<HashMap> listaHashProductosFiltrados = new ArrayList();

                    for (Producto p : listaProductos) {
                        HashMap map = p.toHash();
                        listaHashProductosFiltrados.add(map);
                    }

                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashProductosFiltrados);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                //USUARIO
                case "insertar usuario":
                    mapUsuario = (HashMap) peticion.get("argumento");
                    usuario = new Usuario(mapUsuario);

                    try {
                        componente.insertarUsuario(usuario);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "eliminar usuario":
                    usuarioId = (Integer) peticion.get("argumento");
                    usuario = new Usuario(usuarioId);
                    try {
                        componente.eliminarUsuario(usuario);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "modificar usuario":
                    mapUsuario = (HashMap) peticion.get("argumento");
                    usuario = new Usuario(mapUsuario);
                    try {
                        componente.modificarUsuario(usuario);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer usuario":
                    usuarioId = (Integer) peticion.get("argumento");
                    try {
                        usuario = componente.leerUsuario(usuarioId);
                        HashMap map = usuario.toHash();
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(map);
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer usuarios":
                    try {
                    listaUsuarios = componente.leerUsuarios("");
                    ArrayList<HashMap> listaHashUsuarios = new ArrayList();

                    for (Usuario u : listaUsuarios) {
                        HashMap map = u.toHash();
                        listaHashUsuarios.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashUsuarios);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                
                case "leer usuarios no imagen":
                    try {
                    listaUsuarios = componente.leerUsuarios("");
                    ArrayList<HashMap> listaHashUsuarios = new ArrayList();

                    for (Usuario u : listaUsuarios) {
                        u.setFoto(null);
                        HashMap map = u.toHash();
                        listaHashUsuarios.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashUsuarios);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                
                case "leer usuarios filtro":
                    try {
                    listaUsuarios = componente.leerUsuarios((String) peticion.get("argumento"));
                    ArrayList<HashMap> listaHashUsuarios = new ArrayList();

                    for (Usuario u : listaUsuarios) {
                        HashMap map = u.toHash();
                        listaHashUsuarios.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashUsuarios);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                //CATEGORÍA
                case "insertar categoria":
                    mapCategoria = (HashMap) peticion.get("argumento");
                    categoria = new Categoria(mapCategoria);
                    try {
                        componente.insertarCategoria(categoria);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "eliminar categoria":
                    categoriaId = (Integer) peticion.get("argumento");
                    categoria = new Categoria(categoriaId);
                    try {
                        componente.eliminarCategoria(categoria);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }

                    break;
                case "modificar categoria":
                    mapCategoria = (HashMap) peticion.get("argumento");
                    categoria = new Categoria(mapCategoria);
                    try {
                        componente.modificarCategoria(categoria);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer categoria":
                    categoriaId = (Integer) peticion.get("argumento");
                    try {
                        categoria = componente.leerCategoria(categoriaId);
                        HashMap map = categoria.toHash();
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(map);
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer categorias":
                    try {
                    List<Categoria> listaCategorias = componente.leerCategorias();
                    ArrayList<HashMap> listaHashCategorias = new ArrayList();
                    for (Categoria c : listaCategorias) {
                        HashMap map = c.toHash();
                        listaHashCategorias.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashCategorias);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                //VALORACIÓN
                case "insertar valoracion":
                    mapValoracion = (HashMap) peticion.get("argumento");
                    valoracion = new Valoracion(mapValoracion);
                    try {
                        componente.insertarValoracion(valoracion);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "eliminar valoracion":
                    valoracionId = (HashMap) peticion.get("argumento");
                    valoracion = new Valoracion(new ValoracionId((Integer) valoracionId.get("usuario valorado"), (Integer) valoracionId.get("usuario valorador")));
                    try {
                        componente.eliminarValoracion(valoracion);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "modificar valoracion":
                    mapValoracion = (HashMap) peticion.get("argumento");
                    valoracion = new Valoracion(mapValoracion);
                    try {
                        componente.modificarValoracion(valoracion);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer valoracion":
                    valoracionId = (HashMap) peticion.get("argumento");
                    try {
                        valoracion = componente.leerValoracion((Integer) valoracionId.get("usuario valorado"), (Integer) valoracionId.get("usuario valorador"));
                        HashMap map = valoracion.toHash();
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(map);
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer valoraciones":
                    try {
                    listaValoraciones = componente.leerValoraciones("");
                    ArrayList<HashMap> listaHashValoraciones = new ArrayList();
                    for (Valoracion v : listaValoraciones) {
                        HashMap map = v.toHash();
                        listaHashValoraciones.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashValoraciones);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                case "leer valoraciones filtro":
                    try {
                    listaValoraciones = componente.leerValoraciones((String) peticion.get("argumento"));
                    ArrayList<HashMap> listaHashValoraciones = new ArrayList();
                    for (Valoracion v : listaValoraciones) {
                        HashMap map = v.toHash();
                        listaHashValoraciones.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashValoraciones);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                //SORTEO
                case "insertar sorteo":
                    mapSorteo = (HashMap) peticion.get("argumento");
                    sorteo = new Sorteo(mapSorteo);
                    try {
                        componente.insertarSorteo(sorteo);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "eliminar sorteo":
                    sorteoId = (Integer) peticion.get("argumento");
                    sorteo = new Sorteo(sorteoId);
                    try {
                        componente.eliminarSorteo(sorteo);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "modificar sorteo":
                    mapSorteo = (HashMap) peticion.get("argumento");
                    sorteo = new Sorteo(mapSorteo);
                    try {
                        componente.modificarSorteo(sorteo);
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject("ok");
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer sorteo":
                    sorteoId = (Integer) peticion.get("argumento");
                    try {
                        sorteo = componente.leerSorteo(sorteoId);
                        HashMap map = sorteo.toHash();
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(map);
                        flujoSalida.close();
                    } catch (ExcepcionTradeT ex) {
                        ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                        flujoSalida.flush();
                        flujoSalida.writeObject(ex);
                        flujoSalida.close();
                    }
                    break;
                case "leer sorteos":           
                    try {
                    List<Sorteo> listaSorteos = componente.leerSorteos();
                    ArrayList<HashMap> listaHashSorteos = new ArrayList();
                    for (Sorteo s : listaSorteos) {
                        HashMap map = s.toHash();
                        listaHashSorteos.add(map);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(listaHashSorteos);
                    flujoSalida.close();
                } catch (ExcepcionTradeT ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(ex);
                    flujoSalida.close();
                }
                break;
                case "hacer copia":
                    try {
                    String comando = "cmd /c " + ubicacionXampp + "\\mysql\\bin\\mysqldump --no-defaults -h localhost -u root tradet > %USERPROFILE%\\Documents\\tradet.sql";
                    Runtime.getRuntime().exec(comando);
                    FTPClient client = new FTPClient();
                    String sFTP = "localhost";
                    String sUser = "tradet";
                    String sPassword = "kk";

                    try {
                        client.connect(sFTP);
                        boolean login = client.login(sUser, sPassword);
                        
                        FileInputStream fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\tradet.sql");
                        System.out.println(login);
                        client.setFileType(BINARY_FILE_TYPE);
                        client.setDataTimeout(10000000);
                        client.setDefaultTimeout(100000000);
                        client.setBufferSize(1000000000);
                        client.storeFile("/tradet.sql", fis);
                        fis.close();
                        client.logout();
                        client.disconnect();
                    } catch (IOException ioe) {
                        System.out.println(ioe);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject("ok");
                    flujoSalida.close();
                } catch (IOException ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject("error");
                    flujoSalida.close();
                }

                break;

                case "restaurar copia":
                    try {

                    FTPClient client = new FTPClient();
                    String sFTP = "localhost";
                    String sUser = "tradet";
                    String sPassword = "kk";

                    try {
                        client.connect(sFTP);
                        boolean login = client.login(sUser, sPassword);

                        FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"\\Documents\\tradet.sql");
                        System.out.println(login);
                        client.setFileType(BINARY_FILE_TYPE);
                        client.setDataTimeout(10000000);
                        client.setDefaultTimeout(100000000);
                        client.setBufferSize(1000000000);
                        client.retrieveFile("/tradet.sql", fos);
                        fos.close();
                        client.logout();
                        client.disconnect();
                        String comando = "cmd /c " + ubicacionXampp + "\\mysql\\bin\\mysql -h localhost -u root tradet < %USERPROFILE%\\Documents\\tradet.sql";
                        Runtime.getRuntime().exec(comando);
                    } catch (IOException ioe) {
                        System.out.println(ioe);
                    }
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject("ok");
                    flujoSalida.close();
                } catch (IOException ex) {
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject("error");
                    flujoSalida.close();
                }

                break;

                //INESPERADO
                default:
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(cliente.getOutputStream());
                    flujoSalida.flush();
                    flujoSalida.writeObject(0);
                    flujoSalida.close();
                    break;
            }
            flujoEntrada.close();
            cliente.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
