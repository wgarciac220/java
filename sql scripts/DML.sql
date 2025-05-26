USE `biblioteca`;

-- Roles
INSERT INTO `rol` (`nombreRol`) 
VALUES 
  ('Administrador'), 
  ('Bibliotecario'), 
  ('Miembro');

-- Usuarios
INSERT INTO `usuario` (`name`, `email`, `fotografia`, `fechaRegistro`, `contrasena`, `rolId`, `salt`) 
VALUES 
  ('William Garcia', 'wgarcia1@miumg.edu.gt', '', '2025-05-01', '+Lyo177VFhebwuIOzEnTxd36p3JPzoIntckQDjjngyQ=', '1', '/oPp05/Pa7Lj9mp2xobzFw=='),
  ('Mariana Mendoza', 'mmendoza@gmail.com', '', '2025-05-02', 'qpa6JndkiiKQp8grrhDzwqH2fdn4Ch8VRHdYE9AH8tM=', '2', 'aYjORV31RWCNgWtQ46S4ZA=='),
  ('Daphne Garcia', 'dgarcia@gmail.com', '', '2025-05-10', 'FzdlQskxZEH/mGAeSg8dY45yOwbcH7kNozzpxDKh+A4=', '3', 'CpTmb9hKOQrcYefNYrDQ1A==');

-- Autores
INSERT INTO `autor` (`id`, `nombre`, `biografia`, `paisOrigen`, `fotografia`) 
VALUES 
  (1, 'Miguel de Cervantes', 'Fue un novelista, poeta, dramaturgo y soldado español. Es ampliamente considerado una de las máximas figuras de la literatura española.', 'España', ''),
  (2, 'William Shakespeare', 'Fue un dramaturgo, poeta y actor inglés. Conocido en ocasiones como el Bardo de Avon, se le considera el escritor más importante en lengua inglesa y como uno de los más célebres de la literatura universal.​Según la Enciclopedia Británica', 'Reino Unido', ''),
  (3, 'Autor desconocido', 'Autor completamente desconido, su pais de origen se asume por el contenido de sus obras literarias', 'Arabe', '');

-- Libros
INSERT INTO `libro` (`id`, `titulo`, `isbn`, `genero`, `cantidad`, `portada`, `anoPublicacion`, `editorial`, `idioma`, `rating`, `estado`, `autorId`, `prestados`) 
VALUES 
  (1, 'Hamlet', '1475168241', 'Ficcion y Poesia', 3, '', 2012, 'CreateSpace Independent Publishing Platform', 'Español', 0.0, 'Disponible', 2, 0),
  (2, 'Don Quijote de La Mancha', '8471664577', 'Aventura', 10, '', 2001, 'Edaf Antillas', 'Español', 0.0, 'Disponible', 1, 0),
  (3, 'La Banana es Amarilla', '12313548', 'Ficcion', 0, 'C:\\Users\\willc\\Downloads\\Imagen de WhatsApp 2025-04-25 a las 06.43.11_8d86826f.jpg', 1973, 'Santa Piedra', 'Español', 0.0, 'Retirado', 3, 0);

