// types 0- woda, 1- ziemia, 2- powietrze, 3- ogien, 4- lod, 5- stal
#include <iostream>
#include <string>
#include <vector>
#include <windows.h>
#include <conio.h>
#include <fstream>

class pokemon;

const double ATTACK_EFFECT_MULTIPLY = 0.5; // modyfikator z zgodnych zywiolow

int difficulty = 4; // 4 - latwy, 5 - sredni, 6 - trudny

const int types[6][6] = { // zgodnosci zywiolow
	{-1, 1, 0, 1, 0, 0},
	{ 0, 0,-1, 1, 1, 1},
	{ 0,-1, 0, 0, 1,-1},
	{-1,-1, 0, 0, 1, 1},
	{-1, 1, 0,-1,-1, 0},
	{ 1, 0, 1,-1, 0,-1}
};

/* Losuje liczbe z przedzialu [a - b] */
int random(int a, int b) {
	return rand() % (b - a + 1) + a;
}

/* Funkcja do ladniejszego wyswietlania tekstu na konsoli
(dla oszczedzenia czasu wartosc Sleep() jest ustawiona na 0 zamiast na 1)
*/
void sout(std::string n) {
	for (size_t i = 0; i < n.size(); i++)
	{
		std::cout << n[i];
		Sleep(0);  // Mozna zmienic na 1 dla ladniejszego wyswietlania
	}
	std::cout << '\n';
}

/* Klasa sluzaca do przechowywania informacji o ataku specjalnym i go egzekwowania

//	type:
//  0 - atak jednorazowy(amount dmg),
//	1 - trucizna(amount dmg co ture na duration tur),
//	2 - buff (o amount buff s[1 - str, 2 - dodge] statyki na duration tur),
//	3 - ogluszenie(na duration tur),
//	4 - leczenie (o amount hp),
//	5 - debuff (o amount debuff s[1 - str, 2 - dodge] statyki na duration tur)
//	6 - regeneracja (o amount leczenie hp na duration tur)

*/
class attack {
public:
	int type, amount, time, stat, duration;
	std::string name;

public:
	// Zaladowanie informacji o ataku
	void create(int type, int amount, int duration, int stat, std::string name) {
		this->type = type;
		this->amount = amount;
		this->duration = duration;
		this->stat = stat;
		this->name = name;
	}

	void round(pokemon* owner, pokemon* enemy);

	void use(pokemon* owner, pokemon* enemy);

	void end(pokemon* owner, pokemon* enemy);


	/* Funkcja zbierajaca informacje o specjalnym ataku i je zwracajaca jako string */
	std::string show() {
		std::string res = "";
		res += name;
		std::string tmp;
		switch (type) {
		case 0:
			res += " zadaje " + std::to_string(amount) + " obrazen";
			break;
		case 1:
			res += " zadaje " + std::to_string(amount) + " obrazen przez " + std::to_string(duration) + " tury";
			break;
		case 2:
			if (stat == 1) tmp = "sile";
			else if (stat == 2) tmp = "unik";
			else tmp = "#ERROR_ATTACK_SHOW_CASE_2";
			res += " wzmacnia " + tmp + " o " + std::to_string(amount) + " na " + std::to_string(duration) + " tur";
			break;
		case 3:
			res += " oglusza przecniwnika na " + std::to_string(duration) + " tury";
			break;
		case 4:
			res += " leczy o " + std::to_string(amount);
			break;
		case 5:
			if (stat == 1) tmp = "sile";
			else if (stat == 2) tmp = "unik";
			else tmp = "#ERROR_ATTACK_SHOW_CASE_2";
			res += " oslabia przeciwnika " + tmp + " o " + std::to_string(amount) + " na " + std::to_string(duration) + "tury";
			break;
		case 6:
			res += " regeneruje pasywnie " + std::to_string(amount) + " przez " + std::to_string(duration) + " tury";
			break;
		}
		return res;
	}
};

