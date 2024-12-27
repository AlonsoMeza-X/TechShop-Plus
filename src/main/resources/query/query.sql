-- Analisis de Ordernes por Cliente
-- Crear una consulta que muestre para cada cliente:
-- 1 Nombre del cliente
-- 2.Total de órdenes realizadas
-- 3.Monto total de todas sus compras
-- 4.Fecha de su primera y última orden
-- 5.Promedio de productos por orden
-- Seleccionamos los datos que queremos obtener
SELECT
    c.name as nombre_cliente,  -- El nombre del cliente (de la tabla 'customers')
    COUNT(o.id) as total_ordenes_realizadas,  -- El número total de órdenes realizadas por el cliente (de la tabla 'orders')
    SUM(o.total_amount) as monto_total,  -- La suma total de los montos de todas las órdenes (de la tabla 'orders')
    MIN(o.order_date) as primera_orden,  -- La fecha de la primera orden realizada por el cliente (de la tabla 'orders')
    MAX(o.order_date) as ultima_orden,  -- La fecha de la última orden realizada por el cliente (de la tabla 'orders')
    AVG(order_counts.contar_productos_por_orden) as promedio_de_productos_por_orden  -- El promedio de productos por orden (calculado a partir de la subconsulta)
FROM
    orders o  -- De la tabla 'orders', alias 'o'

    -- Unimos la tabla 'customers' para obtener información del cliente
        INNER JOIN
    customers c ON o.customer_id = c.id  -- Realizamos la unión entre 'orders' y 'customers' utilizando el 'customer_id' y 'id' del cliente

    -- Unimos la tabla 'order_items' para obtener los detalles de los productos de las órdenes
        LEFT JOIN
    order_items oi ON o.id = oi.order_id  -- Realizamos una unión izquierda con 'order_items', para obtener productos de cada orden. Asegura que se incluyan todas las órdenes, incluso si no tienen productos.

    -- Unimos una subconsulta que cuenta el número de productos por orden
        LEFT JOIN(
        SELECT order_id, COUNT(id) AS contar_productos_por_orden  -- Contamos los productos en cada orden
        FROM order_items  -- De la tabla 'order_items'
        GROUP BY order_id  -- Agrupamos por 'order_id' para contar los productos de cada orden
    ) AS order_counts ON o.id = order_counts.order_id  -- Unimos la subconsulta con la tabla 'orders' usando 'order_id' para obtener el número de productos por orden

-- Filtramos para obtener los datos solo del cliente con 'id' = 1
WHERE
    c.id = 1  -- Especificamos que queremos los datos solo del cliente con id = 1

-- Agrupamos los resultados por cliente
GROUP BY
    c.id;  -- Agrupamos por el 'id' del cliente para obtener los resultados por cliente

-- Top Productos Vendidos
--  Desarrollar una consulta que muestre:
-- 1. Nombre del producto
-- 2. Cantidad total vendida
-- 3. Número de órdenes diferentes en las que aparece
-- 4. Precio promedio al que se ha vendido
-- 5. Monto total generado
-- 6. La consulta debe ordenar los resultados por monto total generado de manera descendente

SELECT
    oi.order_items,
    COUNT(o.status WHERE 'Completed' and 'Cancelled'),

    o.total_amount
FROM
    orders o
        INNER JOIN
    customers c ON o.customer_id = c.id
        LEFT JOIN
    order_items oi ON o.id = oi.order_id
GROUP BY o.id, c.id
ORDER BY o.total_amount DESC





