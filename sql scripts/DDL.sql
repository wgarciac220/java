-- Schema biblioteca
CREATE SCHEMA IF NOT EXISTS `biblioteca` DEFAULT CHARACTER SET utf8 ;
USE `biblioteca` ;

-- Opcional: Eliminación de tablas (descomenta para hard reset)

DROP TABLE IF EXISTS `Resena`;
DROP TABLE IF EXISTS `HistorialPrestamos`;
DROP TABLE IF EXISTS `RecordarUsuario`;
DROP TABLE IF EXISTS `Libro`;
DROP TABLE IF EXISTS `Usuario`;
DROP TABLE IF EXISTS `Autor`;
DROP TABLE IF EXISTS `Rol`;


-- Table Rol
CREATE TABLE IF NOT EXISTS `Rol` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombreRol` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- Table Usuario
CREATE TABLE IF NOT EXISTS `Usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `fotografia` VARCHAR(255) NOT NULL,
  `fechaRegistro` DATETIME NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `rolId` INT NOT NULL,
  `salt` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `RolID_idx` (`rolId` ASC) VISIBLE,
  CONSTRAINT `RolID`
    FOREIGN KEY (`rolId`)
    REFERENCES `Rol` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- Table Autor
CREATE TABLE IF NOT EXISTS `Autor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(20) NOT NULL,
  `biografia` VARCHAR(255) NOT NULL,
  `paisOrigen` VARCHAR(20) NOT NULL,
  `fotografia` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- Table Libro
CREATE TABLE IF NOT EXISTS `Libro` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(50) NOT NULL,
  `isbn` VARCHAR(20) NOT NULL,
  `genero` VARCHAR(20) NOT NULL,
  `cantidad` INT NOT NULL DEFAULT 0,
  `portada` VARCHAR(255) NOT NULL,
  `anoPublicacion` VARCHAR(4) NOT NULL,
  `editorial` VARCHAR(50) NOT NULL,
  `idioma` VARCHAR(20) NOT NULL,
  `rating` DECIMAL(2,1) NOT NULL DEFAULT 0,
  `estado` VARCHAR(10) NOT NULL,
  `autorId` INT NOT NULL,
    `prestados` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `AutorID_idx` (`autorId` ASC) VISIBLE,
  CONSTRAINT `AutorID`
    FOREIGN KEY (`autorId`)
    REFERENCES `Autor` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- Table HistorialPrestamos
CREATE TABLE IF NOT EXISTS `HistorialPrestamos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuarioId` INT NOT NULL,
  `libroId` INT NOT NULL,
  `fechaPrestamo` DATETIME NOT NULL,
  `estado` VARCHAR(25) NOT NULL,
  `fechaDevolucion` DATETIME NOT NULL,
  `multa` DECIMAL(3,0) NOT NULL DEFAULT 0,
  `resena` VARCHAR(255) NOT NULL,
  `rating` DECIMAL(2,1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `UsuarioID_idx` (`usuarioId` ASC) VISIBLE,
  INDEX `LibroID_idx` (`libroId` ASC) VISIBLE,
  CONSTRAINT `UsuarioID_Historial`
    FOREIGN KEY (`usuarioId`)
    REFERENCES `Usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `LibroID_Historial`
    FOREIGN KEY (`libroId`)
    REFERENCES `Libro` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

-- Table RecordarUsuario
CREATE TABLE IF NOT EXISTS `RecordarUsuario` (
  `email` VARCHAR(50) NOT NULL,
  `recordar` TINYINT(1) NOT NULL DEFAULT 0)
ENGINE = InnoDB;

-- Triggers
DELIMITER $$

CREATE TRIGGER actualizar_rating_libro
AFTER UPDATE ON HistorialPrestamos
FOR EACH ROW
BEGIN
    IF NEW.rating IS NOT NULL THEN
        UPDATE Libro
        SET rating = (
            SELECT AVG(rating)
            FROM HistorialPrestamos
            WHERE libroId = NEW.libroId AND rating IS NOT NULL
        )
        WHERE id = NEW.libroId;
    END IF;
END$$

CREATE TRIGGER sumar_prestado_despues_insert
AFTER INSERT ON HistorialPrestamos
FOR EACH ROW
BEGIN
    IF LOWER(NEW.estado) = 'prestado' THEN
        UPDATE Libro
        SET prestados = prestados + 1
        WHERE id = NEW.libroId;
    END IF;
END$$

CREATE TRIGGER ajustar_prestados_por_estado
AFTER UPDATE ON HistorialPrestamos
FOR EACH ROW
BEGIN
    IF LOWER(NEW.estado) = 'devuelto' THEN
        UPDATE Libro
        SET prestados = prestados - 1
        WHERE id = NEW.libroId;
    END IF;
END$$

DELIMITER ;

