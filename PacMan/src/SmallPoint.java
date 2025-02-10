public class SmallPoint extends PowerUp{
    public SmallPoint(Model model, Tile t) {
        super(model, t);
        spriteNum = 5;
    }

    @Override
    public void use() {
        super.use();
        model.player.addScore(1);
        Menu.points.setText("Wynik tego poziomu: " + String.valueOf(model.player.score));
        System.out.println("Zjedzono SmallPoint (score: " + model.player.score + ")");
    }
}
