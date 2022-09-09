CREATE TABLE `alumnos` (
  `idalum` int(11) NOT NULL AUTO_INCREMENT,
  `matricula` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
  `titulo` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
  `apellidos` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
  `nombre` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
  `mail` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL DEFAULT '@alumnos.upm.es',
  `fechreg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idalum`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `asignaturas` (
  `idasig` int(11) NOT NULL AUTO_INCREMENT,
  `cod_asig` int(11) NOT NULL,
  `nombre` varchar(45) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
  `notmin` decimal(5,2) DEFAULT NULL,
  `notmax` decimal(5,2) DEFAULT NULL,
  `notmed` decimal(5,2) DEFAULT NULL,
  `fechreg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idasig`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `asignaturas` (`idasig`, `cod_asig`, `nombre`)
VALUES (1,'105000015','Prog2'),(2,'105000016','PPS'),(3,'105000021','BBDD'),(4,'105000167','ADW');

CREATE TABLE `notas` (
  `idnota` int(11) NOT NULL AUTO_INCREMENT,
  `califc` varchar(3) DEFAULT NULL,
  `notnum` decimal(5,2) DEFAULT NULL,
  `notpos` int(11) DEFAULT NULL,
  `idalum` int(11) NOT NULL,
  `idasig` int(11) NOT NULL,
  `fechreg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idnota`),
  KEY `idal_idx` (`idalum`),
  KEY `idas_idx` (`idasig`),
  CONSTRAINT `notas_ibfk_1` FOREIGN KEY (`idalum`) REFERENCES `alumnos` (`idalum`) ON UPDATE CASCADE,
  CONSTRAINT `notas_ibfk_2` FOREIGN KEY (`idasig`) REFERENCES `asignaturas` (`idasig`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Variante con ALTER TABLE
--
CREATE TABLE `notas` (
  `idnota` int(11) NOT NULL AUTO_INCREMENT,
  `califc` varchar(3) DEFAULT NULL,
  `notnum` decimal(5,2) DEFAULT NULL,
  `notpos` int(11) DEFAULT NULL,
  `idalum` int(11) NOT NULL,
  `idasig` int(11) NOT NULL,
  `fechreg` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idnota`),
  KEY `idal_idx` (`idalum`),
  KEY `idas_idx` (`idasig`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `notas`
  ADD FOREIGN KEY (`idalum`) REFERENCES `alumnos` (`idalum`) ON UPDATE CASCADE,
  ADD FOREIGN KEY (`idasig`) REFERENCES `asignaturas` (`idasig`) ON UPDATE CASCADE;