/* Klasa pokemon  */
class pokemon {
public:
	std::string name;
	int stats[3]; // 0-maxhp, 1-str, 2-dodge 
	int hp, xp, type, worth_xp, special_limit, special_count, lvl;
	attack special;
	bool stun; // true -> jest ogloszony
	bool alive; // false -> nie zyje
	// Tworzy pokemona
	void create(std::string name, int hp, int maxhp, int strength, int dodge, int type, int xp, int worth_xp, attack special, int special_limit, int special_count, int lvl) {
		this->alive = true;
		this->lvl = lvl;
		this->stun = false;
		this->name = name;
		this->hp = hp;
		this->stats[0] = maxhp;
		this->stats[1] = strength;
		this->stats[2] = dodge;
		this->type = type;
		this->special = special;
		this->xp = xp;
		this->worth_xp = worth_xp;
		this->special_limit = special_limit;
		this->special_count = special_count;
	}
	// Sprawdza czy moze ewoluowac
	// @return bool czy moze ewoluowac
	bool can_evolve() {
		if (xp > lvl * 100) return true;
		return false;
	}
	// Sprawdzy czy udalo sie uniknac ataku
	// @param (n) ile dmg ma przyjac oraz (enemy) kto go atakuje
	void hit(int n, pokemon* enemy) {
		if (random(1, 100) <= stats[2]) { // sprawdza czy udalo sie uniknac
			sout(name + " uniknal obrazen! (" + std::to_string(stats[2]) + "%)");
		}
		else {
			dmg(n, enemy); // wywoluje jesli nie uniknie
		}
	}
	// Funkcja do przeprowadzania normalnego ataku
	//@param (enemy) przeprowadza normalny atak na enemy
	void attack_normal(pokemon* enemy) {
		sout(name + " wykonal normalny atak! (" + std::to_string(stats[1]) + "dmg)");
		enemy->hit(stats[1], this);
	}
	// Funkcja do przeprowadzania specjalnego ataku
	//@param (enemy) do zaatakowania
	void attack_special(pokemon* enemy) {
		if (special_count <= 0) std::cout << "#ERROR_ATTACK_SPECIAL";
		special_count--;
		special.use(this, enemy);
	}
	// Funkcja do odswierzenia zycia i ilosci specjalnych atakow pomiedzy walkami
	void refreash() {
		hp = stats[0];
		alive = true;
		special_count = special_limit;
	}
	// Funkcja do przyjmowania obrazen po nieudanym uniku
	// @param (n) ile obrazen oraz (enemy) od kogo
	void dmg(int n, pokemon* enemy) {
		int effect = types[enemy->type][type];
		std::string show_effect;
		switch (effect)
		{
		case -1:
			show_effect = "niezbyt efektywny...";
			break;
		case 0:
			show_effect = "normalny";
			break;
		case 1:
			show_effect = "super efektywny!";
			break;
		default:
			sout("ERROR #DMG");
			break;
		}
		n = n + (n * (ATTACK_EFFECT_MULTIPLY * effect));  // modyfikowanie n na podstawie zgodnosci zywiolow
		sout("jest " + show_effect);
		sout(name + " otrzymal " + std::to_string(n) + "obrazen!\n");
		hp -= n;
		isAlive();
	}
	// Sprawdza czy pokemon nadal zyje
	//@return bool mowiacy czy pokemon zyje
	bool isAlive() {
		if (hp <= 0)alive = false;
		return alive;
	}
	// Wywoluje pasywne efekty pochodzace ze swojego specjalnego ataku
	void round(pokemon* enemy) {
		special.round(this, enemy);
	}
	// Zbiera informacje o specjalnym ataku i sobie 
	// @return zwraca je w formie string
	std::string info() {
		std::string res = "";
		res += std::to_string(special.type) + " " + std::to_string(special.amount) + " " + std::to_string(special.time) + " " + std::to_string(special.stat) + " " + std::to_string(special.duration) + " " + special.name;
		res += " " + name + " " + std::to_string(hp) + " " + std::to_string(stats[0]) + " " + std::to_string(stats[1]) + " " + std::to_string(stats[2]) + " " + std::to_string(type) + " " + std::to_string(xp) + " " + std::to_string(worth_xp) + " " + std::to_string(special_limit) + " " + std::to_string(special_count) + " " + std::to_string(lvl);
		return res;
	}
	// Leczy pokemon bez przekraczania limitu
	// @params (n) o ile uleczyc
	void heal(int n) {
		if (hp + n > stats[0])hp = stats[0];
		else hp += n;
	}

