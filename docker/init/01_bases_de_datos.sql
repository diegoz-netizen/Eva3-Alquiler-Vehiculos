-- Una base de datos por microservicio y un usuario por servicio:
-- cada servicio solo puede entrar a SU base de datos (no comparten tablas).
-- Credenciales solo para desarrollo local, no son reales.

CREATE DATABASE IF NOT EXISTS bd_cliente  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_vehiculo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_alquiler CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'cliente_user'@'%'  IDENTIFIED BY 'cliente_dev';
CREATE USER IF NOT EXISTS 'vehiculo_user'@'%' IDENTIFIED BY 'vehiculo_dev';
CREATE USER IF NOT EXISTS 'alquiler_user'@'%' IDENTIFIED BY 'alquiler_dev';

GRANT ALL PRIVILEGES ON bd_cliente.*  TO 'cliente_user'@'%';
GRANT ALL PRIVILEGES ON bd_vehiculo.* TO 'vehiculo_user'@'%';
GRANT ALL PRIVILEGES ON bd_alquiler.* TO 'alquiler_user'@'%';
FLUSH PRIVILEGES;
