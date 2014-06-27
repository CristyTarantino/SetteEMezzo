CREATE SCHEMA IF NOT EXISTS `setteemezzo_standalone`;

CREATE TABLE IF NOT EXISTS `setteemezzo_standalone`.`transazione` (
  `id_partita` INT(11)  NOT NULL AUTO_INCREMENT,
  `giocatore` VARCHAR(20)  NOT NULL,
  `ruolo` VARCHAR(8)  NOT NULL DEFAULT '',
  `punteggio` DOUBLE  DEFAULT NULL,
  `vincita` VARCHAR(6)  NOT NULL DEFAULT '0',
  `data` DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id_partita`)
)
ENGINE = InnoDB;

GRANT SELECT, INSERT 
   ON `setteemezzo_standalone`.*
   TO 'user'@'localhost' 
   IDENTIFIED BY 'user';
