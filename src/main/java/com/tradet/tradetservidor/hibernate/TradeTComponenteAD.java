/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.tradetservidor.hibernate;

import com.tradet.excepciones.ExcepcionTradeT;
import java.util.List;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;

/**
 *
 * @author ricar
 */
public class TradeTComponenteAD {

    private Session sesion;
    private SessionFactory fabricaSesiones;

    /**
     * Método que cierra la sesión de Hibernate.
     *
     */
    private void cerrarSesionHibernate() {
        try {
            sesion.close();
        } catch (Exception ex) {
        }
    }

    public void cerrarHibernate() {
        try {
            fabricaSesiones.close();
        } catch (Exception ex) {

        }
    }

    /**
     * Método que abre la sesión de Hibernate.
     *
     */
    private void arrancarSesionHibernate() {
        try {
            sesion = (Session) fabricaSesiones.openSession();
        } catch (Exception ex) {
        }
    }

    public TradeTComponenteAD() {
        try {
            fabricaSesiones = SessionFactoryUtil.getSessionFactory();
        } catch (Exception ex) {
        }
    }

    public Integer insertarProducto(Producto p) throws ExcepcionTradeT {
        Integer registrosAfectados = 0;
        try {
            arrancarSesionHibernate();
            Transaction t = sesion.beginTransaction();

            sesion.save(p);
            t.commit();
            cerrarSesionHibernate();
            registrosAfectados = 1;
        } catch (ConstraintViolationException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getSQLException().getMessage());
            e.setCodigoError(ex.getSQLException().getErrorCode());
            e.setMetodoError("Insertar producto");
            switch (e.getCodigoError()) {
                case 1062:
                    e.setMensajeUsuario("El identificador del producto no se puede repetir.");
                    break;
                case 1048:
                    e.setMensajeUsuario("Todos los campos son obligatorios.");
                    break;
                case 1452:
                    e.setMensajeUsuario("El usuario y la categoria introducidos deben existir.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                    break;
            }
            cerrarSesionHibernate();
            throw e;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Insertar producto");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
        return registrosAfectados;
    }

    public Integer eliminarProducto(Producto p) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.delete(p);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (OptimisticLockException | StaleStateException ex) {
            cerrarSesionHibernate();
            return 0;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Eliminar producto");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer modificarProducto(Producto p) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.update(p);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar producto");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("El identificador del producto no se puede repetir.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos son obligatorios.");
                        break;
                    case 1452:
                        e.setMensajeUsuario("El usuario y la categoria introducidos deben existir.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            return 0;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Modificar producto");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Producto leerProducto(Integer pId) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Producto where productoId = " + pId);
            Producto p = (Producto) q.uniqueResult();
            return p;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer Producto");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public List<Producto> leerProductos(String filtro) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q;
            q = sesion.createQuery("from Producto p " + filtro);
            List<Producto> l = q.list();
            l.forEach((p) -> {
                Hibernate.initialize(p.getUsuario());
                Hibernate.initialize(p.getCategoria());
            });
            cerrarSesionHibernate();
            return l;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer productos");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }

    }

