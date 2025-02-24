package com.tradet.tradetservidor.hibernate;
// Generated 08-abr-2020 12:45:27 by Hibernate Tools 4.3.1


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Categoria generated by hbm2java
 */
@Entity
@Table(name="categoria"
    ,catalog="tradet"
)
public class Categoria  implements java.io.Serializable {


     private Integer categoriaId;
     private String nombre;
     private Set<Producto> productos = new HashSet<Producto>(0);

    public Categoria() {
    }

    public Categoria(Integer categoriaId){
        this.categoriaId = categoriaId;
    }
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    public Categoria(String nombre, Set<Producto> productos) {
       this.nombre = nombre;
       this.productos = productos;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="CATEGORIA_ID", nullable=false)
    public Integer getCategoriaId() {
        return this.categoriaId;
    }
    
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    
    @Column(name="NOMBRE", length=30)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="categoria")
    public Set<Producto> getProductos() {
        return this.productos;
    }
    
    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Categoria{" + "categoriaId=" + categoriaId + ", nombre=" + nombre + '}';
    }

    public HashMap toHash(){
        HashMap map = new HashMap();
        map.put("id", categoriaId);
        map.put("nombre", nombre);
        return map;
    }
    
    public Categoria(HashMap map){
        if(map.containsKey("id")){
            categoriaId = (Integer) map.get("id");
        }else{
            categoriaId = null;
        }
        nombre = (String) map.get("nombre");
    }


}


