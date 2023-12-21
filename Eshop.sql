-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Час створення: Гру 21 2023 р., 18:08
-- Версія сервера: 10.4.27-MariaDB
-- Версія PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База даних: `Eshop`
--

-- --------------------------------------------------------

--
-- Структура таблиці `kosik`
--

CREATE TABLE `kosik` (
  `ID` int(11) NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Дамп даних таблиці `kosik`
--

INSERT INTO `kosik` (`ID`, `ID_pouzivatela`, `ID_tovaru`, `cena`, `ks`) VALUES
(1, 4, 2, 69, 1);

-- --------------------------------------------------------

--
-- Структура таблиці `obj_polozky`
--

CREATE TABLE `obj_polozky` (
  `ID` int(11) NOT NULL,
  `ID_objednavky` int(11) NOT NULL,
  `ID_tovaru` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `ks` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Дамп даних таблиці `obj_polozky`
--

INSERT INTO `obj_polozky` (`ID`, `ID_objednavky`, `ID_tovaru`, `cena`, `ks`) VALUES
(2, 2, 2, 69, 1),
(3, 2, 1, 36, 1),
(4, 3, 3, 77, 1),
(5, 3, 1, 36, 2),
(6, 4, 4, 56, 1),
(7, 5, 3, 77, 1),
(8, 6, 1, 36, 2),
(9, 7, 1, 36, 3),
(11, 9, 2, 69, 1),
(12, 11, 5, 10, 1),
(15, 14, 2, 69, 1),
(19, 18, 2, 34, 1),
(20, 19, 5, 5, 2),
(21, 19, 2, 34, 3),
(22, 20, 2, 69, 1),
(23, 21, 5, 5, 1),
(24, 21, 3, 38, 1),
(25, 21, 4, 28, 1),
(26, 21, 2, 34, 1),
(27, 21, 7, 23, 1),
(28, 21, 8, 9, 1),
(29, 21, 9, 9, 1),
(30, 21, 10, 39, 1),
(31, 21, 11, 13, 1),
(34, 24, 2, 34, 5),
(35, 25, 2, 34, 1),
(36, 26, 2, 34, 1),
(37, 27, 2, 34, 1);

-- --------------------------------------------------------

--
-- Структура таблиці `obj_zoznam`
--

CREATE TABLE `obj_zoznam` (
  `ID` int(11) NOT NULL,
  `obj_cislo` varchar(20) NOT NULL,
  `datum_objednavky` date NOT NULL,
  `ID_pouzivatela` int(11) NOT NULL,
  `suma` int(11) NOT NULL,
  `stav` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Дамп даних таблиці `obj_zoznam`
--

INSERT INTO `obj_zoznam` (`ID`, `obj_cislo`, `datum_objednavky`, `ID_pouzivatela`, `suma`, `stav`) VALUES
(2, 'ORDER1702678138734', '2023-12-15', 3, 105, 'Shipped'),
(3, 'ORDER1702678452390', '2023-12-15', 3, 149, 'Shipped'),
(4, 'ORDER1702678664424', '2023-12-15', 3, 56, 'Shipped'),
(5, 'ORDER1702761962616', '2023-12-16', 3, 77, 'Shipped'),
(6, 'ORDER1702762697280', '2023-12-16', 4, 72, 'Shipped'),
(7, 'ORDER1702762714648', '2023-12-16', 4, 108, 'Shipped'),
(9, 'ORDER1702815597845', '2023-12-17', 3, 69, 'Shipped'),
(21, 'ORDER1702934653492', '2023-12-18', 3, 201, 'Shipped'),
(11, 'ORDER1702826006209', '2023-12-17', 3, 10, 'Processed'),
(14, 'ORDER1702828300352', '2023-12-17', 3, 69, 'Processed'),
(23, 'ORDER1703018573018', '2023-12-19', 5, 69, 'Processed'),
(18, 'ORDER1702829107237', '2023-12-17', 3, 34, 'Paid'),
(19, 'ORDER1702846351613', '2023-12-17', 3, 113, 'Paid'),
(20, 'ORDER1702846801424', '2023-12-17', 4, 69, 'Paid');

-- --------------------------------------------------------

--
-- Структура таблиці `sklad`
--

CREATE TABLE `sklad` (
  `ID` int(11) NOT NULL,
  `nazov` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `ks` int(11) NOT NULL,
  `cena` int(11) NOT NULL,
  `popis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `photo_url` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Дамп даних таблиці `sklad`
--

INSERT INTO `sklad` (`ID`, `nazov`, `ks`, `cena`, `popis`, `photo_url`) VALUES
(1, 'WHEY PROTEIN', 0, 36, 'Quickly absorbable instant whey protein with high BCAA content and excellent taste.\r\nQuickly absorbable instant whey protein with high BCAA content and excellent taste. Whey protein contains all important amino acids, including a valuable dose of BCAA. This makes it an ideal choice for all athletes and people who want to increase their daily protein intake, thus supporting muscle growth, their protection and accelerating regeneration after training.', 'https://www.bodyworld.eu/media/product/4631/original.png?nc=1697785782'),
(2, 'WHEY GOLD STANDARD 100%', 126, 69, 'Delicious whey protein with high biological value enriched with a mixture of digestive enzymes.\r\nDelicious whey protein with high biological value enriched with a mixture of digestive enzymes. It is among the best-selling proteins in the world. Its main ingredient is high-quality whey isolate supplemented with whey concentrate and hydrolyzed isolate. This unique mixture ensures the intake of quickly absorbable proteins, which are essential for muscle growth, to improve regeneration after demanding training, but also during reduction diets to protect muscle mass from its loss.', 'https://www.bodyworld.eu/media/product/231/original.png?nc=1630323144'),
(3, 'WHEY PROTEIN', 15, 77, 'Quickly absorbable instant whey protein with high BCAA content and excellent taste.\r\nQuickly absorbable instant whey protein with high BCAA content and excellent taste. Whey protein contains all important amino acids, including a valuable dose of BCAA. This makes it an ideal choice for all athletes and people who want to increase their daily protein intake, thus supporting muscle growth, their protection and accelerating regeneration after training.', 'https://www.bodyworld.eu/media/product/4801/original.png?nc=1683096100'),
(4, 'PROTEIN POWER', 3, 56, 'Excellent flavored multi-component protein enriched with micronized creatine.\r\nExcellent flavored multi-component protein enriched with micronized creatine intended for athletes who want to increase performance, improve muscle growth and accelerate regeneration. It contains three types of protein: soy protein isolate, calcium caseinate and whey protein concentrate.', 'https://www.bodyworld.eu/media/product/1417/original.png?nc=1635241728'),
(5, 'VEGAN PROTEIN 500G', 61, 10, 'Delicious multi-ingredient vegetable protein with no added sugar.\r\nDelicious multi-ingredient vegetable protein with no added sugar. The basis of Vegan Protein is soy isolate, which is enriched with another 6 types of protein from pea, cranberry, rice, hemp, sunflower and almond protein. Soy isolate is a valuable source of healthy plant proteins with a low carbohydrate and fat content. The proteins contained in Vegan Protein contribute to the growth and maintenance of muscle mass and to the maintenance of healthy bones.', 'https://www.bodyworld.eu/media/product/5891/original.png?nc=1578496356'),
(7, 'WPC 80 1000 g', 11, 46, 'Quality and tasty whey protein made in Slovakia.\r\nTasty and easily soluble whey concentrate made in Slovakia from the highest quality domestic raw materials. It is characterized by a proportion of proteins approaching 80%, a high biological value and a rich content of important BCAA amino acids. WPC 80 is intended for all athletes and people who want to increase their daily protein intake, start muscle growth, support their protection and speed up the regeneration process after hard training.', 'https://www.bodyworld.eu/media/product/4055/original.png?nc=1678259911'),
(6, 'ISOLATE PROTEIN 908 G', 1, 33, 'Instant whey isolate with up to 85% of rapidly absorbable proteins.\r\nFirst-class instant whey isolate with up to 85% of quickly absorbable and easily digestible proteins. Isolate Protein starts muscle growth without fat, prevents catabolism and accelerates post-workout regeneration. It is characterized by an excellent amino acid profile rich in BCAA branched amino acids and an extremely high biological value of BV 159.', 'https://www.bodyworld.eu/media/product/4935/original.png?nc=1662461845'),
(8, 'NUTLOVE PROTEIN SHAKE', 30, 19, 'Delicious creamy protein based on milk concentrates with added buttermilk and nuts.\r\nDelicious creamy protein based on milk concentrates with added buttermilk and nuts. This excellent protein primarily supplements our diet with proteins that are necessary for muscle growth and protection. Thanks to the three flavors and the creamy consistency, supplementing the daily protein intake turns into a perfect pleasure and successfully replaces any dessert.', 'https://www.bodyworld.eu/media/product/7070/original.png?nc=1643186566'),
(9, 'PRO CASEIN', 10, 19, 'Delicious micellar casein enriched with LactoSpore® and DigeZyme®.\r\nDelicious micellar casein enriched with LactoSpore® probiotic complex and DigeZyme® enzyme complex. Micellar casein is characterized by slow absorption, which guarantees a gradual release of amino acids. It is especially useful at night while sleeping or when you have been without food for a long time. Pro Casein contains a full spectrum of essential and non-essential amino acids, has a naturally high content of BCAA and glutamic acid, and all this with a minimal content of fat and carbohydrates.', 'https://www.bodyworld.eu/media/product/7581/original.png?nc=1671470345'),
(10, 'PRO WHEY', 5, 79, 'Tasty whey protein with added amino acids enriched with digestive enzymes.\r\nTasty protein made from high-quality whey concentrate and whey isolate. In addition, extra amino acids and digestive enzymes were added to the protein. Whey protein supports the growth of muscles, their protection and accelerates regeneration after demanding training. The added digestive enzymes papain and bromelain ensure trouble-free digestion of proteins and maximum utilization of nutrients.', 'https://www.bodyworld.eu/media/product/16/original.png?nc=1694425246'),
(11, 'ISO WHEY ZERO', 2, 27, 'Premium extremely quickly absorbable whey isolate enriched with amino acids BCAA and glutamine.\r\nExtremely quickly and easily absorbed whey isolate enriched with amino acids BCAA and glutamine. The gentle production process through the process of micro and ultrafiltration is a guarantee of high purity of the resulting protein, which is cleaned of all unwanted substances. Iso Whey does not contain added sugar, it is lactose and gluten free. Proteins support muscle growth, improve regeneration and protect muscles during reduction diets.', 'https://www.bodyworld.eu/media/product/1449/original.png?nc=1650968867');

-- --------------------------------------------------------

--
-- Структура таблиці `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `adresa` varchar(50) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `zlava` int(11) NOT NULL,
  `meno` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `priezvisko` varchar(20) CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `poznamky` text CHARACTER SET utf8 COLLATE utf8_slovak_ci NOT NULL,
  `je_admin` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Дамп даних таблиці `users`
--

INSERT INTO `users` (`ID`, `login`, `passwd`, `adresa`, `zlava`, `meno`, `priezvisko`, `poznamky`, `je_admin`) VALUES
(1, 'jskalka@ukf.sk', '123', 'Zeleninova 4, Nitra', 20, 'Jan ', 'Skalka', 'tester', 1),
(2, 'jmrkva@ukf.sk', '123', 'Zahrada 11', 3, 'Jozef', 'Mrkva', 'druhý tester', 0),
(3, '1@gmail.com', '1', 'Bratislavská 1, 949 01 Nitra', 50, '1', '11', '11', 0),
(4, '2@gmail.com', '2', 'Piaristická 1379/2, 949 01 Nitra', 0, '2', '2', '2', 1),
(5, 'lbenko@ukf.sk', '123', 'Cajkovskeho 434/40, 949 11 Nitra', 0, 'Lubomir', 'Benko', 'tester', 0);

--
-- Індекси збережених таблиць
--

--
-- Індекси таблиці `kosik`
--
ALTER TABLE `kosik`
  ADD PRIMARY KEY (`ID`);

--
-- Індекси таблиці `obj_polozky`
--
ALTER TABLE `obj_polozky`
  ADD PRIMARY KEY (`ID`);

--
-- Індекси таблиці `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  ADD PRIMARY KEY (`ID`);

--
-- Індекси таблиці `sklad`
--
ALTER TABLE `sklad`
  ADD PRIMARY KEY (`ID`);

--
-- Індекси таблиці `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT для збережених таблиць
--

--
-- AUTO_INCREMENT для таблиці `kosik`
--
ALTER TABLE `kosik`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT для таблиці `obj_polozky`
--
ALTER TABLE `obj_polozky`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT для таблиці `obj_zoznam`
--
ALTER TABLE `obj_zoznam`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT для таблиці `sklad`
--
ALTER TABLE `sklad`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT для таблиці `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