	// Zbiera informacje w czytelnie formie 
	// @return zwraca je jako string
	std::string show() {
		std::string res = name;
		res += " lvl: " + std::to_string(lvl);
		res += " hp: " + std::to_string(hp);
		res += " maxhp: " + std::to_string(stats[0]);
		res += " sila: " + std::to_string(stats[1]);
		res += " unik: " + std::to_string(stats[2]);
		res += " xp: " + std::to_string(xp);
		res += " typ: " + std::to_string(type);
		res += " wart_xp: " + std::to_string(worth_xp);
		res += " specialny atak: " + special.show();
		return res;
	}
};

// Funkcja do przeprowadzenia pasywnych efektow specjalnego ataku (typu trucina czy regeneracja)
// @params (owner) wlasciciel ataku oraz (enemy) jego przeciwnik
void attack::round(pokemon* owner, pokemon* enemy) {
	if (time == 0) end(owner, enemy);
	else if (time > 0) {
		switch (type) {
		case 1:
			sout("Trucizna z ataku " + owner->name + " zadala " + std::to_string(amount) + "dmg " + enemy->name + "!\n");
			enemy->dmg(amount, owner);
			break;
		case 6:
			sout(owner->name + " leczy sie pasywnie o " + std::to_string(amount) + " dzieki " + name);
			break;
		}
	}
	time--;
}
// Funkcja do uzycia specjalnego ataku
// @params (owner) wlasciciel ataku oraz (enemy) jego przeciwnik
void attack::use(pokemon* owner, pokemon* enemy) {
	time = duration;
	switch (type)
	{
	case 0:
		sout(owner->name + " uzywa " + name + "! ");
		enemy->hit(amount, owner);
		break;
	case 1:
		sout(owner->name + " uzywa " + name + "! " + enemy->name + " bedzie tracil co ture hp!");
		break;
	case 2:
		sout(owner->name + " uzywa " + name + "! Bedzie teraz lepszy");
		owner->stats[stat] += amount;
		break;
	case 3:
		sout(owner->name + " uzywa " + name + "! " + enemy->name + " jest teraz ogloszony!");
		enemy->stun = true;
		break;
	case 4:
		sout(owner->name + " uzywa " + name + "! ");
		sout(owner->name + " ulczeyl sie za " + std::to_string(amount));
		owner->heal(amount);
		break;
	case 5:
		sout(owner->name + " uzywa " + name + "! " + enemy->name + " jest teraz slabszy");
		enemy->stats[stat] -= amount;
		break;
	case 6:
		sout(owner->name + " uzywa " + name + "! Bedzie sie teraz pasywnie regenerowal");
		break;
	}
}
// Funkcja konczaca pasywne efekty specjalnego ataku
// @params (owner) wlasciciel ataku oraz (enemy) jego przeciwnik
void attack::end(pokemon* owner, pokemon* enemy) {
	time = -1;
	switch (type) {
	case 1:
		sout("Trucizna z ataku " + owner->name + +" na " + enemy->name + " przestala dzialac!\n");
		break;
	case 2:
		sout("buff " + owner->name + " przestal dzialac!\n");
		owner->stats[stat] -= amount;
		break;
	case 3:
		enemy->stun = false;
		sout("Ogluszenie " + enemy->name + " przestalo dzialac!");
		break;
	case 5:
		sout("debuff " + owner->name + " na graczu " + enemy->name + " przestal dzialac!\n");
		enemy->stats[stat] += amount;
		break;
	case 6:
		sout("pasywna regeneracja " + owner->name + " przestala dzialac!\n");
		break;
	}
}


