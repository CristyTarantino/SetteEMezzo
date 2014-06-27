CREATE SCHEMA IF NOT EXISTS `setteemezzo`;


CREATE TABLE IF NOT EXISTS `setteemezzo`.`utente` (
  `email` VARCHAR(20)  NOT NULL,
  `nickname` VARCHAR(20)  NOT NULL,
  `password` VARCHAR(10)  NOT NULL,
  PRIMARY KEY (`email`)
)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `setteemezzo`.`transazione` (
  `id_partita` INT(11)  NOT NULL AUTO_INCREMENT,
  `giocatore` VARCHAR(20)  NOT NULL,
  `ruolo` VARCHAR(8)  NOT NULL DEFAULT '',
  `punteggio` DOUBLE  DEFAULT NULL,
  `vincita` VARCHAR(6)  NOT NULL DEFAULT '0',
  `data` DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
  FOREIGN KEY (`giocatore`) REFERENCES `utente`(`email`) ON DELETE CASCADE,
  PRIMARY KEY (`id_partita`)
)
ENGINE = InnoDB;


GRANT SELECT, INSERT 
   ON `setteemezzo`.*
   TO 'user'@'localhost' 
   IDENTIFIED BY 'user';
