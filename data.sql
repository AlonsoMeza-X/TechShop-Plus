-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS techshop_plus;
USE techshop_plus;

-- Tabla de clientes
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de pedidos
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Tabla de items de pedido
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Datos de prueba: Clientes
INSERT INTO customers (name, email) VALUES ('Juan Pérez', 'juan.perez@email.com');
INSERT INTO customers (name, email) VALUES ('María García', 'maria.garcia@email.com');
INSERT INTO customers (name, email) VALUES ('Carlos López', 'carlos.lopez@email.com');
INSERT INTO customers (name, email) VALUES ('Ana Martínez', 'ana.martinez@email.com');
INSERT INTO customers (name, email) VALUES ('Roberto Sánchez', 'roberto.sanchez@email.com');

-- Datos de prueba: Pedidos
INSERT INTO orders (customer_id, order_date, status) VALUES (1, '2024-01-15 10:00:00', 'COMPLETED');
INSERT INTO orders (customer_id, order_date, status) VALUES (1, '2024-02-01 15:30:00', 'PENDING');
INSERT INTO orders (customer_id, order_date, status) VALUES (2, '2024-02-10 09:15:00', 'COMPLETED');
INSERT INTO orders (customer_id, order_date, status) VALUES (3, '2024-02-15 14:20:00', 'PENDING');
INSERT INTO orders (customer_id, order_date, status) VALUES (4, '2024-02-20 11:45:00', 'COMPLETED');

-- Datos de prueba: Items de pedido
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (1, 'Laptop Dell XPS 13', 1299.99, 1);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (1, 'Mouse Logitech MX', 99.99, 2);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (2, 'Monitor LG 27"', 299.99, 1);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (3, 'Teclado Mecánico', 149.99, 1);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (3, 'Webcam HD', 79.99, 1);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (4, 'iPad Pro', 899.99, 1);
INSERT INTO order_items (order_id, product_name, price, quantity) VALUES (5, 'AirPods Pro', 249.99, 2);

-- Actualizar totales de pedidos
UPDATE orders o 
SET total_amount = (
    SELECT SUM(price * quantity) 
    FROM order_items 
    WHERE order_id = o.id
);