package com.aguacatemakeup.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearProductoDTO {

    private String id;
    private String sku;

    private String nombre;
    private String marca;
    private String descripcion;

    private String categoria;
    private String subcategoria;

    private Double precio;
    private Double preciopromocional;

    private Integer stock;
    private String tamano;

    private String disponibilidad;
    private String beneficios;
    private String modouso;
    private String ingredientes;

    private String textoAlternativo;
    private String imagen;

    private Boolean destacado;
    private Boolean novedades;
    private String estado;
}