#include "GameTorrent.h"
#include "FilmTorrent.h"
#include "SoftwareTorrent.h"

int main()
{
    GameTorrent game1("Windows" ,'E', "Super Mario", 150, "Stoyancho", 5);
    GameTorrent game2("Windows", 'M', "Minecraft", -15, "Dancho", 1);
    GameTorrent game3("", 'V', "Minecraft", -15, "Dancho", 1);

    FilmTorrent film1("Vasko", 65, "Bulgarski", "Jivotut na Vasko", 450, "Vasko", 100000);
    FilmTorrent film2("Stoyan", 85, "Bulgarski", "Kak da imate nad 80 score na typing test", 680, "Vasko", -6);
    FilmTorrent film3("Dani", 1, "Bulgarski", "Spisukut mi s priqteli", 3, "Vasko", 0);

    cout << game1.toString() << endl;
    cout << film3.toString() << endl;
    cout << film1.toString() << endl;

    return 0;
}