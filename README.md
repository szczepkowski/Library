sem3-library
============

Maven+Swing+Hibernate+JPA+MySQL

Projekt ma segment administracyjny[admin] i użytkowy[user]. Zacząłem od strony administracyjnej która jest zwykłym crudem .. zapisuje, czyta, edytuje i usuwa książki .. najpierw zrobiłem POJO z JPA[ Class : Book, Category, Author i asocjacje ] .. potem dostęp do nich w pakiecie DAO 

Następnie żeby mieć już jakieś przykładowe dane zapisałem je do pliku user.txt i  za pomocą klasy App.java wczytałem je do bazy danych [ configuracje MySQL z hibernate pomijam .. do znalezienia w necie ]

Później potrzebowałem zaprezentować dane z DB i zrobiłem to za pomocą swinga .. stworzyłem ramki, guziki i reszte żeby można było je łatwo edytować dodawać .. wiem, że Spring MVC jest sensowniejszy ale nic mi się nie stanie jak po drodze nauczę się też trochę Swinga

Gdy wszystko hulało stwierdziłem, że to trochę mało i chciałem stworzyć możliwość wypożyczania książek oraz pobierania opłaty za przytrzymanie książki powyżej miesiąca.

To już cześć użytkowa .. najpierw stworzyłem ramkę do logowania i rejestrowania użytkowników .. zapisałem i wczytałem kilku przykładowych użytkowników z pliku za pomocą App.java i klasy StringTokenizer .. mogłem już się logować .. potem dorobiłem możliwość rejestracji ..

Gdy już się zalogowałem stworzyłem guziki w Swingu aby móc pożyczać , oddawać , oglądać historię wypożyczeń oraz stworzyłem możliwość spłaty długów  

Dodatkowe klasy POJO Z JPA jakie powstały to m.in : Lend, LendHistory,User,Account,Address .. oraz dostęp z DAO.. ta część sprawiła mi najwięcej problemów.

Przy okazji wykorzystałem możliwość odwzorowania dziedziczenia za pomocą Hibernate i JPA .. stworzyłem klasę abstrakcyjną Person którą rozszerza klasa Librarian[ bibliotekarz dodaje książki i hisotria wpisów zostaje zapisana w DB żeby było wiadomo kogo opieprzyć ] i klasa User.

Na koniec żeby przetestować czy hula odpaliłem timer ze słuchaczem zdarzenia który co 5 sek dodaje jeden dzień do daty i zlicza upływający czas .. każde wypożyczenie jest darmowe przez 30 dni .. po upływie tego czasu naliczania jest skromna opłata w wysokości 10 groszy.

Wygrałem kilka Lendów[wypożyczeń]  z pliku za pomocą App.java .. stworzyłem nowe  konto .. wypożyczyłem kilka książek [ zrobiłem też guzik który dodaje od razy 1 dzien do daty ] date zwiększyłem o 60 dni .. po wielu błędach naliczyłem poprawnie zadłużenie [Debt] każdego z Userów .. każdy User ma wgląd do tylko swojej Historii wypożyczeń .. potem zapłaciłem karę za przytrzymanie książki i podziękowałem ;]

Jest to mój pierwszy projekt który coś robi więc przydałaby mi się konstruktywna krytyka odnośnie tego co robię dobrze a co źle .. co można zrobić inaczej .. mniej interesują mnie zmiany w Swingu bo następny projekt to sklep w Spring MVC + Hibernate [ Sem 4 na PWR ] a byłbym wdzięczny każdemu kto mi doradzi jak efektywniej konstruować odwzorowania do bazy, jak budować całą sieć relacyjną bazy danych oraz jak sensownie tworzyć pliki DAO .. coś w stylu znanych przez Was wzorców projektowych ETC .. 

Dziękuję za uwagę i proszę o konstruktywny komentarz.
