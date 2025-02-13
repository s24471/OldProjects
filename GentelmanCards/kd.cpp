#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <windows.h>
#include <ctime>
#include <random>
#pragma warning(disable : 4996)
using namespace std;

const bool debug_mode = false;

const int size_players = 8;
const int size_hand = 5;
const int size_pyt = 46;
const int size_odp = 145;

int size_pyt_act = size_pyt;
int size_hand_act[size_players];
int size_odp_pula = size_odp;

int szybkosc_tekstu = 0;
int tura = 0;
int points[size_players];

string player_hand[size_players][size_hand];
string pytania[size_pyt];
string odpowiedzi[size_odp];
string imie[size_players];

string plik_pytania_nazwa;



string czas()
{
    time_t timetoday;
    time(&timetoday);
    string date = asctime(localtime(&timetoday));
    return date;
}
void wyswietl_tekst(string tekst, int x, int enter)
{
    for (int i = 0; i < tekst.size(); i++)
    {
        cout << tekst[i];
        Sleep(x);
    }
    for (int i = 0; i < enter; i++)cout << endl;
    return;
}
string wstaw_blank(string pyt, string odp)
{
    int counter = 0;
    int pocz = 0;
    string wynik = "";
    bool check = false;
    bool check2 = false;
    for (int i = 0; i < pyt.size(); i++)
    {
        if ((pyt[i] == '.') && (check == false))
        {
            if (counter == 0)pocz = i;
            counter++;
        }
        else if(check == false)
        {
            
            if (counter >= 3)
            {
                wynik.append("[");
                check = true;
                wynik.append(odp);
                wynik.append("]");
                wynik.append(" ");
                check2 = true;

            }    
            
            wynik += pyt[i];      
        }
        else
        {
            wynik += pyt[i];
        }
    }
    if (counter >= 3 && check==false)
    {
        wynik.append("[");
        check = true;
        wynik.append(odp);
        wynik.append("]");
        wynik.append(" ");

    }
    else if (check2 == false)
    {
        wynik.append(" [");
        wynik.append(odp);
        wynik.append("]");
    }
    return wynik;
}
void plik(string log)
{
    ofstream myOutStream(plik_pytania_nazwa, ios::app);
    myOutStream << log;
}
int count_blank(string n)
{
    int wynik = 0;
    int count = 0;
    for (int i = 0; i < n.length(); i++)
    {
        if (n[i] == '.')count++;
        else
        {
            if (count >= 3) wynik++;
            count = 0;
        }
    }
    if (count >= 3) wynik++;
    return wynik;
}
int random(int x, int y)
{
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> distr(x, y);
    return distr(gen);
}
void wait()
{
    system("pause");
}
void usun_odp_pula(int x)
{
    for (int i = x + 1; i < size_odp_pula; i++)
    {
        odpowiedzi[i - 1] = odpowiedzi[i];
    }
    size_odp_pula--;
}
string losuj_odp()
{
    int x = random(0, size_odp_pula - 1);
    string wynik = odpowiedzi[x];
    usun_odp_pula(x);
    return wynik;
}
void usun_odp(int nr_gracza, int nr_odp)
{
    for (int i = nr_odp + 1; i < size_hand_act[nr_gracza]; i++)
    {
        player_hand[nr_gracza][i - 1] = player_hand[nr_gracza][i];
    }
    size_hand_act[nr_gracza]--;
}
string odp(int nr_gracza, string pyt)
{
    string wynik = pyt;
    wyswietl_tekst("Reka " + imie[nr_gracza] + ":", szybkosc_tekstu, 2);
    if(!debug_mode)wait();
    system("CLS");
    int wartosc_count_blank = count_blank(pyt);
    if (wartosc_count_blank == 0) wartosc_count_blank++;
    for (int i = 0; i < wartosc_count_blank; i++) {
        int x;
        system("CLS");
        
        cout << endl << endl;
        wyswietl_tekst(wynik, szybkosc_tekstu, 3);
        wyswietl_tekst("Reka " + imie[nr_gracza] + ": ", szybkosc_tekstu, 2);
        for (int i = 0; i < size_hand_act[nr_gracza]; i++)
        {
            wyswietl_tekst((""+to_string(i + 1) + ". " + player_hand[nr_gracza][i]), szybkosc_tekstu, 1);
        }
        wyswietl_tekst("podaj odp: ", szybkosc_tekstu, 0);
        if (debug_mode) x = random(1, size_hand_act[nr_gracza]);
        else cin >> x;
        x--;
        wynik = wstaw_blank(wynik, player_hand[nr_gracza][x]);
        usun_odp(nr_gracza, x);
    }
    for (int i = 0; i < wartosc_count_blank; i++)
    {
        if (size_odp_pula > 0)
        {
            player_hand[nr_gracza][size_hand_act[nr_gracza]] = losuj_odp();
            size_hand_act[nr_gracza]++;
        }
    }
    return wynik;
}
void usun_pyt(int x)
{
    for (int i = x + 1; i < size_pyt_act; i++)
    {
        pytania[i - 1] = pytania[i];
    }
    size_pyt_act--;
}
string losujpyt()
{
    int x = random(0, size_pyt_act - 1);
    string wynik = pytania[x];
    usun_pyt(x);
    return wynik;
}
void scoreboard()
{
    wyswietl_tekst("Aktualna punktacja:", szybkosc_tekstu, 1);
    for (int i = 0; i < size_players; i++)
    {
        wyswietl_tekst(imie[i] + "   ->   " + to_string(points[i]), szybkosc_tekstu, 1);
    }
}
void runda()
{
    if (tura == size_players)tura = 0;
    
    string aktualne_pytanie = losujpyt();
    string odpowiedzi_aktualne[size_players];
    wyswietl_tekst(imie[tura] + " wybiera odpowiedzi do pytania: ", szybkosc_tekstu, 2);
    wyswietl_tekst(aktualne_pytanie, szybkosc_tekstu, 6);
    if(!debug_mode)wait();
    int numery[size_players];
    for (int i = 0; i < size_players; i++)
    {
        if (tura != i)
        {
            system("CLS");
            cout<<endl << endl;
            odpowiedzi_aktualne[i] = odp(i, aktualne_pytanie);
        }
        numery[i] = i;
    }
    system("CLS");
    
    for (int i = tura + 1; i < size_players; i++)
    {
        odpowiedzi_aktualne[i - 1] = odpowiedzi_aktualne[i];
        numery[i - 1] = numery[i];
    }
    string odpowiedzi_aktualne_pomieszane[size_players - 1];
    int numery_pomieszane[size_players - 1];
    for (int i = 0; i < size_players - 1; i++)
    {
        int x = random(0, size_players - 2 - i);
        odpowiedzi_aktualne_pomieszane[i] = odpowiedzi_aktualne[x];
        numery_pomieszane[i] = numery[x];
        for (int j = x + 1; j < size_players - 1 - i; j++)
        {
            odpowiedzi_aktualne[j - 1] = odpowiedzi_aktualne[j];
            numery[j - 1] = numery[j];
        }
    }
    wyswietl_tekst(imie[tura] + " wybiera: ", szybkosc_tekstu, 2);
    cout << endl;
    for (int i = 0; i < size_players - 1; i++)
    {
        wyswietl_tekst(to_string(i + 1) + ". " + odpowiedzi_aktualne_pomieszane[i], szybkosc_tekstu, 1);
    }
    cout << endl << endl << endl;
    wyswietl_tekst("Ktora odpowiedz byla najlepsza?", szybkosc_tekstu, 1);
    int winner;
    if (debug_mode) winner = random(1, size_players - 1);
    else cin >> winner;
    winner--;
    points[numery_pomieszane[winner]]++;
    system("CLS");
    wyswietl_tekst("Zwyciezyl " + imie[numery_pomieszane[winner]], szybkosc_tekstu, 2);
    plik(czas() + "XwygralX: " + odpowiedzi_aktualne_pomieszane[winner] + " XgraczaX: " + imie[numery_pomieszane[winner]] + " XwybralX: " + imie[tura]+"\n");
    scoreboard();
    if(!debug_mode)wait();
    tura++;
}
void setup()
{
    ifstream sprawdz_plik_gry;
    sprawdz_plik_gry.open("nr_gry.txt");
    if (!sprawdz_plik_gry.good())
    {
        sprawdz_plik_gry.close();
        ofstream new_game_file;
        new_game_file.open("nr_gry.txt");
        new_game_file << 0;
        new_game_file.close();
    }
    sprawdz_plik_gry.close();
    ifstream plik_pytania;
    plik_pytania.open("kd-pytania.txt");
    for (int i = 0; i < size_pyt; i++)
    {
        getline(plik_pytania, pytania[i]);
    }
    plik_pytania.close();

    ifstream plik_odpowiedzi;
    plik_odpowiedzi.open("kd-odpowiedzi.txt");
    for (int i = 0; i < size_odp; i++)
    {
        getline(plik_odpowiedzi, odpowiedzi[i]);
    }
    plik_odpowiedzi.close();

    ifstream nr_gry;
    nr_gry.open("nr_gry.txt");
    int gra = 0;
    nr_gry >> gra;
    gra++;
    nr_gry.close();
    ofstream nr_gry2;
    nr_gry2.open("nr_gry.txt");
    nr_gry2 << gra;
    nr_gry2.close();
    plik_pytania_nazwa = ("wygrane_gry_" + to_string(gra)+".txt");
    plik((czas() + to_string(size_players) + "\n"));
    for (int i = 0; i < size_players; i++)
    {
        size_hand_act[i] = size_hand;
        points[i] = 0;
        wyswietl_tekst("Podaj imie gracza nr. " + to_string(i + 1), szybkosc_tekstu, 1);
        if (debug_mode) imie[i] = "g" + to_string(i+1);
        else cin >> imie[i];
        plik(imie[i] + "\n");
        system("CLS");
        for (int j = 0; j < size_hand; j++)
        {
            player_hand[i][j] = losuj_odp();
        }
    }
}
void graj()
{
    while (size_pyt_act > 0)
    {
        runda();
        system("CLS");
    }
    wyswietl_tekst("koniec!!", szybkosc_tekstu, 1);
    scoreboard();
    plik(czas() + " koniec:\n");
    for (int i = 0; i < size_players; i++)
    {
        plik(imie[i] + "   ->   " + to_string(points[i])+"\n");
    }
    cout << endl << endl << endl;
    plik("Zostalo: " + to_string(size_odp_pula));
}
int main()
{
        setup();
        graj();
}