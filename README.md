# Пројеткни задатак - DoctorAppointmentApp

## Поставка задатка

### Предмет: Razvoj informacionog sistema za rešavanje konkretnog problema zakazivanja pregleda kod lekara
Zadatak koji studenti treba da realizuju odnosi se na razvoj informacionog sistema za rešavanje
konkretnog problema zakazivanja pregleda kod lekara u jednoj manjoj ambulanti ili zdravstvenoj
ustanovi. U praksi se često dešava da više pacijenata pokuša da zakaže termin kod istog lekara za
isto vreme, što dovodi do konflikata, neefikasnosti i potrebe za ručnom proverom rasporeda.
Pored toga, osoblje ne uspeva uvek da odmah odgovori svakom pacijentu o tome da li je njegov
zahtev prihvaćen, što izaziva dodatnu konfuziju i nezadovoljstvo. Rešenje koje treba da se
razvije ima za cilj da automatizuje ovaj proces, digitalizuje zakazivanje i omogući da sistem sam
odlučuje da li zahtev može da bude prihvaćen, na osnovu dostupnosti lekara u konkretnom
terminu.
Studenti treba da implementiraju aplikaciju u kojoj pacijent putem REST API-ja podnosi zahtev
za pregled kod lekara, navodeći svoje osnovne podatke, ime lekara i željeni datum i vreme
pregleda. Po prijemu zahteva, sistem ga beleži kao „neobrađen“ i šalje ga kao poruku u red
unutar RabbitMQ sistema. Poruka se zatim obrađuje asinhrono, tako što pozadinski proces
(consumer) proverava da li lekar već ima zakazan pregled u tom terminu. Ako nema, sistem
automatski potvrđuje termin i ažurira njegov status. Ako lekar jeste zauzet, zahtev se odbacuje i
označava kao neuspešan. Na taj način se simulira realni proces obrade zakazivanja u
ordinacijama, ali bez potrebe za ljudskom intervencijom.
Sama aplikacija se razvija korišćenjem Spring Boot frejmvorka, koji omogućava kreiranje REST
kontrolera i servisne logike. Za rad sa bazom koristi se JPA/Hibernate, pri čemu se podaci o
pacijentima, lekarima i zakazivanjima trajno čuvaju u MySQL bazi podataka. Model podataka
uključuje entitete pacijent, lekar i zakazivanje, gde zakazivanje sadrži vezu ka lekaru i pacijentu,
kao i vreme pregleda i trenutni status. Početni status svakog novog zahteva je „PENDING“, a
nakon obrade prelazi u „CONFIRMED“ ili „REJECTED“. Asinhrona obrada se implementira uz
pomoć Spring AMQP biblioteke, preko koje aplikacija komunicira sa RabbitMQ brokerom.
Uloga RabbitMQ-a u ovom projektu je da omogući skalabilnu i odloženu obradu zahteva za
terminima, što predstavlja važan koncept u arhitekturi savremenih softverskih sistema. Kada
korisnik kreira zahtev, on ne biva odmah potvrđen – sistem ga šalje u red i njegova obrada se
odvija nezavisno, u pozadini. Studentima ovaj pristup omogućava da razumeju razliku između
sinhrone i asinhrone logike i da steknu uvid u to kako se realni zahtevi mogu distribuirati i
obrađivati efikasnije. MySQL se koristi kao glavna baza podataka, u kojoj se čuvaju svi entiteti i
statusi, i u okviru koje se izvršava provera raspoloživosti termina kod lekara.

Od studenata se očekuje da aplikaciju razviju modularno, sa jasno razdvojenim slojevima:
kontrolerima, servisima, repozitorijumima i konfiguracionim klasama. REST API treba da
podržava kreiranje zahteva, pregled svih zakazanih termina i filtriranje termina po statusu.
Student treba da demonstrira razumevanje osnovnih principa JPA relacija, asinhronog slanja
poruka, konfigurisanja Spring Boot aplikacije za rad sa MySQL bazom, kao i pravilnog
korišćenja RabbitMQ-a u pozadini.
Zadatak ne uključuje korisnički interfejs, ali se podstiče dodatna nadogradnja za studente koji
žele da pokažu više – na primer, prikaz termina kroz jednostavan frontend ili dodatnu validaciju
unosa. Suština zadatka je da se provere temeljno usvojeni koncepti savremene Java aplikacione
arhitekture i sposobnost rešavanja konkretnih problema kroz integraciju različitih tehnologija.
