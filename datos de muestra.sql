-- AGUACATE MAKE UP
-- ARCHIVO 2: DATOS DE MUESTRA
-- Ejecutar después de 01_crear_base_y_tablas.sql.
-- Incluye 5 registros por tabla, excepto roles: el negocio definió solo
-- dos roles válidos (Cliente y Administrador), por lo que no se inventan roles.
-- Los correos y teléfonos son datos ficticios de prueba.
-- HASH_DEMO_NO_USAR es un marcador; reemplazar por hashes reales generados
-- por el backend antes de utilizar autenticación real.

USE aguacate_makeup;

START TRANSACTION;

-- 1. ROLES: 2 filas válidas según las reglas del negocio.
INSERT INTO roles (id_rol, nombre, descripcion) VALUES
(1, 'Cliente', 'Consulta el catálogo y utiliza el carrito'),
(2, 'Administrador', 'Administra productos, categorías y plataforma')
ON DUPLICATE KEY UPDATE
  descripcion = VALUES(descripcion);

-- 2. USUARIOS: 5 registros (1 administrador y 4 clientes).
INSERT INTO usuarios
(id_usuario, rol_id, nombres, apellidos, correo, telefono, password_hash,
 acepta_terminos, recibe_novedades, activo, verificado)
VALUES
(1, 2, 'Admin', 'Demo', 'admin@aguacatemakeup.example', '3000000001', 'HASH_DEMO_NO_USAR', 1, 0, 1, 1),
(2, 1, 'Ana', 'Martínez', 'ana@aguacatemakeup.example', '3000000002', 'HASH_DEMO_NO_USAR', 1, 1, 1, 0),
(3, 1, 'Laura', 'Pérez', 'laura@aguacatemakeup.example', '3000000003', 'HASH_DEMO_NO_USAR', 1, 0, 1, 0),
(4, 1, 'Sofía', 'Gómez', 'sofia@aguacatemakeup.example', '3000000004', 'HASH_DEMO_NO_USAR', 1, 1, 1, 0),
(5, 1, 'Camila', 'Rodríguez', 'camila@aguacatemakeup.example', '3000000005', 'HASH_DEMO_NO_USAR', 1, 0, 1, 0)
ON DUPLICATE KEY UPDATE
  rol_id = VALUES(rol_id),
  nombres = VALUES(nombres),
  apellidos = VALUES(apellidos),
  correo = VALUES(correo),
  telefono = VALUES(telefono),
  password_hash = VALUES(password_hash);

-- 3. CATEGORIAS: 5 registros.
INSERT INTO categorias (id_categoria, nombre, descripcion) VALUES
(1, 'Skincare', 'Cuidado de la piel'),
(2, 'Maquillaje', 'Productos cosméticos'),
(3, 'Accesorios', 'Accesorios de belleza - dato de muestra'),
(4, 'Cuidado capilar', 'Categoría de muestra'),
(5, 'Cuidado corporal', 'Categoría de muestra')
ON DUPLICATE KEY UPDATE
  descripcion = VALUES(descripcion);

-- 4. SUBCATEGORIAS: 5 registros.
INSERT INTO subcategorias (id_subcategoria, categoria_id, nombre) VALUES
(1, 1, 'Hidratantes'),
(2, 1, 'Limpiadores'),
(3, 2, 'Labios'),
(4, 2, 'Ojos'),
(5, 2, 'Cara')
ON DUPLICATE KEY UPDATE
  categoria_id = VALUES(categoria_id),
  nombre = VALUES(nombre);

-- 5. PRODUCTOS: 5 registros basados en los productos semilla compartidos.
INSERT INTO productos
(id_producto, codigo_producto, sku, subcategoria_id, nombre, marca, descripcion,
 precio_base, precio_promocional, stock, tamano, estado, destacado, novedades)
VALUES
(1, 'PROD-001', 'SKTON001', 1, 'Tónico de rosas y manzanilla', 'Aguacate Make Up',
 'Tónico facial para refrescar la piel.', 15000.00, NULL, 25, '120 ml', 'Publicado', 1, 1),
(2, 'PROD-002', 'MQLAB001', 3, 'Aura Brillo Líquido', 'Aguacate Make Up',
 'Producto líquido para labios con acabado brillante.', 10000.00, NULL, 30, '5 ml', 'Publicado', 0, 0),
(3, 'PROD-003', 'SKDES001', 2, 'Bob Esponja Desmaquillante Bifásico', 'Aguacate Make Up',
 'Desmaquillante bifásico para retirar maquillaje.', 20000.00, NULL, 18, '100 ml', 'Publicado', 1, 0),
(4, 'PROD-004', 'MQDEL001', 4, 'Colo Party', 'Aguacate Make Up',
 'Delineador cosmético para definir la mirada.', 10000.00, NULL, 22, '1 unidad', 'Publicado', 0, 0),
