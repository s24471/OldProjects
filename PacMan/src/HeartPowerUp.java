public class HeartPowerUp extends  PowerUp {
    public HeartPowerUp(Model model, Tile t) {
        super(model, t);
        spriteNum = 6;
    }

    @Override
    public void use() {
        super.use();
        Model.lifes++;
        Menu.livess.setText(Model.lifes+"");
        System.out.println("zyskano zycie");
    }
}