    public Integer insertarUsuario(Usuario u) throws ExcepcionTradeT {
        Integer registrosAfectados = 0;
        try {
            arrancarSesionHibernate();
            Transaction t = sesion.beginTransaction();
            sesion.save(u);
            t.commit();
            cerrarSesionHibernate();
            registrosAfectados = 1;
        } catch (ConstraintViolationException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getSQLException().getMessage());
            e.setCodigoError(ex.getSQLException().getErrorCode());
            e.setMetodoError("Insertar usuario");
            switch (e.getCodigoError()) {
                case 1062:
                    e.setMensajeUsuario("El identificador del usuario, el nombre, el email y el número de teléfono no se pueden repetir.");
                    break;
                case 1048:
                    e.setMensajeUsuario("Todos los campos menos la foto son obligatorios.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                    break;
            }
            cerrarSesionHibernate();
            throw e;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Insertar usuario");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
        return registrosAfectados;
    }

    public Integer eliminarUsuario(Usuario u) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.delete(u);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (OptimisticLockException | StaleStateException ex) {
            cerrarSesionHibernate();
            return 0;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Eliminar usuario");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer modificarUsuario(Usuario u) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.update(u);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar usuario");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("El identificador del usuario, el nombre, el email y el número de teléfono no se pueden repetir.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos menos la foto son obligatorios.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            return 0;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Modificar usuario");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Usuario leerUsuario(Integer uId) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Usuario where usuarioId = " + uId);
            Usuario u = (Usuario) q.uniqueResult();
            return u;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer usuario");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public List<Usuario> leerUsuarios(String filtro) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Usuario u " + filtro);
            List<Usuario> l = q.list();
            cerrarSesionHibernate();
            return l;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer usuarios");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }

    }
    
    public Integer insertarCategoria(Categoria c) throws ExcepcionTradeT {
        Integer registrosAfectados = 0;
        try {
            arrancarSesionHibernate();
            Transaction t = sesion.beginTransaction();
            sesion.save(c);
            t.commit();
            cerrarSesionHibernate();
            registrosAfectados = 1;
        } catch (ConstraintViolationException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getSQLException().getMessage());
            e.setCodigoError(ex.getSQLException().getErrorCode());
            e.setMetodoError("Insertar categoría");
            switch (e.getCodigoError()) {
                case 1062:
                    e.setMensajeUsuario("El identificador de la categoría y el nombre no se pueden repetir.");
                    break;
                case 1048:
                    e.setMensajeUsuario("Todos los campos son obligatorios.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                    break;
            }
            cerrarSesionHibernate();
            throw e;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Insertar categoría");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
        return registrosAfectados;
    }

    public Integer eliminarCategoria(Categoria c) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.delete(c);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Eliminar categoría");
                switch (e.getCodigoError()) {
                    case 1451:
                        e.setMensajeUsuario("No se puede borrar una categoria a la cual pertenece algún producto.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            throw e;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Eliminar categoria");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer modificarCategoria(Categoria c) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.update(c);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar categoria");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("El identificador de la categoría no se puede repetir.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos son obligatorios.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            return 0;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Modificar categoria");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Categoria leerCategoria(Integer cId) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Categoria where categoriaId = " + cId);
            Categoria c = (Categoria) q.uniqueResult();
            return c;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer categoría");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public List<Categoria> leerCategorias() throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Categoria c");
            List<Categoria> l = q.list();
            cerrarSesionHibernate();
            return l;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer categorías");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }

    }

     public Integer insertarValoracion(Valoracion v) throws ExcepcionTradeT {
        Integer registrosAfectados = 0;
        try {
            arrancarSesionHibernate();
            Transaction t = sesion.beginTransaction();
            sesion.save(v);
            t.commit();
            cerrarSesionHibernate();
            registrosAfectados = 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Insertar valoración");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("Un usuario no puede valorar más de una vez a otro.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos son obligatorios.");
                        break;
                    case 1452:
                        e.setMensajeUsuario("El usuario Valorado y el usuario valorador deben existir.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof GenericJDBCException) {
                e.setMensajeAdministrador(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((GenericJDBCException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Insertar valoración");
                switch (e.getMensajeAdministrador()) {
                    case "Un usuario no se puede calificar a si mismo":
                        e.setMensajeUsuario("Un usuario no se puede calificar a si mismo.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Insertar valoración");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
        return registrosAfectados;
    }

    public Integer eliminarValoracion(Valoracion v) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.delete(v);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (OptimisticLockException | StaleStateException ex) {
            cerrarSesionHibernate();
            return 0;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Eliminar valoración");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer modificarValoracion(Valoracion v) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.update(v);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar valoración");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("Un usuario no puede valorar más de una vez a otro.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos son obligatorios.");
                        break;
                    case 1452:
                        e.setMensajeUsuario("El usuario Valorado y el usuario valorador deben existir.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else if (ex.getCause() instanceof GenericJDBCException) {
                e.setMensajeAdministrador(((GenericJDBCException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((GenericJDBCException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar valoración");
                switch (e.getMensajeAdministrador()) {
                    case "Un usuario no se puede calificar a si mismo":
                        e.setMensajeUsuario("Un usuario no se puede calificar a si mismo.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            return 0;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Modificar valoración");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Valoracion leerValoracion(Integer uValoradoId, Integer uValoradorId) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Valoracion where usuarioByUsuarioValoradoId = " + uValoradoId + " and usuarioByUsuarioValoradorId = " + uValoradorId);
            Valoracion v = (Valoracion) q.uniqueResult();
            return v;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer valoración");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public List<Valoracion> leerValoraciones(String filtro) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q;
            q = sesion.createQuery("from Valoracion v " + filtro);
            List<Valoracion> l = q.list();
            l.forEach((v) -> {
                Hibernate.initialize(v.getUsuarioByUsuarioValoradoId());
                Hibernate.initialize(v.getUsuarioByUsuarioValoradorId());
            });
            cerrarSesionHibernate();
            return l;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer valoraciones");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer insertarSorteo(Sorteo s) throws ExcepcionTradeT {
        Integer registrosAfectados = 0;
        try {
            arrancarSesionHibernate();
            Transaction t = sesion.beginTransaction();
            sesion.save(s);
            t.commit();
            cerrarSesionHibernate();
            registrosAfectados = 1;
        } catch (ConstraintViolationException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getSQLException().getMessage());
            e.setCodigoError(ex.getSQLException().getErrorCode());
            e.setMetodoError("Insertar sorteo");
            switch (e.getCodigoError()) {
                case 1062:
                    e.setMensajeUsuario("El identificador del sorteo no se puede repetir.");
                    break;
                case 1048:
                    e.setMensajeUsuario("Todos los campos son obligatorios.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                    break;
            }
            cerrarSesionHibernate();
            throw e;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Insertar sorteo");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
        return registrosAfectados;
    }

    public Integer eliminarSorteo(Sorteo s) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.delete(s);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (OptimisticLockException | StaleStateException ex) {
            cerrarSesionHibernate();
            return 0;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Eliminar sorteo");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Integer modificarSorteo(Sorteo s) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            sesion.beginTransaction();
            Transaction t = sesion.getTransaction();
            sesion.update(s);
            t.commit();
            cerrarSesionHibernate();
            return 1;
        } catch (javax.persistence.PersistenceException ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            if (ex.getCause() instanceof ConstraintViolationException) {
                e.setMensajeAdministrador(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
                e.setCodigoError(((ConstraintViolationException) ex.getCause()).getSQLException().getErrorCode());
                e.setMetodoError("Modificar sorteo");
                switch (e.getCodigoError()) {
                    case 1062:
                        e.setMensajeUsuario("El identificador del sorteo no se puede repetir.");
                        break;
                    case 1048:
                        e.setMensajeUsuario("Todos los campos son obligatorios.");
                        break;
                    default:
                        e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
                        break;
                }
                cerrarSesionHibernate();
                throw e;
            } else if (ex.getCause() instanceof StaleStateException || ex.getCause() instanceof OptimisticLockException) {
                cerrarSesionHibernate();
            } else {
                e.setMensajeUsuario("Error general del sistema, consulte con el administrador.");
            }
            return 0;

        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Modificar categoria");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public Sorteo leerSorteo(Integer sId) throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Sorteo where sorteoId = " + sId);
            Sorteo s = (Sorteo) q.uniqueResult();
            return s;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer sorteo");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }
    }

    public List<Sorteo> leerSorteos() throws ExcepcionTradeT {
        try {
            arrancarSesionHibernate();
            Query q = sesion.createQuery("from Sorteo s");
            List<Sorteo> l = q.list();
            cerrarSesionHibernate();
            return l;
        } catch (Exception ex) {
            ExcepcionTradeT e = new ExcepcionTradeT();
            e.setMensajeAdministrador(ex.getMessage());
            e.setMetodoError("Leer sorteos");
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            cerrarSesionHibernate();
            throw e;
        }

    }

    

}
