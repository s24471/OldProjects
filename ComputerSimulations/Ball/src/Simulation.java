import java.math.BigDecimal;
import java.math.RoundingMode;

public class Simulation {
    public BigDecimal h = new BigDecimal("20");//wysokosc trojkata
    public BigDecimal alfa = BigDecimal.valueOf(Math.toRadians(45)); // kat nachylenia plaszczyzny
    public BigDecimal m = new BigDecimal("1");//masa
    public BigDecimal g = new BigDecimal("10");//grawitacja
    public BigDecimal r = new BigDecimal("3");//promien kuli
    public BigDecimal lsb = new BigDecimal("2").divide(new BigDecimal("5"), RoundingMode.HALF_UP).multiply(m).multiply(r).multiply(r);// wartość momentu bezwładności kuli
    public BigDecimal acc = g.multiply(BigDecimal.valueOf(Math.sin(alfa.doubleValue()))).divide(new BigDecimal("1").add(lsb.divide(m.multiply(r.multiply(r)), 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);//przyspieszenie
    public BigDecimal eps = acc.divide(r, 4, RoundingMode.HALF_UP);//przyspieszenia kuli wzdłuż osi obrotu.
    public BigDecimal dt = new BigDecimal("0.005");//czas
    public BigDecimal sX = BigDecimal.ZERO;//starting x kuli
    public BigDecimal sY= h;//start y kuli
    public BigDecimal sX2= h.divide(BigDecimal.valueOf(Math.tan(alfa.doubleValue())), 4, RoundingMode.HALF_UP);// koniec
    public BigDecimal sY2= BigDecimal.ZERO;//koniec
    public BigDecimal Sx_r = BigDecimal.ZERO;
    public BigDecimal Sy_r = r;
    public BigDecimal V = BigDecimal.ZERO;
    public BigDecimal Dsx = V.multiply(dt);
    public BigDecimal DV = acc.multiply(dt);
    public BigDecimal x_r = Sx_r.multiply(BigDecimal.valueOf(Math.cos(-alfa.doubleValue()))).subtract(Sy_r.multiply(BigDecimal.valueOf(Math.sin(-alfa.doubleValue()))));
    public BigDecimal y_r = Sx_r.multiply(BigDecimal.valueOf(Math.sin(-alfa.doubleValue()))).add(Sy_r.multiply(BigDecimal.valueOf(Math.cos(-alfa.doubleValue())))).add(h);
    public BigDecimal beta = BigDecimal.ZERO;
    public BigDecimal dw = eps.multiply(dt);
    public BigDecimal w = BigDecimal.ZERO;
    public BigDecimal db = BigDecimal.ZERO;
    public BigDecimal x = x_r;
    public BigDecimal y = y_r;
    public BigDecimal x2 = r.multiply(BigDecimal.valueOf(Math.cos(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(x);
    public BigDecimal y2 = r.multiply(BigDecimal.valueOf(Math.sin(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(y);
    public BigDecimal xk = r.multiply(BigDecimal.valueOf(Math.cos(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(x_r);
    public BigDecimal yk = r.multiply(BigDecimal.valueOf(Math.sin(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(y_r);
    public BigDecimal t = BigDecimal.ZERO;
    public BigDecimal Ep = BigDecimal.ZERO; // energia potencjalna
    public BigDecimal Ek = BigDecimal.ZERO; // energia kinetyczna
    public BigDecimal Et = BigDecimal.ZERO; // energia całkowita
    public Simulation() {}

    public void calc(){
        Sx_r=Sx_r.add(Dsx);
        x_r = Sx_r.multiply(BigDecimal.valueOf(Math.cos(-alfa.doubleValue()))).subtract(Sy_r.multiply(BigDecimal.valueOf(Math.sin(-alfa.doubleValue()))));
        y_r = Sx_r.multiply(BigDecimal.valueOf(Math.sin(-alfa.doubleValue()))).add(Sy_r.multiply(BigDecimal.valueOf(Math.cos(-alfa.doubleValue())))).add(h);
        V=V.add(DV);
        Dsx=V.multiply(dt);
        xk = r.multiply(BigDecimal.valueOf(Math.cos(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(x_r);
        yk = r.multiply(BigDecimal.valueOf(Math.sin(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(y_r);
        beta=beta.add(db);
        w=w.add(dw);
        db=w.multiply(dt);
        x = x_r;
        y = y_r;
        x2 = r.multiply(BigDecimal.valueOf(Math.cos(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(x);
        y2 = r.multiply(BigDecimal.valueOf(Math.sin(BigDecimal.valueOf(Math.toRadians(90)).subtract(beta).doubleValue()))).add(y);
        t=t.add(dt);
        Ep = m.multiply(g).multiply(y_r);
        Ek = new BigDecimal("0.5").multiply(m).multiply(V.pow(2)).add(new BigDecimal("0.5").multiply(lsb).multiply(w.pow(2)));
        Et = Ep.add(Ek);}

    public String get1(){
        return "" + t + " " + Sx_r.setScale(4, RoundingMode.HALF_UP)+" " + Sy_r.setScale(4, RoundingMode.HALF_UP) + " " + V.setScale(4, RoundingMode.HALF_UP) +" " + Dsx.setScale(4, RoundingMode.HALF_UP) +" " + DV.setScale(4, RoundingMode.HALF_UP) + " " + x_r.setScale(4, RoundingMode.HALF_UP) +" " + y_r.setScale(4, RoundingMode.HALF_UP);
    }

    public String get2(){
        return "" + t + " " + beta.setScale(4, RoundingMode.HALF_UP)+" " + w.setScale(4, RoundingMode.HALF_UP) + " " + db.setScale(4, RoundingMode.HALF_UP) +" " + dw.setScale(4, RoundingMode.HALF_UP) +" " + x2.setScale(4, RoundingMode.HALF_UP) + " " + y2.setScale(4, RoundingMode.HALF_UP) ;
    }
}