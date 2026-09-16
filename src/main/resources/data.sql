-- =====================
-- USERS
-- =====================
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('karim.alaoui', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('fatima.benali', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('youssef.tazi', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('nadia.chraibi', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('hassan.idrissi', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('sara.mansouri', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, role) VALUES ('omar.berrada', 'pass123', 'CLIENT') ON CONFLICT (username) DO NOTHING;

-- =====================
-- CLIENTS
-- =====================
INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Karim Alaoui', 'karim.alaoui@techmaroc.ma', '0661234567', 'Bd Mohammed V, Casablanca', 'PLATINUM', 25, 18500.00, '2024-01-10 09:00:00', '2024-11-20 14:30:00', (SELECT id FROM users WHERE username = 'karim.alaoui'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Fatima Benali', 'fatima.benali@infonet.ma', '0662345678', 'Av Hassan II, Rabat', 'GOLD', 12, 7200.00, '2024-02-15 10:00:00', '2024-11-18 11:00:00', (SELECT id FROM users WHERE username = 'fatima.benali'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Youssef Tazi', 'youssef.tazi@digitalpro.ma', '0663456789', 'Rue Ibn Sina, Fès', 'GOLD', 11, 6100.00, '2024-03-05 08:30:00', '2024-11-15 16:00:00', (SELECT id FROM users WHERE username = 'youssef.tazi'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Nadia Chraibi', 'nadia.chraibi@smartbiz.ma', '0664567890', 'Quartier Industriel, Tanger', 'SILVER', 5, 2300.00, '2024-05-20 09:00:00', '2024-11-10 13:00:00', (SELECT id FROM users WHERE username = 'nadia.chraibi'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Hassan Idrissi', 'hassan.idrissi@maroc-it.ma', '0665678901', 'Av des FAR, Marrakech', 'SILVER', 4, 1800.00, '2024-06-01 10:00:00', '2024-11-05 15:00:00', (SELECT id FROM users WHERE username = 'hassan.idrissi'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Sara Mansouri', 'sara.mansouri@techsud.ma', '0666789012', 'Bd Zerktouni, Agadir', 'BASIC', 2, 850.00, '2024-09-10 11:00:00', '2024-10-22 10:00:00', (SELECT id FROM users WHERE username = 'sara.mansouri'))
ON CONFLICT (email) DO NOTHING;

INSERT INTO clients (nom, email, telephone, adresse, tier, total_orders, total_spent, first_order_date, last_order_date, user_id)
VALUES ('Omar Berrada', 'omar.berrada@netplus.ma', '0667890123', 'Rue Allal Ben Abdellah, Meknès', 'BASIC', 1, 420.00, '2024-10-30 09:30:00', '2024-10-30 09:30:00', (SELECT id FROM users WHERE username = 'omar.berrada'))
ON CONFLICT (email) DO NOTHING;

-- =====================
-- PRODUCTS
-- =====================
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Laptop HP ProBook 450', 'Intel Core i5, 8GB RAM, 256GB SSD', 8500.00, 30, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Dell Monitor 24"', 'Full HD IPS, 75Hz, HDMI/VGA', 1950.00, 50, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Clavier Logitech MK270', 'Sans fil, AZERTY, récepteur USB', 280.00, 100, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Switch TP-Link 8 ports', 'Gigabit non manageable, plug & play', 420.00, 60, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Imprimante HP LaserJet Pro', 'Monochrome, réseau Wi-Fi, recto-verso', 3200.00, 20, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Disque Dur Externe 1TB', 'USB 3.0, portable, Seagate', 650.00, 80, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Routeur Cisco RV160', 'VPN, 4 ports LAN, sécurité avancée', 1800.00, 25, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Webcam Logitech C920', 'Full HD 1080p, micro intégré', 750.00, 45, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Onduleur APC 650VA', 'Protection contre les coupures, 4 prises', 980.00, 35, false) ON CONFLICT DO NOTHING;
INSERT INTO products (nom, description, prix_unitaire, stock, deleted) VALUES ('Câble RJ45 Cat6 (10m)', 'Blindé, haute vitesse, 10 mètres', 55.00, 200, false) ON CONFLICT DO NOTHING;

-- =====================
-- ORDERS
-- =====================
-- Order 1 - Karim (PLATINUM) - CONFIRMED
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'karim.alaoui@techmaroc.ma'),
    '2024-11-20 14:30:00', 10450.00, 1567.50, 8882.50, 1776.50, 10659.00, 0.00, 'PLAT20', 'CONFIRMED', 0.20
);

-- Order 2 - Fatima (GOLD) - CONFIRMED
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'fatima.benali@infonet.ma'),
    '2024-11-18 11:00:00', 4150.00, 290.50, 3859.50, 771.90, 4631.40, 0.00, NULL, 'CONFIRMED', 0.20
);

-- Order 3 - Youssef (GOLD) - PENDING
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'youssef.tazi@digitalpro.ma'),
    '2024-11-15 16:00:00', 2230.00, 156.10, 2073.90, 414.78, 2488.68, 2488.68, NULL, 'PENDING', 0.20
);

