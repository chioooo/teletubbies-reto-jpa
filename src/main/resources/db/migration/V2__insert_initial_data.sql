INSERT INTO customers (name, email, city, created_at) VALUES
    ('Ana López',       'ana.lopez@mail.com',       'CDMX',        '2025-01-10'),
    ('Carlos Pérez',    'carlos.perez@mail.com',    'Guadalajara', '2025-02-03'),
    ('María García',    'maria.garcia@mail.com',    'Monterrey',   '2025-02-20'),
    ('Jorge Ramírez',   'jorge.ramirez@mail.com',   'CDMX',        '2025-03-15'),
    ('Lucía Torres',    'lucia.torres@mail.com',    'Puebla',      '2025-04-01'),
    ('Pedro Sánchez',   'pedro.sanchez@mail.com',   'Guadalajara', '2025-05-12'),
    ('Sofía Hernández', 'sofia.hernandez@mail.com', 'Querétaro',   '2025-06-08'),
    ('Diego Morales',   'diego.morales@mail.com',   'Monterrey',   '2025-07-22');

INSERT INTO products (name, category, price, stock) VALUES
    ('Laptop 14"',           'Electrónica', 15999.00,  10),
    ('Mouse inalámbrico',    'Electrónica',   349.00,  50),
    ('Teclado mecánico',     'Electrónica',  1299.00,  25),
    ('Monitor 24"',          'Electrónica',  3499.00,  15),
    ('Silla ergonómica',     'Muebles',      4299.00,   8),
    ('Escritorio',           'Muebles',      2899.00,   5),
    ('Cuaderno profesional', 'Papelería',      65.00, 200),
    ('Paquete de plumas',    'Papelería',      45.00, 150),
    ('Mochila',              'Accesorios',    799.00,  30),
    ('Audífonos',            'Electrónica',   999.00,  20);

INSERT INTO orders (customer_id, order_date, status) VALUES
    (1, '2025-03-01', 'ENTREGADO'),
    (1, '2025-05-18', 'ENTREGADO'),
    (2, '2025-03-10', 'ENTREGADO'),
    (3, '2025-04-05', 'CANCELADO'),
    (3, '2025-06-22', 'ENTREGADO'),
    (4, '2025-07-01', 'ENVIADO'),
    (5, '2025-07-15', 'PENDIENTE'),
    (6, '2025-08-02', 'ENTREGADO'),
    (6, '2025-08-30', 'ENVIADO'),
    (2, '2025-09-05', 'PENDIENTE');

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
    (1,  1,  1, 15999.00),
    (1,  2,  1,   349.00),
    (2,  7,  5,    65.00),
    (2,  8,  3,    45.00),
    (3,  3,  1,  1299.00),
    (3,  4,  2,  3499.00),
    (4,  5,  1,  4299.00),
    (5,  9,  1,   799.00),
    (5,  2,  2,   349.00),
    (6,  1,  1, 15999.00),
    (6, 10,  1,   999.00),
    (7,  7, 10,    65.00),
    (7,  8, 10,    45.00),
    (8,  4,  1,  3499.00),
    (8,  3,  1,  1299.00),
    (8,  2,  1,   349.00),
    (9, 10,  2,   999.00),
    (10, 9,  1,   799.00),
    (10, 7,  3,    65.00);