pokemon player_pokemons[6]; // Tablica wybranych pokemonow przez gracza
std::string player_pokemon_name[6]; // Tablica imion wybranych pokemonw przez gracza
std::vector<pokemon> enemy_pokemons[6]; // Tablica vektor pokemonow wszystkich przeciwnikow
pokemon pokemons[15]; // tablica wszystkich wygenerowanych pokemonow
int progress = 0; // z ktorym przeciwnikiem walczy gracz 
int player; // ktory pokemon aktualnie walczy gracza

std::string help = "Grasz wpisujac wartosci liczbowe w konsoli odpowiadajace opcji ktora chcesz wybrac z podanych\nMozesz w trakcie walki wykonac:\nNormalny atak (zadajacy tyle obrazen jakas masz sile * modyfikator z zywiolu x(0,5; 1; 2) przeciwnik ma % szansy na ucieczke (statystyka unik)\nSpecjalny unik -> masz limitowana ilosc uzyc na walke, mozesz uzyc opcji zbadaj pokemony pomiedzy walkami aby dowiedziec sie wiecej\nEwolucja -> zwieksz 2 wybrane statystyki\nZmien pokemona-> wymienia pokemona ale tracisz ture\nPowodzenia!";

// sprawdza czy napis to liczba i konwertuje
// @param (n) napis 
// @return liczbe ktora byla w napisie(n) lub -1 jesli to nie byla liczba 
// intencjonalnie dziala tylko na dodatnie
int number(std::string n) {
	int tmp = 0;
	for (size_t i = 0; i < n.length(); i++)
	{
		if (n[i] < '0' || n[i]>'9')return -1;
		else {
			tmp *= 10;
			tmp += n[i] - '0';
		}
	}
	return tmp;
}

// Tworzy dialog z opcjami do wyboru sprawdzajac czy uzytkownik nie wpisal -h lub --h
// w celu dialogu pomocy oraz czy podana liczba jest z zakresu (od 1 do ilosc_pytan)
// @param (begin) dialog poczatkowy, (options) dialog z opcjami, (count) ilosc opcji
// @return wybrana opcja przez uzytkownika - 1
int choice(std::string begin, std::string options, int count)
{
	sout(begin + '\n');
	sout(options);
	sout("Wybierz opcje: ");
	std::string x;
	std::cin >> x;
	if (x == "-h" || x == "--help") { // sprawdza czy uzytkownik nie chce wywolwac dialogu z pomoca
		sout(help);
		system("pause");
		system("CLS");
		return choice(begin, options, count);
	}
	int tmp = number(x) - 1;
	if (tmp >= 0 && tmp < count)
	{
		sout("\n");
		return tmp;
	}
	else
	{
		system("CLS");
		sout("Wybrana opcja nie istnieje, prosze sprobowac ponownie");
		return choice(begin, options, count);
	}
}
// Konwertuje tablice z napisami na dialog z ponumerowanymi opcjami w celu wywolania innej wersji choice
// @param (begin) dialog poczatkowy, (options) tablica napisow, (count) ilosc opcji
// @return wybrana opcja przez uzytkownika - 1
int choice(std::string begin, std::string options[], int count)
{
	std::string tmp = "";
	for (int i = 0; i < count; i++)
	{
		tmp += std::to_string(i + 1) + ". " + options[i] + '\n';
	}
	return choice(begin, tmp, count);
}

