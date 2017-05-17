package lukashempel.trinkspielgedoenstestnumero2;

public class CPlayer {

    public String getName() {
        return sName;
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    private String sName;

    public CPlayer(String sName){
        this.sName = sName;
    }
}
