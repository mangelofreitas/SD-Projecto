Para executar o programa basta ligar o MySQL do XAMPP (https://www.apachefriends.org/pt_br/download_success.html), esta deve conter uma base de dados chamada Fund_Starter com as sete tabelas devidas (para o verificar deve ligar o Apache tamb�m e ir ao endere�o /localhost/phpmyadmin/ no seu browser. Depois clicar na base de dados fund_starter), se n�o constar nem a base de dados nem a tabela � necess�rio fazer import do ficheiro fund_starter.sql que criar� automaticamente as tabelas necess�rias (para realizar esse import deve realizar os mesmos passos que na verifica��o da base de dados, mas neste caso depois de estar na p�gina inicial do phpmyadmin deve clicar no Novo/New para adicionar a base de dados com o nome fund_starter. Ap�s criar a base de dados, clique sobre ela depois, na parte superior central, estar� o importar/import, clique e depois selecione o ficheiro fund_starter.sql que est� neste local e execute). 
Por fim, correr os ficheiros .jar na pasta out/artifacts pela seguinte ordem: 
		1� RMIServer;
		2� RMIConn (colocar o endere�o ip correspondente do RMI);
		3� TomCat.
	
Como correr um ficheiro .jar?
	1� Abrir a linha de comandos no path do respectivo .jar;
	2� Executar o ficheiro: java -jar [file_name].
Para correr o TomCat deve copiar a pasta SD_Meta2 e colocar na pasta webapps do TomCat e depois correr, ap�s acabar de correr liga-se via web ao endere�o onde se encontra colocando a porta 8080 e o nome do projecto (SD_Meta2), por exemplo: localhost:8080/SD_Meta2/.
N�o esquecer que o ficheiro politics.policy deve constar no mesmo path que o ficheiro RMIServer.jar.