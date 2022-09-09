CREATE TABLE IF NOT EXISTS`club` (
	`nombre` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `cp` int(5) NOT NULL,
    `localidad` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `telefono`int(9) NOT NULL,
    `persona_contacto` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `escalera` tinyint NOT NULL,
    `numero` tinyint NOT NULL,
    `calle` varchar(100) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
 PRIMARY KEY(`nombre`)
);

CREATE TABLE IF NOT EXISTS `categoria_edad`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `descripcion` mediumtext CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `edad_minima` tinyint NOT NULL,
    `edad_maxima` tinyint NOT NULL,
PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `categoria_competicion`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `descripcion` mediumtext CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `numero_max_equipos` int NOT NULL,
PRIMARY KEY(`id`)
);
    
CREATE TABLE IF NOT EXISTS `equipo` (
     `licencia` varchar(100) NOT NULL,
    `nombre`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `telefono`int(9) NOT NULL,
    `nombre_club` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `id_categoria_edad` int(11),
    `id_categoria_competicion` int(11),
PRIMARY KEY(licencia),
KEY `id_categoria_edadx`(`id_categoria_edad`),
KEY `id_categoria_competicionx`(`id_categoria_competicion`)
);     

ALTER TABLE `equipo`
	ADD FOREIGN KEY (`id_categoria_edad`) REFERENCES `categoria_edad`(`id`) ON UPDATE CASCADE,
	ADD FOREIGN KEY (`id_categoria_competicion`) REFERENCES `categoria_competicion`(`id`) ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS`centro_deportivo` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `calle`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `numero` tinyint NOT NULL, 
    `cp` int(5) NOT NULL,
    `localidad`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `arbitro`(
	`numero_colegiado`int(5) NOT NULL,
    `nombre`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `apellido1`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `apellido2`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
PRIMARY KEY (`numero_colegiado`)
);

CREATE TABLE IF NOT EXISTS `tipo_evento`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `nombre`varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `descripcion` MEDIUMTEXT CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `jugador`(
	`nif` int(11) NOT NULL,
    `nombre` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `apellido1` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `apellido2` varchar(10) CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
    `fecha_de_nacimiento` DATE NOT NULL,

PRIMARY KEY (`nif`)
);

CREATE TABLE IF NOT EXISTS `acta`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `tantos_equipo_visitante` int(6) NOT NULL,
    `tantos_equipo_local` int(6) NOT NULL,
    `hora_inicio`TIME NOT NULL,

PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `partido`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `fecha` DATE NOT NULL,
    
PRIMARY KEY (`id`),
    `id_acta` int(11) NOT NULL,
    `licencia_equipo_local` varchar (100) NOT NULL,
    `licencia_equipo_visitante` varchar (100) NOT NULL,
    `numero_colegiado_arbitro` int(11) NOT NULL,
    `id_centro_deportivo` int(11) NOT NULL
);

ALTER TABLE `partido`
	ADD FOREIGN KEY (`id_acta`) REFERENCES `acta`(`id`) ON UPDATE CASCADE,
    ADD FOREIGN KEY (`licencia_equipo_local`) REFERENCES `equipo`(`licencia`) ON UPDATE CASCADE,
    ADD FOREIGN KEY (`licencia_equipo_visitante`) REFERENCES `equipo`(`licencia`) ON UPDATE CASCADE,
    ADD FOREIGN KEY (`numero_colegiado_arbitro`) REFERENCES `arbitro`(`numero_colegiado`) ON UPDATE CASCADE,
	ADD FOREIGN KEY (`id_centro_deportivo`) REFERENCES `centro_deportivo`(`id`) ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS `evento`(
	`id` int(11) NOT NULL REFERENCES `acta`(`id`),
    `hora_partido` Timestamp NOT NULL,
    `descripción` MEDIUMTEXT CHARACTER SET latin1 COLLATE latin1_spanish_ci NOT NULL,
PRIMARY KEY (`id`, `hora_partido`),
    `nif_futbolista` int(11) NOT NULL,
    `id_tipo_evento` int(11) NOT NULL,
    KEY `nif_futbolistax` (`nif_futbolista`),
    CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`nif_futbolista`) REFERENCES `jugador` (`nif`) ON UPDATE CASCADE,
    KEY `id_tipo_eventox` (`id_tipo_evento`),
    CONSTRAINT `evento_ibfk_2` FOREIGN KEY (`id_tipo_evento`) REFERENCES `tipo_evento` (`id`) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `Jugador_milita_equipo`(
	`fecha_inicio` DATE NOT NULL,
    `licencia_equipo` int(11) NOT NULL REFERENCES `equipo`(`licencia`),
    `nif_futbolista` int(11) NOT NULL REFERENCES `jugador`(`nif`),
    `fecha_fin` DATE NOT NULL,

PRIMARY KEY (fecha_inicio, licencia_equipo, nif_futbolista, fecha_fin)
);

CREATE TABLE IF NOT EXISTS `Jugador_registra_acta`(
	`nif_futbolista` int(11) NOT NULL REFERENCES `jugador`(`nif`),
    `id_acta` int(11) NOT NULL REFERENCES `acta`(`id`),

PRIMARY KEY (nif_futbolista, id_acta)
);


