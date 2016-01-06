-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 19-Dez-2015 às 20:45
-- Versão do servidor: 10.1.8-MariaDB
-- PHP Version: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fund_starter`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserByID` (IN `usernameID` INT, OUT `username` VARCHAR(50), OUT `mail` VARCHAR(50))  SELECT users.username, users.mail INTO username, mail FROM users WHERE users.usernameID = usernameID$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `messages_reply`
--

CREATE TABLE `messages_reply` (
  `messageReplyID` int(11) NOT NULL,
  `message` text NOT NULL,
  `messageSendID` int(11) NOT NULL,
  `usernameID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `messages_reply`
--

INSERT INTO `messages_reply` (`messageReplyID`, `message`, `messageSendID`, `usernameID`) VALUES
(4, 'ya', 4, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `messages_send`
--

CREATE TABLE `messages_send` (
  `messageSendID` int(11) NOT NULL,
  `message` text NOT NULL,
  `projectID` int(11) NOT NULL,
  `usernameID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `messages_send`
--

INSERT INTO `messages_send` (`messageSendID`, `message`, `projectID`, `usernameID`) VALUES
(3, 'ola Freitas, como estas?', 9, 1),
(4, 'uiqwjkdsaxnmzjsduuuuuuu', 9, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `projects`
--

CREATE TABLE `projects` (
  `projectID` int(11) NOT NULL,
  `usernameID` int(11) NOT NULL,
  `projectName` varchar(200) NOT NULL,
  `description` varchar(200) NOT NULL,
  `dateLimit` date NOT NULL,
  `requestedValue` int(11) NOT NULL,
  `currentAmount` int(11) NOT NULL,
  `alive` tinyint(1) NOT NULL,
  `success` tinyint(1) NOT NULL,
  `finalProduct` varchar(500) DEFAULT NULL,
  `postID` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `projects`
--

INSERT INTO `projects` (`projectID`, `usernameID`, `projectName`, `description`, `dateLimit`, `requestedValue`, `currentAmount`, `alive`, `success`, `finalProduct`, `postID`) VALUES
(1, 2, 'Coisas', 'Cenas muito fixes', '1970-01-01', 10000, 0, 0, 0, NULL, NULL),
(5, 3, 'CellarMarket', 'Site com detalhes e estat¡sticas dos vinhos desde que foram criados at‚ h  altura actual.', '2015-10-20', 1000, 110, 0, 0, NULL, NULL),
(6, 3, 'Kiski Area Upper Elementary Makerspace', 'With a new makerspace and learning experiences, Kiski Area Upper Elementary is bridging STEM and the humanities with hands-on learning', '2015-10-22', 10000, 0, 0, 0, NULL, NULL),
(7, 3, 'Cenas', 'ya', '2015-10-21', 100, 0, 0, 0, NULL, NULL),
(8, 3, 'MAry fixe cenas', 'sqfetrytfuyiu', '0018-05-08', 10000, 12, 0, 0, NULL, NULL),
(9, 3, 'banana', 'ajhjhsahd', '2015-10-29', 100, 120, 0, 1, 'azul', NULL),
(10, 3, 'wefefwwefg', 'wefwrgrw', '2015-10-30', 10, 0, 0, 0, NULL, NULL),
(11, 1, 'FitJump', 'Realizacao de uma corda de saltar que conta o numero de saltos por minuto.', '2015-10-30', 100, 700, 0, 1, 'vermelho, azul, inclusao de um modulo bluetooth para transmitir a informacao ao telemovel.', NULL),
(12, 2, 'Cenas', 'Realizar alguma grande', '2015-10-31', 100, 150, 0, 1, 'mais cenas', NULL),
(13, 3, 'ola', 'adjfjjdjjd', '2015-12-12', 120, 20, 0, 0, NULL, NULL),
(14, 7, 'casota com claraboia', 'casota bonita', '2015-11-08', 10000, 80, 0, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `rewards`
--

CREATE TABLE `rewards` (
  `rewardID` int(11) NOT NULL,
  `projectID` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `description` varchar(200) NOT NULL,
  `valueOfReward` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `rewards`
--

INSERT INTO `rewards` (`rewardID`, `projectID`, `name`, `description`, `valueOfReward`) VALUES
(8, 9, 'ola', 'asafsfd', 10),
(9, 9, 'wefoiw', 'wgergergher', 80),
(11, 11, '1', 'O name do apoiante aparece na lista de apoiantes do projecto na caixa.', 5),
(12, 11, '2', 'O apoiante recebe uma unidade de FitJumpingRope.', 60),
(13, 11, '3', 'O apoiante recebe duas unidades de FitJumpingRope.', 100),
(14, 12, '1', 'Recebe uma cena', 50),
(15, 13, 'ola', 'qwqerqerwq', 10),
(16, 1, 'fwrger', 'egerge', 10);

-- --------------------------------------------------------

--
-- Estrutura da tabela `types_products`
--

CREATE TABLE `types_products` (
  `typeProductID` int(11) NOT NULL,
  `projectID` int(11) NOT NULL,
  `votes` int(11) NOT NULL,
  `type` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `types_products`
--

INSERT INTO `types_products` (`typeProductID`, `projectID`, `votes`, `type`) VALUES
(6, 9, 5, 'azul'),
(8, 11, 0, 'azul'),
(9, 11, 7, 'vermelho'),
(10, 12, 3, 'mais cenas'),
(11, 13, 2, 'azul');

-- --------------------------------------------------------

--
-- Estrutura da tabela `users`
--

CREATE TABLE `users` (
  `usernameID` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `mail` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  `money` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `users`
--

INSERT INTO `users` (`usernameID`, `username`, `mail`, `password`, `money`) VALUES
(1, 'mfreitas', 'bla@cenas.com', '123', 4760),
(2, 'gajo', 'teste@coisa.com', '321', 250),
(3, 'Mary', 'mary@teste.com', 'mary', 240),
(5, 'gergr', '123', 'sad', 100),
(6, 'snopy', 'snopy@gmail.com', 'snopy', 100),
(7, 'ideafix', 'ideafix@fundstarter.com', 'ideafix', 100),
(8, 'Maria', 'maria@cavalo.com', 'cavalo', 100),
(9, 'Freitas Boss', 'boss@freitas.com', 'freitas', 100),
(10, 'mangelofreitas', 'mangelofreitas.tumblr.com', 'mangelofreitas', 100),
(11, 'fundstartertest', 'fundstartertest.tumblr.com', 'fundstartertest', 100);

-- --------------------------------------------------------

--
-- Estrutura da tabela `users_contributes`
--

CREATE TABLE `users_contributes` (
  `contributeID` int(11) NOT NULL,
  `projectID` int(11) NOT NULL,
  `usernameID` int(11) NOT NULL,
  `moneyGiven` int(11) NOT NULL,
  `typeProductID` int(11) NOT NULL,
  `rewardID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `users_contributes`
--

INSERT INTO `users_contributes` (`contributeID`, `projectID`, `usernameID`, `moneyGiven`, `typeProductID`, `rewardID`) VALUES
(6, 9, 3, 10, 6, 8),
(7, 9, 3, 10, 6, 8),
(8, 9, 1, 10, 6, 8),
(9, 9, 3, 10, 6, 8),
(10, 9, 1, 80, 6, 9),
(11, 11, 3, 100, 9, 13),
(12, 11, 3, 100, 9, 13),
(13, 11, 3, 100, 9, 13),
(14, 11, 3, 100, 9, 13),
(15, 11, 3, 100, 9, 13),
(16, 11, 3, 100, 9, 13),
(17, 11, 3, 100, 9, 13),
(18, 12, 1, 50, 10, 14),
(19, 12, 1, 50, 10, 14),
(20, 12, 1, 50, 10, 14),
(21, 13, 3, 10, 11, 15),
(22, 13, 6, 10, 11, 15);

--
-- Acionadores `users_contributes`
--
DELIMITER $$
CREATE TRIGGER `retrieve_money` AFTER DELETE ON `users_contributes` FOR EACH ROW UPDATE users SET money = money + OLD.moneyGiven WHERE usernameID = OLD.usernameID
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `messages_reply`
--
ALTER TABLE `messages_reply`
  ADD PRIMARY KEY (`messageReplyID`),
  ADD KEY `mensageSendID` (`messageSendID`),
  ADD KEY `usernameID` (`usernameID`);

--
-- Indexes for table `messages_send`
--
ALTER TABLE `messages_send`
  ADD PRIMARY KEY (`messageSendID`),
  ADD KEY `projectID` (`projectID`),
  ADD KEY `usernameID` (`usernameID`);

--
-- Indexes for table `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`projectID`),
  ADD KEY `usernameID` (`usernameID`);

--
-- Indexes for table `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`rewardID`),
  ADD KEY `projectID` (`projectID`);

--
-- Indexes for table `types_products`
--
ALTER TABLE `types_products`
  ADD PRIMARY KEY (`typeProductID`),
  ADD KEY `projectID` (`projectID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`usernameID`);

--
-- Indexes for table `users_contributes`
--
ALTER TABLE `users_contributes`
  ADD PRIMARY KEY (`contributeID`),
  ADD KEY `projectID` (`projectID`),
  ADD KEY `usernameID` (`usernameID`),
  ADD KEY `typeProductID` (`typeProductID`),
  ADD KEY `rewardID` (`rewardID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `messages_reply`
--
ALTER TABLE `messages_reply`
  MODIFY `messageReplyID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `messages_send`
--
ALTER TABLE `messages_send`
  MODIFY `messageSendID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `projects`
--
ALTER TABLE `projects`
  MODIFY `projectID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `rewards`
--
ALTER TABLE `rewards`
  MODIFY `rewardID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `types_products`
--
ALTER TABLE `types_products`
  MODIFY `typeProductID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `usernameID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `users_contributes`
--
ALTER TABLE `users_contributes`
  MODIFY `contributeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `messages_reply`
--
ALTER TABLE `messages_reply`
  ADD CONSTRAINT `messages_reply_ibfk_1` FOREIGN KEY (`messageSendID`) REFERENCES `messages_send` (`messageSendID`),
  ADD CONSTRAINT `messages_reply_ibfk_2` FOREIGN KEY (`usernameID`) REFERENCES `users` (`usernameID`);

--
-- Limitadores para a tabela `messages_send`
--
ALTER TABLE `messages_send`
  ADD CONSTRAINT `messages_send_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`),
  ADD CONSTRAINT `messages_send_ibfk_2` FOREIGN KEY (`usernameID`) REFERENCES `users` (`usernameID`);

--
-- Limitadores para a tabela `projects`
--
ALTER TABLE `projects`
  ADD CONSTRAINT `projects_ibfk_1` FOREIGN KEY (`usernameID`) REFERENCES `users` (`usernameID`);

--
-- Limitadores para a tabela `rewards`
--
ALTER TABLE `rewards`
  ADD CONSTRAINT `rewards_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`);

--
-- Limitadores para a tabela `types_products`
--
ALTER TABLE `types_products`
  ADD CONSTRAINT `types_products_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`);

--
-- Limitadores para a tabela `users_contributes`
--
ALTER TABLE `users_contributes`
  ADD CONSTRAINT `users_contributes_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`),
  ADD CONSTRAINT `users_contributes_ibfk_2` FOREIGN KEY (`usernameID`) REFERENCES `users` (`usernameID`),
  ADD CONSTRAINT `users_contributes_ibfk_3` FOREIGN KEY (`typeProductID`) REFERENCES `types_products` (`typeProductID`),
  ADD CONSTRAINT `users_contributes_ibfk_4` FOREIGN KEY (`rewardID`) REFERENCES `rewards` (`rewardID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