// Pozawal uzytkonik zmienic aktualnie walczacego pokemona
// @param aktualnie walczacy pokemon
// @return bool - czy udalo sie zmienic pokemona 
bool pokemon_change(pokemon* p1) {
	for (size_t i = 0; i < 6; i++)
	{
		if (player_pokemons[i].isAlive())
			sout(std::to_string(i + 1) + ". " + player_pokemon_name[i]);
	}
	sout("7. Anuluj");
	std::string x;
	std::cin >> x;
	if (x == "-h" || x == "--help") {
		sout(help);
		std::cin.get();
		system("CLS");
		return pokemon_change(p1);
	}
	int tmp = number(x) - 1;
	if (tmp == 6)return false;
	if (player_pokemons[tmp].isAlive() == false) {
		sout("\nNiepoprawny numer");
		return pokemon_change(p1);
	}
	sout("Pokemon " + p1->name + " zostal zmienony na " + player_pokemon_name[tmp]);
	player = tmp;
	return true;

}

// Przeprowadza ewolucje pokemon
// @param (p) pokemon do ewolucji
void evolution(pokemon* p) {
	p->lvl++;
	sout("Wybierz 2 statystyki do polepszenia (moga sie powtarzac)");
	std::string stats[] = { "maxHP + 20", "sila + 10", "szansa na unik + 10" };
	int tmp = choice("pierwsza statystyka:", stats, 3);
	p->stats[tmp] += tmp == 0 ? 20 : 10;

	std::string stat[] = { "maxHP + 20", "sila + 10", "szansa na unik + 10" };
	tmp = choice("druga statystyka:", stat, 3);
	p->stats[tmp] += tmp == 0 ? 20 : 10;
}

// Funkcja do prowadzenia pojedynczej tury gracza
// @param (p1) pokemon gracza, (p2) pokemon przeciwnika
void fight_player(pokemon* p1, pokemon* p2) {
	sout("===Tura gracza===");
	sout(p1->name + " (" + std::to_string(p1->hp) + " /  " + std::to_string(p1->stats[0]) + " hp)		vs		" + p2->name + " (" + std::to_string(p2->hp) + " / " + std::to_string(p2->stats[0]) + " hp)\n");
	if (p1->stun) sout("Nadal jestes ogloszony"); // sprawdza czy pokemon jest ogloszony
	else {
		std::string options = "1. Normalny atak\n2. Zmien pokemona\n";
		int options_count = 2;
		if (p1->special_count > 0) { // sprawdza czy ma specjalne ataki
			options += "3. Specjalny atak\n";
			options_count++;
		}
		else if (p1->can_evolve()) { // sprawdza czy moze ewoluowac
			options += "3. Ewolucja\n";
			options_count++;
		}
		if (p1->special_count > 0 && p1->can_evolve()) { // sprawdza czy oba powyzsze sa prawdziwe
			options += "\n4. Ewolucja\n";
			options_count++;
		}
		switch (choice("Wybierz akcje: ", options, options_count)) // pyta uzytkownika co chce zrobic
		{

		case 0:
			p1->attack_normal(p2);
			break;
		case 1:
			if (pokemon_change(p1) == false) { // gracz chcial zmienic pokemona ale anulowal -> rozpoczecie od nowa jego tury
				fight_player(p1, p2);
				return;
			}
			break;
		case 2:
			if (p1->special_count > 0) {
				p1->attack_special(p2);
			}
			else if (p1->can_evolve()) {
				evolution(p1);
			}
			else {
				sout("niepoprawny wybor");
				fight_player(p1, p2);
			}
			break;
		case 3:
			if (p1->can_evolve())evolution(p1);
			break;
		default:
			std::cout << "#ERROR_FIGHT_PLAYER";
			break;
		}
		system("pause");
		system("CLS");
	}
}
// Funkcja w przypadku smierci pokemona gracza
// @param (p) pokemon ktory walczycl
void death_player(pokemon* p) {
	sout("Twoj pokemon Zemdlal! Wybierz nowego na jego miejsce: ");
	bool check = true; // true - nie ma zadnych zywych pokemonow gracza
	for (size_t i = 0; i < 6; i++)
	{
		if (player_pokemons[i].isAlive()) check = false;
	}
	if (check) {
		sout("Przegrales!");
		exit(0);
	}
	do {
		check = pokemon_change(p);
	} while (check != true);
}