(5, 'PROD-005', 'MQBAS001', 5, 'Magic Concealer Corrector', 'Aguacate Make Up',
 'Corrector de maquillaje para unificar visualmente el tono.', 20000.00, NULL, 15, '6 ml', 'Publicado', 1, 0)
ON DUPLICATE KEY UPDATE
  subcategoria_id = VALUES(subcategoria_id),
  nombre = VALUES(nombre),
  marca = VALUES(marca),
  descripcion = VALUES(descripcion),
  precio_base = VALUES(precio_base),
  precio_promocional = VALUES(precio_promocional),
  stock = VALUES(stock),
  tamano = VALUES(tamano),
  estado = VALUES(estado),
  destacado = VALUES(destacado),
  novedades = VALUES(novedades);

-- 6. IMAGENES_PRODUCTO: 5 imágenes principales, una por producto.
INSERT INTO imagenes_producto
(producto_id, url, texto_alternativo, es_principal, orden)
VALUES
(1, '../img/img-catalogo/Prod8.jpeg', 'Tónico facial de rosas y manzanilla', 1, 1),
(2, '../img/img-catalogo/Prod2.jpeg', 'Aura Brillo Líquido para labios', 1, 1),
(3, '../img/img-catalogo/Prod1.jpeg', 'Desmaquillante bifásico de Bob Esponja', 1, 1),
(4, '../img/img-catalogo/Prod4.jpeg', 'Delineador Colo Party', 1, 1),
(5, '../img/img-catalogo/Prod3.jpeg', 'Magic Concealer Corrector', 1, 1)
ON DUPLICATE KEY UPDATE
  url = VALUES(url),
  texto_alternativo = VALUES(texto_alternativo),
  es_principal = VALUES(es_principal);

-- 7. CARRITOS: 5 registros (3 de clientes y 2 de visitantes).
INSERT INTO carritos (id_carrito, usuario_id, token_invitado) VALUES
(1, 2, NULL),
(2, 3, NULL),
(3, 4, NULL),
(4, NULL, '00000000-0000-4000-8000-000000000004'),
(5, NULL, '00000000-0000-4000-8000-000000000005')
ON DUPLICATE KEY UPDATE
  usuario_id = VALUES(usuario_id),
  token_invitado = VALUES(token_invitado);

-- 8. DETALLE_CARRITO: 5 registros.
INSERT INTO detalle_carrito (id_detalle, carrito_id, producto_id, cantidad) VALUES
(1, 1, 1, 2),
(2, 1, 2, 1),
(3, 2, 3, 1),
(4, 4, 4, 2),
(5, 5, 5, 1)
ON DUPLICATE KEY UPDATE
  carrito_id = VALUES(carrito_id),
  producto_id = VALUES(producto_id),
  cantidad = VALUES(cantidad);

-- 9. MENSAJES: 5 registros (visitantes y usuarios registrados).
INSERT INTO mensajes
(id_mensaje, usuario_id, nombre_completo, correo, telefono, mensaje, acepta_politica_datos)
VALUES
(1, NULL, 'Visitante Uno', 'visitante1@example.com', '3100000001', 'Consulta sobre productos de skincare.', 1),
(2, 2, 'Ana Martínez', 'ana@aguacatemakeup.example', '3000000002', '¿Cuándo estará disponible nuevamente el producto?', 1),
(3, NULL, 'Visitante Tres', 'visitante3@example.com', '3100000003', 'Necesito información sobre los envíos.', 1),
(4, 3, 'Laura Pérez', 'laura@aguacatemakeup.example', '3000000003', 'Tengo una consulta sobre maquillaje.', 1),
(5, 5, 'Camila Rodríguez', 'camila@aguacatemakeup.example', '3000000005', '¿Tienen recomendaciones para piel sensible?', 1)
ON DUPLICATE KEY UPDATE
  usuario_id = VALUES(usuario_id),
  nombre_completo = VALUES(nombre_completo),
  correo = VALUES(correo),
  telefono = VALUES(telefono),
  mensaje = VALUES(mensaje),
  acepta_politica_datos = VALUES(acepta_politica_datos);

COMMIT;

-- VERIFICACION: cantidad de registros por tabla.
SELECT 'roles' AS tabla, COUNT(*) AS registros FROM roles
UNION ALL SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL SELECT 'categorias', COUNT(*) FROM categorias
UNION ALL SELECT 'subcategorias', COUNT(*) FROM subcategorias
UNION ALL SELECT 'productos', COUNT(*) FROM productos
UNION ALL SELECT 'imagenes_producto', COUNT(*) FROM imagenes_producto
UNION ALL SELECT 'carritos', COUNT(*) FROM carritos
UNION ALL SELECT 'detalle_carrito', COUNT(*) FROM detalle_carrito
UNION ALL SELECT 'mensajes', COUNT(*) FROM mensajes;

-- CONSULTA DERIVADA: disponibilidad calculada desde stock.
SELECT codigo_producto, nombre, stock,
       CASE WHEN stock > 0 THEN 'En stock' ELSE 'Agotado' END AS disponibilidad
FROM productos
ORDER BY id_producto;
