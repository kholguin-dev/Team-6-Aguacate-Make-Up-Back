package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.ImagenProducto;
import com.aguacatemakeup.backend.entity.Producto;
import com.aguacatemakeup.backend.repository.ImagenProductoRepository;
import com.aguacatemakeup.backend.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenProductoService {

    private final ImagenProductoRepository imagenProductoRepository;
    private final ProductoRepository productoRepository;

    public ImagenProductoService(
            ImagenProductoRepository imagenProductoRepository,
            ProductoRepository productoRepository) {

        this.imagenProductoRepository = imagenProductoRepository;
        this.productoRepository = productoRepository;
    }

    // 1. Obtener las imágenes de un producto
    public List<ImagenProducto> obtenerImagenesPorProducto(
            Long idProducto) {

        if (!productoRepository.existsById(idProducto)) {
            throw new RuntimeException("Producto no encontrado");
        }

        return imagenProductoRepository
                .findByProducto_IdProducto(idProducto);
    }

    // 2. Registrar una imagen
    @Transactional
    public ImagenProducto guardarImagen(ImagenProducto imagen) {

        validarImagen(imagen);

        Long idProducto = imagen.getProducto().getIdProducto();

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        long cantidadImagenes =
                imagenProductoRepository
                        .countByProducto_IdProducto(idProducto);

        if (cantidadImagenes >= 5) {
            throw new RuntimeException(
                    "Un producto no puede tener más de cinco imágenes");
        }

        imagen.setProducto(producto);

        if (imagen.getEsPrincipal() == null) {
            imagen.setEsPrincipal(cantidadImagenes == 0);
        }

        if (imagen.getOrden() == null) {
            imagen.setOrden((int) cantidadImagenes + 1);
        }

        if (imagen.getEsPrincipal()) {
            desmarcarImagenesPrincipales(idProducto);
        }

        return imagenProductoRepository.save(imagen);
    }

    // 3. Actualizar una imagen
    @Transactional
    public ImagenProducto actualizarImagen(
            Long idImagen, ImagenProducto datosActualizados) {

        ImagenProducto imagenExistente =
                imagenProductoRepository.findById(idImagen)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Imagen no encontrada"));

        validarImagen(datosActualizados);

        Long idProducto =
                imagenExistente.getProducto().getIdProducto();

        if (datosActualizados.getEsPrincipal() == null) {
            throw new RuntimeException(
                    "Debe especificar si la imagen es principal");
        }

        if (datosActualizados.getEsPrincipal()) {
            desmarcarImagenesPrincipales(idProducto);
        }

        imagenExistente.setUrl(datosActualizados.getUrl());
        imagenExistente.setTextoAlternativo(
                datosActualizados.getTextoAlternativo());
        imagenExistente.setEsPrincipal(
                datosActualizados.getEsPrincipal());
        imagenExistente.setOrden(datosActualizados.getOrden());

        return imagenProductoRepository.save(imagenExistente);
    }

    // 4. Eliminar una imagen
    @Transactional
    public void eliminarImagen(Long idImagen) {

        ImagenProducto imagen =
                imagenProductoRepository.findById(idImagen)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Imagen no encontrada"));

        Long idProducto =
                imagen.getProducto().getIdProducto();

        long cantidadImagenes =
                imagenProductoRepository
                        .countByProducto_IdProducto(idProducto);

        if (cantidadImagenes <= 1) {
            throw new RuntimeException(
                    "El producto debe conservar al menos una imagen");
        }

        imagenProductoRepository.delete(imagen);
    }

    // 5. Establecer una imagen como principal
    @Transactional
    public ImagenProducto establecerImagenPrincipal(Long idImagen) {

        ImagenProducto imagen =
                imagenProductoRepository.findById(idImagen)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Imagen no encontrada"));

        Long idProducto =
                imagen.getProducto().getIdProducto();

        desmarcarImagenesPrincipales(idProducto);

        imagen.setEsPrincipal(true);

        return imagenProductoRepository.save(imagen);
    }

    // Desmarcar las imágenes principales anteriores
    private void desmarcarImagenesPrincipales(Long idProducto) {

        List<ImagenProducto> imagenes =
                imagenProductoRepository
                        .findByProducto_IdProducto(idProducto);

        for (ImagenProducto imagen : imagenes) {
            imagen.setEsPrincipal(false);
        }

        imagenProductoRepository.saveAll(imagenes);
    }

    // Validar los datos de una imagen
    private void validarImagen(ImagenProducto imagen) {

        if (imagen.getProducto() == null
                || imagen.getProducto().getIdProducto() == null) {

            throw new RuntimeException(
                    "La imagen debe estar asociada a un producto");
        }

        if (imagen.getUrl() == null || imagen.getUrl().isBlank()) {
            throw new RuntimeException(
                    "La URL de la imagen es obligatoria");
        }

        if (imagen.getUrl().length() > 500) {
            throw new RuntimeException(
                    "La URL no puede superar 500 caracteres");
        }

        if (imagen.getTextoAlternativo() == null
                || imagen.getTextoAlternativo().isBlank()) {

            throw new RuntimeException(
                    "El texto alternativo es obligatorio");
        }

        if (imagen.getOrden() != null
                && (imagen.getOrden() < 1 || imagen.getOrden() > 5)) {

            throw new RuntimeException(
                    "El orden debe estar entre 1 y 5");
        }
    }
}