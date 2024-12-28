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
    oi.product_name AS nombre_producto, -- Seleccionamos el nombre del producto para identificarlo.
    SUM(oi.quantity) AS cantidad_total_vendida, -- Sumamos la cantidad de unidades vendidas de cada producto.
    COUNT(DISTINCT oi.order_id) AS numero_de_ordenes, -- Contamos cuántas órdenes diferentes incluyen este producto.
    AVG(oi.price) AS precio_promedio, -- Calculamos el precio promedio al que se ha vendido el producto.
    SUM(oi.price * oi.quantity) AS monto_total_generado -- Multiplicamos precio por cantidad para cada fila y sumamos para obtener el total generado.
FROM
    order_items oi -- Trabajamos sobre la tabla `order_items` que contiene los detalles de cada producto vendido.
GROUP BY
    oi.product_name -- Agrupamos los datos por el nombre del producto para obtener resultados específicos por producto.
ORDER BY
    monto_total_generado DESC; -- Ordenamos los resultados por el monto total generado, de mayor a menor.


-- Análisis de Ventas por Periodo
-- Implementar una consulta que muestre por mes:
-- 1. Mes y año
-- 2. Cantidad de órdenes
-- 3. Número de clientes diferentes que compraron
-- 4. Total de productos vendidos
-- 5. Monto total de ventas
-- 6. Comparación porcentual de ventas respecto al mes anterior

SELECT
    -- Formateamos la fecha del pedido para que muestre únicamente el año y mes (en formato 'YYYY-MM').
    DATE_FORMAT(o.order_date, '%Y-%m') AS mes_anio,

    -- Contamos el número total de órdenes distintas en cada mes.
    COUNT(DISTINCT o.id) AS cantidad_de_ordenes,

    -- Contamos el número de clientes únicos que realizaron pedidos en cada mes.
    COUNT(DISTINCT o.customer_id) AS clientes_diferentes,

    -- Calculamos la suma total de los productos vendidos (cantidad) en cada mes.
    SUM(oi.quantity) AS total_productos_vendidos,

    -- Calculamos el monto total generado por las ventas (precio * cantidad) en cada mes.
    SUM(oi.price * oi.quantity) AS monto_total_ventas,

    -- Calculamos la comparación porcentual de las ventas con respecto al mes anterior:
    -- Usamos la función LAG para obtener el monto total de ventas del mes anterior.
    (SUM(oi.price * oi.quantity) -
     LAG(SUM(oi.price * oi.quantity)) OVER (ORDER BY DATE_FORMAT(o.order_date, '%Y-%m'))
        ) /
    LAG(SUM(oi.price * oi.quantity)) OVER (ORDER BY DATE_FORMAT(o.order_date, '%Y-%m')) * 100
    AS comparacion_porcentual
FROM
    -- Usamos la tabla de órdenes como base.
    orders o

        -- Hacemos un LEFT JOIN con la tabla de productos en las órdenes, para obtener información de cada producto.
        LEFT JOIN
    order_items oi ON o.id = oi.order_id

WHERE
    -- Filtramos las órdenes que estén en el rango de fechas específico (en este caso, diciembre de 2024).
    o.order_date BETWEEN '2024-12-01' AND '2024-12-31'

GROUP BY
    -- Agrupamos los resultados por mes y año.
    mes_anio

ORDER BY
    -- Ordenamos los resultados de manera cronológica, mes por mes.
    mes_anio;