// Funkcja do przeprowadzania tury przeciwnika
void fight_enemy(pokemon* p1, pokemon* p2) {
	sout("===Tura przeciwnika===");
	sout(p1->name + " (" + std::to_string(p1->hp) + " /  " + std::to_string(p1->stats[0]) + " hp)		vs		" + p2->name + " (" + std::to_string(p2->hp) + " / " + std::to_string(p2->stats[0]) + " hp)\n");

	if (p2->stun) sout("Przeciwnik nadal jest ogloszony!");
	else {
		if (p2->special.time < 0 && p2->special_count > 0) { // sprawdza dostepnosc specjalnych atakow
			if (p2->special.type == 5 || p2->special.type == 6) { // sprawdza czy specjalny atak nie jest leczacy w przypadku gdy ma pelne zdrowie lub czy jego efekt nie jest jeszcze aktywny
				if (p2->hp < p2->stats[0]) p2->attack_special(p1);
				else p2->attack_normal(p1);
			}
			else
				p2->attack_special(p1);
		}
		else {
			p2->attack_normal(p1);
		}
	}
}
// Sprawdza czy jakis pokemon zemdlal
// @param (p1) pokemon gracza (p2) pokemon przeciwnika
bool check_for_life(pokemon* p1, pokemon* p2) {
	if (p1->alive == false)death_player(p1);
	if (p2->alive == false) {
		p1->xp += p2->worth_xp;
		sout(p2->name + " mdleje!");
		sout(p1->name + " zdobywa " + std::to_string(p2->worth_xp) + "xp");
		enemy_pokemons[progress].pop_back();
		if (enemy_pokemons[progress].size() == 0) return false;

		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		sout("Przeciwnik wystawia nowego pokemona " + p2->name);
		system("pause");
		system("CLS");
	}
	return true;
}

// Przeprowadza cala walke pomiedzy graczem a przeciwnikiem
void fight_begin(int p1_choice) {
	system("CLS");
	player = p1_choice;
	pokemon* p1 = &player_pokemons[player];
	pokemon* p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
	bool check = true;
	sout(p1->name + " walczy teraz z: " + p2->name + "!!!\n");
	while (check) {

		p1->round(p2);  // pasywne efekty gracza

		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		if (check_for_life(p1, p2) == false)return;
		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];

		fight_player(p1, p2); // tura gracza

		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		if (check_for_life(p1, p2) == false)return;
		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];

		p2->round(p1); // pasywne efekty przeciwnika

		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		if (check_for_life(p1, p2) == false)return;
		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];

		fight_enemy(p1, p2); // tura przeciwnika

		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		if (check_for_life(p1, p2) == false)return;
		p1 = &player_pokemons[player];
		p2 = &enemy_pokemons[progress][enemy_pokemons[progress].size() - 1];
		system("pause");
		system("CLS");
	}
}
// Zapisuje aktualy stan gry do pliku save1.txt i wychodzi
void save() {
	std::ofstream f("save1.txt");
	f << progress << ' ' << difficulty << '\n';
	for (size_t i = 0; i < 6; i++)
	{
		f << player_pokemons[i].info() + '\n';
	}
}