-- Order 4 - Nadia (SILVER) - CONFIRMED
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'nadia.chraibi@smartbiz.ma'),
    '2024-11-10 13:00:00', 1300.00, 0.00, 1300.00, 260.00, 1560.00, 0.00, NULL, 'CONFIRMED', 0.20
);

-- Order 5 - Hassan (SILVER) - CANCELED
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'hassan.idrissi@maroc-it.ma'),
    '2024-11-05 15:00:00', 840.00, 0.00, 840.00, 168.00, 1008.00, 1008.00, NULL, 'CANCELED', 0.20
);

-- Order 6 - Sara (BASIC) - PENDING
INSERT INTO orders (client_id, date_creation, sous_total, montant_remise, montant_ht_apres_remise, tva, total_ttc, montant_restant, code_promo, status, taux_tva)
VALUES (
    (SELECT id FROM clients WHERE email = 'sara.mansouri@techsud.ma'),
    '2024-10-22 10:00:00', 705.00, 0.00, 705.00, 141.00, 846.00, 846.00, NULL, 'PENDING', 0.20
);

-- =====================
-- ORDER ITEMS
-- =====================
-- Order 1 items (Karim)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'karim.alaoui@techmaroc.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Laptop HP ProBook 450'), 1, 8500.00, 8500.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'karim.alaoui@techmaroc.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Dell Monitor 24"'), 1, 1950.00, 1950.00);

-- Order 2 items (Fatima)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'fatima.benali@infonet.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Imprimante HP LaserJet Pro'), 1, 3200.00, 3200.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'fatima.benali@infonet.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Clavier Logitech MK270'), 2, 280.00, 560.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'fatima.benali@infonet.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Câble RJ45 Cat6 (10m)'), 7, 55.00, 385.00);

-- Order 3 items (Youssef)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'youssef.tazi@digitalpro.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Switch TP-Link 8 ports'), 2, 420.00, 840.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'youssef.tazi@digitalpro.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Webcam Logitech C920'), 1, 750.00, 750.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'youssef.tazi@digitalpro.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Câble RJ45 Cat6 (10m)'), 12, 55.00, 660.00);

-- Order 4 items (Nadia)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'nadia.chraibi@smartbiz.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Disque Dur Externe 1TB'), 2, 650.00, 1300.00);

-- Order 5 items (Hassan)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'hassan.idrissi@maroc-it.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Onduleur APC 650VA'), 1, 980.00, 980.00);

-- Order 6 items (Sara)
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'sara.mansouri@techsud.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Clavier Logitech MK270'), 1, 280.00, 280.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'sara.mansouri@techsud.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Câble RJ45 Cat6 (10m)'), 5, 55.00, 275.00);
INSERT INTO order_items (order_id, product_id, quantite, prix_unitaire, total_ligne)
VALUES ((SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'sara.mansouri@techsud.ma') LIMIT 1),
        (SELECT id FROM products WHERE nom = 'Webcam Logitech C920'), 1, 750.00, 750.00);

-- =====================
-- PAYMENTS
-- =====================
-- Payment for Order 1 (Karim - fully paid via virement)
INSERT INTO payments (order_id, numero_paiement, montant, type_paiement, date_paiement, date_encaissement, status, reference, banque, date_echeance)
VALUES (
    (SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'karim.alaoui@techmaroc.ma') LIMIT 1),
    1, 10659.00, 'VIREMENT', '2024-11-20 15:00:00', '2024-11-21', 'ENCAISSE', 'VIR-2024-001', 'Attijariwafa Bank', NULL
);

-- Payment for Order 2 (Fatima - cheque)
INSERT INTO payments (order_id, numero_paiement, montant, type_paiement, date_paiement, date_encaissement, status, reference, banque, date_echeance)
VALUES (
    (SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'fatima.benali@infonet.ma') LIMIT 1),
    1, 4631.40, 'CHEQUE', '2024-11-18 12:00:00', '2024-11-25', 'ENCAISSE', 'CHQ-2024-045', 'CIH Bank', '2024-11-25'
);

-- Payment for Order 3 (Youssef - pending virement)
INSERT INTO payments (order_id, numero_paiement, montant, type_paiement, date_paiement, date_encaissement, status, reference, banque, date_echeance)
VALUES (
    (SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'youssef.tazi@digitalpro.ma') LIMIT 1),
    1, 2488.68, 'VIREMENT', '2024-11-15 17:00:00', NULL, 'EN_ATTENTE', 'VIR-2024-002', 'BMCE Bank', '2024-11-30'
);

-- Payment for Order 4 (Nadia - especes)
INSERT INTO payments (order_id, numero_paiement, montant, type_paiement, date_paiement, date_encaissement, status, reference, banque, date_echeance)
VALUES (
    (SELECT id FROM orders WHERE client_id = (SELECT id FROM clients WHERE email = 'nadia.chraibi@smartbiz.ma') LIMIT 1),
    1, 1560.00, 'ESPECES', '2024-11-10 14:00:00', '2024-11-10', 'ENCAISSE', NULL, NULL, NULL
);
