package Items;

import Game.Model;

public class Key extends Item{

    public Key(int y, int x, Model model) {
        super(y, x, model);
        setSpriteName("Key");
    }

    @Override
    public void use() {
        super.use();
        //żeby to zrobić, trzeba ogarnąć eq chyba
    }
}