// Przygotowuje walke z natepnym przeciwnikiem
void fight_prepare() {
	std::string tmp[] = { "Wybierz pokemona ktory rozpocznie walke", "Zbadaj swoje pokemony", "Zapisz i wyjdz" };
	switch (choice("Walczysz teraz z " + std::to_string(progress + 1) + " przeciwniekiem z " + std::to_string(difficulty), tmp, 3))
	{
	case 0:
		system("CLS");
		fight_begin(choice("Wybierz pokemona ktory rozpocznie walke:", player_pokemon_name, 6));
		return;
		break;
	case 1:
		system("CLS");
		sout(player_pokemons[choice("Wybierz pokemona do zbadania:", player_pokemon_name, 6)].show());
		system("pause");
		system("CLS");
		fight_prepare();
		return;
		break;
	case 2:
		save();
		exit(0);
		break;
	default:
		std::cout << "#ERROR_FIGHT_CHOICE";
		break;
	}
}

// Opcja dialogowa pomiedzy walkami i odswierzenie pokemonow
void play() {
	sout("Twoj aktualny progres: " + std::to_string(progress + 1) + "/" + std::to_string(difficulty));
	if (progress + 1 == difficulty) {
		sout("WYGRALES KONIEC GRY!");
		exit(0);
	}
	std::vector<pokemon> tmp;
	for (size_t i = 0; i < difficulty; i++)
	{
		tmp.push_back(pokemons[random(0, 14)]);
	}
	enemy_pokemons[progress] = tmp;
	fight_prepare();
	for (size_t i = 0; i < 6; i++)
	{
		player_pokemons[i].refreash();
	}
	progress++;
	play();
}
// Wczytuje stan gry z pliku save1.txt
void loadGame() {
	std::ifstream f("save1.txt");
	f >> progress;
	f >> difficulty;
	for (size_t i = 0; i < 6; i++)
	{
		int special_type;
		f >> special_type;
		int special_amount;
		f >> special_amount;
		int special_time;
		f >> special_time;
		int special_stat;
		f >> special_stat;
		int special_duration;
		f >> special_duration;
		std::string special_name;
		f >> special_name;
		std::string name;
		f >> name;
		int hp;
		f >> hp;
		int maxhp;
		f >> maxhp;
		int strength;
		f >> strength;
		int dodge;
		f >> dodge;
		int type;
		f >> type;
		int xp;
		f >> xp;
		int worth_xp;
		f >> worth_xp;
		int special_limit;
		f >> special_limit;
		int special_count;
		f >> special_count;
		int lvl;
		f >> lvl;
		attack atmp;
		atmp.create(special_type, special_amount, special_duration, special_stat, special_name);
		pokemon ptmp;
		ptmp.create(name, hp, maxhp, strength, dodge, type, xp, worth_xp, atmp, special_limit, special_count, lvl);
		player_pokemons[i] = ptmp;
		player_pokemon_name[i] = player_pokemons[i].name;
	}
	play();
}

// Tworzy 15 nowych pokemonow i je zapisuje w tablicy pokemons[]
void setup() {

	pokemon p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15;
	attack a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15;
	// woda
	a1.create(0, 30, -1, -1, "Wodny_Bicz");
	p1.create("Nemo", 100, 100, 15, 20, 0, 0, 70, a1, 2, 2, 1);

	a2.create(4, 15, 4, -1, "Morska_Bryza");
	p2.create("Dorsz", 150, 150, 13, 16, 0, 0, 85, a2, 1, 1, 1);
	// ziemia
	a3.create(2, 20, 2, 1, "Ziemna_Lepianka");
	p3.create("Slimak", 150, 150, 8, 10, 1, 0, 100, a3, 2, 2, 1);

	a4.create(3, -1, 3, -1, "Rzucenie_Kamieniem");
	p4.create("Kamieniak", 200, 200, 8, 10, 1, 0, 110, a4, 3, 3, 1);
	//powietrze
	a5.create(2, 40, 3, 2, "Wietrzne_Nurty");
	p5.create("Komar", 50, 50, 25, 20, 2, 0, 65, a5, 2, 2, 1);

	a6.create(1, 15, 3, -1, "Ukaszenie");
	p6.create("Szerszen", 85, 85, 12, 30, 2, 0, 85, a6, 2, 2, 1);
	//ogien
	a7.create(0, 50, -1, -1, "Ognisty_Pocisk");
	p7.create("Chochlik", 100, 100, 25, 8, 3, 0, 120, a7, 1, 1, 1);

	a8.create(1, 18, 3, -1, "Ognisty_Dotyk");
	p8.create("Ognisko", 75, 75, 20, 20, 3, 0, 89, a8, 2, 2, 1);
	//lod
	a9.create(3, -1, 4, -1, "Lodowy_Podmuch");
	p9.create("Sniezka", 65, 65, 15, 10, 4, 0, 100, a9, 1, 1, 1);

	a10.create(6, 15, 4, -1, "Sniezyca");
	p10.create("Lodowiec", 200, 200, 8, 8, 4, 0, 120, a10, 1, 1, 1);
	//stal
	a11.create(5, 7, 3, 1, "Rdzewienie");
	p11.create("Kula", 150, 150, 15, 18, 5, 0, 70, a11, 1, 1, 1);

	a12.create(3, -1, 3, -1, "Stalowa_Kurtyna");
	p12.create("Mlot", 60, 60, 15, 10, 5, 0, 90, a12, 1, 1, 1);
	//lod
	a13.create(2, 30, 2, 2, "Sniezne_Sanie");
	p13.create("Renifer", 70, 70, 25, 20, 4, 0, 85, a13, 2, 2, 1);

	a14.create(3, -1, 2, -1, "Zaspa_Sniezna");
	p14.create("Gora", 300, 300, 8, 8, 4, 0, 70, a14, 2, 2, 1);

	a15.create(5, 6, 6, 2, "Gleboki_Snieg");
	p15.create("Balwan", 150, 150, 25, 8, 4, 0, 65, a15, 2, 2, 1);

	pokemons[0] = p1;
	pokemons[1] = p2;
	pokemons[2] = p3;
	pokemons[3] = p4;
	pokemons[4] = p5;
	pokemons[5] = p6;
	pokemons[6] = p7;
	pokemons[7] = p8;
	pokemons[8] = p9;
	pokemons[9] = p10;
	pokemons[10] = p11;
	pokemons[11] = p12;
	pokemons[12] = p13;
	pokemons[13] = p14;
	pokemons[14] = p15;
}
// Tworzy nowa gre
void newGame() {
	system("CLS");
	//wybor druzyny gracza
	std::string names = "";
	for (size_t i = 0; i < 15; i++)
	{
		names += std::to_string(i + 1) + ". " + pokemons[i].show() + '\n';
	}
	for (size_t i = 0; i < 6; i++)
	{
		player_pokemons[i] = pokemons[choice("Wybierz pokemona do druzyny (" + std::to_string(i + 1) + "/6): ", names, 15)];
		player_pokemon_name[i] = player_pokemons[i].name;
		system("CLS");
	}
	attack tmp;
	sout("Twoja druzyna:");
	for (size_t i = 0; i < 6; i++)
	{
		sout(player_pokemons[i].show());
	}
	sout("\nRuszajmy!");
	system("pause");
	system("CLS");
	//wybor trudnosci
	difficulty = 4 + choice("Wybierz poziom trudnosci:", "1. Latwy (4 przeciwnikow z 4 pokemonami)\n2. Sredni (5 przeciwnikow z 5 pokemonami)\n3. Trudny (6 przeciwnikow z 6 pokemonami)", 3);
	system("CLS");
	play(); //rozpoczenie
}


int main()
{
	srand(time(NULL));
	setup();

	sout("Witaj!");
	std::string opt[] = { "Zacznij nowa przygode! (Wybieranie 6 pokemonow)", "Wczytaj przygode!" };
	if (choice("Wybierz opcje", opt, 2) == 0) {
		newGame();
	}
	else {
		loadGame();
	}
}
